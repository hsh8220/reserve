package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.service.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {

    @Autowired
    JwtService jwtService;

    @Test
    public void jwtTest() {
        Congregation con = new Congregation();
        con.setId(1);
        con.setName("대방");

        Member member = new Member();
        member.setUserId("test");
        member.setPw("test");
        member.setName("test");
        member.setRole("BASIC");
        member.setCongregation(con);

        try {
            String jwt = jwtService.makeJwt(member);
            assertTrue(jwtService.checkJwt(jwt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
