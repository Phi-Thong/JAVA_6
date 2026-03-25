package asm.poly.asm_java6.config;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatar = (String) attributes.get("picture"); // Google avatar

        // Kiểm tra user đã tồn tại chưa
        users user = usersRepository.findByEmail(email);
        if (user == null) {
            user = new users();
            user.setEmail(email);
            user.setHoTen(name);
            user.setMatKhau(""); // Không có mật khẩu
            user.setAvatar(avatar);
            user.setTrangThai(false); // Hoạt động
            user.setVaiTro(false); // USER
            user.setCreatedAt(new java.util.Date());
            usersRepository.save(user);
        }

        // Chuyển hướng về home
        response.sendRedirect("/home");
    }
}