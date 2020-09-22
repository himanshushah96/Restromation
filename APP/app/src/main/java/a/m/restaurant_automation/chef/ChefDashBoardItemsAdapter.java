package a.m.restaurant_automation.chef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.responseModel.MenuItems;

public class ChefDashBoardItemsAdapter extends RecyclerView.Adapter<ChefDashBoardItemsAdapter.ViewHolder> {
    ArrayList<MenuItems> menuItems;
    Context context;
    public ChefDashBoardItemsAdapter(ArrayList<MenuItems> menuItems){
        this.menuItems = menuItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chef_itemlist, parent, false);
        this.context = context;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
