//package com.mw.springcloud.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.mw.springcloud.dao.LdapDao;
//import com.mw.springcloud.dao.UserDao;
//import com.mw.springcloud.pojo.po.LdapUser;
//import com.mw.springcloud.pojo.po.UserPO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Service
//public class LdapUserDetailsService implements ReactiveUserDetailsService {
//
//    @Autowired
//    private LdapDao service;
//
//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        LdapUser user = service.get(username);
//        if( user == null){
//            return null;
//        }
//        List<String> permission = service.getPermmissionByRole(user.getOu());
//        //将permissions转成数组
//        String[] permissionArray = new String[permission.size()];
//        permission.toArray(permissionArray);
//        UserDetails userDetails = User.withUsername(JSON.toJSONString(user)).password(user.getPassword()).authorities(permissionArray).build();
//        return Mono.just(userDetails);
//    }
//}
