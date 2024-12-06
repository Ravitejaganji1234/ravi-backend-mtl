package com.accesshr.emsbackend.Service.jwt;


import com.accesshr.emsbackend.Entity.EmployeeManager;
import com.accesshr.emsbackend.Repo.EmployeeManagerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTToken {

    @Autowired
    private EmployeeManagerRepository employeeManagerRepository;
    private String secretkey = "";

    public JWTToken() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String email) {
        EmployeeManager employeeData = employeeManagerRepository.findByCorporateEmail(email);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", employeeData.getEmail());
        claims.put("firstName", employeeData.getFirstName());
        claims.put("lastName", employeeData.getLastName());
        claims.put("role", employeeData.getRole());
        System.out.println(60 * 60 * 30);

//        return Jwts.builder()
//                .setSubject(email)  // Set the subject (usually email)
//                .claim("email", email)  // Set the email claim
//                .claim("firstName", firstName)  // Set the first name claim
//                .claim("lastName", lastName)  // Set the last name claim
//                .claim("role", role)  // Set the role claim
//                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the issue time
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // Set expiration (1 hour)
//                .signWith(getKey())  // Sign the token with the private key
//                .compact();  // Return the compact JWT token string

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 30 * 10))
                .and()
                .signWith(getKey())
                .compact();
    }



    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractEmail(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractEmail(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}
