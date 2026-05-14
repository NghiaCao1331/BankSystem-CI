package org.example;

/**
 * Lớp đại diện cho tài khoản vãng lai (Checking Account).
 * Kế thừa từ lớp trừu tượng Account và triển khai các phương thức nạp/rút tiền cụ thể.
 */
public class CheckingAccount extends Account {

    /**
     * Khởi tạo một tài khoản vãng lai mới.
     *
     * @param accountNumber Số tài khoản.
     * @param balance       Số dư ban đầu.
     */
    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        try {
            // Gọi logic nạp tiền xử lý chung từ lớp cha
            doDepositing(amount);

            double finalBalance = getBalance();
            // Khai báo hằng số từ lớp Transaction (Ví dụ giả định TYPE_DEPOSIT_CHECKING hoặc đổi theo file của bạn)
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);

            addTransaction(transaction);

        } catch (InvalidFundingAmountException e) {
            // ĐÃ SỬA: Thay thế System.out.println bằng logger.error để ghi nhận lỗi giao dịch tài chính
            logger.error("Lỗi nghiệp vụ khi nạp tiền vào tài khoản vãng lai {}: {}",
                    getAccountNumber(), e.getMessage());
            // Ném lại RuntimeException hoặc xử lý tiếp tùy kiến trúc để tầng trên biết giao dịch thất bại
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            // Gọi logic rút tiền xử lý chung từ lớp cha
            doWithdrawing(amount);

            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);

            addTransaction(transaction);

        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            // ĐÃ SỬA: Gom nhóm catch các Exception cụ thể thay vì catch bừa bãi
            logger.error("Lỗi nghiệp vụ khi rút tiền từ tài khoản vãng lai {}: {}",
                    getAccountNumber(), e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}