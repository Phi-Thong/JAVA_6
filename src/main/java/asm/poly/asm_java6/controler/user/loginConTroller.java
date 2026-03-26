package asm.poly.asm_java6.controler.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class loginConTroller {

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

        // 1️⃣ Check rỗng
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin");
            return "User/login";
        }

        // 2️⃣ Tìm user theo email
        users user = usersRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
            return "User/login";
        }

        // 2a️⃣ Xử lý mật khẩu {noop} nếu có
        String dbPassword = user.getMatKhau();
        if (dbPassword.startsWith("{noop}")) {
            dbPassword = dbPassword.substring(6); // loại bỏ {noop}
        }

        // 2b️⃣ So sánh mật khẩu
        if (!dbPassword.equals(password)) {
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
                        .password(dbPassword)
                        .authorities(authorities)
                        .build();

        // 6️⃣ Tạo Authentication token
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        // 7️⃣ Lưu Authentication vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 8️⃣ Lưu vào session để các request sau nhận biết user đã login
        request.getSession().setAttribute(
                "SPRING_SECURITY_CONTEXT",
                SecurityContextHolder.getContext()
        );

        // 8.1️⃣ Nếu có tick "Ghi nhớ đăng nhập", set cookie remember-me
        if (rememberMe != null) {
            String token = user.getEmail() + ":" + dbPassword;
            String encoded = java.util.Base64.getEncoder().encodeToString(token.getBytes());
             System.out.println("Set remember-me cookie");
            Cookie cookie = new Cookie("remember-me", encoded);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(cookie);
        }

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