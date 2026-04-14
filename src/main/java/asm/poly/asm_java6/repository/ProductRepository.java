package asm.poly.asm_java6.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
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

    //
    Page<Product> findByBrandId(Integer brandId, Pageable pageable);

    Page<Product> findByBrandIdIn(List<Integer> brandIds, Pageable pageable);

    @Query("""
                SELECT DISTINCT p FROM Product p
                JOIN p.productSizes ps
                WHERE (:size IS NULL OR ps.size = :size)
                AND (:brandIds IS NULL OR p.brand.id IN :brandIds)
            """)
    Page<Product> findByFilters(
            @Param("size") Integer size,
            @Param("brandIds") List<Long> brandIds,
            Pageable pageable
    );

    //
    @Query("SELECT p FROM Product p WHERE "
            + "(:productSize IS NULL OR EXISTS (SELECT 1 FROM p.productSizes ps WHERE ps.size = :productSize)) "
            + "AND (:brandId IS NULL OR p.brand.id IN :brandId) "
            + "AND (:keyword IS NULL OR LOWER(p.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findByFilters(
            @Param("productSize") Integer productSize,
            @Param("brandId") List<Long> brandId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    List<Product> findTop4ByBrandIdAndIdNot(Long brandId, Long excludeProductId);

    Page<Product> findByBrandId(Long brandId, Pageable pageable);

    Page<Product> findByTenSanPhamContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByTenSanPhamContainingIgnoreCaseAndBrandId(String keyword, Long brandId, Pageable pageable);

    long countByBrandId(Long brandId);
}