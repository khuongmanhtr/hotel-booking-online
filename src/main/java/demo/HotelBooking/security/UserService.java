package demo.HotelBooking.security;

import demo.HotelBooking.entity.Role;
import demo.HotelBooking.entity.User;
import demo.HotelBooking.entity.UserRole;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.UserRoleRepository;
import demo.HotelBooking.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleService roleService;

    public User findUserByUsername (String username) {
        return userRepository.findByUsername(username);
    }

    public Map<Object, Object> handleAllAccount(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<User> userList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("username", "Username");

        // Check null
        if (filter == null) {
            filter = "all";
        }

        if (searchValue == null) {
            searchValue = "";
        }
        // Check searchValue is not empty
        if (!searchValue.isEmpty()) {
            searchValue = searchValue.trim();
            switch (filter) {
                case "all":
                    break;
                case "username":
                    resultQuantity = userRepository.countAllAccountByUsername(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    userList = userRepository.getAllAccountByUsername(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = userRepository.countAllAccount();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            userList = userRepository.getAllAccount(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("userList", userList);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public void saveAccount(Map<Object, Object> account) {
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        String username = (String) account.get("username");
        String password = (String) account.get("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword("{sha256}" + passwordEncoder.encode(password));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public Map<Object, Object> getUserInformation(String username) {
        Map<Object, Object> userInfo = new LinkedHashMap<>();
        User user = findUserByUsername(username);

        userInfo.put("username", user.getUsername());
        userInfo.put("enabled", user.isEnabled());

        Set<UserRole> userRoles = user.getUserRoleList();
        if (userRoles.size() > 0) {
            List<String> roleList = new ArrayList<>();
            for (UserRole role : userRoles) {
                roleList.add(role.getRole().getName());
            }
            userInfo.put("roles", roleList);
        } else {
            userInfo.put("roles", userRoles);
        }

        return userInfo;
    }

    public void updateUser(Map<Object,Object> data) {
        String username = (String) data.get("username");
        boolean enabled = (boolean) data.get("enabled");

        User user = findUserByUsername(username);
        user.setEnabled(enabled);
        userRepository.save(user);

        Set<UserRole> userRoleList =  user.getUserRoleList();
        for (UserRole userRole : userRoleList) {
            userRoleRepository.delete(userRole);
        }

        List<String> roles = (List<String>) data.get("roles");
        for (String role : roles) {
            UserRole userRole = new UserRole();
            Role roleEntity = roleService.getRoleByName(role);

            userRole.setUser(user);
            userRole.setRole(roleEntity);

            userRoleRepository.save(userRole);
        }
    }

    public void deleteUser(String username) {
        User user = findUserByUsername(username);
        Set<UserRole> userRoleList =  user.getUserRoleList();
        for (UserRole userRole : userRoleList) {
            userRoleRepository.delete(userRole);
        }
        userRepository.delete(user);
    }

    public boolean changePassword(String username, String password) {
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        User user = findUserByUsername(username);
        if (user != null) {
            user.setPassword("{sha256}" + passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

}
