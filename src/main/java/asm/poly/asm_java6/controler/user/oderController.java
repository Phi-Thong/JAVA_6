package asm.poly.asm_java6.controler.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class oderController {

    @GetMapping("/order")
    public String order() {
        return "User/oder";
    }

}