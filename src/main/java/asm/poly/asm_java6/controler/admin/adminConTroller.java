package asm.poly.asm_java6.controler.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class adminConTroller {
    @GetMapping("/dashboard")
    public String home() {
        return "admin/admin";
    }

    @GetMapping("producttype")
    public String producttype() {
        return "admin/producttype";
    }


}
