package com.exhibition.reserve.web;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Exhibition;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.model.ReserveState;
import com.exhibition.reserve.service.FcmService;
import com.exhibition.reserve.service.JwtService;
import com.exhibition.reserve.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ReserveAPIController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment environment;

    @Autowired
    private FcmService fcmService;

    private Logger logger = LoggerFactory.getLogger(ReserveController.class);

    @GetMapping(value = "/profile")
    public String getProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("");
    }

    @RequestMapping(value = "/api/congregation", method = RequestMethod.GET)
    @ResponseBody
    public List<Congregation> getAllCongregation(
    ) {
        return repositoryService.getCongregationAll().orElse(null);
    }

    @RequestMapping(value = "/api/congregation", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveCongregation(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Congregation congregation
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.addCongregation(congregation);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/congregation", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> modifyCongregation(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Congregation congregation
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.modifyCongregation(congregation);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/congregation/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> deleteCongregation(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                Congregation con = repositoryService.getCongregationById(id).orElse(null);
                repositoryService.removeCongregation(con);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public List<Member> getAllUser(
            @RequestHeader(value = "accessToken") String accessToken
    ) {
        if (jwtService.checkJwt(accessToken)) {
            return repositoryService.getUserAll().orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/user/congregation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Member> getUserByCongregation(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        if (jwtService.checkJwt(accessToken)) {
            Congregation con = repositoryService.getCongregationById(id).orElse(null);
            return repositoryService.getUserByCongregation(con).orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Member member
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                member.setPw("000000");
                repositoryService.addUser(member);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> modifyUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Member member
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.modifyUser(member);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> deleteUser(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.removeUser(id);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(
            @RequestBody Member member
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("member", "");
        result.put("accessToken", "");

        try {
            Member rawMamber = repositoryService.getUserByIdByCongregation(member.getUserId(), member.getCongregation()).get();
            logger.info(rawMamber.getUserId() + "님이 로그인하였습니다.");
            String accessToken = jwtService.makeJwt(rawMamber);
            result.put("member", rawMamber);
            result.put("result", "success");
            result.put("accessToken", accessToken);
        } catch (NoSuchElementException e) {
            result.put("result", "noID");
        } catch (Exception e) {
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/fcmToken/{token}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveFcmToken(
            @PathVariable(value = "token") String fcmToken,
            @RequestBody Member member
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            Member rawMamber = repositoryService.getUserByIdByCongregation(member.getUserId(), member.getCongregation()).get();
            rawMamber.setFcmToken(fcmToken);
            repositoryService.modifyUser(rawMamber);
            result.put("result", "success");
        } catch (NoSuchElementException e) {
            result.put("result", "noID");
        } catch (Exception e) {
            result.put("result", "error");
        }
        return result;
    }

    @RequestMapping(value = "/api/exhibition", method = RequestMethod.GET)
    @ResponseBody
    public List<Exhibition> getAllExhibitions(
            @RequestHeader(value = "accessToken") String accessToken
    ) {
        if (jwtService.checkJwt(accessToken)) {
            return repositoryService.getExhibitionAll().orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/exhibition/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Exhibition getExhibitionById(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        if (jwtService.checkJwt(accessToken)) {
            return repositoryService.getExhibitionById(id).orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/exhibition/congregation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Exhibition> getExhibitionsByCongregation(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        if (jwtService.checkJwt(accessToken)) {
            Congregation con = repositoryService.getCongregationById(id).orElse(null);
            return repositoryService.getExhibitionByCongregation(con).orElse(null);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/exhibition", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveExhibition(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Exhibition exhibition
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.addExhibition(exhibition);
                //전시대 생성 알림
                fcmService.sendToTopic(exhibition.getCongregation(), "전시대 생성 알림", "새로운 전시대 마련이 생성되었습니다.");
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/exhibition", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> modifyExhibition(
            @RequestHeader(value = "accessToken") String accessToken,
            @RequestBody Exhibition exhibition
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.modifyExhibition(exhibition);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
    }

    @RequestMapping(value = "/api/exhibition/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> modifyExhibition(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            if (jwtService.checkJwt(accessToken)) {
                repositoryService.removeExhibition(id);
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "error");
        }

        return result;
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

    //month는 2018-12 형식의 String
    @RequestMapping(value = "/api/state/month/{id}/{month}", method = RequestMethod.GET)
    @ResponseBody
    public List<ReserveState> getReserveStatesMonth(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "month") String month
    ) {
        try {
            if (jwtService.checkJwt(accessToken)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(dateFormat.parse(month + "-01").getTime());
                Timestamp start = new Timestamp(cal.getTimeInMillis());
                int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                Timestamp end = new Timestamp(dateFormat.parse(month + "-" + lastDay).getTime());

                return repositoryService.getStateByExhibitionId(id, start, end).orElse(null);
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
                Congregation con = repositoryService.getCongregationById((Integer) jwtInfo.get("congregation")).orElse(null);
                Member member = repositoryService.getUserByIdByCongregation(String.valueOf(jwtInfo.get("id")), con).orElse(null);
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

    @RequestMapping(value = "/api/state/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteReserveStates(
            @RequestHeader(value = "accessToken") String accessToken,
            @PathVariable(value = "id") Integer id
    ) {
        Map<String, Object> result = new HashMap<>();
        if (jwtService.checkJwt(accessToken)) {
            try {
                repositoryService.removeState(id);
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
