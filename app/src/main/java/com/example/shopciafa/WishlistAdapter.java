package com.example.shopciafa;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.shopciafa.DatabaseQueries.wishList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewhHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int last_position = -1;

    //constructor method
    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewhHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items,parent,false);
        return new ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewhHolder holder, int position) {
        String product_id = wishlistModelList.get(position).getProduct_id();
        String resource = wishlistModelList.get(position).getProduct_image();
        String title = wishlistModelList.get(position).getProduct_title();
        String rating = wishlistModelList.get(position).getProduct_rating();
        long total_ratings = wishlistModelList.get(position).getTotal_ratings();
        String product_price = wishlistModelList.get(position).getProduct_price();
        String price_before_discount = wishlistModelList.get(position).getPrice_before_discount();
        Boolean payment_method = wishlistModelList.get(position).getPayment_method();
        holder.setData(product_id,resource,title,rating,total_ratings,product_price,price_before_discount,payment_method,position);

        if (last_position < position){
            last_position = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewhHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_title;
        private TextView product_rating;
        private TextView total_ratings;
        private TextView product_price;
        private View price_divider;
        private TextView price_before_discount;
        private TextView payment_method;
        private ImageButton delete_button;

        public ViewhHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image_wishlist_items);
            product_title = itemView.findViewById(R.id.product_title_wishlist_items);
            product_rating = itemView.findViewById(R.id.rating_of_product_textView);
            total_ratings = itemView.findViewById(R.id.total_ratings_textView);
            product_price = itemView.findViewById(R.id.product_price_wishlist_items);
            price_divider = itemView.findViewById(R.id.price_divider_wishlist_items);
            price_before_discount = itemView.findViewById(R.id.price_befor_discount_wishlist_items);
            payment_method = itemView.findViewById(R.id.payment_method_wishlist_items);
            delete_button = itemView.findViewById(R.id.btn_delete);
        }

        private void setData(final String product_id, String resource, String title, String avarageRate, long totalRate, String price, String priceBeforeDiscountValue, Boolean paymentMethod, final int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(product_image);
            product_title.setText(title);
            product_rating.setText(avarageRate);
            total_ratings.setText("(" + totalRate + "ratings)");
            product_price.setText(price);
            price_before_discount.setText(priceBeforeDiscountValue);

            if (paymentMethod){
                payment_method.setVisibility(View.VISIBLE);
            }
            else{
                payment_method.setVisibility(View.INVISIBLE);
            }

            if (wishlist){
                delete_button.setVisibility(View.VISIBLE);
            }
            else{
                delete_button.setVisibility(View.GONE);
            }
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseQueries.removeProductFromWishlist(index,itemView.getContext());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent product_details_intent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    product_details_intent.putExtra("PRODUCT_ID",product_id);
                    itemView.getContext().startActivity(product_details_intent);
                }
            });

        }
    }
}
