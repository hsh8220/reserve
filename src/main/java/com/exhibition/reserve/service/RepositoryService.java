package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RepositoryService {
    Optional<Member> getUserById (String userId);
    Optional<List<Member>> getUserByName (String name);
    Optional<List<Member>> getUserAll ();
    void addUser (Member member);
    void modifyUser (Member member);
    void removeUser (String userId);

    Optional<Exhibition> getExhibitionByName (String name);
    Optional<Exhibition> getExhibitionById (Integer id);
    Optional<List<Exhibition>> getExhibitionAll ();
    void addExhibition (String name, String time, String guide);
    void modifyExhibition (Exhibition exhibition);
    void removeExhibition (String name);

    Optional<List<ReserveState>> getStateByUserId(String userId);

    /**
     *
     * @param exhibitionId
     * @param date 검색하고자 하는 날을 입력
     * @return
     */
    Optional<List<ReserveState>> getStateByExhibitionId(Integer exhibitionId, Timestamp date);
    Optional<List<ReserveState>> getStateByExhibitionName(String exhibitionName, Timestamp date);
    void addState(ReserveState state);
    void modifyState(ReserveState state);
    void removeState(Integer id);
}
