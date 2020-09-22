package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCartItemResponseModel {
    @SerializedName("cartId")
    @Expose
    private Integer cartId;
    @SerializedName("menuItemId")
    @Expose
    private Integer menuItemId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("addedBy")
    @Expose
    private Integer addedBy;
    @SerializedName("menuItemName")
    @Expose
    private String menuItemName;
    @SerializedName("menuItemDescription")
    @Expose
    private String menuItemDescription;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("itemStatus")
    @Expose
    private Boolean itemStatus;
    @SerializedName("itemImage")
    @Expose
    private String itemImage;
    @SerializedName("checkoutPrice")
    @Expose
    private Double checkoutPrice;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getMenuItemDescription() {
        return menuItemDescription;
    }

    public void setMenuItemDescription(String menuItemDescription) {
        this.menuItemDescription = menuItemDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Double getCheckoutPrice() {
        return checkoutPrice;
    }

    public void setCheckoutPrice(Double checkoutPrice) {
        this.checkoutPrice = checkoutPrice;
    }

}
