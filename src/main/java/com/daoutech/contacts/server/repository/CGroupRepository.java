package com.daoutech.contacts.server.repository;

import com.daoutech.contacts.server.domain.CGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CGroupRepository extends JpaRepository<CGroup, Integer> {

	Page<CGroup> findAllByUserId(Integer userId, Pageable pageable);

	Optional<CGroup> findByIdAndUser_Id(Integer id, Integer userId);

	long deleteByIdAndUser_Id(Integer id, Integer userId);
}
