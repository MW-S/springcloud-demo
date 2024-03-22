package com.mw.springcloud.control;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mw.springcloud.dao.UserDao;
import com.mw.springcloud.pojo.po.UserPO;
import com.mw.springcloud.service.impl.MyUserDetailsService;
import com.mw.springcloud.utill.AccessTokenResponse;
import com.mw.springcloud.utill.JwtUtils;
import com.mw.springcloud.utill.LoginUtill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String clientSecret;
    @Autowired
    private LoginUtill loginUtill;

    @GetMapping("/oauth/redirect")
    public Map handleRedirect(@RequestParam("code") String requestToken) {
        Map res = new HashMap();
        // 使用RestTemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        // 获取Token的Url
        String tokenUrl = "https://github.com/login/oauth/access_token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + requestToken;

        // 使用restTemplate向GitHub发送请求，获取Token
        AccessTokenResponse tokenResponse = restTemplate.postForObject(tokenUrl, null, AccessTokenResponse.class);

        // 从响应体中获取Token数据
        String accessToken = tokenResponse.getAccessToken();

        // 携带Token向GitHub发送请求
        String apiUrl = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        JSONObject obj = JSON.parseObject(response.getBody());
        String username = obj.getString("login");
        String pass = obj.getString("node_id");
        if(!ObjectUtil.isAllEmpty(username)){
            Map temp = loginUtill.addUser(username, pass);
            if((Boolean)temp.get("isComplete")){
                res.put("token", temp.get("token"));
                res.put("code", 200);
                res.put("msg", "登录成功！");
            }else{
                res.put("code", 101);
                res.put("msg", "用户创建失败！");
            }
            return res;
        }else{
            res.put("code", 100);
            res.put("msg", "登录失败！");
            return res;
        }
    }

    @GetMapping("/loginError")
    public String error(HttpServletRequest request, Model model){
        //security框架里存的
        AuthenticationException authenticationException = (AuthenticationException) request.getSession()
                .getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (authenticationException instanceof UsernameNotFoundException || authenticationException instanceof BadCredentialsException) {
            model.addAttribute("msg","用户名或密码错误");
        } else if (authenticationException instanceof DisabledException) {
            model.addAttribute("msg","用户已被禁用");
        } else if (authenticationException instanceof LockedException) {
            model.addAttribute("msg","账户被锁定");
        } else if (authenticationException instanceof AccountExpiredException) {
            model.addAttribute("msg","账户过期");
        } else if (authenticationException instanceof CredentialsExpiredException) {
            model.addAttribute("msg","证书过期");
        }
        return "pages/login";
    }

    @GetMapping("/success")
    public String success(){
        return "登录验证成功";
    }
}
