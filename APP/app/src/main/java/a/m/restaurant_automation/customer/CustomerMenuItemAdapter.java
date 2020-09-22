package a.m.restaurant_automation.customer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.chef.ChefDashboard;
import a.m.restaurant_automation.requestModel.AddToCartRequestModel;
import a.m.restaurant_automation.requestModel.DeleteOrModifyCart;
import a.m.restaurant_automation.responseModel.GetCartItemResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.service.IDataService;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerMenuItemAdapter extends RecyclerView.Adapter<CustomerMenuItemAdapter.ViewHolder> {

    private ArrayList<MenuItemResponse> menuItemResponsecustomer;
    private ArrayList<GetCartItemResponseModel> getCartItemResponseModel;
    int countQuantity, countQantityCart;
    int size = 0;
    AddToCartRequestModel tag;
    DeleteOrModifyCart cartTag;
    private Context context;
    public View.OnClickListener onItemListener_customer;
    public View.OnClickListener onItemListenerCart;
    String url = "https://cdn2.creativecirclemedia.com/neni/original/20190917-140036-Ratatouille-T5_93975.jpg";
    boolean isCart = false;
    boolean isMenu = false;
    int test = 0;

    public ItemsChangedListener itemsChangedListener;


    public CustomerMenuItemAdapter(ArrayList<MenuItemResponse> menuItemResponse, Context context) {
        this.menuItemResponsecustomer = menuItemResponse;
        size = this.menuItemResponsecustomer.size();
        this.context = context;
        this.isMenu = true;
    }

    public CustomerMenuItemAdapter(ArrayList<GetCartItemResponseModel> getCartItemResponseModel, Context context, ItemsChangedListener listener) {
        this.getCartItemResponseModel = getCartItemResponseModel;
        size = this.getCartItemResponseModel.size();
        this.context = context;
        this.test = test;
        this.isCart = true;
        itemsChangedListener = listener;
        //this.isMenu=false;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isMenu == true) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_customer_menu_list, parent, false);
            return new ViewHolder(view);
        } else if (isCart == true) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cart, parent, false);
            return new ViewHolder(view);

        }
        return null;
    }

    private final Handler handler = new Handler();

    @Override
    public void onBindViewHolder(@NonNull final CustomerMenuItemAdapter.ViewHolder holder, final int position) {
        if (isMenu) {
            holder.menuItemName.setText("NAME: " + menuItemResponsecustomer.get(position).getMenuItemName());
            holder.menuItemPrice.setText("PRICE: " + menuItemResponsecustomer.get(position).getPrice().toString() + " $");
            holder.menuItemDescription.setText("DESCRIPTION: " + menuItemResponsecustomer.get(position).getMenuItemDescription());
            if (menuItemResponsecustomer.get(position).getItemImage() == null || menuItemResponsecustomer.get(position).getItemImage().equals("")) {
                Picasso.get().load(url).into(holder.menuItemImage);
            } else {
                Picasso.get().load(menuItemResponsecustomer.get(position).getItemImage()).into(holder.menuItemImage);
            }
            holder.getAdapterPosition();
            holder.addItemButton.setTag(tag);
            tag = new AddToCartRequestModel();

            holder.plusItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            countQuantity = Integer.parseInt(String.valueOf(holder.menuItemQuantity.getText()));
                            countQuantity++;
                            tag.itemId = menuItemResponsecustomer.get(position).getMenuItemId();
                            tag.quantity = countQuantity;
                            holder.addItemButton.setTag(tag);
                            holder.menuItemQuantity.setText("" + countQuantity);
                        }
                    });

                }
            });
            holder.subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            countQuantity = Integer.parseInt(String.valueOf(holder.menuItemQuantity.getText()));
                            if (countQuantity == 0) {
                                holder.menuItemQuantity.setText("0");
                            } else {
                                countQuantity -= 1;
                                holder.menuItemQuantity.setText("" + countQuantity);
                            }
                            tag.itemId = menuItemResponsecustomer.get(position).getMenuItemId();
                            tag.quantity = countQuantity;
                            holder.addItemButton.setTag(tag);
                        }
                    });

                }
            });


        } else if (isCart) {
            if (getCartItemResponseModel.size() > 0) {


                holder.textView_itemName.setText("NAME: " + getCartItemResponseModel.get(position).getMenuItemName());
                holder.itemQuantity.setText("" + getCartItemResponseModel.get(position).getQuantity());
                holder.textView_totalItemPrice.setText("" + getCartItemResponseModel.get(position).getPrice() + " $");
                if (getCartItemResponseModel.get(position).getItemImage() == null || getCartItemResponseModel.get(position).getItemImage().equals("")) {
                    Picasso.get().load(url).into(holder.itemImageCart);
                } else {
                    Picasso.get().load(getCartItemResponseModel.get(position).getItemImage()).into(holder.itemImageCart);
                }

                double sum = 0;
                for (int i = 0; i < getCartItemResponseModel.size(); i++) {
                    sum = sum + getCartItemResponseModel.get(i).getPrice();
                }
                itemsChangedListener.onItemsChanged(sum);
                holder.getAdapterPosition();
                cartTag = new DeleteOrModifyCart();


                holder.removeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrModifyCart(getCartItemResponseModel.get(position).getCartId(), 0, true, holder, position);
                    }
                });

                holder.addItemCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                countQantityCart = Integer.parseInt(String.valueOf(holder.itemQuantity.getText()));
                                countQantityCart++;
                                cartTag.cartId = getCartItemResponseModel.get(position).getCartId();
                                cartTag.quantity = countQantityCart;
                                holder.addItemCart.setTag(cartTag);
                                holder.itemQuantity.setText("" + countQantityCart);
                                deleteOrModifyCart(cartTag.cartId, cartTag.quantity, false, holder, position);
                            }
                        });

                    }
                });
                holder.subtractItemCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                countQantityCart = Integer.parseInt(String.valueOf(holder.itemQuantity.getText()));
                                if (countQantityCart == 1) {
                                    holder.itemQuantity.setText("1");
                                } else {
                                    countQantityCart -= 1;
                                    holder.itemQuantity.setText("" + countQantityCart);
                                    deleteOrModifyCart(getCartItemResponseModel.get(position).getCartId(), countQantityCart, false, holder, position);
                                }

                            }
                        });

                    }
                });

            }
        }

    }


    @Override
    public int getItemCount() {
        return size;
    }

    public interface ItemsChangedListener {
        void onItemsChanged(double sum);
    }

    public void setItemsChangedListener(ItemsChangedListener listener) {
        this.itemsChangedListener = listener;
    }


    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        if (isMenu == true) {
            onItemListener_customer = onClickListener;
        } else if (isCart == true) {

            onItemListenerCart = onClickListener;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuItemName, menuItemPrice, menuItemQuantity, menuItemDescription, test;
        Button addItemButton;
        ImageView menuItemImage, plusItem, subItem;


        TextView textView_itemName, textView_itemPrice, itemQuantity, textView_totalItemPrice;
        ImageView removeItem, addItemCart, subtractItemCart;
        CircleImageView itemImageCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (isMenu) {
                menuItemImage = itemView.findViewById(R.id.imageView_menuItem_customer);
                menuItemName = itemView.findViewById(R.id.textView_menuName_customer);
                menuItemPrice = itemView.findViewById(R.id.textView_menuPrice_customer);
                menuItemDescription = itemView.findViewById(R.id.textView_menuItemDescription);
                addItemButton = itemView.findViewById(R.id.customer_addItemButton);
                plusItem = itemView.findViewById(R.id.buttonPlusCapacityItem);
                subItem = itemView.findViewById(R.id.buttonMinusCapacityItem);
                plusItem.setOnClickListener(onItemListener_customer);
                subItem.setOnClickListener(onItemListener_customer);

                addItemButton.setOnClickListener(onItemListener_customer);
                menuItemQuantity = itemView.findViewById(R.id.textviewcapacity);
                itemView.setTag(this);

            } else if (isCart) {
                textView_itemName = itemView.findViewById(R.id.textView_itemName_cart);
               // textView_itemPrice = itemView.findViewById(R.id.textView_itemPrice_cart);
                removeItem = itemView.findViewById(R.id.remove_item);
                itemQuantity = itemView.findViewById(R.id.textviewQuantityCart);
                addItemCart = itemView.findViewById(R.id.buttonAddQuantity);
                addItemCart.setOnClickListener(onItemListenerCart);
                textView_totalItemPrice = itemView.findViewById(R.id.textView_totalItemPrice);
                subtractItemCart = itemView.findViewById(R.id.buttonSubtractQuantity);
                itemImageCart = itemView.findViewById(R.id.itemImageCart);
                itemView.setTag(this);


            }
        }
    }

    public void deleteOrModifyCart(int cartIdCart, int cartQuantity, final boolean cartIsDelete, final CustomerMenuItemAdapter.ViewHolder holder, final int Postition) {
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        DeleteOrModifyCart deleteOrModifyCart = new DeleteOrModifyCart();
        deleteOrModifyCart.cartId = cartIdCart;
        deleteOrModifyCart.quantity = cartQuantity;
        deleteOrModifyCart.isDelete = cartIsDelete;


        Call<ResponseModel<StatusCheckResponse>> call = dataService.deleteOrModifyCartItems(cartIdCart, cartQuantity, cartIsDelete);
        call.enqueue(new Callback<ResponseModel<StatusCheckResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<StatusCheckResponse>> call, Response<ResponseModel<StatusCheckResponse>> response) {
                ResponseModel<StatusCheckResponse> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(context, responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        itemsChangedListener.onItemsChanged(responseModel.getData().total);
                        if (cartIsDelete && responseModel.getData().statusCode.equals("1")) {
                            getCartItemResponseModel.remove(Postition);
                            size = getCartItemResponseModel.size();
                            notifyDataSetChanged();
                        } else if (responseModel.getData().statusCode.equals("1")) {
                            holder.itemQuantity.setText(responseModel.getData().quantity);
                            holder.textView_totalItemPrice.setText(Double.toString(responseModel.getData().itemTotal) + " $");

                        }
                        //  Toast.makeText(context.getApplicationContext(),responseModel.getData().statusMessage, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<StatusCheckResponse>> call, Throwable t) {
                Toast.makeText(context, "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
