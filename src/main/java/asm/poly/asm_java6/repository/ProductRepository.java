package asm.poly.asm_java6.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import asm.poly.asm_java6.enity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByIdDesc(Pageable pageable);

    // Lấy chi tiết sản phẩm kèm brand
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand WHERE p.id = :id")
    Optional<Product> findProductDetailById(@Param("id") Long id);

    // Tổng số lượng tồn kho của sản phẩm
    @Query("SELECT SUM(s.soLuong) FROM ProductSize s WHERE s.product.id = :productId")
    Integer getTotalStock(@Param("productId") Long productId);

    // Danh sách size còn hàng (nếu trường là 'size', nếu không hãy sửa lại)
    @Query("SELECT s.size FROM ProductSize s WHERE s.product.id = :productId AND s.soLuong > 0")
    List<Integer> getAvailableSizes(@Param("productId") Long productId);
}