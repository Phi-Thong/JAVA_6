package asm.poly.asm_java6.controler.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class profileController {

    @GetMapping("/profile")
    public String profile() {
        return "User/profile";
    }

}