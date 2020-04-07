package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.ContactFilter;
import com.daoutech.contacts.server.DuplicateType;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.exception.BadRequestException;
import com.daoutech.contacts.server.exception.ErrorResponse;
import com.daoutech.contacts.server.service.ContactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ContactController {

	@Autowired
	private ContactService contactService;

	@GetMapping("/v1.0/contacts")
	public Page<Contact> getAll(ContactFilter filter, @PageableDefault(sort = "name", size = 100) Pageable pageable) {
		return contactService.findAll(1, filter, pageable);
	}

	@GetMapping("/v1.0/duplicates")
	public Page<Contact> getDuplicates(@RequestParam(name = "type", required = false) DuplicateType type,
									   @RequestParam(name = "cGroupId", required = false) Integer cGroupId,
									   @PageableDefault(size = 100) Pageable pageable) {
		if (type == null) type = DuplicateType.TEL;
		return contactService.findDuplicates(1, cGroupId, type, pageable);
	}

	@GetMapping("/v1.0/contacts/{id}")
	public Contact getOne(@PathVariable("id") Integer id) throws JsonProcessingException {
		return contactService.findById(id, 1);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1.0/contacts")
	public Contact save(@Valid @RequestBody Contact contact) {
		if (contact.getCGroup() == null) {
			throw new BadRequestException(new ErrorResponse.Field("cGroup", "그룹 정보가 없습니다."));
		}

		contact.getCGroup().setUser(new User(1));
		return contactService.save(contact);
	}

	@PutMapping("/v1.0/contacts/{id}")
	public Contact update(@PathVariable("id") Integer id, @Valid @RequestBody Contact contact) {
		return contactService.updateById(id, 1, contact);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/v1.0/contacts")
	public Map<String, Long> delete(@RequestBody Integer[] ids) {
		long count = contactService.deleteAllByIds(ids, 1);
		return Collections.singletonMap("count", count);
	}
}