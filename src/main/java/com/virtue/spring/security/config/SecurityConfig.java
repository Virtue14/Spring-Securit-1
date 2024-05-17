package com.virtue.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_C > ROLE_B > ROLE_A");

        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 경로 인가
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/").hasAnyRole("A") // A B C
                        .requestMatchers("/manager").hasAnyRole("B") // B C
                        .requestMatchers("/admin").hasAnyRole("C") // C
                        .anyRequest().authenticated()
                );

        // 폼 로그인 방식
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        // HttpBasic
//        http
//                .httpBasic(Customizer.withDefaults());

        // csrf
//        http
//                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        // true : 초과 시 새로운 로그인 차단
        // false : 초과시 기존 세션 하나 삭제

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
        // 설명: 세션 고정보호
        // none : 로그인 시 세션 정보 변경 안함
        // newSession : 로그인 시 세션 새로 생성
        // changeSessionId : 로그인 시 동일한 세션에 대한 id 변경 -> 주로 쓰임

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout"));

        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.builder()
                .username("user1")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("C")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("B")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

}
