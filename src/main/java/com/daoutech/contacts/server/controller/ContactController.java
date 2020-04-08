package com.daoutech.contacts.server.controller;

import com.daoutech.contacts.server.ContactFilter;
import com.daoutech.contacts.server.RestPageResponse;
import com.daoutech.contacts.server.DuplicateType;
import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import com.daoutech.contacts.server.exception.BadRequestException;
import com.daoutech.contacts.server.exception.ErrorResponse;
import com.daoutech.contacts.server.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/v1.0/contacts")
    public RestPageResponse<Contact> getAll(ContactFilter filter, @PageableDefault(sort = "name", size = 100) Pageable pageable,
                                   @AuthenticationPrincipal User currentUser) {
        Page<Contact> contactPage = contactService.findAll(currentUser.getId(), filter, pageable);
        return new RestPageResponse<>(contactPage);
    }

    @GetMapping("/v1.0/duplicates")
    public RestPageResponse<Contact> getDuplicates(@RequestParam(name = "type", required = false) DuplicateType type,
                                          @RequestParam(name = "cGroupId", required = false) Integer cGroupId,
                                          @PageableDefault(size = 100) Pageable pageable, @AuthenticationPrincipal User currentUser) {
        if (type == null) type = DuplicateType.TEL;
        Page<Contact> contactPage = contactService.findDuplicates(currentUser.getId(), cGroupId, type, pageable);
        return new RestPageResponse<>(contactPage);
    }

    @GetMapping("/v1.0/contacts/{id}")
    public Contact getOne(@PathVariable("id") Integer id, @AuthenticationPrincipal User currentUser) {
        return contactService.findById(id, currentUser.getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1.0/contacts")
    public Contact save(@Valid @RequestBody Contact contact, @AuthenticationPrincipal User currentUser) {
        if (contact.getCGroup() == null) {
            throw new BadRequestException(new ErrorResponse.Field("cGroup", "그룹 정보가 없습니다."));
        }

        contact.getCGroup().setUser(currentUser);
        return contactService.save(contact);
    }

    @PutMapping("/v1.0/contacts/{id}")
    public Contact update(@PathVariable("id") Integer id, @Valid @RequestBody Contact contact,
                          @AuthenticationPrincipal User currentUser) {
        return contactService.updateById(id, currentUser.getId(), contact);
    }

    @DeleteMapping("/v1.0/contacts")
    public Map<String, Long> delete(@RequestBody Integer[] ids, @AuthenticationPrincipal User currentUser) {
        long count = contactService.deleteAllByIds(ids, currentUser.getId());
        return Collections.singletonMap("count", count);
    }
}