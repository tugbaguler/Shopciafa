package com.example.shopciafa;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class HomePageAdapter extends RecyclerView.Adapter {

    public List<HomePageModel> homePageModelList;

    //RecycledViewPool allows Views to be shared among multiple RecyclerViews
    private RecyclerView.RecycledViewPool recycledViewPool;

    public Boolean setOkay = false;

    //constructor method
    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return HomePageModel.BANNER_SLIDER;

            case 1:
                return HomePageModel.STRIP_AD_BANNER;

            case 2:
                return HomePageModel.LISTING_PRODUCT_HORIZOTALLY;

            case 3:
                return HomePageModel.GRID_LISTING_PRODUCT;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case HomePageModel.BANNER_SLIDER:
                View slider_banner_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_ad_banner,parent,false);
                return new BannerSliderViewholder(slider_banner_view);

            case HomePageModel.STRIP_AD_BANNER:
                View strip_ad_banner_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_banner,parent,false);
                return new StripAdBannerViewholder(strip_ad_banner_view);

            case HomePageModel.LISTING_PRODUCT_HORIZOTALLY:
                View listing_product_horizontally = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_listing_of_products,parent,false);
                return new ListingProductHorizontallyViewholder(listing_product_horizontally);

            case HomePageModel.GRID_LISTING_PRODUCT:
                View grid_listing_product = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_listing_of_product,parent,false);
                return new GridListingProductViewHolder(grid_listing_product);

            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()){
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewholder)holder).setBannerSliderViewPaper2(sliderModelList);
                break;

            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewholder)holder).setStripAd(resource,color);
                break;

            case HomePageModel.LISTING_PRODUCT_HORIZOTALLY:
                String layout_color = homePageModelList.get(position).getBackgroundColor();
                String listing_horizontal_product_title = homePageModelList.get(position).getTitle();
                List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList = homePageModelList.get(position).getHorizontalScrollListingOfProductModelList();
                ((ListingProductHorizontallyViewholder)holder).setListingProductHorizontally(horizontalScrollListingOfProductModelList,listing_horizontal_product_title,layout_color,viewAllProductList);
                break;

            case HomePageModel.GRID_LISTING_PRODUCT:
                String grid_layout_color = homePageModelList.get(position).getBackgroundColor();
                String listing_grid_product_title = homePageModelList.get(position).getTitle();
                List<HorizontalScrollListingOfProductModel> gridListingProductModelList = homePageModelList.get(position).getHorizontalScrollListingOfProductModelList();
                ((GridListingProductViewHolder)holder).setGridListingProduct(gridListingProductModelList,listing_grid_product_title,grid_layout_color);
                break;

            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewholder extends RecyclerView.ViewHolder{

        private ViewPager bannerSliderViewPaper2;
        private List<SliderModel> sliderModelList;
        private int current_page;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME =3000;
        private List<SliderModel> organizedList;

        //create constructing matching super
        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPaper2 = itemView.findViewById(R.id.banner_slider_viewPager2);
        }

        private void setBannerSliderViewPaper2(final List<SliderModel>sliderModelList){

            // solve timer problem
            current_page = 2;
            if (timer != null){
                timer.cancel();
            }
            organizedList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++){
                organizedList.add(x,sliderModelList.get(x));
            }
            organizedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            organizedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            organizedList.add(sliderModelList.get(0));
            organizedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter = new SliderAdapter(organizedList);
            bannerSliderViewPaper2.setAdapter(sliderAdapter);
            bannerSliderViewPaper2.setClipToPadding(false);

            bannerSliderViewPaper2.setCurrentItem(current_page);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    current_page = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(organizedList);
                    }
                }
            };
            bannerSliderViewPaper2.addOnPageChangeListener(onPageChangeListener);

            StartBannerSlideShow(organizedList);

            bannerSliderViewPaper2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(organizedList);
                    StopBannerSlideShow();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        StartBannerSlideShow(organizedList);
                    }
                    return false;
                }
            });
        }
        private void pageLooper(List<SliderModel>sliderModelList){
            if(current_page == sliderModelList.size()-2){
                current_page = 2;
                bannerSliderViewPaper2.setCurrentItem(current_page,false);
            }
            if(current_page == 1){
                current_page = sliderModelList.size()-3;
                bannerSliderViewPaper2.setCurrentItem(current_page,false);
            }

        }
        private void StartBannerSlideShow(final List<SliderModel>sliderModelList){
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (current_page >= sliderModelList.size()){
                        current_page = 1;
                    }
                    bannerSliderViewPaper2.setCurrentItem(current_page++,true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);
        }
        private void StopBannerSlideShow(){
            timer.cancel();
        }
    }

    public class StripAdBannerViewholder extends RecyclerView.ViewHolder{

        private ImageView strip_ad_image;
        private ConstraintLayout strip_add_container;

        public StripAdBannerViewholder(@NonNull View itemView) {
            super(itemView);
            strip_ad_image = itemView.findViewById(R.id.strip_ad_banner_image);
            strip_add_container = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resource, String color){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(strip_ad_image);
            strip_add_container.setBackgroundColor(Color.parseColor(color));
        }

    }

    public class ListingProductHorizontallyViewholder extends RecyclerView.ViewHolder{

        private ConstraintLayout container;
        private TextView horizontal_title;
        private Button btn_horizontal_view_all;
        private RecyclerView horizontal_recyclerView;

        public ListingProductHorizontallyViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_horizontal_scroll_listing_of_products);
            horizontal_title = itemView.findViewById(R.id.horizontal_scroll_title);
            btn_horizontal_view_all = itemView.findViewById(R.id.btn_horizontal_scroll);
            horizontal_recyclerView = itemView.findViewById(R.id.horizontal_scroll_recyclerView);
            horizontal_recyclerView.setRecycledViewPool(recycledViewPool);
        }

        private void setListingProductHorizontally(List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList, final String title, String color, final List<WishlistModel> viewAllProductList){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontal_title.setText(title);
            if (horizontalScrollListingOfProductModelList.size() > 10){
                btn_horizontal_view_all.setVisibility(View.VISIBLE);
                btn_horizontal_view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent view_all_intent = new Intent(itemView.getContext(),ViewAllActivity.class);
                        view_all_intent.putExtra("layout_code",0);
                        view_all_intent.putExtra("title",title);
                        itemView.getContext().startActivity(view_all_intent);
                    }
                });
            }else{
                btn_horizontal_view_all.setVisibility(View.INVISIBLE);
            }

            HorizontalScrollListingOfProductAdapter horizontalScrollListingOfProductAdapter = new HorizontalScrollListingOfProductAdapter(horizontalScrollListingOfProductModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontal_recyclerView.setLayoutManager(linearLayoutManager);
            horizontal_recyclerView.setAdapter(horizontalScrollListingOfProductAdapter);
            horizontalScrollListingOfProductAdapter.notifyDataSetChanged();
        }

    }

    public class GridListingProductViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout container;
        private TextView grid_listing_product_title;
        private Button btn_grid_listing_view_all;
        private GridLayout product_grid_layout;

        public GridListingProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_grid_listing_of_product);
            grid_listing_product_title = itemView.findViewById(R.id.girid_listing_product_title);
            btn_grid_listing_view_all = itemView.findViewById(R.id.btn_grid_listing_view_all);
            product_grid_layout = itemView.findViewById(R.id.grid_layout);
        }

        private void setGridListingProduct(final List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList, final String title, String Color){
            //container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            grid_listing_product_title.setText(title);

            for (int x = 0; x < 4; x++){
                ImageView product_image = product_grid_layout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_image);
                TextView product_title = product_grid_layout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_brand);
                TextView product_description = product_grid_layout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_short_definition);
                TextView product_price = product_grid_layout.getChildAt(x).findViewById(R.id.horizontal_scroll_product_price);

                Glide.with(itemView.getContext()).load(horizontalScrollListingOfProductModelList.get(x).getProduct_image()).apply(new RequestOptions().placeholder(R.mipmap.picture)).into(product_image);
                product_title.setText(horizontalScrollListingOfProductModelList.get(x).getProduct_brand());
                product_description.setText(horizontalScrollListingOfProductModelList.get(x).getProduct_short_description());
                product_price.setText(horizontalScrollListingOfProductModelList.get(x).getProduct_price());

                final int finalX = x;
                product_grid_layout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent product_details_intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        product_details_intent.putExtra("PRODUCT_ID",horizontalScrollListingOfProductModelList.get(finalX).getProduct_ID());
                        itemView.getContext().startActivity(product_details_intent);
                    }
                });

            }

            btn_grid_listing_view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewAllActivity.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
                    Intent grid_listing_view_all_intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    grid_listing_view_all_intent.putExtra("layout_code", 1);
                    grid_listing_view_all_intent.putExtra("title", title);
                    itemView.getContext().startActivity(grid_listing_view_all_intent);
                }
            });

        }
    }

}
