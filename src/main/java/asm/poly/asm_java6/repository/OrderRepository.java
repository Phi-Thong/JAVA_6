package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Order;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    // ... các method khác
    long countByTrangThai(String trangThai);
}