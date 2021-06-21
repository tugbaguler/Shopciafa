package com.example.shopciafa;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.Viewholder> {

    private List<MyOrdersItemsModel> myOrdersItemsModelList;

    //constructor method
    public MyOrdersAdapter(List<MyOrdersItemsModel> myOrdersItemsModelList) {
        this.myOrdersItemsModelList = myOrdersItemsModelList;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_items,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.Viewholder viewholder, int position) {
        int resource = myOrdersItemsModelList.get(position).getProduct_image();
        String title = myOrdersItemsModelList.get(position).getProduct_title();
        String delivered_info = myOrdersItemsModelList.get(position).getDelivery_information();
        int rating =myOrdersItemsModelList.get(position).getRating();
        viewholder.setData(resource,title,delivered_info,rating);
        /*
        //The area where the background colors are arranged to make the orders look neat
        if (position % 2 == 0){
            viewholder.constraintlayout_myorders.setBackgroundColor(Color.parseColor("#FDFDFD"));
        }else{
            viewholder.constraintlayout_myorders.setBackgroundColor(Color.parseColor("#DFDFDF"));
        }
        */
    }

    @Override
    public int getItemCount() {
        return myOrdersItemsModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ConstraintLayout constraintlayout_myorders;
        private ImageView product_image;
        private ImageView order_indicator_dot;
        private TextView product_title;
        private TextView delivery_information;
        private LinearLayout giving_rate_star_container;

        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image_in_my_orders);
            order_indicator_dot = itemView.findViewById(R.id.order_status_indicator_dot);
            product_title = itemView.findViewById(R.id.product_title_in_my_orders);
            delivery_information = itemView.findViewById(R.id.order_delivery_date_information);
            giving_rate_star_container = itemView.findViewById(R.id.rate_star_container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent order_details_intent = new Intent(itemView.getContext(),OrderDetailsActivity.class);
                    itemView.getContext().startActivity(order_details_intent);

                }
            });
        }

        private void setData(int resource, String title, String delivered_info, int rating){
            product_image.setImageResource(resource);
            product_title.setText(title);
            if(delivered_info.equals("Cancelled")){
                order_indicator_dot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
            }
            else{
                order_indicator_dot.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }
            delivery_information.setText(delivered_info);

            /*-----Product Rating Area-----*/
            setRating(rating);
            for (int x = 0; x < giving_rate_star_container.getChildCount(); x++){
                final int starPosition = x;
                giving_rate_star_container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setRating(starPosition);
                    }
                });
            }
        }

        private void setRating(int starPosition) {
            for(int x = 0; x < giving_rate_star_container.getChildCount(); x++){
                ImageView btn_star = (ImageView) giving_rate_star_container.getChildAt(x);
                btn_star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                if(x <= starPosition){
                    btn_star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffc107")));
                }
            }
        }
    }
}
