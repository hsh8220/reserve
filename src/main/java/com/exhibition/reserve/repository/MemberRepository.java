package com.exhibition.reserve.repository;

import com.exhibition.reserve.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findByName(String name);
    Member findByUserId(String userId);
    @Transactional
    Integer deleteByUserId(String userId);
}
