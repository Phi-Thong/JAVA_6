package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.dto.OrderDetailDto;
import asm.poly.asm_java6.dto.OrderDto;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.ProductRepository;
import asm.poly.asm_java6.repository.ProductReviewRepository;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import asm.poly.asm_java6.service.OrderService;

import asm.poly.asm_java6.dto.ProductReviewRequest;

import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.enity.ProductReview;


import java.time.LocalDateTime;
import java.util.List;


import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class profileController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "User/profile";
    }

    // API cập nhật thông tin user
    @PutMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> payload, Principal principal) {
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);

        // Validate dữ liệu
        String hoTen = (String) payload.get("hoTen");
        String sdt = (String) payload.get("sdt");
        String ngaySinh = (String) payload.get("ngaySinh");
        Boolean gioiTinh = (Boolean) payload.get("gioiTinh");

        Map<String, String> errors = new HashMap<>();
        if (hoTen == null || hoTen.trim().isEmpty()) {
            errors.put("hoTen", "Họ và tên không được để trống!");
        }
        if (sdt == null || !sdt.matches("\\d{10,11}")) {
            errors.put("sdt", "Số điện thoại phải là 10-11 số!");
        }
        if (ngaySinh == null || ngaySinh.isEmpty()) {
            errors.put("ngaySinh", "Vui lòng chọn ngày sinh!");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", errors.values().iterator().next()));
        }

        // Cập nhật thông tin
        user.setHoTen(hoTen);
        user.setSdt(sdt);
        LocalDate localDate = LocalDate.parse(ngaySinh);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setNgaySinh(date);
        user.setGioiTinh(gioiTinh);

        usersRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Cập nhật thành công!"));
    }

    // API cập nhật ảnh đại diện
    @PostMapping("/api/avatar")
    @ResponseBody
    public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng chọn file ảnh!"));
        }

        try {
            // Tạo tên file duy nhất
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "C:/upload/avatars/"; // <-- Sửa đường dẫn này cho đúng thư mục bạn đã tạo
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            java.io.File dest = new java.io.File(uploadDir + fileName);
            file.transferTo(dest);

            // Đường dẫn truy cập ảnh từ client
            String avatarUrl = "/avatars/" + fileName;

            // Cập nhật vào user
            String email = principal.getName();
            users user = usersRepository.findByEmail(email);
            user.setAvatar(avatarUrl);
            usersRepository.save(user);

            return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl, "message", "Cập nhật ảnh thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi lưu ảnh: " + e.getMessage()));
        }
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public List<OrderDto> getUserOrders(Principal principal) {
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);
        return orderService.getOrderSummariesByUserId(user.getId());
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDetailDtoById(id));
        //                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    @PostMapping("/api/change-password")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestBody Map<String, String> payload, Principal principal) {
        String email = principal.getName();
        String current = payload.get("currentPassword");
        String news = payload.get("newPassword");

        Map<String, Object> res = new HashMap<>();
        // Validate backend
        if (current == null || current.isEmpty()) {
            res.put("success", false);
            res.put("errorField", "currentPassword");
            res.put("message", "Vui lòng nhập mật khẩu hiện tại");
            return res;
        }
        if (news == null || news.length() < 6) {
            res.put("success", false);
            res.put("errorField", "newPassword");
            res.put("message", "Mật khẩu mới phải ít nhất 6 ký tự");
            return res;
        }
        // Kiểm tra mật khẩu hiện tại
        users user = usersRepository.findByEmail(email);
        if (!passwordEncoder.matches(current, user.getMatKhau())) {
            res.put("success", false);
            res.put("errorField", "currentPassword");
            res.put("message", "Mật khẩu hiện tại không chính xác");
            return res;
        }
        // Đổi mật khẩu
        user.setMatKhau(passwordEncoder.encode(news));
        usersRepository.save(user);
        res.put("success", true);
        return res;
    }

    @PostMapping("/api/review")
    @ResponseBody
    public ResponseEntity<?> addProductReview(@RequestBody ProductReviewRequest req, Principal principal) {
        // Lấy user hiện tại
        String email = principal.getName();
        users user = usersRepository.findByEmail(email);

        // Lấy sản phẩm
        Product product = productRepository.findById(req.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }

        // Validate số sao
        if (req.getRating() < 1 || req.getRating() > 5) {
            return ResponseEntity.badRequest().body("Số sao phải từ 1 đến 5!");
        }
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập bình luận!");
        }

        // Tạo đánh giá mới
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        review.setCreatedAt(LocalDateTime.now());

        productReviewRepository.save(review);

        return ResponseEntity.ok("Đánh giá thành công!");
    }
}
