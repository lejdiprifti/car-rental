package com.ikubinfo.rental.util;

import com.ikubinfo.rental.entity.RoleEntity;
import com.ikubinfo.rental.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TokenCreator {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void createAdminToken() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        String token = jwtTokenUtil.generateToken(createUserDetails(FakeUsers.ADMIN.username, FakeUsers.ADMIN.password), roleEntity);
        jwtTokenUtil.validateToken(token, createUserDetails(FakeUsers.ADMIN.username, FakeUsers.ADMIN.password));
    }

    private UserDetails createUserDetails(String username, String password) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "Admin123_";
            }

            @Override
            public String getUsername() {
                return "admintester";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
