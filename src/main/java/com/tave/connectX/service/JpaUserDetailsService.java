package com.tave.connectX.service;

import com.tave.connectX.config.CustomUserDetails;
import com.tave.connectX.entity.User;
import com.tave.connectX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByOauthId(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication")
        );

        return new CustomUserDetails(user);
    }
}
