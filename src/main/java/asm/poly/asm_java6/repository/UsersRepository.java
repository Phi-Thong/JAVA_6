package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.dto.CustomerSummaryDTO;
import asm.poly.asm_java6.enity.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<users, Long> {

    // =========================
    // BASIC
    // =========================
    users findByEmail(String email);

    users findByToken(String token);


    // =========================
    // LẤY TẤT CẢ CUSTOMER (có phân trang)
    // =========================
    @Query("""
            SELECT new asm.poly.asm_java6.dto.CustomerSummaryDTO(
                a.id, a.hoTen, a.avatar, a.email, a.sdt,
                COUNT(o.id),
                COALESCE(SUM(o.tongTien), 0),
                a.trangThai
            )
            FROM users a
            LEFT JOIN Order o ON o.user.id = a.id
            WHERE a.vaiTro = false
            GROUP BY a.id, a.hoTen, a.avatar, a.email, a.sdt, a.trangThai
            ORDER BY a.id DESC
            """)
    Page<CustomerSummaryDTO> findAllCustomerSummaries(Pageable pageable);


    // =========================
    // FILTER THEO TRẠNG THÁI
    // =========================
    @Query("""
            SELECT new asm.poly.asm_java6.dto.CustomerSummaryDTO(
                a.id, a.hoTen, a.avatar, a.email, a.sdt,
                COUNT(o.id),
                COALESCE(SUM(o.tongTien), 0),
                a.trangThai
            )
            FROM users a
            LEFT JOIN Order o ON o.user.id = a.id
            WHERE a.vaiTro = false AND a.trangThai = :trangThai
            GROUP BY a.id, a.hoTen, a.avatar, a.email, a.sdt, a.trangThai
            ORDER BY a.id DESC
            """)
    Page<CustomerSummaryDTO> findAllCustomerSummariesByTrangThai(Boolean trangThai, Pageable pageable);


    // =========================
    // SEARCH + FILTER (QUAN TRỌNG NHẤT)
    // =========================
    @Query("""
            SELECT new asm.poly.asm_java6.dto.CustomerSummaryDTO(
                a.id, a.hoTen, a.avatar, a.email, a.sdt,
                COUNT(o.id),
                COALESCE(SUM(o.tongTien), 0),
                a.trangThai
            )
            FROM users a
            LEFT JOIN Order o ON o.user.id = a.id
            WHERE 
                a.vaiTro = false
                AND (:trangThai IS NULL OR a.trangThai = :trangThai)
                AND (
                    :keyword IS NULL OR
                    LOWER(a.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    a.sdt LIKE CONCAT('%', :keyword, '%')
                )
            GROUP BY a.id, a.hoTen, a.avatar, a.email, a.sdt, a.trangThai
            ORDER BY a.id DESC
            """)
    Page<CustomerSummaryDTO> searchCustomers(
            String keyword,
            Boolean trangThai,
            Pageable pageable
    );
}