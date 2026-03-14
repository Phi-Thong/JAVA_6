package asm.poly.asm_java6.controler.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ShoppingCartController {
    @RequestMapping("/shoppingCart")
    public String shoppingCart() {
        return "User/ShoppingCart";
    }
}
