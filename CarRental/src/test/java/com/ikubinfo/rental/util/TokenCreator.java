package com.ikubinfo.rental.util;

import com.ikubinfo.rental.entity.RoleEntity;
import com.ikubinfo.rental.security.jwt_configuration.JwtTokenUtil;
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
        UserDetails userDetails = createUserDetails(FakeUsers.ADMIN.username, FakeUsers.ADMIN.password);
        String token = jwtTokenUtil.generateToken(userDetails, roleEntity);
        jwtTokenUtil.validateToken(token, userDetails);
    }

    public void createUserToken() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2);
        UserDetails userDetails = createUserDetails(FakeUsers.USER.username, FakeUsers.USER.password);
        String token = jwtTokenUtil.generateToken(userDetails, roleEntity);
        jwtTokenUtil.validateToken(token, userDetails);
    }

    private UserDetails createUserDetails(String username, String password) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return username;
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
