package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.dto.OrderDto;
import asm.poly.asm_java6.dto.OrderSummaryDTO;
import asm.poly.asm_java6.enity.Order;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import asm.poly.asm_java6.dto.OrderDetailDto;
import asm.poly.asm_java6.dto.OrderItemDto;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public OrderDto getOrderDetailById(Long id) {
        // Lấy đơn hàng
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id: " + id));

        // Đếm số sản phẩm trong đơn
        Long soSanPham = (long) order.getOrderItems().size();

        // Lấy ảnh đại diện: ảnh chính của sản phẩm đầu tiên trong đơn hàng
        String anhChinh = null;
        if (!order.getOrderItems().isEmpty()) {
            anhChinh = order.getOrderItems().get(0).getProduct().getAnhChinh();
        }

        // Tạo DTO trả về
        return new OrderDto(
                order.getId(),
                order.getNgayDat(),
                soSanPham,
                order.getTongTien(),
                order.getTrangThai(),
                anhChinh
        );
    }

    @Override
    public OrderDetailDto getOrderDetailDtoById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;

        List<OrderItemDto> items = order.getOrderItems().stream().map(oi -> {
            Product p = oi.getProduct();
            BigDecimal soLuong = BigDecimal.valueOf(oi.getSoLuong());
            BigDecimal gia = oi.getGia();
            BigDecimal tongTienSanPham = soLuong.multiply(gia);

            return new OrderItemDto(
                    p.getId(),
                    p.getTenSanPham(),
                    p.getAnhChinh(),
                    oi.getSoLuong(),
                    gia.longValue(), // hoặc gia nếu DTO nhận BigDecimal
                    tongTienSanPham.longValue() // hoặc tongTienSanPham nếu DTO nhận BigDecimal
            );
        }).collect(Collectors.toList());

        return new OrderDetailDto(
                order.getId(),
                order.getNgayDat().toString(),
                order.getTrangThai(),
                order.getTongTien().longValue(), // nếu getTongTien là BigDecimal
                order.getPhiVanChuyen().longValue(),
                order.getGhiChu(),
                order.getHoTenNguoiNhan(),
                order.getSdtNguoiNhan(),
                order.getDiaChiGiaoHang(),
                order.getEmailNguoiNhan(),
                items
        );
    }

}