package asm.poly.asm_java6.service;

import asm.poly.asm_java6.dto.CustomerSummaryDTO;
import asm.poly.asm_java6.enity.users;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    users findByEmail(String email);

    void save(users user);

    void sendVerifyEmail(String toEmail, String fullName, String token);

    users findByToken(String token);

//    List<CustomerSummaryDTO> getAllCustomerSummaries();

    Page<CustomerSummaryDTO> getAllCustomerSummaries(Pageable pageable);

    Page<CustomerSummaryDTO> getAllCustomerSummariesByStatus(boolean trangThai, Pageable pageable);

    Page<CustomerSummaryDTO> searchCustomers(String keyword, Boolean trangThai, Pageable pageable);
}