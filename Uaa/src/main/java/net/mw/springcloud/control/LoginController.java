package net.mw.springcloud.control;

import net.mw.springcloud.dao.LdapDao;
import net.mw.springcloud.pojo.vo.UserVO;
//import com.mw.springcloud.service.impl.LdapUserDetailsService;
import net.mw.springcloud.service.impl.MyUserDetailsService;
import net.mw.springcloud.utill.LoginUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    LoginUtill loginUtill;
    @Autowired
    MyUserDetailsService myUserDetailsService;
/*    @Autowired
    LdapUserDetailsService ldapDetailsService;*/
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    LdapDao ldapDao;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
   /* @PostMapping("/memory/login")
    public Mono<Map> memoryLogin(@RequestBody UserVO u){
        Map map = new HashMap();
        UserDetails userDetails = null;
        String username = u.getUsername();
        String pass = u.getPassword();
        try {
            Mono<UserDetails> m = userDetailsService.findByUsername(username);
            if(m == null){
                throw new UsernameNotFoundException("");
            }
            userDetails =m.block();
            if(!passwordEncoder.matches(pass,userDetails.getPassword())){
                map.put("mess","密码错误");
                return Mono.just(map);
            }
            //生成token  返回
            String token =  loginUtill.loginHandle(username, pass, userDetailsService);
            map.put("mess","登录成功");
            map.put("token",token);
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
        } catch (Exception e){
            map.put("mess", "登录失败！");
            System.out.print(e.getMessage());
        }
        return Mono.just(map);
    }*/

    @PostMapping("/ldap/login")
    public Mono<Map> ldapLogin(@RequestBody UserVO u){
        Map map = new HashMap();
        String username = u.getUsername();
        String pass = u.getPassword();
        try {
            if(ldapDao.get(username) == null){
                throw new UsernameNotFoundException("");
            }
            if(!ldapDao.authenticateUser(username, pass)){
                map.put("mess","密码错误");
                return Mono.just(map);
            }
            //生成token  返回
            String token =  loginUtill.loginHandle(username, pass, myUserDetailsService);
            map.put("mess","登录成功");
            map.put("token",token);
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
        } catch (Exception e){
            map.put("mess", "登录失败！");
            e.printStackTrace();
        }
        return Mono.just(map);
    }

    @PostMapping("/jwt/login")
    public Mono<Map> login(@RequestBody UserVO u){
        Map map = new HashMap();
        String username = u.getUsername();
        String pass = u.getPassword();
        UserDetails userDetails = null;
        try {
                if(!loginUtill.Exists(username)){
                    throw new UsernameNotFoundException("");
                }
                Mono<UserDetails> m  =  myUserDetailsService.findByUsername(username);
                userDetails = m.block();
                if(userDetails == null){
                    throw new UsernameNotFoundException("");
                }
                if(!passwordEncoder.matches(pass,userDetails.getPassword())){
                    map.put("mess","密码错误");
                }else{
                    String token =  loginUtill.loginHandle(username, pass, myUserDetailsService);
                    map.put("mess","登录成功");
                    map.put("token",token);
                }
        } catch (UsernameNotFoundException e) {
            map.put("mess","账号不存在");
        } catch (Exception e){
            map.put("mess", "登录失败！");
            System.out.print(e.getMessage().toString());
        }
        return Mono.just(map);
    }
}
