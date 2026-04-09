package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.dto.AddToCartRequest;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import asm.poly.asm_java6.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user/cart")
public class CartRestController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest req, Principal principal) {

        users user = usersRepository.findByEmail(principal.getName());

        cartService.addToCart(user, req.getProductId(), req.getQuantity());

        return ResponseEntity.ok().body("Đã thêm vào giỏ hàng!");
    }
}