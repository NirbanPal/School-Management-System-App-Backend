package com.example.SMSApp.service;

import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Role;
import com.example.SMSApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String publicId) throws UsernameNotFoundException {
        try {
            UUID uuid = UUID.fromString(publicId);
            AppUser user = userRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + publicId));
            System.out.println("Working with UUID");
            return new User(
                    user.getPublicId().toString(),
                    user.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        } catch (IllegalArgumentException e) {
            // If publicId is not a valid UUID, try to find by email It will work at the time of login
            AppUser user = userRepository.findByEmail(publicId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + publicId));
            System.out.println("Working with Email");

            return new User(
                    user.getPublicId().toString(),
                    user.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }
    }

    public AppUser createAdmin(String email, String password) {
        AppUser user = AppUser.builder()
                .email(email)
                .password(password)
                .role(Role.ADMIN)
                .build();

        return userRepository.save(user);
    }
}
