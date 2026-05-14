package org.example;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp Transaction đại diện cho thông tin một giao dịch tài chính trong hệ thống.
 * Tuân thủ nghiêm ngặt các quy tắc định dạng của bộ tiêu chuẩn Google Java Style.
 */
public class Transaction {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    /**
     * Khởi tạo một giao dịch với đầy đủ các thông số tài chính.
     *
     * @param type           Kiểu giao dịch (1-4).
     * @param amount         Số tiền giao dịch.
     * @param initialBalance Số dư trước khi giao dịch.
     * @param finalBalance   Số dư sau khi giao dịch.
     */
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    /**
     * Chuyển đổi mã kiểu giao dịch (int) thành chuỗi văn bản mô tả tương ứng.
     * ĐÃ SỬA: Đổi tên hàm từ snake_case sang camelCase, thay Magic Number bằng hằng số.
     *
     * @param transactionType Mã kiểu giao dịch cần chuyển đổi.
     * @return Chuỗi mô tả tên kiểu giao dịch.
     */
    public static String getTypeString(int transactionType) {
        switch (transactionType) {
            case TYPE_DEPOSIT_CHECKING:
                return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING:
                return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS:
                return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS:
                return "Rút tiền tiết kiệm";
            default:
                return "Không rõ";
        }
    }

    /**
     * Xuất chuỗi tóm tắt thông tin chi tiết của giao dịch phục vụ in ấn lịch sử.
     * ĐÃ SỬA: Thay System.out bằng logger.debug, ngắt dòng code quá dài theo chuẩn.
     *
     * @return Chuỗi định dạng tóm tắt giao dịch.
     */
    public String getTransactionSummary() {
        // ĐÃ SỬA: Thay thế System.out bằng structured logging mức độ DEBUG
        logger.debug("Bắt đầu xử lý kết xuất tóm tắt giao dịch cho kiểu: {}", this.type);

        // ĐÃ SỬA: Tách nhỏ các đoạn định dạng số thực để dòng code không bị quá dài
        String initialStr = String.format(Locale.US, "%.2f", initialBalance);
        String amountStr = String.format(Locale.US, "%.2f", amount);
        String finalStr = String.format(Locale.US, "%.2f", finalBalance);

        // Sử dụng toán tử cộng chuỗi có ngắt dòng rõ ràng, thụt lề chuẩn Google Style
        return "- Kiểu giao dịch: " + getTypeString(type)
                + ". Số dư ban đầu: $" + initialStr
                + ". Số tiền: $" + amountStr
                + ". Số dư cuối: $" + finalStr + ".";
    }
}