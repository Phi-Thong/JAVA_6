package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Controller
public class registerConTroller {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register() {
        return "User/register";
    }

    @PostMapping("/register")
    public String doRegister(
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(name = "agree", required = false) String agree,
            Model model
    ) {
        // Kiểm tra nhập thiếu
        if (!StringUtils.hasText(fullName) ||
                !StringUtils.hasText(email) ||
                !StringUtils.hasText(password) ||
                !StringUtils.hasText(confirmPassword) ||
                agree == null) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin và đồng ý điều khoản.");
            return "User/register";
        }

        // Kiểm tra định dạng email đơn giản
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            model.addAttribute("error", "Email không đúng định dạng.");
            return "User/register";
        }

        // Kiểm tra email đã tồn tại
        if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "Email đã được sử dụng.");
            return "User/register";
        }

        // Kiểm tra mật khẩu tối thiểu 6 ký tự
        if (password.length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "User/register";
        }

        // Kiểm tra mật khẩu trùng khớp
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
            return "User/register";
        }

        // Tạo user mới (chưa kích hoạt)
        users user = new users();
        user.setHoTen(fullName);
        user.setEmail(email);
        user.setMatKhau(passwordEncoder.encode(password));
        user.setTrangThai(true); // 1 = chưa xác thực
        user.setVaiTro(false);// xét lun cho nó là user

        // Sinh token xác thực
        String token = UUID.randomUUID().toString();
        user.setToken(token);

        // Lưu user vào DB
        userService.save(user);

        // Gửi email xác thực
        userService.sendVerifyEmail(email, fullName, token);

        model.addAttribute("success", "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
        return "User/register";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token) {

        users user = userService.findByToken(token);

        if (user != null && user.getTrangThai()) {
            user.setTrangThai(false);
            user.setToken(null);
            userService.save(user);
        }

        return "redirect:/home"; //
    }
}

