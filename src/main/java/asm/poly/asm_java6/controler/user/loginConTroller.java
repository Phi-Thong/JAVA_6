package asm.poly.asm_java6.controler.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import asm.poly.asm_java6.config.JwtUtils;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class loginConTroller {

@Autowired
private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;

    // Hiển thị trang login
    @GetMapping("/login")
    public String login() {
        return "User/login";
    }

    // Xử lý login thủ công
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String email,
                          @RequestParam("password") String password,
                          @RequestParam(value = "remember-me", required = false) String rememberMe,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        System.out.println("rememberMe = " + rememberMe);

        // 1️ Check rỗng
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin");
            return "User/login";
        }

        // 2️ Tìm user theo email
        users user = usersRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "User/login";
        }

        if (!passwordEncoder.matches(password, user.getMatKhau())) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "User/login";
        }


        // 3️⃣ Check trạng thái (true = bị khóa)
        if (Boolean.TRUE.equals(user.getTrangThai())) {
            model.addAttribute("errorMessage", "Tài khoản này đã bị khóa");
            return "User/login";
        }

        // 4️⃣ Xác định ROLE
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (Boolean.TRUE.equals(user.getVaiTro())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // 5️⃣ Tạo UserDetails để Spring Security nhận diện
        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getMatKhau())
                        .authorities(authorities)
                        .build();

        // 6️⃣ Tạo Authentication token
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

       // Sau khi xác thực thành công:
        List<String> roles = new ArrayList<>();
        roles.add(user.getVaiTro() ? "ROLE_ADMIN" : "ROLE_USER");
        String token = jwtUtils.generateToken(user.getEmail(), roles, user.getHoTen(), null);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(cookie);

        // 9️⃣ Redirect theo role
        if (Boolean.TRUE.equals(user.getVaiTro())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/home";
        }
    }

    // Root redirect về login
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}