package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.dto.OrderDto;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import asm.poly.asm_java6.enity.Order;
import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<OrderSummaryDTO> getAllOrderSummaries(Pageable pageable) {
        return orderRepository.findAllOrderSummaries(pageable);
    }

    @Override
    public OrderSummaryDTO getOrderSummaryById(Long id) {

        return orderRepository.findOrderSummaryById(id);
        // Nếu không có, bạn có thể tự map từ entity:

    }

    @Override
    public boolean updateOrderStatus(Long id, String status) {
        Optional<Order> opt = orderRepository.findById(id);
        if (opt.isPresent()) {
            Order order = opt.get();
            order.setTrangThai(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    @Override
    public Page<OrderSummaryDTO> getAllOrderSummariesByStatus(String status, Pageable pageable) {
        return orderRepository.findAllOrderSummariesByStatus(status, pageable);
    }

    @Override
    public List<OrderDto> getOrderSummariesByUserId(Long userId) {
        return orderRepository.findOrderSummariesByUserId(userId);
    }

}