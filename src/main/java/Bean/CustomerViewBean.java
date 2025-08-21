package Bean;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class CustomerViewBean {

    private int accountNo;
    private String name;
    private String address;
    private String phone;
    private String email;
    private int unitsConsumed;
    private List<Bill> bills = new ArrayList<>();

    // Getters and Setters
    public int getAccountNo() { return accountNo; }
    public void setAccountNo(int accountNo) { this.accountNo = accountNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }
    public List<Bill> getBills() { return bills; }
    public void setBills(List<Bill> bills) { this.bills = bills; }

    // Inner class for Bill
    public static class Bill {
        private int billId;
        private double totalAmount;
        private Date billDate;

        public int getBillId() { return billId; }
        public void setBillId(int billId) { this.billId = billId; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        public Date getBillDate() { return billDate; }
        public void setBillDate(Date billDate) { this.billDate = billDate; }
    }
}
