package asm.poly.asm_java6.controler.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import asm.poly.asm_java6.service.ProductReviewService;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.service.ProductService;
import asm.poly.asm_java6.service.ProductSizeService;

@Controller
public class DetailController {
    @Autowired
    private ProductReviewService productReviewService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSizeService productSizeService;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        // Lấy chi tiết sản phẩm
        Product product = productService.findProductDetailById(id);

        // Tổng số lượng tồn kho
        int totalStock = productSizeService.getTotalStockByProductId(id.intValue());

        // Danh sách size còn hàng (nếu có hàm trong ProductSizeService, ví dụ: findDistinctSizesByProductId)
        // Nếu chưa có, bạn có thể dùng hàm findDistinctSizes() và lọc theo productId
        // List<Integer> sizes = productSizeService.findDistinctSizesByProductId(id.intValue());
        List<Integer> sizes = productSizeService.getAvailableSizes(id.intValue());

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
        //  lấy 4 sản phẩm 
        List<Product> relatedProducts = productService.findRelatedProducts(
                product.getBrand().getId(), id
        );

        model.addAttribute("product", product);
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("sizes", sizes);
        model.addAttribute("anhPhu", anhPhu);
        model.addAttribute("relatedProducts", relatedProducts);
        List<asm.poly.asm_java6.enity.ProductReview> reviews = productReviewService.getReviewsByProductId(id);
        model.addAttribute("reviews", reviews);

// Tính trung bình rating
        double averageRating = 0.0;
        if (!reviews.isEmpty()) {
            averageRating = reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
        }
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviews.size());

        return "User/Detail";
    }
}