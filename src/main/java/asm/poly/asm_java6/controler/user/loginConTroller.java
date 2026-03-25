package asm.poly.asm_java6.controler.user;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;

@Controller
public class loginConTroller {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/login")
    public String login() {
        return "User/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String email,
                          @RequestParam("password") String password,
                          Model model,
                          HttpServletRequest request) {

        // 1. Check rỗng
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin");
            return "User/login";
        }

        // 2. Tìm user
        users user = usersRepository.findByEmail(email);

        if (user == null || !user.getMatKhau().equals(password)) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "User/login";
        }

        // ✅ 3. CHECK TRẠNG THÁI (true = bị khóa)
        if (Boolean.TRUE.equals(user.getTrangThai())) {
            model.addAttribute("errorMessage", "Tài khoản này đã bị khóa");
            return "User/login";
        }

        // ==============================
        // 🔥 SET ROLE
        // ==============================

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (Boolean.TRUE.equals(user.getVaiTro())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        // 🔥 set authentication
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 🔥 LƯU SESSION (QUAN TRỌNG)
        request.getSession().setAttribute(
                "SPRING_SECURITY_CONTEXT",
                SecurityContextHolder.getContext()
        );

        // ==============================

        // 4. Redirect theo role
        if (Boolean.TRUE.equals(user.getVaiTro())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}