package com.daoutech.contacts.server.service;

import com.daoutech.contacts.server.domain.CGroup;
import com.daoutech.contacts.server.exception.DataNotFoundException;
import com.daoutech.contacts.server.repository.CGroupRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.daoutech.contacts.server.domain.QCGroup.cGroup;
import static com.daoutech.contacts.server.domain.QContact.contact;

@Slf4j
@Service
public class CGroupService extends QuerydslRepositorySupport {

	@Autowired
	private CGroupRepository cGroupRepository;

	public CGroupService() {
		super(CGroup.class);
	}

	public Page<CGroup> findAllByUserId(Integer userId, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(cGroup.user.id.eq(userId));
		// TODO 그룹 별 연락처 개수 가져오기
		JPQLQuery<CGroup> query = from(cGroup).where(builder);
		List<CGroup> result = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
		return PageableExecutionUtils.getPage(result, pageable, query::fetchCount);
	}

	public CGroup findById(Integer id, Integer userId) {
		return cGroupRepository.findByIdAndUser_Id(id, userId).orElseThrow(DataNotFoundException::new);
	}

	public CGroup save(CGroup cGroup) {
		return cGroupRepository.save(cGroup);
	}

	public CGroup updateById(Integer id, Integer userId, CGroup updateData) {
		CGroup cGroup = findById(id, userId);
		cGroup.update(updateData);
		return save(cGroup);
	}

	@Transactional
	public long deleteById(Integer id, Integer userId) {
		delete(contact).where(contact.cGroup.id.eq(id).and(contact.cGroup.user.id.eq(userId))).execute();
		return cGroupRepository.deleteByIdAndUser_Id(id, userId);
	}
}
