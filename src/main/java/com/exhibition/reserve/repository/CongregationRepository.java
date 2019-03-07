package com.exhibition.reserve.repository;

import com.exhibition.reserve.model.Congregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CongregationRepository extends JpaRepository<Congregation, Integer> {
    Congregation findByName (String name);
    @Transactional
    void deleteByName (String name);
}
