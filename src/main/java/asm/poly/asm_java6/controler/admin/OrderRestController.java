package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.dto.OrderSummaryDTO;
import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    // Lấy danh sách đơn hàng phân trang
    @GetMapping
    public Page<OrderSummaryDTO> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false, defaultValue = "all") String status,
            @RequestParam(required = false) String keyword

    ) {
        System.out.println("Status param: " + status);
        Sort.Direction direction = "oldest".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, "ngayDat"));
        Page<OrderSummaryDTO> result;
        if ("all".equalsIgnoreCase(status)) {
            result = orderService.getAllOrderSummaries(pageRequest);
        } else {
            result = orderService.getAllOrderSummariesByStatus(status, pageRequest);
        }
        for (OrderSummaryDTO dto : result.getContent()) {
            System.out.println("Order ID: " + dto.getId() + " - Trạng thái: " + dto.getStatus());
        }
        return result;
    }

    // Lấy chi tiết 1 đơn hàng theo id (dùng cho modal cập nhật)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderSummaryDTO dto = orderService.getOrderSummaryById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body("Order not found");
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        boolean updated = orderService.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success"));
        } else {
            return ResponseEntity.status(400).body(Map.of("status", "fail", "message", "Không cập nhật được trạng thái!"));
        }
    }

    @GetMapping("/count-by-status")
    public Map<String, Long> countOrdersByStatus() {
        Map<String, Long> result = new HashMap<>();
        result.put("all", orderRepository.count());
        result.put("processing", orderRepository.countByTrangThai("ÐANG_CH?_XÁC_NH?N"));
        result.put("shipping", orderRepository.countByTrangThai("DANG_GIAO"));
        result.put("done", orderRepository.countByTrangThai("DA_GIAO"));
        result.put("cancel", orderRepository.countByTrangThai("DA_HUY"));
        return result;
    }
}