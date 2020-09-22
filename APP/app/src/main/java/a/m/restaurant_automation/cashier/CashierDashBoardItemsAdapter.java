package a.m.restaurant_automation.cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.responseModel.MenuItems;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CashierDashBoardItemsAdapter extends RecyclerView.Adapter<CashierDashBoardItemsAdapter.ViewHolder> {
    ArrayList<MenuItems> menuItems;
    Context context;
    public CashierDashBoardItemsAdapter(ArrayList<MenuItems> menuItems){
        this.menuItems = menuItems;
    }
    @NonNull
    @Override
    public CashierDashBoardItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chef_itemlist, parent, false);
        this.context = context;
        return new CashierDashBoardItemsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CashierDashBoardItemsAdapter.ViewHolder holder, int position) {
        holder.itemTV.setText(menuItems.get(position).itemName);
        holder.qtyTV.setText(""+menuItems.get(position).itemQty);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV, qtyTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTV = itemView.findViewById(R.id.txtItemNameItemlist);
            qtyTV = itemView.findViewById(R.id.txtItemQuantityItemlist);
        }
    }

}
