package asm.poly.asm_java6.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    private Long id;
    private LocalDateTime ngayDat;
    private Long soSanPham;
    private BigDecimal tongTien;
    private String trangThai;

    public OrderDto() {
    }

   
    public OrderDto(Long id, LocalDateTime ngayDat, Long soSanPham, BigDecimal tongTien, String trangThai) {
        this.id = id;
        this.ngayDat = ngayDat;
        this.soSanPham = soSanPham;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Long getSoSanPham() {
        return soSanPham;
    }

    public void setSoSanPham(Long soSanPham) {
        this.soSanPham = soSanPham;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}