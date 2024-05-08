package com.itkon.school.security;


import com.itkon.school.model.Person;
import com.itkon.school.model.Roles;
import com.itkon.school.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ITKonSchoolPwdAuthProvider implements AuthenticationProvider {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Person person = personRepository.readByEmail(email);
        if (person != null && person.getPersonId() > 0 &&
                passwordEncoder.matches(pwd, person.getPwd())) {
            return new UsernamePasswordAuthenticationToken(
                    person.getEmail(),
                    null,
                    getGrantedAuthorities(person.getRoles())
            );
        } else {
            throw new BadCredentialsException("Invalid credentials!");
        }
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Roles roles) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
