package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.repository.BrandRepository;
import asm.poly.asm_java6.repository.ProductRepository;
import asm.poly.asm_java6.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductCreateController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository ProductRepository;


    @PostMapping("/product-create")
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("brandId") Long brandId,
            @RequestParam("price") String price,
            @RequestParam("desc") String desc,
            @RequestParam("mainImage") MultipartFile mainImage,
            @RequestParam("subImages") List<MultipartFile> subImages
    ) {
        try {
            String mainImageUrl = cloudinaryService.uploadFile(mainImage);
            String subImage1 = cloudinaryService.uploadFile(subImages.get(0));
            String subImage2 = cloudinaryService.uploadFile(subImages.get(1));
            String subImage3 = cloudinaryService.uploadFile(subImages.get(2));
            String subImage4 = cloudinaryService.uploadFile(subImages.get(3));

            Brand brandObj = brandRepository.findById(brandId).orElse(null);

            Product product = new Product();
            product.setTenSanPham(name);
            product.setGia(new BigDecimal(price.replace(",", "")));
            product.setMoTa(desc);
            product.setBrand(brandObj);
            product.setAnhChinh(mainImageUrl);
            product.setAnhPhu1(subImage1);
            product.setAnhPhu2(subImage2);
            product.setAnhPhu3(subImage3);
            product.setAnhPhu4(subImage4);
            product.setCreatedAt(LocalDateTime.now());

            ProductRepository.save(product);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Lỗi khi lưu sản phẩm: " + e.getMessage()
            ));
        }
    }
}