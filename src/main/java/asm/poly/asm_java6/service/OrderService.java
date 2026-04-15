package asm.poly.asm_java6.service;

import asm.poly.asm_java6.dto.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderSummaryDTO> getAllOrderSummaries(Pageable pageable);

    // Thêm hàm lấy chi tiết đơn hàng theo id
    OrderSummaryDTO getOrderSummaryById(Long id);

    boolean updateOrderStatus(Long id, String status);

    Page<OrderSummaryDTO> getAllOrderSummariesByStatus(String status, Pageable pageable);
    
}