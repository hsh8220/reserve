package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Member;

import java.util.Map;

public interface JwtService {
    public String makeJwt(Member member) throws Exception;
    public boolean checkJwt(String jwt);
    public Map<String, Object> getJwtInfo(String jwt);
}
