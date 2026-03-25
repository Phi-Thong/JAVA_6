package asm.poly.asm_java6.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        users user = usersRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (Boolean.TRUE.equals(user.getVaiTro())) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        // In ra thông tin user và quyền
        System.out.println("Đăng nhập với email: " + email);
        System.out.println("Mật khẩu: " + user.getMatKhau());
        System.out.println("Vai trò: " + (user.getVaiTro() ? "ADMIN" : "USER"));
        System.out.println("Trạng thái: " + (user.getTrangThai() ? "Bị khóa" : "Hoạt động"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getMatKhau(), authorities
        );
    }
}