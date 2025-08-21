package Bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class BillBean {
    private int billId;
    private int customerId;
    private Date billDate;
    private BigDecimal totalAmount;

    private CustomerBean customer;          // joined
    private List<BillItemBean> items;       // joined

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public CustomerBean getCustomer() { return customer; }
    public void setCustomer(CustomerBean customer) { this.customer = customer; }

    public List<BillItemBean> getItems() { return items; }
    public void setItems(List<BillItemBean> items) { this.items = items; }
}
