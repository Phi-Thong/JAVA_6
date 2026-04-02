package asm.poly.asm_java6.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import asm.poly.asm_java6.enity.Product;

public interface ProductService {
    List<Product> findAll();

    Page<Product> findPaginated(int page, int size);

    Product findProductDetailById(Long id);

    Integer getTotalStock(Long productId);

    List<Integer> getAvailableSizes(Long productId);

    Page<Product> findByBrandId(Integer brandId, int page, int size);

    Page<Product> findPaginated(Pageable pageable);

    Page<Product> findByBrandIds(List<Integer> brandIds, Pageable pageable);

    List<Integer> getAllSizes();

    // lọc theo size
  

    Page<Product> findByFilters(Integer size, List<Long> brandIds, Pageable pageable);
}