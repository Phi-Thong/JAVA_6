package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.dto.BrandSummaryDTO;
import asm.poly.asm_java6.enity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    // Lấy tất cả BrandSummaryDTO (không phân trang)
    @Query("SELECT new asm.poly.asm_java6.dto.BrandSummaryDTO(" +
            "b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao, COUNT(p.id)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand.id = b.id " +
            "GROUP BY b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao " +
            "ORDER BY b.tenThuongHieu")
    List<BrandSummaryDTO> findAllBrandSummaries();

    // Lấy BrandSummaryDTO có phân trang
    @Query("SELECT new asm.poly.asm_java6.dto.BrandSummaryDTO(" +
            "b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao, COUNT(p.id)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand.id = b.id " +
            "GROUP BY b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao")
    Page<BrandSummaryDTO> findAllBrandSummaries(Pageable pageable);

    // Lọc theo trạng thái (có phân trang)
    @Query("SELECT new asm.poly.asm_java6.dto.BrandSummaryDTO(" +
            "b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao, COUNT(p.id)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand.id = b.id " +
            "WHERE (:trangThai IS NULL OR b.trangThai = :trangThai) " +
            "GROUP BY b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao")
    Page<BrandSummaryDTO> findBrandSummariesByTrangThai(
            @Param("trangThai") String trangThai,
            Pageable pageable
    );

    @Query("SELECT new asm.poly.asm_java6.dto.BrandSummaryDTO(" +
            "b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao, COUNT(p.id)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand.id = b.id " +
            "WHERE (:trangThai IS NULL OR b.trangThai = :trangThai) " +
            "AND (:keyword IS NULL OR LOWER(b.tenThuongHieu) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "GROUP BY b.id, b.tenThuongHieu, b.moTa, b.trangThai, b.ngayTao")
    Page<BrandSummaryDTO> searchBrands(
            @Param("keyword") String keyword,
            @Param("trangThai") String trangThai,
            Pageable pageable
    );
}