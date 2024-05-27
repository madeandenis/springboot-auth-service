package com.dendev.auth_service.services;

import com.dendev.auth_service.model.User;
import com.dendev.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(usernameOrEmail);
        if (user.isEmpty()){
            user = userRepository.findByEmail(usernameOrEmail);
        }
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User Not Found with username: " + usernameOrEmail);
        }
        return new UserDetailsImpl(user.get());
    }

}
