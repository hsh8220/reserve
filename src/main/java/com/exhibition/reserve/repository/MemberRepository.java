package com.exhibition.reserve.repository;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findByName(String name);
    List<Member> findByCongregation(Congregation congregation);
    Member findByUserId(String userId);
    Member findByUserIdAndCongregation(String userId, Congregation congregation);
    @Transactional
    void deleteByUserId(String userId);
}
