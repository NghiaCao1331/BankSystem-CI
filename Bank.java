package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp quản lý hệ thống ngân hàng (Bank).
 * Chứa danh sách khách hàng và các phương thức xử lý dữ liệu, sắp xếp thông tin.
 */
public class Bank {

    private static final Logger logger = LoggerFactory.getLogger(Bank.class);

    // Biểu thức chính quy kiểm tra ID khách hàng (9 chữ số)
    private static final String ID_REGEX = "\\d{9}";

    // ĐÃ SỬA: Đổi tên biến c_list (snake_case) sang customerList (camelCase)
    private List<Customer> customerList;

    /**
     * Hàm khởi tạo hệ thống ngân hàng mặc định.
     */
    public Bank() {
        this.customerList = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Thiết lập danh sách khách hàng mới cho ngân hàng.
     *
     * @param customerList Danh sách khách hàng cần thiết lập.
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
        } else {
            this.customerList = customerList;
        }
    }

    /**
     * Đọc danh sách khách hàng và tài khoản từ một luồng dữ liệu (InputStream).
     * ĐÃ SỬA: Giảm lồng cấu trúc (nested if), thay thế System.out bằng Logger và catch cụ thể.
     *
     * @param inputStream Luồng dữ liệu đầu vào chứa thông tin khách hàng.
     */
    public void readCustomerList(InputStream inputStream) {
        logger.info("Bắt đầu đọc dữ liệu danh sách khách hàng từ luồng đầu vào.");

        if (inputStream == null) {
            logger.warn("Luồng dữ liệu đầu vào bị null. Hủy tác vụ đọc.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            Customer currentCustomer = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                int lastSpaceIndex = line.lastIndexOf(' ');
                if (lastSpaceIndex <= 0) {
                    continue;
                }

                String token = line.substring(lastSpaceIndex + 1).trim();

                // Trường hợp dòng này là thông tin Khách hàng (Có ID 9 chữ số ở cuối)
                if (token.matches(ID_REGEX)) {
                    String fullName = line.substring(0, lastSpaceIndex).trim();
                    currentCustomer = new Customer(Long.parseLong(token), fullName);
                    customerList.add(currentCustomer);
                    logger.debug("Đã thêm khách hàng mới vào bộ nhớ: {}", fullName);
                    continue;
                }

                // Trường hợp dòng này là thông tin Tài khoản của khách hàng hiện tại
                if (currentCustomer != null) {
                    parseAndAddAccount(currentCustomer, line);
                }
            }
            logger.info("Đã hoàn tất đọc dữ liệu. Tổng số khách hàng: {}", customerList.size());

        } catch (IOException e) {
            logger.error("Lỗi đọc file (I/O Error) trong quá trình xử lý danh sách: {}", e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Lỗi định dạng số khi phân tích dữ liệu tài khoản: {}", e.getMessage());
        }
    }

    /**
     * Hàm phụ trợ phân tích dòng dữ liệu để thêm tài khoản vào khách hàng (Tránh overload hàm chính).
     */
    private void parseAndAddAccount(Customer customer, String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            return;
        }

        long accNum = Long.parseLong(parts[0]);
        String accType = parts[1];
        double balance = Double.parseDouble(parts[2]);

        // ĐÃ SỬA: Sử dụng hằng số từ lớp Account để so sánh thay vì Magic String
        if (Account.CHECKING_TYPE.equals(accType)) {
            customer.addAccount(new CheckingAccount(accNum, balance));
        } else if (Account.SAVINGS_TYPE.equals(accType)) {
            customer.addAccount(new SavingsAccount(accNum, balance));
        }
    }

    /**
     * Lấy thông tin toàn bộ khách hàng được sắp xếp theo số ID tăng dần.
     */
    public String getCustomersInfoByIdOrder() {
        logger.debug("Thực hiện sắp xếp khách hàng theo ID số.");

        // ĐÃ SỬA: Thay thế Anonymous class bằng Lambda Expression ngắn gọn
        customerList.sort(Comparator.comparingLong(Customer::getIdNumber));

        return buildCustomerInfoString(customerList);
    }

    /**
     * Lấy thông tin toàn bộ khách hàng được sắp xếp theo Tên (nếu trùng tên thì xét đến ID).
     */
    public String getCustomersInfoByNameOrder() {
        logger.debug("Thực hiện sắp xếp khách hàng theo Tên.");

        List<Customer> sortedCopy = new ArrayList<>(customerList);

        // ĐÃ SỬA: Căn chỉnh format Lambda chuẩn Google Style, ngắt dòng hợp lý
        sortedCopy.sort((c1, c2) -> {
            int nameCompare = c1.getFullName().compareTo(c2.getFullName());
            return nameCompare != 0 ? nameCompare : Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });

        return buildCustomerInfoString(sortedCopy);
    }

    /**
     * Hàm dùng chung để tạo chuỗi thông tin từ danh sách khách hàng (Giải quyết Code Duplication).
     * ĐÃ SỬA: Sử dụng StringBuilder thay cho phép cộng chuỗi lặp gây tốn tài nguyên.
     */
    private String buildCustomerInfoString(List<Customer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i).getCustomerInfo());
            if (i < list.size() - 1) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}