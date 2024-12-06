package com.accesshr.emsbackend.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EmployeeManagerPrincipal implements UserDetails {


    private final EmployeeManager employeeManager;


    public EmployeeManagerPrincipal(EmployeeManager employeeManager){
        this.employeeManager=employeeManager;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return  Collections.singleton(new SimpleGrantedAuthority("admin"));
        return List.of(new SimpleGrantedAuthority("admin"),
                new SimpleGrantedAuthority("employee"));
    }

    @Override
    public String getPassword() {
        return employeeManager.getPassword();
    }

    @Override
    public String getUsername() {

        return employeeManager.getEmail();
    }


    public String getEmail() {
        return employeeManager.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
