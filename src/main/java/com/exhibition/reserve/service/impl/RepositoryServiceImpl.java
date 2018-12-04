package com.exhibition.reserve.service.impl;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import com.exhibition.reserve.repository.ExhibitionRepository;
import com.exhibition.reserve.repository.ReserveStateRepository;
import com.exhibition.reserve.repository.MemberRepository;
import com.exhibition.reserve.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    private Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReserveStateRepository reserveStateRepository;

    @Autowired
    ExhibitionRepository exhibitionRepository;

    @Override
    public Optional<Member> getUserById(String userId) {
        return Optional.ofNullable(memberRepository.findByUserId(userId));
    }

    @Override
    public Optional<List<Member>> getUserByName(String name) {
        return Optional.ofNullable(memberRepository.findByName(name));
    }

    @Override
    public Optional<List<Member>> getUserAll() {
        return Optional.ofNullable(memberRepository.findAll(new Sort(Sort.Direction.ASC, "name")));
    }

    @Override
    public void addUser(Member member) {
        memberRepository.save(member);
        logger.info(member.getUserId() + "사용자 추가.");
    }

    @Override
    public void modifyUser(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void removeUser(String userId) {
        Member member = memberRepository.findByUserId(userId);
        reserveStateRepository.deleteByMember(member);
        memberRepository.delete(member);
        logger.info(member.getUserId() + "사용자 삭제.");
    }

    @Override
    public void removeUser(Integer id) {
        Member member = memberRepository.findById(id).orElse(null);
        reserveStateRepository.deleteByMember(member);
        memberRepository.deleteById(id);
        logger.info(member.getUserId() + "사용자 삭제.");
    }

    @Override
    public Optional<Exhibition> getExhibitionByName(String name) {
        return Optional.ofNullable(exhibitionRepository.findByName(name));
    }

    @Override
    public Optional<Exhibition> getExhibitionById(Integer id) {
        return exhibitionRepository.findById(id);
    }

    @Override
    public Optional<List<Exhibition>> getExhibitionAll() {
        return Optional.ofNullable(exhibitionRepository.findAll(new Sort(Sort.Direction.ASC, "name")));
    }

    @Override
    public void addExhibition(String name, String time, String guide) {
        Exhibition exhibition  = new Exhibition();
        exhibition.setName(name);
        exhibition.setTime(time);
        exhibition.setGuide(guide);
        exhibitionRepository.save(exhibition);
        logger.info(name + "전시대 추가");
    }

    @Override
    public void addExhibition(Exhibition exhibition) {
        exhibitionRepository.save(exhibition);
        logger.info(exhibition.getName() + "전시대 추가");
    }

    @Override
    public void modifyExhibition(Exhibition exhibition) {
        exhibitionRepository.save(exhibition);
    }

    @Override
    public void removeExhibition(String name) {
        Exhibition exhibition = exhibitionRepository.findByName(name);
        reserveStateRepository.deleteByExhibition(exhibition);
        exhibitionRepository.delete(exhibition);
        logger.info(name + "전시대 삭제");
    }

    @Override
    public void removeExhibition(Integer id) {
        Exhibition exhibition = exhibitionRepository.findById(id).orElse(null);
        reserveStateRepository.deleteByExhibition(exhibition);
        exhibitionRepository.delete(exhibition);
        logger.info(exhibition.getName() + "전시대 삭제");
    }

    @Override
    public Optional<List<ReserveState>> getStateByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId);
        return Optional.ofNullable(reserveStateRepository.findByMember(member));
    }

    @Override
    public Optional<List<ReserveState>> getStateByExhibitionId(Integer exhibitionId, Timestamp date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Timestamp start = new Timestamp(c.getTimeInMillis());

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Timestamp end = new Timestamp(c.getTimeInMillis());

        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).orElse(null);

        return Optional.ofNullable(reserveStateRepository.findByExhibitionAndStartTimeBetween(exhibition, start, end));
    }

    @Override
    public Optional<List<ReserveState>> getStateByExhibitionName(String exhibitionName, Timestamp date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Timestamp start = new Timestamp(c.getTimeInMillis());

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Timestamp end = new Timestamp(c.getTimeInMillis());

        Exhibition exhibition = Optional.ofNullable(exhibitionRepository.findByName(exhibitionName)).orElse(null);

        return Optional.ofNullable(reserveStateRepository.findByExhibitionAndStartTimeBetween(exhibition, start, end));
    }

    @Override
    public void addState(ReserveState state) {
        reserveStateRepository.save(state);
    }

    @Override
    public void modifyState(ReserveState state) {
        reserveStateRepository.save(state);
    }

    @Override
    public void removeState(Integer id) {
        reserveStateRepository.deleteById(id);
    }
}
