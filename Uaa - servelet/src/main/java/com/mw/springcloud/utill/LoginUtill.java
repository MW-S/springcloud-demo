package com.mw.springcloud.utill;

import com.mw.springcloud.dao.UserDao;
import com.mw.springcloud.pojo.po.UserPO;
import com.mw.springcloud.service.impl.MyUserDetailsService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginUtill {
    @Autowired
    private UserDao dao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    public String loginHandle(String username, String pass, UserDetailsService service){
        //生成token  返回
        String token = JwtUtils.getToken(username);
        UserDetails userDetails = service.loadUserByUsername(username);
        //要放到security里面去
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,pass,userDetails.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return token;
    }

    public Map<String, Object> addUser(String username, String pass){
        Map res = new HashMap<String, Object>();
        boolean isComplete = true;
        if(dao.countByUserName(username) == 0){
            //生成token  返回
            UserPO userPO = new UserPO();
            userPO.setUsername(username);
            userPO.setPassword(passwordEncoder.encode(pass));
            if(dao.insert(userPO) == 0){
                isComplete =  false;
            }
        }
        if(isComplete)
            res.put("token", loginHandle(username, pass, myUserDetailsService));
        res.put("isComplete", isComplete);
        return res;
    }
}
