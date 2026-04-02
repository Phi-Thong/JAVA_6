package asm.poly.asm_java6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import asm.poly.asm_java6.enity.ProductSize;

public interface productSizeRepository extends JpaRepository<ProductSize, Long> {
    @Query("SELECT DISTINCT ps.size FROM ProductSize ps ORDER BY ps.size")
    List<Integer> findDistinctSizes();
}