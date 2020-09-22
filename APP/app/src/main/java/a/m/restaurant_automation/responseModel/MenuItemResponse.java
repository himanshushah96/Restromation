
package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuItemResponse {

    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("menuItemId")
    @Expose
    private Integer menuItemId;
    @SerializedName("menuItemName")
    @Expose
    private String menuItemName;
    @SerializedName("menuItemDescription")
    @Expose
    private String menuItemDescription;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("availablequantity")
    @Expose
    private Integer availablequantity;
    @SerializedName("itemStatus")
    @Expose
    private Boolean itemStatus;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("deletedBy")
    @Expose
    private String deletedBy;
    @SerializedName("categoryTitle")
    @Expose
    private String categoryTitle;

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    @SerializedName("itemImage")
    @Expose
    private String itemImage;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
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

    public Integer getAvailablequantity() {
        return availablequantity;
    }

    public void setAvailablequantity(Integer availablequantity) {
        this.availablequantity = availablequantity;
    }

    public Boolean getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(Boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}
