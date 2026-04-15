package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.repository.OrderRepository;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class dashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository userRepository;

    // Doanh thu tháng & phần trăm tăng trưởng
    @GetMapping("/revenue")
    public Map<String, Object> getRevenueStats() {
        Object[] result = orderRepository.getRevenueStats().get(0); // lấy dòng đầu tiên
        double current = result[0] != null ? ((Number) result[0]).doubleValue() : 0;
        double prev = result[1] != null ? ((Number) result[1]).doubleValue() : 0;
        int percent = prev == 0 ? (current > 0 ? 100 : 0) : (int) Math.round((current - prev) * 100 / prev);

        Map<String, Object> map = new HashMap<>();
        map.put("revenue", current);
        map.put("percent", percent);
        return map;
    }

    // Tổng đơn hàng trong tháng hiện tại & phần trăm tăng trưởng
    @GetMapping("/orders")
    public Map<String, Object> getOrderCountStats() {
        Object[] result = orderRepository.getOrderCountStats().get(0);
        double current = result[0] != null ? ((Number) result[0]).doubleValue() : 0;
        double prev = result[1] != null ? ((Number) result[1]).doubleValue() : 0;
        int percent = prev == 0 ? (current > 0 ? 100 : 0) : (int) Math.round((current - prev) * 100 / prev);

        Map<String, Object> map = new HashMap<>();
        map.put("orders", current);
        map.put("percent", percent);
        return map;
    }

    // Khách hàng mới trong tháng hiện tại & phần trăm tăng trưởng
    @GetMapping("/new-customers")
    public Map<String, Object> getNewCustomerStats() {
        Object[] result = userRepository.getNewCustomerStats().get(0);
        double current = result[0] != null ? ((Number) result[0]).doubleValue() : 0;
        double prev = result[1] != null ? ((Number) result[1]).doubleValue() : 0;
        int percent = prev == 0 ? (current > 0 ? 100 : 0) : (int) Math.round((current - prev) * 100 / prev);

        Map<String, Object> map = new HashMap<>();
        map.put("newCustomers", current);
        map.put("percent", percent);
        return map;
    }

    @GetMapping("/revenue-chart")
    public Map<String, Object> getRevenueChart() {
        // Ví dụ: lấy doanh thu từng mốc, bạn cần viết truy vấn phù hợp
        List<Double> data = Arrays.asList(120.0, 200.0, 150.0, 300.0, 250.0, 400.0);
        Map<String, Object> map = new HashMap<>();
        map.put("labels", List.of("1-5", "6-10", "11-15", "16-20", "21-25", "26-31"));
        map.put("data", data);
        return map;
    }

    @GetMapping("/vip-customers")
    public List<Map<String, Object>> getVipCustomers() {
        List<Object[]> rows = userRepository.getTopVipCustomers();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("fullname", row[0]);
            map.put("avatar", row[1]);
            map.put("email", row[2]);
            map.put("totalOrders", row[3]);
            map.put("totalSpent", row[4]);
            result.add(map);
        }
        return result;
    }
}