package asm.poly.asm_java6.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatar = null;

      
       if (attributes.containsKey("picture")) {
    Object pictureAttr = attributes.get("picture");
    if (pictureAttr instanceof Map) {
        // Facebook: picture là Map
        Map<String, Object> pictureObj = (Map<String, Object>) pictureAttr;
        if (pictureObj != null && pictureObj.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) pictureObj.get("data");
            if (data != null && data.containsKey("url")) {
                avatar = (String) data.get("url");
            }
        }
    } else if (pictureAttr instanceof String) {
        // Google/GitHub: picture là String (URL)
        avatar = (String) pictureAttr;
    }
}

        // Nếu vẫn null → fallback default hoặc kiểu URL cũ
        if (avatar == null) {
            if (attributes.containsKey("id")) {
                String id = (String) attributes.get("id");
                avatar = "https://graph.facebook.com/" + id + "/picture?type=large";
            } else {
                avatar = "/img/default.jpg"; // fallback default
            }
        }

        // 🔥 Fix trường hợp Facebook không có email
        if (email == null) {
            String id = (String) attributes.get("id");
            email = id + "@facebook.com";
        }

        // 👉 Kiểm tra user tồn tại
        users user = usersRepository.findByEmail(email);

        if (user == null) {
            user = new users();
            user.setEmail(email);
            user.setHoTen(name);
            user.setMatKhau(""); // KHÔNG để null
            user.setAvatar(avatar);
            user.setTrangThai(false); // hoạt động
            user.setVaiTro(false); // USER
            user.setCreatedAt(new java.util.Date());

            usersRepository.save(user);
        }

        // 👉 Redirect sau login
        response.sendRedirect("/home");
    }
}