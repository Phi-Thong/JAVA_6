package asm.poly.asm_java6.config;

import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice // Áp dụng cho tất cả controller, giúp chia sẻ dữ liệu chung
public class GlobalControllerAdvice {

    @Autowired
    UsersRepository usersRepository; // Repository để truy vấn user từ CSDL

    // Thêm biến "name" vào model cho tất cả view
    @ModelAttribute("name")
    public String getName() {
        // Lấy thông tin đăng nhập hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Nếu chưa đăng nhập hoặc là anonymousUser thì trả về null
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return null;
        }

        // Tìm user trong CSDL theo email (tên đăng nhập)
        users user = usersRepository.findByEmail(auth.getName());

        // Trả về họ tên nếu có, không thì null
        return user != null ? user.getHoTen() : null;
    }

    // Thêm biến "picture" vào model cho tất cả view
    @ModelAttribute("picture")
    public String getPicture() {
        // Lấy thông tin đăng nhập hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Nếu chưa đăng nhập hoặc là anonymousUser thì trả về null
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return null;
        }

        // Tìm user trong CSDL theo email
        users user = usersRepository.findByEmail(auth.getName());

        // Trả về URL avatar nếu có, không thì null
        return user != null ? user.getAvatar() : null;
    }
}