package asm.poly.asm_java6.controler.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class DetailController {
    @GetMapping("/detail")
    public String detail() {
        return "User/Detail";
    }
}





