package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Timestamp;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReserveApplicationTests {

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void userRepositioryTest() {

        Member member1 = new Member();
        member1.setUserId("test1");
        member1.setPw("000000");
        member1.setName("test1");
        member1.setRole("ADMIN");
        Member member2 = new Member();
        member2.setUserId("test2");
        member2.setPw("000000");
        member2.setName("test2");
        member2.setRole("BASIC");

        repositoryService.addUser(member1);
        repositoryService.addUser(member2);
        assertThat(repositoryService.getUserById("test1").map(Member::getName).orElse("not"), is("test1"));

        Member modiUser = repositoryService.getUserById("test2").orElse(null);
        modiUser.setName("test3");
        repositoryService.modifyUser(modiUser);
        assertThat(repositoryService.getUserById("test2").map(Member::getName).orElse("not"), is("test3"));

        repositoryService.removeUser("test1");
        assertThat(repositoryService.getUserById("test1").map(Member::getName).orElse("not"), is("not"));
        repositoryService.removeUser("test2");

    }

    @Test
    public void exhibitionRepositoryTest() {

        repositoryService.addExhibition("test1", "오전 9:30~11:30", "한상호");
        repositoryService.addExhibition("test2", "오전 9:30~11:30", "김서중");

        assertThat(repositoryService.getExhibitionByName("test1").map(Exhibition::getName).orElse("not"), is("test1"));

        Exhibition ex = repositoryService.getExhibitionByName("test1").get();
        ex.setName("test3");
        repositoryService.modifyExhibition(ex);
        assertThat(repositoryService.getExhibitionByName("test3").map(Exhibition::getName).orElse("not"), is("test3"));

        repositoryService.removeExhibition("test3");
        assertThat(repositoryService.getExhibitionByName("test3").map(Exhibition::getName).orElse("not"), is("not"));
        repositoryService.removeExhibition("test2");
        assertThat(repositoryService.getExhibitionByName("test2").map(Exhibition::getName).orElse("not"), is("not"));

    }

    @Test
    public void stateRepositoryTest() {

        Timestamp t1 = new Timestamp(118, 7, 7, 10, 0, 0, 0);
        Timestamp t2 = new Timestamp(118, 7, 7, 12, 0, 0, 0);
        Timestamp t3 = new Timestamp(118, 7, 7, 14, 0, 0, 0);
        Timestamp t4 = new Timestamp(118, 7, 7, 16, 0, 0, 0);
        Timestamp date = new Timestamp(118, 7, 7, 0, 0, 0, 0);

        Member member1 = new Member();
        member1.setUserId("test1");
        member1.setPw("1");
        member1.setName("test1");
        Member member2 = new Member();
        member2.setUserId("test2");
        member2.setPw("1");
        member2.setName("test2");
        Member member3 = new Member();
        member3.setUserId("test3");
        member3.setPw("1");
        member3.setName("test3");

        repositoryService.addUser(member1);
        repositoryService.addUser(member2);
        repositoryService.addUser(member3);
        repositoryService.addExhibition("spot1", "오전9시30분", "한상호");
        repositoryService.addExhibition("spot2", "오전9시30분", "김서중");

        ReserveState status1 = new ReserveState();
        status1.setStartTime(t1);
        status1.setEndTime(t2);
        status1.setMember(member1);
        status1.setExhibition(repositoryService.getExhibitionByName("spot1").get());
        ReserveState status2 = new ReserveState();
        status2.setStartTime(t3);
        status2.setEndTime(t4);
        status2.setMember(member2);
        status2.setExhibition(repositoryService.getExhibitionByName("spot1").get());
        ReserveState status3 = new ReserveState();
        status3.setStartTime(t3);
        status3.setEndTime(t4);
        status3.setMember(member3);
        status3.setExhibition(repositoryService.getExhibitionByName("spot2").get());

        repositoryService.addState(status1);
        repositoryService.addState(status2);
        repositoryService.addState(status3);

        assertThat(repositoryService.getStateByExhibitionName("spot2", date).map(List::size).orElse(0), is(1));

        assertThat(repositoryService.getStateByUserId("test2").map(List::size).orElse(0), is(1));

        status1.setExhibition(repositoryService.getExhibitionByName("spot2").get());
        repositoryService.modifyState(status1);
        assertThat(repositoryService.getStateByExhibitionName("spot2", date).map(List::size).orElse(0), is(2));

        repositoryService.removeState(status1.getId());
        assertThat(repositoryService.getStateByExhibitionName("spot2", date).map(List::size).orElse(0), is(1));

        repositoryService.removeUser("test2");
        assertThat(repositoryService.getStateByExhibitionName("spot1", date).map(List::size).orElse(-1), is(0));

        repositoryService.removeExhibition("spot2");
        assertThat(repositoryService.getStateByExhibitionName("spot2", date).map(List::size).orElse(-1), is(0));

        repositoryService.removeExhibition("spot1");
        repositoryService.removeUser("test1");
        repositoryService.removeUser("test3");
    }

}
