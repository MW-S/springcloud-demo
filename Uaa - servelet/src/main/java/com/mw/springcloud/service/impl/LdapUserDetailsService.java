package com.mw.springcloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.mw.springcloud.dao.UserDao;
import com.mw.springcloud.pojo.po.LdapUser;
import com.mw.springcloud.pojo.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LdapUserDetailsService implements UserDetailsService {
    @Autowired
    private LdapService service;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LdapUser user = service.get(username);
        if(user ==null){
            throw new UsernameNotFoundException("");
        }
        List<String> permission = service.getPermmissionByRole(user.getOu());
        //将permissions转成数组
        String[] permissionArray = new String[permission.size()];
        permission.toArray(permissionArray);
        UserDetails userDetails = User.withUsername(JSON.toJSONString(user)).password(user.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
