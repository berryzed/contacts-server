package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.domain.CGroup;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.exception.BadRequestException;
import com.daoutech.contacts.server.exception.ErrorResponse;
import com.daoutech.contacts.server.service.CGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
	public Page<CGroup> getAll(@PageableDefault(sort = "name") Pageable pageable) {
		return cGroupService.findAllByUserId(1, pageable);
	}

	@GetMapping("/v1.0/cgroups/{id}")
	public CGroup getOne(@PathVariable("id") Integer id) {
		return cGroupService.findById(id, 1);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1.0/cgroups")
	public CGroup save(@Valid @RequestBody CGroup cGroup) {
		try {
			cGroup.setUser(new User(1));
			return cGroupService.save(cGroup);
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException(new ErrorResponse.Field("name", "이미 동일한 그룹명이 존재합니다."));
		}
	}

	@PutMapping("/v1.0/cgroups/{id}")
	public CGroup update(@PathVariable("id") Integer id, @RequestBody CGroup updateData) {
		try {
			return cGroupService.updateById(id, 1, updateData);
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException(new ErrorResponse.Field("name", "이미 동일한 그룹명이 존재합니다."));
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/v1.0/cgroups/{id}")
	public Map<String, Long> delete(@PathVariable("id") Integer id) {
		long count = cGroupService.deleteById(id, 1);
		return Collections.singletonMap("count", count);
	}
}