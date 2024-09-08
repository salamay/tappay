package com.tappay.service.security.MySecurityFilter;

import com.tappay.service.security.Jwt.Jwtutils;
import com.tappay.service.webservice.User.MyUserDetail;
import com.tappay.service.webservice.User.MyUserDetailsService;
import com.tappay.service.webservice.User.model.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class TokenFilter extends OncePerRequestFilter {
    private final String TAG = "[TokenFilter]:--> ";
    @Autowired
    private Jwtutils jwtutils;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        System.out.println(TAG+httpServletRequest.getRemoteAddr()+":Making request");
        final String authHeader=httpServletRequest.getHeader("Authorization");
        String email=null;
        String token = null;
        int uid=0;
        if (authHeader!=null&&authHeader.startsWith("Bearer")){
            token=authHeader.substring(7);
            email=jwtutils.extractEmail(token);
            uid=Integer.parseInt(jwtutils.extractUid(token));
        }
        if (email!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            MyUserDetail userDetails= (MyUserDetail) myUserDetailsService.loadUserByUsername(email);
            if (userDetails!=null){
                if (jwtutils.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                UserModel userModel=new UserModel();
                userModel.setEmail(email);
                userModel.setUid(uid);
                userModel.setPassword("");
                userModel.setDevice_token(userDetails.getDevice_token());
                userModel.setAccount_status(userDetails.getAccount_status());
                userModel.setEnabled(userDetails.isEnabled());
                userModel.setRoles(userDetails.getAuthorities().stream().map(Object::toString).reduce((a,b)->a+","+b).get());
                httpServletRequest.setAttribute("user",userModel);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
