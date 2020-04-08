package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.RestPageResponse;
import com.daoutech.contacts.server.domain.CGroup;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.exception.BadRequestException;
import com.daoutech.contacts.server.exception.ErrorResponse;
import com.daoutech.contacts.server.service.CGroupService;
import com.daoutech.contacts.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class CGroupController {

	@Autowired
	private CGroupService cGroupService;

	@GetMapping("/v1.0/cgroups")
	public RestPageResponse<CGroup> getAll(@PageableDefault(sort = "name") Pageable pageable, @AuthenticationPrincipal User currentUser) {
		Page<CGroup> cGroupPage = cGroupService.findAll(currentUser.getId(), pageable);
		return new RestPageResponse<>(cGroupPage);
	}

	@GetMapping("/v1.0/cgroups/{id}")
	public CGroup getOne(@PathVariable("id") Integer id, @AuthenticationPrincipal User currentUser) {
		return cGroupService.findById(id, currentUser.getId());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1.0/cgroups")
	public CGroup save(@Valid @RequestBody CGroup cGroup, @AuthenticationPrincipal User currentUser) {
		try {
			cGroup.setUser(currentUser);
			return cGroupService.save(cGroup);
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException(new ErrorResponse.Field("name", "이미 동일한 그룹명이 존재합니다."));
		}
	}

	@PutMapping("/v1.0/cgroups/{id}")
	public CGroup update(@PathVariable("id") Integer id, @RequestBody CGroup updateData, @AuthenticationPrincipal User currentUser) {
		try {
			return cGroupService.updateById(id, currentUser.getId(), updateData);
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException(new ErrorResponse.Field("name", "이미 동일한 그룹명이 존재합니다."));
		}
	}

	@DeleteMapping("/v1.0/cgroups/{id}")
	public Map<String, Long> delete(@PathVariable("id") Integer id, @AuthenticationPrincipal User currentUser) {
		long count = cGroupService.deleteById(id, currentUser.getId());
		return Collections.singletonMap("count", count);
	}
}