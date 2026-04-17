package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.enity.*;
import asm.poly.asm_java6.repository.*;
import asm.poly.asm_java6.service.CartService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.repository.OrderItemRepository;
import asm.poly.asm_java6.enity.Order;
import asm.poly.asm_java6.enity.OrderItem;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class oderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Hiển thị trang đặt hàng
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/order")
    public String order(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
            Model model, Principal principal) {

        String email = principal.getName();
        users user = usersRepository.findByEmail(email);

        List<Cart_item> cartItems = new ArrayList<>();

        if (productId != null) {
            // Trường hợp "Mua ngay"
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                model.addAttribute("error", "Sản phẩm không tồn tại!");
                return "User/oder";
            }
            Cart_item item = new Cart_item();
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItems.add(item);
        } else {
            // Trường hợp lấy từ giỏ hàng
            cartItems = cartService.getCartItemsByUser(user);
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getGia().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30000);
        BigDecimal grandTotal = total.add(shippingFee);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("orderForm", new OrderForm());

        return "User/oder";
    }

    // Xử lý đặt hàng qua AJAX (JSON)
    @PostMapping(value = "/order", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> placeOrderAjax(@RequestBody OrderForm orderForm, Principal principal) {
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);

        List<Cart_item> cartItems = cartService.getCartItemsByUser(user);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Giỏ hàng trống!");
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getGia().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30000);

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setHoTenNguoiNhan(orderForm.getHoTen());
        order.setSdtNguoiNhan(orderForm.getSdt());
        order.setEmailNguoiNhan(orderForm.getEmail());
        order.setDiaChiGiaoHang(orderForm.getDiaChi());
        order.setGhiChu(orderForm.getGhiChu());
        order.setTongTien(total);
        order.setPhiVanChuyen(shippingFee);
        order.setNgayDat(LocalDateTime.now());
        order.setTrangThai("ĐANG_CHỜ_XÁC_NHẬN");

        order = orderRepository.save(order);

        // Lưu từng sản phẩm trong giỏ hàng vào OrderItem
        for (Cart_item item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setSoLuong(item.getQuantity());
            orderItem.setGia(item.getProduct().getGia());
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(user);

        // Trả về thông báo thành công (hoặc URL để redirect)
        return ResponseEntity.ok("Đặt hàng thành công!");
    }

    // Nếu muốn hỗ trợ cả submit truyền thống (không AJAX)
    @PostMapping(value = "/order", consumes = {"application/x-www-form-urlencoded"})
    public String placeOrder(@ModelAttribute("orderForm") OrderForm orderForm, Principal principal, Model model) {
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);

        List<Cart_item> cartItems = cartService.getCartItemsByUser(user);
        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng trống!");
            return "User/oder";
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getGia().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(30000);

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setHoTenNguoiNhan(orderForm.getHoTen());
        order.setSdtNguoiNhan(orderForm.getSdt());
        order.setEmailNguoiNhan(orderForm.getEmail());
        order.setDiaChiGiaoHang(orderForm.getDiaChi());
        order.setGhiChu(orderForm.getGhiChu());
        order.setTongTien(total);
        order.setPhiVanChuyen(shippingFee);
        order.setNgayDat(LocalDateTime.now());
        order.setTrangThai("ĐANG_CHỜ_XÁC_NHẬN");

        order = orderRepository.save(order);

        for (Cart_item item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setSoLuong(item.getQuantity());
            orderItem.setGia(item.getProduct().getGia());
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(user);

        return "redirect:/user/order-success";
    }

    // Form nhận thông tin người nhận từ view
    @Data
    public static class OrderForm {
        private String hoTen;
        private String sdt;
        private String email;
        private String diaChi;
        private String ghiChu;
    }
}