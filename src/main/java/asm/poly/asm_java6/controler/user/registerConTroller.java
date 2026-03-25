package asm.poly.asm_java6.controler.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class registerConTroller {
    @GetMapping("/register")
    public String register() {
        return "User/register";
    }
}
