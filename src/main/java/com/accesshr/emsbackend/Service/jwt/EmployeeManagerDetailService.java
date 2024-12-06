package com.accesshr.emsbackend.Service.jwt;


import com.accesshr.emsbackend.Entity.EmployeeManager;
import com.accesshr.emsbackend.Entity.EmployeeManagerPrincipal;
import com.accesshr.emsbackend.Repo.EmployeeManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManagerDetailService implements UserDetailsService {

    @Autowired
    private EmployeeManagerRepository employeeManagerRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmployeeManager user=employeeManagerRepository.findByCorporateEmail(email);

        if(user==null){
            System.out.println("User Data is not found");
            throw new UsernameNotFoundException("User Data is not found");
        }
        return new EmployeeManagerPrincipal(user);
    }
}
