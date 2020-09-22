package a.m.restaurant_automation.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.MenuItemResponse;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    public ArrayList<MenuItemResponse> menuItemResponse;
    private ArrayList<CustomerReserveTableResponse> tableResponseModel;
    int size = 0;
    int test=0;
    boolean tableStatus;
    private Context context;
    public View.OnClickListener onItemListener;
    String url = "https://cdn2.creativecirclemedia.com/neni/original/20190917-140036-Ratatouille-T5_93975.jpg";

    boolean isTable =false;
    boolean isMenuItem = false;


    public MenuItemAdapter(ArrayList<MenuItemResponse> menuItemResponse, Context context) {
        this.menuItemResponse = menuItemResponse;
        size = this.menuItemResponse.size();
        isMenuItem = true;
        this.context = context;
    }

    public MenuItemAdapter(ArrayList<CustomerReserveTableResponse> tableResponseModel, Context context,int test) {
        this.tableResponseModel = tableResponseModel;
        size = this.tableResponseModel.size();
        this.context = context;
        this.test=test;
        isTable = true;
    }

    @NonNull
    @Override
    public MenuItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isMenuItem==true) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_employee_menu_list, parent, false);
            return new ViewHolder(view);
        }
        else if (isTable == true){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_employee_table, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdapter.ViewHolder holder, int position) {
        if (isMenuItem) {
            holder.menuItemName.setText("Name: " + menuItemResponse.get(position).getMenuItemName());
            holder.menuItemPrice.setText("Price: " + menuItemResponse.get(position).getPrice().toString() + " $");
            holder.menuItemDescription.setText("Description: " +menuItemResponse.get(position).getMenuItemDescription());
            holder.updateItemButton.setTag(menuItemResponse.get(position).getMenuItemId());
            holder.removeItemButton.setTag(menuItemResponse.get(position).getMenuItemId());
            if (menuItemResponse.get(position).getItemImage() ==null || menuItemResponse.get(position).getItemImage().equals("")){
                Picasso.get().load(url).into(holder.menuItemImage);
            }
            else {
                Picasso.get().load((menuItemResponse.get(position).getItemImage())).into(holder.menuItemImage);
            }

        }
        else if (isTable){
            tableStatus=tableResponseModel.get(position).getAvailability();
            holder.tableNumber.setText("Table Number: "+ tableResponseModel.get(position).getTableId());
            holder.tableCapacity.setText("Table Capacity: "+ tableResponseModel.get(position).getCapacity());
            holder.deleteTable.setTag(tableResponseModel.get(position).getTableId());
            if(tableStatus==true)
            {
                holder.tableStatus.setText("Table Status: Available");

            }else
            {
                holder.tableStatus.setText("Table Status: Unavailable ");
            }

        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {

        if(isMenuItem==true) {
            onItemListener = onClickListener;
        }
        else if(isTable==true)
        {
            onItemListener=onClickListener;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuItemName, menuItemPrice, menuItemDescription;
        Button updateItemButton,removeItemButton;
        ImageView menuItemImage,deleteTable;


        TextView tableNumber, tableCapacity, tableStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(isMenuItem){
                menuItemImage= itemView.findViewById(R.id.imageView_menuItem);
                menuItemName = itemView.findViewById(R.id.textView_menuName);
                menuItemPrice = itemView.findViewById(R.id.textView_menuPrice);
                menuItemDescription = itemView.findViewById(R.id.textView_menuDescription);
                updateItemButton = itemView.findViewById(R.id.updateItemButton);
                updateItemButton.setOnClickListener(onItemListener);
                removeItemButton= itemView.findViewById(R.id.deleteItem);
                removeItemButton.setOnClickListener(onItemListener);
                itemView.setTag(this);
            }
            else if(isTable) {
                tableNumber = itemView.findViewById(R.id.textView_tableNumber);
                tableCapacity = itemView.findViewById(R.id.textView_tableCapacity);
                tableStatus = itemView.findViewById(R.id.textView_tableStatus);
                deleteTable=itemView.findViewById(R.id.imageDelete);
                deleteTable.setOnClickListener(onItemListener);
                itemView.setTag(this);

            }

        }
    }
}
