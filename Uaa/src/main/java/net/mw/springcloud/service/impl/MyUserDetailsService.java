package net.mw.springcloud.service.impl;

import com.alibaba.fastjson.JSON;
import net.mw.springcloud.dao.LdapDao;
import net.mw.springcloud.dao.UserDao;
import net.mw.springcloud.pojo.po.LdapUser;
import net.mw.springcloud.pojo.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.List;

@Service
public class MyUserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private UserDao dao;
    @Autowired
    private LdapDao service;
    /**
     * 当前用户
     * @return 当前用户
     */
    public UserPO currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return dao.getByUserName(username);
    }
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserPO user = dao.getByUserName(username);
        LdapUser l_user = service.get(username);
        if(user ==null && l_user == null){
            return null;
        }
        UserDetails userDetails = null;
        if(user != null){
            List<String> permission = dao.getPermissionById(user.getId());
            //将permissions转成数组
            String[] permissionArray = new String[permission.size()];
            permission.toArray(permissionArray);
            userDetails = User.withUsername(JSON.toJSONString(user)).password(user.getPassword()).authorities(permissionArray).build();
        }else if(l_user != null){
            List<String> permission = service.getPermmissionByRole(l_user.getOu());
            //将permissions转成数组
            String[] permissionArray = new String[permission.size()];
            permission.toArray(permissionArray);
            userDetails = User.withUsername(JSON.toJSONString(l_user)).password(l_user.getPassword()).authorities(permissionArray).build();
        }
        return Mono.just(userDetails);
    }
}
