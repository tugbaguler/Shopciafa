package com.example.shopciafa;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridListingProductAdapter extends BaseAdapter {

    List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList;

    //constructor method
    public GridListingProductAdapter(List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList) {
        this.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
    }

    @Override
    public int getCount() {
        return horizontalScrollListingOfProductModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_items,null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Clicking on the product will go to the product information page
                    Intent product_details_intent = new Intent(viewGroup.getContext(),ProductDetailsActivity.class);
                    product_details_intent.putExtra("PRODUCT_ID",horizontalScrollListingOfProductModelList.get(i).getProduct_ID());
                    viewGroup.getContext().startActivity(product_details_intent);
                }
            });

            ImageView product_image = view.findViewById(R.id.horizontal_scroll_product_image);
            TextView product_brand = view.findViewById(R.id.horizontal_scroll_product_brand);
            TextView product_short_description = view.findViewById(R.id.horizontal_scroll_product_short_definition);
            TextView product_price = view.findViewById(R.id.horizontal_scroll_product_price);

            Glide.with(view.getContext()).load(horizontalScrollListingOfProductModelList.get(i).getProduct_image()).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(product_image);
            product_brand.setText(horizontalScrollListingOfProductModelList.get(i).getProduct_brand());
            product_short_description.setText(horizontalScrollListingOfProductModelList.get(i).getProduct_short_description());
            product_price.setText(horizontalScrollListingOfProductModelList.get(i).getProduct_price());
        }
        else{
            view = convertView;
        }
        return view;
    }
}