package asm.poly.asm_java6.controler.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.service.ProductService;

@Controller
public class DetailController {

    @Autowired
    private ProductService productService;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        // Lấy chi tiết sản phẩm
        Product product = productService.findProductDetailById(id);
        // Tổng số lượng tồn kho
        Integer totalStock = productService.getTotalStock(id);
        // Danh sách size còn hàng
        List<Integer> sizes = productService.getAvailableSizes(id);

        // Tạo danh sách ảnh phụ từ các trường trong Product
        List<String> anhPhu = new ArrayList<>();
        if (product.getAnhPhu1() != null && !product.getAnhPhu1().isEmpty()) anhPhu.add(product.getAnhPhu1());
        if (product.getAnhPhu2() != null && !product.getAnhPhu2().isEmpty()) anhPhu.add(product.getAnhPhu2());
        if (product.getAnhPhu3() != null && !product.getAnhPhu3().isEmpty()) anhPhu.add(product.getAnhPhu3());
        if (product.getAnhPhu4() != null && !product.getAnhPhu4().isEmpty()) anhPhu.add(product.getAnhPhu4());

        model.addAttribute("product", product);
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("sizes", sizes);
        model.addAttribute("anhPhu", anhPhu);

        return "User/Detail";
    }
}