package com.example.shopciafa;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Doing multiple different lists with Viewholder
public class HorizontalScrollListingOfProductAdapter extends RecyclerView.Adapter<HorizontalScrollListingOfProductAdapter.ViewHolder> {

    private List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList;

    //generate constructor method horizontalScrollListingOfProductModelList
    public HorizontalScrollListingOfProductAdapter(List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList) {
        this.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
    }

    @NonNull
    @Override
    //onCreateViewHolder(ViewGroup parent, int viewType)
    //It deals with the inflation of the layout as an item for the RecyclerView.
    // This method is called right when the adapter is created and is used to initialize your ViewHolder(s).
    public HorizontalScrollListingOfProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //The LayoutInflater class layout is used to create XML files into related View objects.
        //In other words, it takes XML file as input and creates View objects.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    //onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    //It deals with the setting of different data and methods related to clicks on particular items of the RecyclerView.
    public void onBindViewHolder(@NonNull HorizontalScrollListingOfProductAdapter.ViewHolder holder, int position) {
        String resource = horizontalScrollListingOfProductModelList.get(position).getProduct_image();
        String name = horizontalScrollListingOfProductModelList.get(position).getProduct_brand();
        String description = horizontalScrollListingOfProductModelList.get(position).getProduct_short_description();
        String price = horizontalScrollListingOfProductModelList.get(position).getProduct_price();
        String product_id = horizontalScrollListingOfProductModelList.get(position).getProduct_ID();
        holder.setData(product_id,resource, name, description, price);

    }

    @Override
    //It Returns the length of the RecyclerView i.e.the number of items inflated or present in it.
    public int getItemCount() {

        if(horizontalScrollListingOfProductModelList.size() > 10){
            return 10;
        }else{
            return horizontalScrollListingOfProductModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_brand;
        private TextView product_short_description;
        private TextView product_price;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.horizontal_scroll_product_image);
            product_brand = itemView.findViewById(R.id.horizontal_scroll_product_brand);
            product_short_description = itemView.findViewById(R.id.horizontal_scroll_product_short_definition);
            product_price = itemView.findViewById(R.id.horizontal_scroll_product_price);
        }

        private void setData(final String product_id, String resource, String name, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(product_image);
            product_brand.setText(name);
            product_short_description.setText(description);
            product_price.setText(price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //To know the details about the product, go to the product details page
                    Intent product_details_intent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    product_details_intent.putExtra("PRODUCT_ID", product_id);
                    itemView.getContext().startActivity(product_details_intent);
                }
            });
        }

    }
}
