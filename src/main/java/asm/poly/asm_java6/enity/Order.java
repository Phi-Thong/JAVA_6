package asm.poly.asm_java6.enity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người đặt hàng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private users user;

    // Thông tin người nhận
    @Column(name = "ho_ten_nguoi_nhan", nullable = false)
    private String hoTenNguoiNhan;

    @Column(name = "sdt_nguoi_nhan", nullable = false)
    private String sdtNguoiNhan;

    @Column(name = "email_nguoi_nhan", nullable = false)
    private String emailNguoiNhan;

    @Column(name = "dia_chi_giao_hang", nullable = false)
    private String diaChiGiaoHang;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "tong_tien", nullable = false)
    private BigDecimal tongTien;

    @Column(name = "phi_van_chuyen", nullable = false)
    private BigDecimal phiVanChuyen;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDateTime ngayDat;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai; // Ví dụ: PENDING, PAID, SHIPPED, CANCELED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<
            OrderItem> orderItems;
}