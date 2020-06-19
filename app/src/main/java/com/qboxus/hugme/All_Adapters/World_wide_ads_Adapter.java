package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;
import com.qboxus.hugme.ViewHolders.UnifiedNativeAdViewHolder;

public class World_wide_ads_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Adapter_ClickListener adapter_clickListener;
    List<Object> nearby_list;
    Context context;

    public static final int MENU_ITEM_VIEW_TYPE = 0;
    StaggeredGridLayoutManager.LayoutParams layoutParams;

    public static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;


    public World_wide_ads_Adapter(Adapter_ClickListener adapter_clickListener, List<Object> nearby_list, Context context) {
        this.adapter_clickListener=adapter_clickListener;
        this.nearby_list = nearby_list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_unified_ad,
                        parent, false);

                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            default:
                View menuItemLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_world_wide, parent, false);
                return new MyViewHolder(menuItemLayoutView);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {


        int viewType = getItemViewType(i);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) nearby_list.get(i);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) viewHolder).getAdView());
                layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                break;
            default:
                Get_Set_Nearby get_nearby = (Get_Set_Nearby) nearby_list.get(i);
                MyViewHolder holder = (MyViewHolder) viewHolder;
                holder.TV_id.setText("" + get_nearby.getFirst_name());
                holder.IV.setClipToOutline(true);

                layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(false);

                try {
                    ImageRequest request =
                            ImageRequestBuilder.newBuilderWithSource(Uri.parse(get_nearby.getImage1()))
                                    .setProgressiveRenderingEnabled(false)
                                    .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setAutoPlayAnimations(false)
                            .build();

                    RoundingParams roundingParams = new RoundingParams();
                    roundingParams.setRoundAsCircle(true);

                    holder.IV.getHierarchy().setPlaceholderImage(R.drawable.profile_image_placeholder);
                    holder.IV.getHierarchy().setFailureImage(R.drawable.profile_image_placeholder);
                    holder.IV.getHierarchy().setRoundingParams(roundingParams);
                    holder.IV.setController(controller);
                } catch (Exception v) {

                }

                holder.setIsRecyclable(false);


                holder.onbind(i, adapter_clickListener);

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = nearby_list.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {

            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return nearby_list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView IV;
        TextView TV_id;
        RelativeLayout Ads_RL;
        public MyViewHolder(View view) {
            super(view);
            IV = itemView.findViewById(R.id.CIV_id);
            TV_id = itemView.findViewById(R.id.TV_id);
            Ads_RL = itemView.findViewById(R.id.Ads_RL);
        }

        protected void onbind(final int pos, final Adapter_ClickListener adapter_clickListener ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter_clickListener.On_Item_Click(pos,null,v);
                }
            });
        }

    }




    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());


        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
