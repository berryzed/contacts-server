package com.daoutech.contacts.server.repository;

import com.daoutech.contacts.server.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Modifying
	@Query("DELETE FROM Contact p WHERE p.id in :ids and p.user.id = :userId")
	int deleteAllByIdsAndUserId(Integer[] ids, Integer userId);
}
