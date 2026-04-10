package asm.poly.asm_java6.controler.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Map;
import java.util.HashMap;

@Controller
public class codeEmail {

    @GetMapping("/codeEmail")
    public String codeEmail() {
        return "User/codeEmail";
    }

    // API xác thực OTP
    @PostMapping("/api/verify-otp")
    @ResponseBody
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> body, HttpSession session) {
        String code = body.get("code");
        String otpInSession = (String) session.getAttribute("otp_code");
        Long otpExpire = (Long) session.getAttribute("otp_expire");
        String email = (String) session.getAttribute("otp_email"); // Lấy email đã lưu khi gửi OTP

        Map<String, String> result = new HashMap<>();

        if (otpInSession == null || otpExpire == null || email == null) {
            result.put("status", "invalid");
            return result;
        }

        long now = System.currentTimeMillis();
        if (now > otpExpire) {
            result.put("status", "expired");
            return result;
        }

        if (code != null && code.equals(otpInSession)) {
            // Lưu email vào session để dùng cho đổi mật khẩu
            session.setAttribute("resetEmail", email);
            result.put("status", "success");
        } else {
            result.put("status", "invalid");
        }
        return result;
    }
}