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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    // Creates a new view holder holding the view inflated from the provided {@code layoutResourceId}.
    // This implementation inflates the view using the {@code parent}'s context and creates a holder that adds no value to the base class {@link ViewHolder}.
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        String icon = categoryModelList.get(position).getCategory_icon_link();
        String name = categoryModelList.get(position).getCategory_name();
        holder.setCategory(name,position);
        holder.setCategory_icon(icon);
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView category_icon;
        private TextView category_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_icon = itemView.findViewById(R.id.category_icon);
            category_name = itemView.findViewById(R.id.category_name);
        }

        private void setCategory_icon(String iconUrl){
            if (!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(category_icon);
            }
            else{
                category_icon.setImageResource(R.mipmap.home_grey);
            }
        }

        private void setCategory(final String name, final int position){
            category_name.setText(name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When the user is on the homepage and clicked on the categories except the homepage, user will be directed to the relevant page.
                    if (position != 0){
                        Intent category_intent = new Intent(itemView.getContext(),CategoryActivity.class);
                        category_intent.putExtra("CategoryName",name);
                        itemView.getContext().startActivity(category_intent);
                    }
                }
            });
        }
    }
}
