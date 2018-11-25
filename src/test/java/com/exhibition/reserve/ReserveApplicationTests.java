package com.exhibition.reserve;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import com.exhibition.reserve.service.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
	public void contextLoads() {
	}

	@Test
	public void userRepositioryTest() {
		Member member1 = new Member();
		member1.setUserId("test");
		member1.setPw("test");
		member1.setName("han");
		Member member2 = new Member();
		member2.setUserId("hsh");
		member2.setPw("test");
		member2.setName("한상호");

		repositoryService.addUser(member1);
		repositoryService.addUser(member2);
		assertThat(repositoryService.getUserById("test").map(Member::getName).orElse("not"), is("han"));
		assertThat(repositoryService.getUserAll().map(List::size).orElse(0), is(2));

		Member modiUser = repositoryService.getUserById("test").orElse(null);
		modiUser.setName("한상호");
		repositoryService.modifyUser(modiUser);
		assertThat(repositoryService.getUserById("test").map(Member::getName).orElse("not"), is("한상호"));

		repositoryService.removeUser("test");
		assertThat(repositoryService.getUserById("test").map(Member::getName).orElse("not"), is("not"));
	}

	@Test
	public void exhibitionRepositoryTest() {

		repositoryService.addExhibition("대방동", "오전 9:30~11:30", "한상호");
		repositoryService.addExhibition("신길역", "오전 9:30~11:30", "김서중");

		assertThat(repositoryService.getExhibitionByName("대방동").map(Exhibition::getName).orElse("not"), is("대방동"));
		assertThat(repositoryService.getExhibitionAll().map(List::size).orElse(0), is(2));

		Exhibition ex = repositoryService.getExhibitionByName("대방동").get();
		ex.setName("공군회관");
		repositoryService.modifyExhibition(ex);
		assertThat(repositoryService.getExhibitionByName("공군회관").map(Exhibition::getName).orElse("not"), is("공군회관"));

		repositoryService.removeExhibition("신길역");
		assertThat(repositoryService.getExhibitionByName("신길역").map(Exhibition::getName).orElse("not"), is("not"));

	}

	@Test
	public void stateRepositoryTest() {
		Timestamp t1 = new Timestamp(118, 7, 7, 10, 0, 0, 0);
		Timestamp t2 = new Timestamp(118, 7, 7, 12, 0, 0, 0);
		Timestamp t3 = new Timestamp(118, 7, 7, 14, 0, 0, 0);
		Timestamp t4 = new Timestamp(118, 7, 7, 16, 0, 0, 0);
		Timestamp date = new Timestamp(118, 7, 7, 0, 0, 0, 0);

		Member member1 = new Member();
		member1.setUserId("hsh8220");
		member1.setPw("vktneo00");
		member1.setName("한상호");
		Member member2 = new Member();
		member2.setUserId("test1");
		member2.setPw("test1");
		member2.setName("오민경");
		Member member3 = new Member();
		member3.setUserId("test2");
		member3.setPw("test2");
		member3.setName("이대현");

		repositoryService.addUser(member1);
		repositoryService.addUser(member2);
		repositoryService.addUser(member3);
		repositoryService.addExhibition("대방역", "오전9시30분", "한상호");
		repositoryService.addExhibition("신길역", "오전9시30분", "김서중");

		ReserveState status1 = new ReserveState();
		status1.setStartTime(t1);
		status1.setEndTime(t2);
		status1.setMember(member1);
		status1.setExhibition(repositoryService.getExhibitionByName("대방역").get());
		ReserveState status2 = new ReserveState();
		status2.setStartTime(t3);
		status2.setEndTime(t4);
		status2.setMember(member2);
		status2.setExhibition(repositoryService.getExhibitionByName("신길역").get());
		ReserveState status3 = new ReserveState();
		status3.setStartTime(t3);
		status3.setEndTime(t4);
		status3.setMember(member3);
		status3.setExhibition(repositoryService.getExhibitionByName("신길역").get());

		repositoryService.addState(status1);
		repositoryService.addState(status2);
		repositoryService.addState(status3);

		assertThat(repositoryService.getStateByExhibitionName("신길역", date).map(List::size).orElse(0), is(2));

		assertThat(repositoryService.getStateByUserId("hsh8220").map(List::size).orElse(0), is(1));

		status1.setExhibition(repositoryService.getExhibitionByName("신길역").get());
		repositoryService.modifyState(status1);
		assertThat(repositoryService.getStateByExhibitionName("신길역", date).map(List::size).orElse(0), is(3));

		repositoryService.removeState(status1.getId());
		assertThat(repositoryService.getStateByExhibitionName("신길역", date).map(List::size).orElse(0), is(2));
	}

}
