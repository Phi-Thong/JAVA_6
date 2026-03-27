package asm.poly.asm_java6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm (nếu cần)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo phân trang
    public Page<Product> findPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }
}