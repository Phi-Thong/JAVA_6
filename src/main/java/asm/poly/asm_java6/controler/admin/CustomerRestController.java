package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.dto.CustomerSummaryDTO;
import asm.poly.asm_java6.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    private final UserService userService;

    public CustomerRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Object getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        Page<CustomerSummaryDTO> pageResult;

        // Convert status -> Boolean
        Boolean trangThai = null;
        if (status != null && !status.isEmpty()) {
            trangThai = status.equals("active");
        }

        // Nếu có keyword hoặc status -> dùng search
        if ((keyword != null && !keyword.isBlank()) || trangThai != null) {
            pageResult = userService.searchCustomers(
                    keyword,
                    trangThai,
                    PageRequest.of(page, size)
            );
        } else {
            // Không filter gì -> lấy tất cả
            pageResult = userService.getAllCustomerSummaries(
                    PageRequest.of(page, size)
            );
        }

        return Map.of(
                "content", pageResult.getContent(),
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements(),
                "page", pageResult.getNumber()
        );
    }
}