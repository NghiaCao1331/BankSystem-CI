package org.example;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng (Account).
 * Tuân thủ nghiêm ngặt tiêu chuẩn định dạng và phong cách viết code của Google Java Style.
 */
public abstract class Account {

    // Tích hợp hệ thống Logging chuyên nghiệp theo yêu cầu Observability
    protected static final Logger logger = LoggerFactory.getLogger(Account.class);

    // ĐÃ SỬA: Chuyển đổi từ camelCase sang UPPER_SNAKE_CASE cho hằng số (static final)
    public static final String CHECKING_TYPE = "CHECKING";
    public static final String SAVINGS_TYPE = "SAVINGS";

    // ĐÃ SỬA: Đổi tên biến rõ nghĩa, loại bỏ dấu gạch dưới, tuân thủ camelCase
    private long accountNumber;
    private double balance;
    protected List<Transaction> transactionList;

    /**
     * Hàm khởi tạo một tài khoản mới với số tài khoản và số dư ban đầu.
     * Định dạng thụt lề chuẩn 2 spaces theo Google Style.
     */
    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionList = new ArrayList<>();
        logger.info("Khởi tạo tài khoản hệ thống thành công. Số TK: {}, Số dư: {}",
                accountNumber, balance);
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    /**
     * Cập nhật danh sách giao dịch cho tài khoản.
     */
    public void setTransactionList(List<Transaction> transactionList) {
        // ĐÃ SỬA: Bắt buộc thêm cặp dấu ngoặc nhọn {} cho câu lệnh if-else
        if (transactionList == null) {
            this.transactionList = new ArrayList<>();
        } else {
            this.transactionList = transactionList;
        }
    }

    /**
     * Thực hiện nạp tiền vào tài khoản.
     *
     * @param amount Số tiền cần nạp (phải lớn hơn 0).
     */
    public abstract void deposit(double amount);

    /**
     * Thực hiện rút tiền từ tài khoản.
     *
     * @param amount Số tiền cần rút.
     */
    public abstract void withdraw(double amount);

    /**
     * Xử lý logic nạp tiền chung nội bộ.
     */
    protected void doDepositing(double amount) throws InvalidFundingAmountException {
        // ĐÃ SỬA: Thêm khoảng trắng xung quanh toán tử so sánh (amount <= 0)
        if (amount <= 0) {
            logger.warn("Nạp tiền thất bại: Số tiền nạp phải > 0. Số TK: {}, Số tiền: {}",
                    accountNumber, amount);
            throw new InvalidFundingAmountException(amount);
        }
        balance += amount;
    }

    /**
     * Xử lý logic rút tiền chung nội bộ.
     * ĐÃ SỬA: Đổi 'throws Exception' chung chung thành các Exception cụ thể.
     */
    protected void doWithdrawing(double amount)
            throws InvalidFundingAmountException, InsufficientFundsException {
        if (amount <= 0) {
            logger.warn("Rút tiền thất bại: Số tiền rút phải > 0. Số TK: {}, Số tiền: {}",
                    accountNumber, amount);
            throw new InvalidFundingAmountException(amount);
        }
        if (amount > balance) {
            logger.warn("Rút tiền thất bại: Số dư không đủ. Số TK: {}, Số dư: {}, Yêu cầu: {}",
                    accountNumber, balance, amount);
            throw new InsufficientFundsException(amount);
        }
        balance -= amount;
    }

    /**
     * Thêm một giao dịch mới vào lịch sử.
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionList.add(transaction);
        }
    }

    /**
     * Lấy chuỗi lịch sử toàn bộ giao dịch của tài khoản.
     */
    public String getTransactionHistory() {
        logger.debug("Bắt đầu kết xuất lịch sử giao dịch cho tài khoản: {}", accountNumber);

        // ĐÃ SỬA: Thay thế cộng chuỗi lặp bằng StringBuilder để tối ưu hiệu năng (Performance)
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");

        for (int i = 0; i < transactionList.size(); i++) {
            stringBuilder.append(transactionList.get(i).getTransactionSummary());
            if (i < transactionList.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        // ĐÃ SỬA: Thay thế hoàn toàn System.out.println bằng log.info có cấu trúc
        logger.info("Đã xuất lịch sử giao dịch thành công cho tài khoản: {}, Tổng số giao dịch: {}",
                accountNumber, transactionList.size());

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        return this.accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        // ĐÃ SỬA: Căn chỉnh định dạng gọn gàng, rõ ràng
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}