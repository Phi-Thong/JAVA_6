package asm.poly.asm_java6.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeConTroller {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}