package asm.poly.asm_java6.controler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.service.ProductService;
import asm.poly.asm_java6.dto.ProductDto;
import asm.poly.asm_java6.dto.ProductSizeDto;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductDto> getProducts(
            @RequestParam(required = false) List<Long> brandId,
            @RequestParam(required = false) Integer productSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 🔥 LUÔN gọi filter chung
        Page<Product> products = productService.findByFilters(productSize, brandId, pageable);

        return products.map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setTenSanPham(product.getTenSanPham());
            dto.setGia(product.getGia());
            dto.setMoTa(product.getMoTa());
            dto.setCategoryId(product.getCategoryId());
            dto.setAnhChinh(product.getAnhChinh());
            dto.setAnhPhu1(product.getAnhPhu1());
            dto.setAnhPhu2(product.getAnhPhu2());
            dto.setAnhPhu3(product.getAnhPhu3());
            dto.setAnhPhu4(product.getAnhPhu4());
            dto.setCreatedAt(product.getCreatedAt());
            dto.setBrand(product.getBrand());

            List<ProductSizeDto> sizes = product.getProductSizes().stream()
                    .map(ps -> {
                        ProductSizeDto psDto = new ProductSizeDto();
                        psDto.setId(ps.getId());
                        psDto.setSize(ps.getSize());
                        psDto.setSoLuong(ps.getSoLuong());
                        return psDto;
                    }).collect(Collectors.toList());

            dto.setProductSizes(sizes);

            return dto;
        });
    }
}