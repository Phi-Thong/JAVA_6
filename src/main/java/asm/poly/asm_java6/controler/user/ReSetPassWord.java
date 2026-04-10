package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ReSetPassWord {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị giao diện đổi mật khẩu
    @GetMapping("/resetPassword")
    public String resetPassWord() {
        return "User/resetPassWord";
    }

    // API xác thực OTP (ví dụ, sau khi xác thực thành công thì lưu email vào session)
   

    // API cập nhật mật khẩu mới
    @PostMapping("/api/reset-password")
    @ResponseBody
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String newPassword = body.get("password");
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            response.put("success", false);
            response.put("message", "Phiên đã hết hạn!");
            return response;
        }

        users user = usersRepository.findByEmail(email);
        if (user == null) {
            response.put("success", false);
            response.put("message", "Không tìm thấy tài khoản!");
            return response;
        }

        user.setMatKhau(passwordEncoder.encode(newPassword));
        user.setToken(null); // Xóa mã OTP sau khi đổi mật khẩu
        usersRepository.save(user);

        // Xóa session resetEmail nếu muốn
        session.removeAttribute("resetEmail");

        response.put("success", true);
        response.put("message", "Cập nhật mật khẩu thành công!");
        return response;
    }
}