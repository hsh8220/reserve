package com.exhibition.reserve.web;

import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import com.exhibition.reserve.model.Role;
import com.exhibition.reserve.service.JwtService;
import com.exhibition.reserve.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@EnableAutoConfiguration
public class ReserveController {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    JwtService jwtService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Member getUser(
            @PathVariable("id") String id
    ) {
        return repositoryService.getUserById(id).orElse(null);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveUser(
            @RequestBody Member member
    ) {
        Map<String, String> result = new HashMap<>();
        try {
            Role role = new Role();
//            member.setPw(member.getPw());
            role.setRoleName("BASIC");
            member.setRole(role);
            repositoryService.addUser(member);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(
            @RequestBody Member member
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("member", "");
        result.put("accessToken", "");

        try {
            Member rawMamber = repositoryService.getUserById(member.getUserId()).get();
            String accessToken = jwtService.makeJwt(rawMamber);
            if (member.getPw().equals(rawMamber.getPw())) {
                result.put("member", rawMamber);
                result.put("result", "success");
                result.put("accessToken", accessToken);
            } else {
                result.put("result", "noPW");
            }
        } catch (NoSuchElementException e) {
            result.put("result", "noID");
        } catch (Exception e) {
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/exhibition", method = RequestMethod.GET)
    @ResponseBody
    public List<Exhibition> getExhibitions(
            @RequestHeader(value = "accessToken") String accessToken
    ) {
        if (jwtService.checkJwt(accessToken)) {
            return repositoryService.getExhibitionAll().orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/state/{id}/{date}", method = RequestMethod.GET)
    @ResponseBody
    public List<ReserveState> getReserveStates(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "date") String date
    ) {
        try {
            if (jwtService.checkJwt(accessToken)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Timestamp timestamp = new Timestamp((dateFormat.parse(date)).getTime());
                return repositoryService.getStateByExhibitionId(id, timestamp).orElse(null);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/api/state/{id}/{date}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setReserveStates(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "date") String date
    ) {
        Map<String, Object> result = new HashMap<>();
        if (jwtService.checkJwt(accessToken)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Timestamp timestamp = new Timestamp((dateFormat.parse(date)).getTime());
                Map<String, Object> jwtInfo = jwtService.getJwtInfo(accessToken);
                Member member = repositoryService.getUserById(String.valueOf(jwtInfo.get("id"))).orElse(null);
                Exhibition exhibition = repositoryService.getExhibitionById(id).orElse(null);

                ReserveState reserveState = new ReserveState();
                reserveState.setMember(member);
                reserveState.setExhibition(exhibition);
                reserveState.setStartTime(timestamp);
                reserveState.setEndTime(timestamp);
                repositoryService.addState(reserveState);
                result.put("result", "success");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.put("result", "error");
                return result;
            }
        } else {
            result.put("result", "error");
            return null;
        }
    }
}
