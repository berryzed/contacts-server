package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.ContactFilter;
import com.daoutech.contacts.server.service.ContactService;
import com.daoutech.contacts.server.DuplicateType;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.Errors;
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
	public Page<Contact> getContacts(@PageableDefault(sort = "name", size = 100) Pageable pageable, ContactFilter filter) {
		return contactService.findAllByUserIdAndFilter(1, filter, pageable);
	}

	@GetMapping("/v1.0/duplicates")
	public Page<Contact> getDuplicates(@PageableDefault(size = 100) Pageable pageable, @RequestParam(name = "type", required = false) DuplicateType type) {
		if (type == null) type = DuplicateType.TEL;
		return contactService.findDuplicates(1, type, pageable);
	}

	@GetMapping("/v1.0/contacts/{id}")
	public Contact getContact(@PathVariable("id") Integer id) {
		return contactService.findByIdAndUserId(id, 1);
	}

	@PostMapping("/v1.0/contacts")
	public Object addContact(@Valid @RequestBody Contact contact, Errors errors) {
		if (errors.hasErrors()) {
			return errors.getAllErrors();
		}

		User user = new User(1);
		contact.setUser(user);

		contactService.save(contact);
		return contact;
	}

	@PutMapping("/v1.0/contacts/{id}")
	public Object updateContact(@PathVariable("id") Integer id, @RequestBody Contact contact, Errors errors) {
		if (errors.hasErrors()) {
			return errors.getAllErrors();
		}

		Contact exists = contactService.findByIdAndUserId(id, 1);
		exists.updateFields(contact);
		contactService.save(exists);
		return exists;
	}

	@DeleteMapping("/v1.0/contacts")
	public Map<String, Object> deleteContact(@RequestBody Integer[] ids) {
		int cnt = contactService.deleteAllByIds(ids, 1);
		return Collections.singletonMap("deleted", cnt);
	}
}

