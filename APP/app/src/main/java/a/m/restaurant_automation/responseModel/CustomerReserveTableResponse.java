package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.SerializedName;

public class CustomerReserveTableResponse {
    @SerializedName("tableId")
    public int tableId;

    @SerializedName("capacity")
    public int capacity;

    @SerializedName("availability")
    public boolean availability;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
