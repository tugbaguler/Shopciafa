package com.example.shopciafa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.shopciafa.DeliveryActivity.SELECT_DELIVERY_ADDRESS;
import static com.example.shopciafa.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.shopciafa.MyAddressesActivity.refreshingTheItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int previous_selected_position;

    //constructor method
    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        previous_selected_position = DatabaseQueries.selected_address;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_items,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String fullname = addressesModelList.get(position).getFullname();
        String address = addressesModelList.get(position).getAddress();
        String pincode = addressesModelList.get(position).getPincode();
        Boolean selected = addressesModelList.get(position).getSelected();
        holder.setData(fullname,address,pincode,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView checked_icon;
        private LinearLayout option_container;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.user_fullname_address_items);
            address = itemView.findViewById(R.id.user_address_address_items);
            pincode = itemView.findViewById(R.id.user_pincode_address_items);
            checked_icon = itemView.findViewById(R.id.icon_checked_address_items);
            option_container = itemView.findViewById(R.id.option_container_addresses_items);
        }
        private void setData(String user_fullname, String user_address, String user_pincode, Boolean selected, final int position){
            fullname.setText(user_fullname);
            address.setText(user_address);
            pincode.setText(user_pincode);

            //Whether the icon in the my addresses page is visible or not
            if (MODE == SELECT_DELIVERY_ADDRESS){
                checked_icon.setImageResource(R.mipmap.checked);
                if (selected){
                    checked_icon.setVisibility(View.VISIBLE);
                    previous_selected_position = position;
                }
                else{
                    checked_icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(previous_selected_position != position){
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(previous_selected_position).setSelected(false);
                            refreshingTheItem(previous_selected_position,position);
                            previous_selected_position = position;
                            DatabaseQueries.selected_address = position;
                        }
                    }
                });

            }else if (MODE == MANAGE_ADDRESS){
                //Now, instead of the check icon image, the icon with 3 dots downwards will appear
                option_container.setVisibility(View.GONE);
                checked_icon.setImageResource(R.mipmap.more);
                checked_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Clicking on 3 points will open the container with edit and delete inside
                        option_container.setVisibility(View.VISIBLE);
                        refreshingTheItem(previous_selected_position,previous_selected_position);
                        previous_selected_position = position;

                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshingTheItem(previous_selected_position,previous_selected_position);
                        previous_selected_position = -1;
                    }
                });
            }
        }
    }
}
