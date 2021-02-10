package com.haoxuan.demo.Auth;

import com.haoxuan.demo.Entity.UserTable;
import com.haoxuan.demo.Helper.Helper;
import com.haoxuan.demo.MySQL.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // 1. query user
        UserTable info = DB.getInstance().queryUser(login);
        if(info==null) {
            throw new UsernameNotFoundException("User " + login + " was not found in db");
        }
        // 2. set role
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        //Encrypt the password
        Helper help = new Helper();
        String decodePwd=help.BDecrypt(info.getPassword(),info.getSalt());

        return new org.springframework.security.core.userdetails.User(login,
                decodePwd, grantedAuthorities);
    }


}
