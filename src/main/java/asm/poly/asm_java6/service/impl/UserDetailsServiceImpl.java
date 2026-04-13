//package asm.poly.asm_java6.service.impl;
//
//import asm.poly.asm_java6.enity.users;
//import asm.poly.asm_java6.repository.UsersRepository;
//import asm.poly.asm_java6.service.CustomUserDetails;
//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UsersRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        // Map User entity sang CustomUserDetails
//        return new CustomUserDetails(
//                user.getId(),
//                user.getUsername(),
//                user.getPassword(),
//                user.getAvatar(),
//                user.getAuthorities()
//        );
//    }
//}