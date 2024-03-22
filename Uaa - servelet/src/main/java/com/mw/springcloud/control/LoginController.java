package com.mw.springcloud.control;

import com.mw.springcloud.service.impl.LdapService;
import com.mw.springcloud.service.impl.LdapUserDetailsService;
import com.mw.springcloud.service.impl.MyUserDetailsService;
import com.mw.springcloud.utill.JwtUtils;
import com.mw.springcloud.utill.LoginUtill;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    LoginUtill loginUtill;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    LdapUserDetailsService ldapDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    LdapService ldapService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @PostMapping("/memory/login")
    public Map memoryLogin(@RequestParam("username") String username, @RequestParam("pass") String pass){
        Map map = new HashMap();
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
            if(!passwordEncoder.matches(pass,userDetails.getPassword())){
                map.put("mess","密码错误");
                return map;
            }
            //生成token  返回
            String token =  loginUtill.loginHandle(username, pass, userDetailsService);
            map.put("mess","登录成功");
            map.put("token",token);
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
            return map;
        } catch (Exception e){
            map.put("mess", "登录失败！");
            System.out.print(e.getMessage());
        }
        return map;
    }

    @PostMapping("/ldap/login")
    public Map ldapLogin(@RequestParam("username") String username, @RequestParam("pass") String pass){
        Map map = new HashMap();
        try {
            if(!ldapService.authenticateUser(username, pass)){
                map.put("mess","密码错误");
                return map;
            }
            //生成token  返回
            String token =  loginUtill.loginHandle(username, pass, ldapDetailsService);
            map.put("mess","登录成功");
            map.put("token",token);
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
            return map;
        } catch (Exception e){
            map.put("mess", "登录失败！");
            e.printStackTrace();
        }
        return map;
    }

    @PostMapping("/jwt/login")
    public Map login(@RequestParam("username") String username, @RequestParam("pass") String pass){
        Map map = new HashMap();
        UserDetails userDetails = null;
        try {
            userDetails = myUserDetailsService.loadUserByUsername(username);
            if(userDetails == null){
                throw new UsernameNotFoundException("");
            }
            if(!passwordEncoder.matches(pass,userDetails.getPassword())){
                map.put("mess","密码错误");
                return map;
            }
            //生成token  返回
            String token =  loginUtill.loginHandle(username, pass, myUserDetailsService);
            map.put("mess","登录成功");
            map.put("token",token);
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
            return map;
        } catch (Exception e){
            map.put("mess", "登录失败！");
            System.out.print(e.getMessage().toString());
        }
        return map;
    }
}
