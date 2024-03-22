package com.mw.springcloud.utill;

import com.alibaba.fastjson.JSON;
import com.mw.springcloud.utill.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        //通过token能不能拿到用户名
        String userName = null;
        try {
            userName = JwtUtils.getUserName(token);
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(e.getMessage()));
            out.flush();
            out.close();
            return;
        }
        UserDetails user = userDetailsService.loadUserByUsername(userName);
        if (JwtUtils.validate(token)) {
            //校验成功
            //将用户的凭证存入到security的上下文中
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userName,user.getPassword(),user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);
    }
}
