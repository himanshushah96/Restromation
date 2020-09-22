package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetReadyForPaymentResponseModel {
    @SerializedName("billId")
    @Expose
    private Integer billId;
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("billDate")
    @Expose
    private String billDate;
    @SerializedName("billingAmount")
    @Expose
    private Double billingAmount;
    @SerializedName("icCardPayment")
    @Expose
    private Boolean icCardPayment;
    @SerializedName("isPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("isReadyToPay")
    @Expose
    private Boolean isReadyToPay;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("tableID")
    @Expose
    private Integer tableID;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Double billingAmount) {
        this.billingAmount = billingAmount;
    }

    public Boolean getIcCardPayment() {
        return icCardPayment;
    }

    public void setIcCardPayment(Boolean icCardPayment) {
        this.icCardPayment = icCardPayment;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Boolean getIsReadyToPay() {
        return isReadyToPay;
    }

    public void setIsReadyToPay(Boolean isReadyToPay) {
        this.isReadyToPay = isReadyToPay;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTableID() {
        return tableID;
    }

    public void setTableID(Integer tableID) {
        this.tableID = tableID;
    }
}
