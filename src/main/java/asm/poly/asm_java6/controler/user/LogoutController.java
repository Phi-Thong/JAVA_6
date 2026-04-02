package asm.poly.asm_java6.controler.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Xóa ngay lập tức
        response.addCookie(cookie);

        // (Tùy chọn) Xóa JSESSIONID nếu muốn
        Cookie jsession = new Cookie("JSESSIONID", "");
        jsession.setPath("/");
        jsession.setMaxAge(0);
        response.addCookie(jsession);

        return "redirect:/login";
    }
}