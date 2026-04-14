package asm.poly.asm_java6.dto;

import java.util.Date;

public class BrandSummaryDTO {
    private Long id;
    private String tenThuongHieu;
    private String moTa;
    private String trangThai;
    private Date ngayTao;
    private Long soLuong;

    // Constructor đúng thứ tự tham số như trong JPQL
    public BrandSummaryDTO(Long id, String tenThuongHieu, String moTa, String trangThai, Date ngayTao, Long soLuong) {
        this.id = id;
        this.tenThuongHieu = tenThuongHieu;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.soLuong = soLuong;
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Long getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Long soLuong) {
        this.soLuong = soLuong;
    }
}