package demo.HotelBooking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/staff/**").hasRole("STAFF")
                    .antMatchers("/manager/**").hasRole("MANAGER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/changePassword").authenticated()
                    .antMatchers("/").permitAll()
                .and()
                    .formLogin()
                        .loginPage("/signIn")
                        .loginProcessingUrl("/")
                        .failureUrl("/loginError")
                        .permitAll()
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/forbidden")
                .and()
                    .rememberMe()
                    .key("uniqueAndSecret")
                    .tokenValiditySeconds(1*24*60*60)
                .and()
                .logout()
                    .logoutUrl("/performLogout")
                    .logoutSuccessUrl("/");
//                    .permitAll();

    }
}
