package com.example.shopciafa;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationModel> productSpecificationModelList;

    ProductSpecificationAdapter(List<ProductSpecificationModel> data) {
        this.productSpecificationModelList = data;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()){
            case 0:
                return ProductSpecificationModel.TITLE_OF_SPECIFICATION;
            case 1:
                return ProductSpecificationModel.BODY_INFO_OF_SPECIFICATION;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ProductSpecificationModel.TITLE_OF_SPECIFICATION:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#333333"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16,parent.getContext()),
                        setDp(16,parent.getContext()),
                        setDp(16,parent.getContext()),
                        setDp(8,parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);
            case ProductSpecificationModel.BODY_INFO_OF_SPECIFICATION:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_items,parent,false);
                return new ViewHolder(view);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {

        switch (productSpecificationModelList.get(position).getType()){
            case ProductSpecificationModel.TITLE_OF_SPECIFICATION:
                holder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case ProductSpecificationModel.BODY_INFO_OF_SPECIFICATION:
                String titleOfFeature = productSpecificationModelList.get(position).getFeature_name();
                String detailsOfFeature = productSpecificationModelList.get(position).getFeature_value();
                holder.setFeatures(titleOfFeature,detailsOfFeature);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView feature_name;
        private TextView feature_value;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setTitle(String titleText){
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeatures(String TitleOfFeature, String DetailsOfFeature){
            feature_name = itemView.findViewById(R.id.feature_name);
            feature_value = itemView.findViewById(R.id.feature_value);
            feature_name.setText(TitleOfFeature);
            feature_value.setText(DetailsOfFeature);
        }
    }

    private int setDp(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
