package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.enity.ProductSize;
import asm.poly.asm_java6.repository.*;
import asm.poly.asm_java6.service.CloudinaryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductCreateController {
    @Autowired
    private productSizeRepository productSizeRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository ProductRepository;
    @Autowired
    private OrderItemRepository OrderItemRepository;
    @Autowired
    private CartItemRepository CartItemRepository;
    @Autowired
    private ProductReviewRepository ProductReviewRepository;


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
            // Thêm size và số lượng mặc định
            int[] sizes = {38, 39, 40, 41, 42};
            for (int size : sizes) {
                ProductSize ps = new ProductSize();
                ps.setProduct(product);
                ps.setSize(size);
                ps.setSoLuong(10);
                productSizeRepository.save(ps);
            }
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Lỗi khi lưu sản phẩm: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/products3")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) Long brandId, // Lọc thương hiệu
            @RequestParam(required = false) String sort,  // Sắp xếp giá
            @RequestParam(required = false) String keyword // Tìm kiếm tên sản phẩm
    ) {
        // Xác định kiểu sắp xếp
        Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("asc".equalsIgnoreCase(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "gia");
        } else if ("desc".equalsIgnoreCase(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "gia");
        }
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Product> productPage;

        // Ưu tiên tìm kiếm theo keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (brandId != null) {
                productPage = ProductRepository.findByTenSanPhamContainingIgnoreCaseAndBrandId(keyword, brandId, pageable);
            } else {
                productPage = ProductRepository.findByTenSanPhamContainingIgnoreCase(keyword, pageable);
            }
        } else if (brandId != null) {
            productPage = ProductRepository.findByBrandId(brandId, pageable);
        } else {
            productPage = ProductRepository.findAll(pageable);
        }

        List<Map<String, Object>> content = productPage.getContent().stream().map(product -> {
            int tongSoLuong = 0;
            if (product.getProductSizes() != null) {
                for (ProductSize ps : product.getProductSizes()) {
                    tongSoLuong += ps.getSoLuong();
                }
            }
            return Map.of(
                    "id", product.getId(),
                    "tenSanPham", product.getTenSanPham(),
                    "gia", product.getGia(),
                    "anhChinh", product.getAnhChinh(),
                    "moTa", product.getMoTa(),
                    "brand", product.getBrand() != null ? Map.of("tenThuongHieu", product.getBrand().getTenThuongHieu()) : null,
                    "tongSoLuong", tongSoLuong
            );
        }).toList();

        Map<String, Object> result = Map.of(
                "content", content,
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements(),
                "page", productPage.getNumber() + 1 // trả về page bắt đầu từ 1
        );
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/products3/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            // Xóa các bảng liên quan trước
            productSizeRepository.deleteByProductId(id);
            OrderItemRepository.deleteByProductId(id);
            CartItemRepository.deleteByProductId(id);
            ProductReviewRepository.deleteByProductId(id);

            // Xóa sản phẩm
            ProductRepository.deleteById(id);

            return ResponseEntity.ok().body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Không thể xóa sản phẩm!"));
        }
    }

    @GetMapping("/products3/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = ProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy sản phẩm!"));
        }
        Product product = productOpt.get();

        // Lấy danh sách ảnh phụ (nếu có)
        List<String> anhPhu = new ArrayList<>();
        if (product.getAnhPhu1() != null) anhPhu.add(product.getAnhPhu1());
        if (product.getAnhPhu2() != null) anhPhu.add(product.getAnhPhu2());
        if (product.getAnhPhu3() != null) anhPhu.add(product.getAnhPhu3());
        if (product.getAnhPhu4() != null) anhPhu.add(product.getAnhPhu4());

        Map<String, Object> result = Map.of(
                "id", product.getId(),
                "tenSanPham", product.getTenSanPham(),

                "gia", product.getGia(),
                "moTa", product.getMoTa(),
                "anhChinh", product.getAnhChinh(),
                "anhPhu", anhPhu,
                "brand", product.getBrand() != null ? Map.of(
                        "id", product.getBrand().getId(),
                        "tenThuongHieu", product.getBrand().getTenThuongHieu()
                ) : null
        );
        return ResponseEntity.ok(result);
    }

    @PutMapping("/products3/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("brandId") Long brandId,
            @RequestParam("price") String price,
            @RequestParam("desc") String desc,
            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "subImages", required = false) List<MultipartFile> subImages
    ) {
        try {
            Optional<Product> productOpt = ProductRepository.findById(id);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Không tìm thấy sản phẩm!"));
            }
            Product product = productOpt.get();

            // Cập nhật thông tin cơ bản
            product.setTenSanPham(name);
            product.setGia(new BigDecimal(price.replace(",", "")));
            product.setMoTa(desc);

            Brand brandObj = brandRepository.findById(brandId).orElse(null);
            product.setBrand(brandObj);

            // Nếu có ảnh chính mới thì upload và cập nhật, không thì giữ ảnh cũ
            if (mainImage != null && !mainImage.isEmpty()) {
                String mainImageUrl = cloudinaryService.uploadFile(mainImage);
                product.setAnhChinh(mainImageUrl);
            }

            // Nếu có ảnh phụ mới thì upload và cập nhật, không thì giữ ảnh cũ
            if (subImages != null && subImages.size() == 4) {
                String subImage1 = cloudinaryService.uploadFile(subImages.get(0));
                String subImage2 = cloudinaryService.uploadFile(subImages.get(1));
                String subImage3 = cloudinaryService.uploadFile(subImages.get(2));
                String subImage4 = cloudinaryService.uploadFile(subImages.get(3));
                product.setAnhPhu1(subImage1);
                product.setAnhPhu2(subImage2);
                product.setAnhPhu3(subImage3);
                product.setAnhPhu4(subImage4);
            }

            ProductRepository.save(product);

            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Lỗi khi cập nhật sản phẩm: " + e.getMessage()
            ));
        }
    }
}