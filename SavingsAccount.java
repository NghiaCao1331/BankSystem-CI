package org.example;

/**
 * Lớp đại diện cho tài khoản tiết kiệm (Savings Account).
 * Áp dụng các quy định nghiêm ngặt về hạn mức rút tiền và số dư tối thiểu.
 */
public class SavingsAccount extends Account {

    // ĐÃ SỬA: Đưa các Magic Number ra thành hằng số rõ nghĩa theo chuẩn UPPER_SNAKE_CASE
    private static final double MAX_WITHDRAW_LIMIT = 1000.0;
    private static final double MIN_REQUIRED_BALANCE = 5000.0;

    /**
     * Khởi tạo một tài khoản tiết kiệm mới.
     *
     * @param accountNumber Số tài khoản.
     * @param balance       Số dư ban đầu.
     */
    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        // ĐÃ SỬA: Thay thế System.err bằng logger.debug để theo dõi luồng xử lý nội bộ
        logger.debug("Đang xử lý giao dịch nạp tiền cho tài khoản tiết kiệm.");

        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();

            // ĐÃ SỬA: Thay số '3' bằng hằng số rõ nghĩa từ lớp Transaction
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);

            // ĐÃ SỬA: Dùng log INFO lưu vết giao dịch thành công thay vì System.out
            logger.info("Nạp tiền tài khoản tiết kiệm thành công. Số TK: {}, Số tiền: +{}",
                    getAccountNumber(), amount);

        } catch (InvalidFundingAmountException e) {
            logger.error("Lỗi nghiệp vụ khi nạp tiền vào tài khoản tiết kiệm {}: {}",
                    getAccountNumber(), e.getMessage());
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            // ĐÃ SỬA: Sử dụng hằng số MAX_WITHDRAW_LIMIT thay vì số 1000.0 ghi chết
            if (amount > MAX_WITHDRAW_LIMIT) {
                logger.warn("Rút tiền thất bại: Vượt hạn mức tối đa một lần. Số TK: {}, Yêu cầu: {}",
                        getAccountNumber(), amount);
                throw new InvalidFundingAmountException(amount);
            }

            // ĐÃ SỬA: Sử dụng hằng số MIN_REQUIRED_BALANCE thay vì số 5000.0 ghi chết
            if (initialBalance - amount < MIN_REQUIRED_BALANCE) {
                logger.warn("Rút tiền thất bại: Số dư còn lại sau rút thấp hơn mức tối thiểu. "
                        + "Số TK: {}, Số dư hiện tại: {}, Yêu cầu: {}", getAccountNumber(), initialBalance, amount);
                throw new InsufficientFundsException(amount);
            }

            doWithdrawing(amount);
            double finalBalance = getBalance();

            // ĐÃ SỬA: Thay số '4' bằng hằng số rõ nghĩa từ lớp Transaction
            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);

            logger.info("Rút tiền từ tài khoản tiết kiệm thành công. Số TK: {}, Số tiền: -{}, Số dư mới: {}",
                    getAccountNumber(), amount, finalBalance);

        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            // ĐÃ SỬA: Ghi lại thông điệp lỗi cụ thể của ngoại lệ vào log và ném tiếp để tầng trên xử lý
            logger.error("Giao dịch rút tiền từ tài khoản tiết kiệm {} bị từ chối: {}",
                    getAccountNumber(), e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}