package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.dto.CustomerSummaryDTO;
import asm.poly.asm_java6.enity.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    @Query(value = "SELECT COUNT(*) FROM users WHERE MONTH(created_at) = MONTH(GETDATE()) AND YEAR(created_at) = YEAR(GETDATE())", nativeQuery = true)
    long countNewUsersThisMonth();

    @Query(value = "SELECT COUNT(*) FROM users WHERE MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(created_at) = YEAR(DATEADD(MONTH, -1, GETDATE()))", nativeQuery = true)
    long countNewUsersLastMonth();

    @Query(value = """
            SELECT 
                ISNULL(SUM(CASE 
                    WHEN MONTH(created_at) = MONTH(GETDATE()) AND YEAR(created_at) = YEAR(GETDATE()) 
                    THEN 1 ELSE 0 END), 0) AS khach_moi_thang,
                ISNULL(SUM(CASE 
                    WHEN MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(created_at) = YEAR(DATEADD(MONTH, -1, GETDATE())) 
                    THEN 1 ELSE 0 END), 0) AS khach_moi_thang_truoc
            FROM users
            """, nativeQuery = true)
    List<Object[]> getNewCustomerStats();

    @Query(value = """
                SELECT TOP 10
                    u.ho_ten AS fullname,
                    u.avatar,
                    u.email,
                    COUNT(o.id) AS total_orders,
                    SUM(o.tong_tien) AS total_spent
                FROM users u
                JOIN orders o ON u.id = o.user_id
                WHERE MONTH(o.ngay_dat) = MONTH(GETDATE())
                  AND YEAR(o.ngay_dat) = YEAR(GETDATE())
                GROUP BY u.ho_ten, u.avatar, u.email
                ORDER BY total_spent DESC
            """, nativeQuery = true)
    List<Object[]> getTopVipCustomers();
}