package com.accesshr.emsbackend.Config;

import com.accesshr.emsbackend.Entity.EmployeeManagerPrincipal;
import com.accesshr.emsbackend.Service.jwt.EmployeeManagerDetailService;
import com.accesshr.emsbackend.Service.jwt.JWTToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTToken jwtToken;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String email=null;


        if(authHeader !=null && authHeader.startsWith("Bearer ")){
           token= authHeader.substring(7);
           email=jwtToken.extractEmail(token);
        }

        if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            System.out.println(email);

            UserDetails userDetails = context.getBean(EmployeeManagerDetailService.class).loadUserByUsername(email);
            if(jwtToken.validateToken(token,userDetails)){

                UsernamePasswordAuthenticationToken authToken=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request,response);

    }
}