package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("Called loadUserByUsername method {}", email);
        if (Strings.isBlank(email)) {
            LOGGER.debug("Email is Blank, returned null");
            return null;
        }
        Optional<User> userOptional = userService.findUserByEmail(email);
        LOGGER.debug("Exists user : {}", userOptional.isPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LOGGER.debug("User {}", user);
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    authorities);
        }
        LOGGER.debug("Returned null");
        return null;
    }
}
