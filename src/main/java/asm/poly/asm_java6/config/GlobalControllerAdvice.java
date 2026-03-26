package asm.poly.asm_java6.config;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    UsersRepository usersRepository;

    @ModelAttribute
    public void addUserInfo(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            // Không đăng nhập => không thêm attribute
            return;
        }

        Object principal = authentication.getPrincipal();

        // Trường hợp login bằng form (UserDetails)
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            users user = usersRepository.findByEmail(email);
            if (user != null) {
                model.addAttribute("name", user.getHoTen());
                model.addAttribute("picture", user.getAvatar());
            }
            return;
        }

        // Trường hợp OAuth2 (Google, Facebook, ...)
        if (principal instanceof OAuth2User) {
            OAuth2User oauth = (OAuth2User) principal;

            // Ưu tiên lấy email để tìm DB (nếu OAuth2User có email)
            String email = oauth.getAttribute("email");
            if (email != null) {
                users user = usersRepository.findByEmail(email);
                if (user != null) {
                    model.addAttribute("name", user.getHoTen());
                    model.addAttribute("picture", user.getAvatar());
                    return;
                }
            }

            // Fallback: lấy thẳng từ attribute OAuth2
            String name = oauth.getAttribute("name");
            String picture = oauth.getAttribute("picture");

            // Fix Facebook: nếu picture == null, dùng graph url với id
            if (picture == null) {
                String id = oauth.getAttribute("id");
                if (id != null) {
                    picture = "https://graph.facebook.com/" + id + "/picture?type=large";
                }
            }

            model.addAttribute("name", name);
            model.addAttribute("picture", picture);
        }
    }
}
