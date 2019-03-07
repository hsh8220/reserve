package com.exhibition.reserve.service.impl;

import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.service.JwtService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private String secretKey = "hshomk20130831";

    private Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Override
    public String makeJwt(Member member) throws Exception{
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 30); //30분간 토큰 유효
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();

        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();

        map.put("id", member.getUserId());
        map.put("role", member.getRole());
        map.put("congregation", member.getCongregation().getId());

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    @Override
    public boolean checkJwt(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            logger.info("토큰 만료");
            return false;
        } catch (JwtException e) {
            logger.info("토큰 변조");
            return false;
        }
    }

    @Override
    public Map<String, Object> getJwtInfo(String jwt) {
        Map<String, Object> result = new HashMap<>();
        if(this.checkJwt(jwt)) {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody();
            result.put("id", claims.get("id"));
            result.put("role", claims.get("role"));
            result.put("congregation", claims.get("congregation"));
            return result;
        }else {
            return null;
        }
    }
}
