package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody users user) {
        // Kiểm tra email đã tồn tại chưa
        if (usersRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "fail", "message", "Email đã tồn tại!")
            );
        }
        user.setCreatedAt(new Date());
        // Mã hóa mật khẩu trước khi lưu
        user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));
        users saved = usersRepository.save(user);
        return ResponseEntity.ok(
                Map.of("status", "success", "data", saved)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        users user = usersRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "fail", "message", "Người dùng không tồn tại!")
            );
        }
        // Kiểm tra user có đơn hàng chưa
        if (orderRepository.countByUserId(id) > 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "fail", "message", "Không thể xóa người dùng đã có đơn hàng. Hãy khóa tài khoản thay vì xóa.")
            );
        }
        usersRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody users updatedUser) {
        users user = usersRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", "Không tìm thấy người dùng!"));
        }
        user.setHoTen(updatedUser.getHoTen());
        user.setEmail(updatedUser.getEmail());
        // Nếu mật khẩu thay đổi thì mã hóa lại để đăng nhập
        if (!updatedUser.getMatKhau().equals(user.getMatKhau())) {
            user.setMatKhau(passwordEncoder.encode(updatedUser.getMatKhau()));
        }
        user.setSdt(updatedUser.getSdt());
        user.setGioiTinh(updatedUser.getGioiTinh());
        user.setNgaySinh(updatedUser.getNgaySinh());
        user.setDiaChi(updatedUser.getDiaChi());
        user.setVaiTro(updatedUser.getVaiTro());
        user.setTrangThai(updatedUser.getTrangThai());
        usersRepository.save(user);
        return ResponseEntity.ok(Map.of("status", "success", "data", user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        users user = usersRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "fail", "message", "Không tìm thấy người dùng!")
            );
        }
        return ResponseEntity.ok(user);
    }
}