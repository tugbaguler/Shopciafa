package com.example.shopciafa;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter {

    private List<CardItemsModel> cardItemsModelsList;
    private int last_position = -1;
    private TextView card_total_amount;
    private boolean show_btn_delete;

    //constructor method
    public CardAdapter(List<CardItemsModel> cardItemsModelsList, TextView card_total_amount, boolean show_btn_delete) {
        this.cardItemsModelsList = cardItemsModelsList;
        this.card_total_amount = card_total_amount;
        this.show_btn_delete = show_btn_delete;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cardItemsModelsList.get(position).getType()){
            case 0:
                return CardItemsModel.CART_ITEM;
            case 1:
                return CardItemsModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case CardItemsModel.CART_ITEM:
                View card_items_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items,parent,false);
                return new CardItemsViewholder(card_items_view);
            case CardItemsModel.TOTAL_AMOUNT:
                View card_total_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_total_amount,parent,false);
                return new CardTotalAmountViewholder(card_total_view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cardItemsModelsList.get(position).getType()){
            case CardItemsModel.CART_ITEM:
                String product_id = cardItemsModelsList.get(position).getProduct_id();
                String resource = cardItemsModelsList.get(position).getProduct_image();
                String product_title = cardItemsModelsList.get(position).getProduct_title();
                String product_price = cardItemsModelsList.get(position).getProduct_price();
                String product_price_without_discount = cardItemsModelsList.get(position).getProduct_price_without_discount();

                ((CardItemsViewholder)holder).setItemDetails(product_id, resource, product_title,product_price,product_price_without_discount, position);
                break;
            case CardItemsModel.TOTAL_AMOUNT:

                int total_items = 0;
                int total_items_price = 0;
                String string_price;
                String new_string_price;
                String delivery_price;
                int new_integer_price = 0;
                int total_amount;
                for (int x = 0 ; x < cardItemsModelsList.size() ; x++){
                    if (cardItemsModelsList.get(x).getType() == CardItemsModel.CART_ITEM){
                        if((cardItemsModelsList.get(x).getProduct_price()).contains("$")){
                            string_price = cardItemsModelsList.get(x).getProduct_price();
                            new_string_price = string_price.substring(0,string_price.length() -1);
                            new_integer_price = Integer.parseInt(new_string_price.substring(0, new_string_price.indexOf('.')));
                        }
                        total_items++;
                        total_items_price = total_items_price + new_integer_price;
                    }
                }
                if (total_items_price > 250){
                    delivery_price = "FREE";
                    total_amount = total_items_price;
                }else{
                    delivery_price = "20";
                    total_amount = total_items_price + Integer.parseInt(delivery_price);
                }


                ((CardTotalAmountViewholder)holder).setTotal_amount(total_items,total_items_price,delivery_price, total_amount);
                break;
            default:
                return;
        }

        if (last_position < position){
            last_position = position;
        }
    }

    @Override
    public int getItemCount() {
        return cardItemsModelsList.size();
    }

    class CardItemsViewholder extends RecyclerView.ViewHolder{

        private ImageView product_image;
        private TextView product_title;
        private TextView product_price;
        private TextView product_price_without_discount;
        private TextView product_quantity;
        private LinearLayout delete_button;

        public CardItemsViewholder(@NonNull View itemView){
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_title = itemView.findViewById(R.id.card_items_product_title_textView);
            product_price = itemView.findViewById(R.id.last_price_textView);
            product_price_without_discount = itemView.findViewById(R.id.price_before_discount_textView);
            product_quantity = itemView.findViewById(R.id.quantity_textView);
            delete_button = itemView.findViewById(R.id.btn_remove_item_from_card);
        }

        private void setItemDetails(String product_id, String  resource, String title, String price, String ppwd, final int position){
            //product_image.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(product_image);
            product_title.setText(title);
            product_price.setText(price);
            product_price_without_discount.setText(ppwd);

            //If more than one order will be placed while purchasing a product, the code (dialog) that determines how many orders will be placed.
            //The user will determine how many products to order
            product_quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog determining_product_quantity_dialog = new Dialog(itemView.getContext());
                    determining_product_quantity_dialog.setContentView(R.layout.determining_product_quantity);
                    determining_product_quantity_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    determining_product_quantity_dialog.setCancelable(false);
                    final EditText quantity_number = determining_product_quantity_dialog.findViewById(R.id.product_quantity_number);
                    Button OK_button = determining_product_quantity_dialog.findViewById(R.id.btn_ok_determining_product_quantity);
                    Button cancel_button = determining_product_quantity_dialog.findViewById(R.id.btn_cancel_determining_product_quantity);

                    OK_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            product_quantity.setText("Qty: " + quantity_number.getText());
                            determining_product_quantity_dialog.dismiss();
                        }
                    });

                    cancel_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            determining_product_quantity_dialog.dismiss();
                        }
                    });
                    determining_product_quantity_dialog.show();
                }
            });
            if (show_btn_delete){
                delete_button.setVisibility(View.VISIBLE);
            }else{
                delete_button.setVisibility(View.GONE);
            }
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseQueries.removeProductFromCard(position,itemView.getContext());
                }
            });
        }
    }

    class CardTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView total_items;
        private TextView total_items_price;
        private TextView total_amount;
        private TextView delivery_price;

        public CardTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            total_items = itemView.findViewById(R.id.total_items);
            total_items_price = itemView.findViewById(R.id.total_items_price);
            delivery_price = itemView.findViewById(R.id.delivery_charge_price_value);
            total_amount = itemView.findViewById(R.id.total_amount_price);
        }

        private void setTotal_amount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText){
            total_items.setText("Price (" + totalItemText + "items)");
            total_items_price.setText(String.valueOf(totalItemPriceText));
            if (!deliveryPriceText.equals("FREE")) {
                delivery_price.setText(deliveryPriceText);
            }
            card_total_amount.setText(String.valueOf(totalAmountText));
            total_amount.setText(String.valueOf(totalAmountText));
        }
    }
}
