/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Asus
 */
@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        com.example.model.User u = this.repo.findByUsername(string).
                orElseThrow(()
                        -> new UsernameNotFoundException("User Not Found"));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(u.getRole())
        );

        return new User(u.getUsername(), u.getPassword(), authorities);
    }
}
