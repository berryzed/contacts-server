package com.daoutech.contacts.server.service;

import com.daoutech.contacts.server.ContactFilter;
import com.daoutech.contacts.server.DuplicateType;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.QCGroup;
import com.daoutech.contacts.server.exception.DataNotFoundException;
import com.daoutech.contacts.server.repository.ContactRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.daoutech.contacts.server.domain.QContact.contact;

@Slf4j
@Service
public class ContactService extends QuerydslRepositorySupport {

	@Autowired
	private ContactRepository contactRepository;

	public ContactService() {
		super(Contact.class);
	}

	public Page<Contact> findAll(Integer userId, ContactFilter filter, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();

		if (filter.getFCGroupId() == null) {
			// 그룹 전체
			List<Integer> cGroupIds = from(QCGroup.cGroup)
					.select(QCGroup.cGroup.id)
//					.where(QCGroup.cGroup.user.id.eq(userId))
					.fetch();

			builder.and(contact.cGroup.id.in(cGroupIds));
		} else {
			// 선택 그룹
			builder.and(contact.cGroup.id.eq(filter.getFCGroupId()));
//			builder.and(contact.cGroup.user.id.eq(userId));
		}
		builder.and(filter.getPredicate());

		JPQLQuery<Integer> contactIdsQuery = from(contact).select(contact.id).where(builder);

		JPQLQuery<Contact> query = from(contact)
				.where(contact.id.in(applyPagination(pageable, contactIdsQuery).fetch()))
				.leftJoin(contact.cGroup, QCGroup.cGroup)
				.fetchJoin().distinct();

		List<Contact> result = applyPagination(pageable, query).fetch();
		return PageableExecutionUtils.getPage(result, pageable, contactIdsQuery::fetchCount);
	}

	// 그룹 전체에서 중복 검색
	public Page<Contact> findDuplicates(Integer userId, Integer cGroupId, DuplicateType type, Pageable pageable) {
		StringPath key = type.getKey();
		String keyName = key.getMetadata().getName();

		Sort.Order order = pageable.getSort().getOrderFor(keyName);
		if (order == null) order = new Sort.Order(Sort.Direction.ASC, keyName);

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(key.isNotNull());
//		builder.and(contact.cGroup.user.id.eq(userId));

		if (cGroupId != null) {
			builder.and(contact.cGroup.id.eq(cGroupId));
		}

		// 서브쿼리보다 나눠서 하는게 빠름
		JPQLQuery<Integer> findDuplicates = from(contact)
				.select(contact.id)
				.where(builder)
				.groupBy(key)
				.having(key.count().gt(1))
				.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, key));
		List<Integer> duplicateIds = findDuplicates.fetch();

		JPQLQuery<Contact> query = from(contact)
				.where(contact.id.in(duplicateIds))
				.leftJoin(contact.cGroup, QCGroup.cGroup)
				.fetchJoin().distinct();

		List<Contact> result = applyPagination(pageable, query).fetch();
		return PageableExecutionUtils.getPage(result, pageable, query::fetchCount);
	}

	public Contact findById(Integer id, Integer userId) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(contact.id.eq(id));
//		builder.and(contact.cGroup.user.id.eq(userId));

		return Optional.ofNullable(from(contact).where(builder).fetchFirst()).orElseThrow(DataNotFoundException::new);
	}

	public Contact save(Contact contact) {
		return contactRepository.save(contact);
	}

	public Contact updateById(Integer id, Integer userId, Contact updateData) {
		Contact contact = findById(id, userId);
		contact.update(updateData);
		return save(contact);
	}

	@Transactional
	public long deleteAllByIds(Integer[] ids, Integer userId) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(contact.id.in(ids));
//		builder.and(contact.cGroup.user.id.eq(userId));

		return delete(contact).where(builder).execute();
	}

	private <T> JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query) {
		return Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
	}
}
