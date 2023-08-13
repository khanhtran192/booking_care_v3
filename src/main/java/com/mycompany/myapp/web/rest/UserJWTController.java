package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.jwt.JWTFilter;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        UserRepository userRepository,
        DoctorRepository doctorRepository,
        HospitalRepository hospitalRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        Long doctorId = null;
        Long hospitalId = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findOneByLogin(authentication.getName()).orElse(new User());
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        Set<String> setAuthorities = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        if (setAuthorities.contains(AuthoritiesConstants.DOCTOR)) {
            Doctor doctor = doctorRepository.findDoctorByUserId(user.getId());
            doctorId = doctor == null ? null : doctor.getId();
        } else if (setAuthorities.contains(AuthoritiesConstants.HOSPITAL)) {
            Hospital hospital = hospitalRepository.findHospitalByUserId(user.getId());
            hospitalId = hospital == null ? null : hospital.getId();
        }
        return new ResponseEntity<>(new JWTToken(jwt, user, doctorId, hospitalId, setAuthorities), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private Set<String> authorities;
        private String username;
        private String name;
        private Long userId;
        private Long doctorId;
        private Long hospitalId;
        private String idToken;

        JWTToken(String idToken, User user, Long doctorId, Long hospitalId, Set<String> authorities) {
            this.idToken = idToken;
            this.username = user.getLogin();
            this.authorities = authorities;
            this.userId = user.getId();
            this.doctorId = doctorId;
            this.hospitalId = hospitalId;
            this.name = user.getFirstName() + " " + user.getLastName();
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Long doctorId) {
            this.doctorId = doctorId;
        }

        public Long getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(Long hospitalId) {
            this.hospitalId = hospitalId;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public Set<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(Set<String> authorities) {
            this.authorities = authorities;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
