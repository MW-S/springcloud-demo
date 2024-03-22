package com.mw.springcloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.mw.springcloud.dao.UserDao;
import com.mw.springcloud.pojo.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao dao;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPO user = dao.getByUserName(username);
        if(user ==null){
            return null;
        }
        List<String> permission = dao.getPermissionById(user.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permission.size()];
        permission.toArray(permissionArray);
        UserDetails userDetails = User.withUsername(JSON.toJSONString(user)).password(user.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
