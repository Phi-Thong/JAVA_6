package asm.poly.asm_java6.controler;

import java.util.List;
import java.util.Map;

import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.repository.BrandRepository;
import asm.poly.asm_java6.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
public class homeConTroller {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UsersRepository usersRepository;


    @GetMapping("/home")
    public String home(Model model,
                       @AuthenticationPrincipal OAuth2User user,
                       @RequestParam(defaultValue = "0") int page,
                       HttpSession session) {

        // User info: Truyền object user từ DB vào model
        if (user != null) {
            String email = user.getAttribute("email");
            asm.poly.asm_java6.enity.users dbUser = usersRepository.findByEmail(email);
            model.addAttribute("user", dbUser);
            if (dbUser != null) {
                model.addAttribute("user", dbUser);
            }
        }

        // CART COUNT
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        int totalQuantity = 0;
        if (cart != null) {
            for (int qty : cart.values()) {
                totalQuantity += qty;
            }
        }
        model.addAttribute("cartCount", totalQuantity);
        System.out.println("Cart count = " + totalQuantity); // debug

        // Product paging
        int pageSize = 8;
        Page<Product> productPage = productService.findPaginated(page, pageSize);
        List<Integer> sizes = productService.getAllSizes();
        model.addAttribute("sizes", sizes);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandRepository.findAll());

        return "home";
    }

    @GetMapping("/api/brands")
    @ResponseBody
    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }


}