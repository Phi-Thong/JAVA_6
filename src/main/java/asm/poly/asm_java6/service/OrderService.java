package asm.poly.asm_java6.service;

import asm.poly.asm_java6.dto.OrderDetailDto;
import asm.poly.asm_java6.dto.OrderDto;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderSummaryDTO> getAllOrderSummaries(Pageable pageable);

    // Thêm hàm lấy chi tiết đơn hàng theo id
    OrderSummaryDTO getOrderSummaryById(Long id);

    boolean updateOrderStatus(Long id, String status);

    Page<OrderSummaryDTO> getAllOrderSummariesByStatus(String status, Pageable pageable);

    List<OrderDto> getOrderSummariesByUserId(Long userId);

    OrderDto getOrderDetailById(Long id);

    OrderDetailDto getOrderDetailDtoById(Long id);

    //    Page<OrderSummaryDTO> searchOrderSummaries(String keyword, String status, Pageable pageable);
    Page<OrderSummaryDTO> searchOrderSummaries(String keyword, String status, Pageable pageable);
}