package com.mw.springcloud.service.impl;

import com.mw.springcloud.pojo.po.LdapUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LdapService {

    @Autowired
    private LdapTemplate ldapTemplate;

    private final  String baseDn = "ou=people,dc=test,dc=com";

/*    public boolean authenticateUser(String username, String password) {
        String baseDn = "ou=people,dc=test,dc=com";
        String filter = "(uid=" + username + ")";
        return ldapTemplate.authenticate(baseDn, filter, password);
    }*/

    public boolean authenticateUser(String username, String password) {
        String filter = "(uid=" + username + ")";
        return ldapTemplate.authenticate("", filter, password);
    }

    public LdapUser get(String username) {
        LdapQuery query = LdapQueryBuilder.query()
                .where("uid")
                .is(username);
        List<LdapUser> list =  ldapTemplate.find(query, LdapUser.class);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<String> getPermmissionByRole(String ou){
        List<String> lst = new ArrayList<>();
        ou = ou.toLowerCase();
        Set<String> set = new HashSet<>();
        if(ou.equals("user")){
            set.add("read");
        }
        if(ou.equals("editor")){
            set.add("create");
            set.add("delete");
            set.add("update");
        }
        if(ou.equals("product_admin")){
            set.add("create");
            set.add("delete");
            set.add("update");
        }
        lst.addAll(set);
        return lst;
    }

}

