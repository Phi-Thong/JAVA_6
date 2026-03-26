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

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String name = null;
        String avatar = null;

        //  Phân biệt Google vs Facebook
        if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            avatar = oAuth2User.getAttribute("picture");
        } else if ("facebook".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");

            // Facebook avatar
            String id = oAuth2User.getAttribute("id");
            avatar = "https://graph.facebook.com/" + id + "/picture?type=large";

            //  Nếu không có email thì tạo email giả
            if (email == null) {
                email = id + "@facebook.com";
            }
        }


        users user = usersRepository.findByEmail(email);


        if (user == null) {
            user = new users();
            user.setEmail(email);
            user.setHoTen(name);
            user.setAvatar(avatar);


            user.setMatKhau(""); // KHÔNG để null

            user.setVaiTro(false); // USER
            user.setTrangThai(false);
            user.setCreatedAt(new Date());

            usersRepository.save(user);
        }

        //
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (Boolean.TRUE.equals(user.getVaiTro())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "name" //
        );
    }
}