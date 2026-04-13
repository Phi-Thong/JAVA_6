package asm.poly.asm_java6.dto;

import java.math.BigDecimal;

public class CustomerSummaryDTO {

    private Long id;
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private Long orderCount;
    private BigDecimal totalSpent;
    private Boolean active;

    // Constructor rỗng
    public CustomerSummaryDTO() {
    }

    // Constructor dùng cho JPQL
    public CustomerSummaryDTO(
            Long id,
            String hoTen,
            String avatar,
            String email,
            String sdt,
            Long orderCount,
            BigDecimal totalSpent,
            Boolean active
    ) {
        this.id = id;
        this.name = hoTen;
        this.avatar = avatar;
        this.email = email;
        this.phone = sdt;
        this.orderCount = orderCount;
        this.totalSpent = totalSpent;
        this.active = active;
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "CustomerSummaryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", orderCount=" + orderCount +
                ", totalSpent=" + totalSpent +
                ", active=" + active +
                '}';
    }
}