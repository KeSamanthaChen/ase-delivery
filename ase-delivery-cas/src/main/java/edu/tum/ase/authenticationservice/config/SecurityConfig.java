package edu.tum.ase.authenticationservice.config;


import edu.tum.ase.authenticationservice.filter.AuthRequestFilter;
import edu.tum.ase.authenticationservice.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Order(3)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthRequestFilter authRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
               .cors().and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests().antMatchers("/api/auth/credentials/**").hasAuthority(UserRole.DISPATCHER)
                .antMatchers("/api/auth/**").permitAll().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Added that filter only applied in this configuration
    @Bean
    public FilterRegistrationBean<AuthRequestFilter> authFilter(){
        FilterRegistrationBean<AuthRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(this.authRequestFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }


}
