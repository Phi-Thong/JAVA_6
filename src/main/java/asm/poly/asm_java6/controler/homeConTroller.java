package asm.poly.asm_java6.controler;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeConTroller {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User user) {

        if (user != null) {
            model.addAttribute("name", user.getAttribute("name"));
            model.addAttribute("picture", user.getAttribute("picture"));
        }

        return "home";
    }
}