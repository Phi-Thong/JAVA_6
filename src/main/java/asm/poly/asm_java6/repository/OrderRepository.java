package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.dto.OrderDto;
import asm.poly.asm_java6.enity.Order;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByUserId(Long userId);

    @Query("SELECT new asm.poly.asm_java6.dto.OrderSummaryDTO(" +
            "a.id, u.hoTen, u.email, a.ngayDat, a.tongTien, SIZE(a.orderItems), a.trangThai) " +
            "FROM Order a JOIN a.user u")
    Page<OrderSummaryDTO> findAllOrderSummaries(Pageable pageable);

    OrderSummaryDTO findOrderSummaryById(Long id);

    @Query("SELECT new asm.poly.asm_java6.dto.OrderSummaryDTO(" +
            "a.id, u.hoTen, u.email, a.ngayDat, a.tongTien, SIZE(a.orderItems), a.trangThai) " +
            "FROM Order a JOIN a.user u WHERE a.trangThai = :status")
    Page<OrderSummaryDTO> findAllOrderSummariesByStatus(@Param("status") String status, Pageable pageable);


    long countByTrangThai(String trangThai);

    @Query(value = """
            SELECT 
                ISNULL(SUM(CASE 
                    WHEN MONTH(o.ngay_dat) = MONTH(GETDATE()) AND YEAR(o.ngay_dat) = YEAR(GETDATE()) 
                    THEN o.tong_tien ELSE 0 END), 0) AS doanh_thu_thang,
                ISNULL(SUM(CASE 
                    WHEN MONTH(o.ngay_dat) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(o.ngay_dat) = YEAR(DATEADD(MONTH, -1, GETDATE())) 
                    THEN o.tong_tien ELSE 0 END), 0) AS doanh_thu_thang_truoc
                FROM orders o
            """, nativeQuery = true)
    List<Object[]> getRevenueStats();

    @Query(value = "SELECT COUNT(*) FROM orders WHERE MONTH(ngay_dat) = MONTH(GETDATE()) AND YEAR(ngay_dat) = YEAR(GETDATE())", nativeQuery = true)
    long countOrdersThisMonth();

    @Query(value = "SELECT COUNT(*) FROM orders WHERE MONTH(ngay_dat) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(ngay_dat) = YEAR(DATEADD(MONTH, -1, GETDATE()))", nativeQuery = true)
    long countOrdersLastMonth();

    @Query(value = """
            SELECT 
                ISNULL(SUM(CASE 
                    WHEN MONTH(o.ngay_dat) = MONTH(GETDATE()) AND YEAR(o.ngay_dat) = YEAR(GETDATE()) 
                    THEN 1 ELSE 0 END), 0) AS tong_don_thang,
                ISNULL(SUM(CASE 
                    WHEN MONTH(o.ngay_dat) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(o.ngay_dat) = YEAR(DATEADD(MONTH, -1, GETDATE())) 
                    THEN 1 ELSE 0 END), 0) AS tong_don_thang_truoc
                FROM orders o
            """, nativeQuery = true)
    List<Object[]> getOrderCountStats();

    @Query("""
                SELECT new asm.poly.asm_java6.dto.OrderDto(
                    o.id,
                    o.ngayDat,
                    COUNT(oi) * 1L,
                    o.tongTien,
                    o.trangThai,
                    MIN(oi.product.anhChinh)
                )
                FROM Order o
                LEFT JOIN o.orderItems oi
                WHERE o.user.id = :userId
                GROUP BY o.id, o.ngayDat, o.tongTien, o.trangThai
                ORDER BY o.ngayDat DESC
            """)
    List<OrderDto> findOrderSummariesByUserId(Long userId);

    Page<Order> findByUser_HoTenContainingIgnoreCase(String hoTen, Pageable pageable);

    Page<Order> findByUser_HoTenContainingIgnoreCaseAndTrangThai(String hoTen, String trangThai, Pageable pageable);
}