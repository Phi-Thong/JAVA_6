package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.dto.BrandSummaryDTO;
import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.repository.BrandRepository;
import asm.poly.asm_java6.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands1")
public class BrandRestController1 {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    // Non-paged (for backward compatibility)
    @GetMapping
    public List<BrandSummaryDTO> getAllBrands() {
        return brandRepository.findAllBrandSummaries();
    }

    // Paged endpoint with filter, sort, search
    @GetMapping("/paged")
    public Page<BrandSummaryDTO> getPagedBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "createdDesc") String sort,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(required = false) String keyword
    ) {
        // Xử lý sort
        Sort sortObj = switch (sort) {
            case "nameAsc" -> Sort.by("tenThuongHieu").ascending();
            case "nameDesc" -> Sort.by("tenThuongHieu").descending();
            default -> Sort.by("ngayTao").descending(); // createdDesc
        };
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // Xử lý filter status
        String trangThai = null;
        if ("active".equals(status)) {
            trangThai = "Ðang s? d?ng";
        } else if ("inactive".equals(status)) {
            trangThai = "Ng?ng s? d?ng";
        }

        // Xử lý tìm kiếm
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword;

        // Gọi repository custom query
        return brandRepository.searchBrands(kw, trangThai, pageable);
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody Brand brand) {
        if (brand.getTenThuongHieu() == null || brand.getTenThuongHieu().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên loại sản phẩm không được để trống!"));
        }
        brand.setNgayTao(new Date());
        brandRepository.save(brand);
        return ResponseEntity.ok(Map.of("message", "Thêm thành công!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandSummaryDTO dto) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (!optionalBrand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy loại sản phẩm!"));
        }
        Brand brand = optionalBrand.get();
        brand.setTenThuongHieu(dto.getTenThuongHieu());
        brand.setMoTa(dto.getMoTa());
        brand.setTrangThai(dto.getTrangThai());
        brandRepository.save(brand);
        return ResponseEntity.ok(Map.of("message", "Cập nhật thành công!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (!optionalBrand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy loại sản phẩm!"));
        }
        long count = productRepository.countByBrandId(id);
        if (count > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Không thể xóa! Loại sản phẩm này đang có sản phẩm hoặc đơn hàng liên quan."));
        }
        brandRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công!"));
    }
}