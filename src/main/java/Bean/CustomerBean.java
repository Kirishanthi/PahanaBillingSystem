package Bean;

import java.sql.Date;

public class CustomerBean {
    private int customerId;
    private String accountNumber;
    private String name;
    private String address;
    private String telephone;
    private Date dateRegistered;

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Date getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(Date dateRegistered) { this.dateRegistered = dateRegistered; }
}
