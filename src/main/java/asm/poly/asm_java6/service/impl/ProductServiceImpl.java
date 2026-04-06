package asm.poly.asm_java6.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.repository.ProductRepository;
import asm.poly.asm_java6.service.ProductService;
import org.springframework.data.domain.Pageable;
import asm.poly.asm_java6.repository.productSizeRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private productSizeRepository productSizeRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Product findProductDetailById(Long id) {
        return productRepository.findProductDetailById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public Integer getTotalStock(Long productId) {
        Integer total = productRepository.getTotalStock(productId);
        return total != null ? total : 0;
    }

    @Override
    public List<Integer> getAvailableSizes(Long productId) {
        List<Integer> sizes = productRepository.getAvailableSizes(productId);
        // Lọc trùng (distinct) trước khi trả về
        return sizes.stream().distinct().toList();
    }

    @Override
    public Page<Product> findByBrandId(Integer brandId, int page, int size) {
        return productRepository.findByBrandId(brandId, PageRequest.of(page, size));
    }

    @Override
    public Page<Product> findPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByBrandIds(List<Integer> brandIds, Pageable pageable) {
        return productRepository.findByBrandIdIn(brandIds, pageable);
    }

    // lấy size giày
    @Override
    public List<Integer> getAllSizes() {
        return productSizeRepository.findDistinctSizes();
    }

    // lọc theo size

    @Override
@Transactional
public Page<Product> findByFilters(Integer size, List<Long> brandIds, String keyword, Pageable pageable) {
    Page<Product> page = productRepository.findByFilters(size, brandIds, keyword, pageable);

    // Nếu có lọc size, chỉ giữ lại productSizes đúng size đó
    if (size != null) {
        page.getContent().forEach(product -> {
            product.setProductSizes(
                product.getProductSizes().stream()
                    .filter(ps -> ps.getSize().equals(size))
                    .toList());
        });
    }
    return page;
}
    // 
     @Override
    public List<Product> findRelatedProducts(Long brandId, Long excludeProductId) {
        return productRepository.findTop4ByBrandIdAndIdNot(brandId, excludeProductId);
    }


}