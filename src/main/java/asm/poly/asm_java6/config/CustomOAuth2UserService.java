package asm.poly.asm_java6.config;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsersRepository usersRepository; // Repository để truy vấn user trong CSDL

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Lấy thông tin user từ Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Lấy email của user từ dữ liệu Google trả về
        String email = oAuth2User.getAttribute("email");

        // Tìm user trong CSDL theo email
        users user = usersRepository.findByEmail(email);

        // Tạo danh sách quyền (roles) cho user
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user != null && Boolean.TRUE.equals(user.getVaiTro())) {
            // Nếu user tồn tại và vai trò là admin -> ROLE_ADMIN
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            // Nếu không phải admin hoặc chưa có trong CSDL -> ROLE_USER
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Trả về một OAuth2User dùng Spring Security
        // - authorities: quyền của user
        // - attributes: thông tin Google trả về
        // - "email": key để xác định user (username)
        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email"
        );
    }
}