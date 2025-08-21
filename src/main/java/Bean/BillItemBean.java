package Bean;

import java.math.BigDecimal;

public class BillItemBean {
    private int billItemId;
    private int billId;
    private int itemId;
    private int quantity;
    private BigDecimal price; // unit price at time of billing
    private String title;     // for display (join from items)

    public int getBillItemId() { return billItemId; }
    public void setBillItemId(int billItemId) { this.billItemId = billItemId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
