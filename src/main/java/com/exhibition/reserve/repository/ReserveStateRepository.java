package com.exhibition.reserve.repository;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReserveStateRepository extends JpaRepository<ReserveState, Integer> {
    List<ReserveState> findByExhibitionAndStartTimeBetween (Exhibition exhibition, Timestamp t1, Timestamp t2);
    List<ReserveState> findByMember (Member member);
}
