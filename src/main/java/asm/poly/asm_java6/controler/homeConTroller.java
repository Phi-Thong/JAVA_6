package asm.poly.asm_java6.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.service.ProductService;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
public class homeConTroller {

    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String home(Model model,
                       @AuthenticationPrincipal OAuth2User user,
                       @RequestParam(defaultValue = "0") int page,
                       HttpSession session) {

        // 👤 User info
        if (user != null) {
            model.addAttribute("name", user.getAttribute("name"));
            model.addAttribute("picture", user.getAttribute("picture"));
        }

        // 🛒 CART COUNT
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        int totalQuantity = 0;

        if (cart != null) {
            for (int qty : cart.values()) {
                totalQuantity += qty;
            }
        }

        model.addAttribute("cartCount", totalQuantity);

        System.out.println("Cart count = " + totalQuantity); // debug

        // 📦 Product paging
        int pageSize = 8;
        Page<Product> productPage = productService.findPaginated(page, pageSize);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "home";
    }

}