package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.dto.BrandSummaryDTO;
import asm.poly.asm_java6.enity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT new asm.poly.asm_java6.dto.BrandSummaryDTO(" +
            "b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao, COUNT(p.id)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand.id = b.id " +
            "GROUP BY b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao " +
            "ORDER BY b.tenThuongHieu")
    List<BrandSummaryDTO> findAllBrandSummaries();
}