package asm.poly.asm_java6.dto;

import java.util.List;

public class OrderDetailDto {
    private Long orderId;
    private String ngayDat;
    private String trangThai;
    private long tongTien;
    private long phiVanChuyen;
    private String ghiChu;
    private String hoTenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiGiaoHang;
    private String emailNguoiNhan;
    private List<OrderItemDto> items;

    public OrderDetailDto() {
    }

    public OrderDetailDto(Long orderId, String ngayDat, String trangThai, long tongTien, long phiVanChuyen,
                          String ghiChu, String hoTenNguoiNhan, String sdtNguoiNhan, String diaChiGiaoHang,
                          String emailNguoiNhan, List<OrderItemDto> items) {
        this.orderId = orderId;
        this.ngayDat = ngayDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.phiVanChuyen = phiVanChuyen;
        this.ghiChu = ghiChu;
        this.hoTenNguoiNhan = hoTenNguoiNhan;
        this.sdtNguoiNhan = sdtNguoiNhan;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.emailNguoiNhan = emailNguoiNhan;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public long getPhiVanChuyen() {
        return phiVanChuyen;
    }

    public void setPhiVanChuyen(long phiVanChuyen) {
        this.phiVanChuyen = phiVanChuyen;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getHoTenNguoiNhan() {
        return hoTenNguoiNhan;
    }

    public void setHoTenNguoiNhan(String hoTenNguoiNhan) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
    }

    public String getSdtNguoiNhan() {
        return sdtNguoiNhan;
    }

    public void setSdtNguoiNhan(String sdtNguoiNhan) {
        this.sdtNguoiNhan = sdtNguoiNhan;
    }

    public String getDiaChiGiaoHang() {
        return diaChiGiaoHang;
    }

    public void setDiaChiGiaoHang(String diaChiGiaoHang) {
        this.diaChiGiaoHang = diaChiGiaoHang;
    }

    public String getEmailNguoiNhan() {
        return emailNguoiNhan;
    }

    public void setEmailNguoiNhan(String emailNguoiNhan) {
        this.emailNguoiNhan = emailNguoiNhan;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}