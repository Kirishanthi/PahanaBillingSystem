package Bean;

import java.math.BigDecimal;

public class CartItemBean {
    private int itemId;
    private String title;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItemBean() {}

    public CartItemBean(int itemId, String title, BigDecimal unitPrice, int quantity) {
        this.itemId = itemId;
        this.title = title;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}
