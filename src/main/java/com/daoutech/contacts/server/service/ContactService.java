package com.daoutech.contacts.server.service;

import com.daoutech.contacts.server.ContactFilter;
import com.daoutech.contacts.server.DataNotFoundException;
import com.daoutech.contacts.server.DuplicateType;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.repository.ContactRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.daoutech.contacts.server.QContact.contact;

@Slf4j
@Service
public class ContactService extends QuerydslRepositorySupport {

	@Autowired
	private ContactRepository contactRepository;

	public ContactService() {
		super(Contact.class);
	}

	public Page<Contact> findAllByUserIdAndFilter(Integer userId, ContactFilter filter, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(contact.user.id.eq(userId));
		builder.and(filter.getPredicate());

		JPQLQuery<Contact> query = from(contact).where(builder);
		List<Contact> result = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
		return PageableExecutionUtils.getPage(result, pageable, query::fetchCount);
	}

	public Page<Contact> findDuplicates(Integer userId, DuplicateType type, Pageable pageable) {
		StringPath key = DuplicateType.getKey(type);

		Sort.Order order = pageable.getSort().getOrderFor(key.toString());
		if (order == null) order = new Sort.Order(Sort.Direction.ASC, key.toString());

		// 서브쿼리보다 나눠서 하는게 빠름
		JPQLQuery<String> findDuplicates = from(contact)
				.select(key)
				.from(contact)
				.where(contact.user.id.eq(userId).and(key.isNotNull()))
				.groupBy(key)
				.having(key.count().gt(1))
				.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, key));
		List<String> duplicateKeys = findDuplicates.fetch();

		JPQLQuery<Contact> query = from(contact)
				.where(key.in(duplicateKeys));

		List<Contact> result = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
		return PageableExecutionUtils.getPage(result, pageable, query::fetchCount);
	}

	public Contact findByIdAndUserId(Integer id, Integer userId) {
		Predicate predicate = contact.id.eq(id).and(contact.user.id.eq(userId));
		return Optional.of(from(contact).where(predicate).fetchOne()).orElseThrow(DataNotFoundException::new);
	}

	public void save(Contact contact) {
		contactRepository.save(contact);
	}

	public int deleteAllByIds(Integer[] ids, Integer userId) {
		return contactRepository.deleteAllByIdsAndUserId(ids, userId);
	}
}
