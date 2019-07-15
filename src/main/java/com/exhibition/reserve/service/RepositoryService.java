package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RepositoryService {
    Optional<Member> getUserByIdByCongregation (String userId, Congregation congregation);
    Optional<Member> getUserById (String userId);
    Optional<List<Member>> getUserByName (String name);
    Optional<List<Member>> getUserAll ();
    Optional<List<Member>> getUserByCongregation (Congregation congregation);
    void addUser (Member member);
    void modifyUser (Member member);
    void removeUser (String userId);
    void removeUser (Integer id);

    Optional<Exhibition> getExhibitionByName (String name);
    Optional<Exhibition> getExhibitionById (Integer id);
    Optional<List<Exhibition>> getExhibitionAll ();
    Optional<List<Exhibition>> getExhibitionByCongregation (Congregation congregation);
    void addExhibition (String name, String time, String guide, Integer limitation, Integer timeLimit, Congregation congregation);
    void addExhibition (Exhibition exhibition);
    void modifyExhibition (Exhibition exhibition);
    void removeExhibition (String name);
    void removeExhibition (Integer id);

    Optional<Congregation> getCongregationById (Integer id);
    Optional<Congregation> getCongregationByName (String name);
    Optional<List<Congregation>> getCongregationAll ();
    void addCongregation (Congregation congregation);
    void modifyCongregation (Congregation congregation);
    void removeCongregation (Congregation congregation);

    Optional<List<ReserveState>> getStateByUserId(String userId);

    /**
     *
     * @param exhibitionId
     * @param date 검색하고자 하는 날을 입력
     * @return
     */
    Optional<List<ReserveState>> getStateByExhibitionId(Integer exhibitionId, Timestamp date);
    Optional<List<ReserveState>> getStateByExhibitionName(String exhibitionName, Timestamp date);
    /**
     *
     * @param exhibitionId
     * @param start 시작 날짜
     * @param end 끝 날짜
     * @return
     */
    Optional<List<ReserveState>> getStateByExhibitionId(Integer exhibitionId, Timestamp start, Timestamp end);
    void addState(ReserveState state);
    void modifyState(ReserveState state);
    void removeState(Integer id);

    void dbDeleteAll();
}
