package demo.HotelBooking.security;


import demo.HotelBooking.entity.User;
import demo.HotelBooking.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        List<GrantedAuthority> authorities = getUserAuthority(user.getUserRoleList());
        return buildUserForAuthentication(user,authorities);
    }

    public List<GrantedAuthority> getUserAuthority (Set<UserRole> userRoleSet) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (UserRole userRole : userRoleSet) {
            roles.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, authorities);
    }
}
