package com.exhibition.reserve.repository;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {
    Exhibition findByName (String name);
    List<Exhibition> findByCongregation (Congregation congregation);
}
