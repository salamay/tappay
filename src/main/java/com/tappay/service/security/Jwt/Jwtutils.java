package com.tappay.service.security.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class Jwtutils {
    private final String SECRET_KEY=System.getenv("tappay_auth");

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public String extractUid(String token){
        return extractClaim(token, Claims::getAudience);
    }

    public Date extractExpiration(String token){ return extractClaim(token,Claims::getExpiration); }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims=extractAllClaim(token);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaim(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails,String uid){
        Map<String,Object> claims=new HashMap<>();
        return CreateToken(claims,userDetails,uid);
    }
    private String CreateToken( Map<String,Object> claims,UserDetails userDetails,String uid){
        return Jwts.builder().addClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000L *60*60*24*364))
                .setAudience(uid)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }
    public Boolean validateToken(String token,UserDetails userDetails){
        final String email=extractEmail(token);
        return (email.equals(userDetails.getUsername())&&! isTokenExpired(token));
    }
    public String extractUidForEmailVerification(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public String generateTokenForEmailVerification(String uid){
        Map<String,Object> claims=new HashMap<>();
        return CreateTokenForEmailVerification(claims,uid);
    }
    //Uid is used here
    private String CreateTokenForEmailVerification( Map<String,Object> claims,String uid){
        return Jwts.builder().addClaims(claims).setSubject(uid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000L *60*60*24*365))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }
    public Boolean validateTokenForVerification(String token){
        return (!isTokenExpired(token));
    }

}
