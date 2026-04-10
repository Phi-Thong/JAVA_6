package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class FormEmailConTroller {
    @Autowired
    private EmailService emailService;

    // Trả về giao diện nhập email (GET)
    @GetMapping("/formEmail")
    public String formEmail() {
        return "User/formEmail"; // Trả về view User/formEmail.html
    }
}

@RestController
@RequestMapping("/api")
class ForgotPasswordApiController {
    @Autowired
    private EmailService emailService;

    // API gửi mã xác nhận qua email (POST)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("email");
        if (email != null && email.endsWith("@gmail.com")) {
            // Sinh mã xác nhận
            String code = String.valueOf((int) (Math.random() * 900000) + 100000);
            // Lưu vào session
            session.setAttribute("otp_code", code);
            session.setAttribute("otp_expire", System.currentTimeMillis() + 5 * 60 * 1000);
            // 5 phút
            session.setAttribute("otp_email", email);
//             lưu luôn trong email
            // Log ra console
            System.out.println("Gửi email tới: " + email + " với mã: " + code);
            // Gửi email
            emailService.sendVerifyEmail(email, "", code);
            return ResponseEntity.ok(Map.of("success", true, "message", "Đã gửi mã xác nhận!"));
        } else {
            return ResponseEntity.ok(Map.of("success", false, "message", "Email không tồn tại!"));
        }
    }
}