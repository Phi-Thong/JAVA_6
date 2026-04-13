package asm.poly.asm_java6.controler.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class adminConTroller {

    @GetMapping("/dashboard")
    public String dashboard(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("content", "admin/dashboard :: dashboard");
        // Nếu là AJAX request, chỉ trả về fragment
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/dashboard :: dashboard";
        }
        return "admin/admin";
    }

    @GetMapping("/producttype")
    public String productType(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("content", "admin/producttype :: producttype");
        // Nếu là AJAX request, chỉ trả về fragment
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/producttype :: producttype";
        }
        return "admin/admin";
    }

    @GetMapping("/product")
    public String product(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("content", "admin/product :: product");
        // Nếu là AJAX request, chỉ trả về fragment
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/product :: product";
        }
        return "admin/admin";
    }

    @GetMapping("/order")
    public String order(Model model,
                        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        model.addAttribute("content", "admin/order :: order");

        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/order :: order";
        }

        return "admin/admin";
    }

    @GetMapping("/user")
    public String user(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("content", "admin/user :: user");
        // Nếu là AJAX request, chỉ trả về fragment
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/user :: user";
        }
        return "admin/admin";
    }

    @GetMapping("/ChatAdmin")
    public String ChatAdmin(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("content", "admin/ChatAdmin :: ChatAdmin");
        // Nếu là AJAX request, chỉ trả về fragment
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin/ChatAdmin :: ChatAdmin";
        }
        return "admin/admin";
    }

}