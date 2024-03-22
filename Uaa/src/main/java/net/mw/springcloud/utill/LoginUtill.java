package net.mw.springcloud.utill;

import net.mw.springcloud.dao.UserDao;
import net.mw.springcloud.pojo.po.UserPO;
import net.mw.springcloud.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
    public Boolean Exists(String username){
        return dao.countByUserName(username) > 0;
    }
    public String loginHandle(String username, String pass, ReactiveUserDetailsService service){
        //生成token  返回
        String token = JwtUtils.getToken(username);
        UserDetails userDetails ;
        Mono<UserDetails> m = service.findByUsername(username);
        if(m == null){
            userDetails = null;
        }
        userDetails = m.block();
        //要放到security里面去
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,pass,userDetails.getAuthorities() );
        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
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
            }else{
                isComplete = dao.bindRole( userPO.getId(), "EDITOR") > 0;
            }
        }
        if(isComplete)
            res.put("token", loginHandle(username, pass, myUserDetailsService));
        res.put("isComplete", isComplete);
        return res;
    }
}
