package asm.poly.asm_java6.dto;

import java.math.BigDecimal;

public class BestSellerProductDTO {
    private Long id;
    private String tenSanPham;
    private BigDecimal gia;
    private String anhChinh;
    private String tenThuongHieu;
    private Long tongBan;

    public BestSellerProductDTO() {
    }

    public BestSellerProductDTO(Long id, String tenSanPham, BigDecimal gia, String anhChinh, String tenThuongHieu, Long tongBan) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.anhChinh = anhChinh;
        this.tenThuongHieu = tenThuongHieu;
        this.tongBan = tongBan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public String getAnhChinh() {
        return anhChinh;
    }

    public void setAnhChinh(String anhChinh) {
        this.anhChinh = anhChinh;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public Long getTongBan() {
        return tongBan;
    }

    public void setTongBan(Long tongBan) {
        this.tongBan = tongBan;
    }
}