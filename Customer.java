package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Customer đại diện cho một khách hàng của ngân hàng.
 * Tuân thủ nghiêm ngặt các quy tắc định dạng của bộ tiêu chuẩn Google Java Style.
 */
public class Customer {
    private long idNumber;
    private String fullName;
    private List<Account> accountList;

    /**
     * Khởi tạo một đối tượng khách hàng mặc định không tham số.
     */
    public Customer() {
        this(0L, "");
    }

    /**
     * Khởi tạo một đối tượng khách hàng với số định danh và họ tên đầy đủ.
     *
     * @param idNumber Số CMND/CCCD của khách hàng.
     * @param fullName Họ và tên đầy đủ của khách hàng.
     */
    public Customer(long idNumber, String fullName) {
        this.idNumber = idNumber;
        this.fullName = fullName;
        this.accountList = new ArrayList<>();
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    /**
     * Thiết lập danh sách tài khoản cho khách hàng.
     *
     * @param accountList Danh sách tài khoản ngân hàng cần gán.
     */
    public void setAccountList(List<Account> accountList) {
        if (accountList == null) {
            this.accountList = new ArrayList<>();
        } else {
            this.accountList = accountList;
        }
    }

    /**
     * Thêm một tài khoản mới vào danh sách tài khoản của khách hàng (nếu chưa tồn tại).
     *
     * @param account Đối tượng tài khoản cần thêm.
     */
    public void addAccount(Account account) {
        if (account == null) {
            return;
        }
        if (!accountList.contains(account)) {
            accountList.add(account);
        }
    }

    /**
     * Xóa một tài khoản khỏi danh sách tài khoản quản lý của khách hàng.
     *
     * @param account Đối tượng tài khoản cần xóa.
     */
    public void removeAccount(Account account) {
        if (account == null) {
            return;
        }
        accountList.remove(account);
    }

    /**
     * Trả về thông tin cơ bản của khách hàng dưới dạng chuỗi văn bản.
     *
     * @return Chuỗi chứa thông tin Số CMND và Họ tên.
     */
    public String getCustomerInfo() {
        return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
    }
}