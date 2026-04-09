package asm.poly.asm_java6.controler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import asm.poly.asm_java6.dto.AddToCartRequest;
import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import asm.poly.asm_java6.service.CartService;

import java.util.Map;

@Controller
public class ShoppingCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UsersRepository usersRepository;

    // Trả về trang giỏ hàng
    @RequestMapping("/shoppingCart")
    public String shoppingCart() {
        return "User/ShoppingCart";
    }

    // API lấy giỏ hàng cho frontend JS
    @GetMapping("/api/cart")
    @ResponseBody
    public ResponseEntity<?> getCartByUser(Authentication authentication) {
        // Kiểm tra đăng nhập
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("===> chưa đăng nhập");
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }

        // Lấy email từ authentication
        String email = authentication.getName();
        users user = usersRepository.findByEmail(email);
        Cart cart = cartService.getCartByUser(user);

        System.out.println("===> API /api/cart: user = " + email);

        if (cart == null) {
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }

        return ResponseEntity.ok(cart.getItems());
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        String email = authentication.getName();
        users user = usersRepository.findByEmail(email);

        Cart cart = cartService.addToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart.getItems());
    }

    @PostMapping("/api/cart/remove")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        String email = authentication.getName();
        users user = usersRepository.findByEmail(email);

        // Lấy productId từ payload
        Object productIdObj = payload.get("productId");
        if (productIdObj == null) {
            return ResponseEntity.badRequest().body("Thiếu productId");
        }
        Long productId;
        try {
            productId = Long.valueOf(productIdObj.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("productId không hợp lệ");
        }

        Cart cart = cartService.removeFromCart(user, productId);
        return ResponseEntity.ok(cart.getItems());
    }
}