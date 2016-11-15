package com.infouna.serveapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.NotificationCard;
import com.infouna.serveapp.datamodel.OrderListCardSP;
import com.infouna.serveapp.datamodel.OrderListCardUser;
import com.infouna.serveapp.datamodel.ServiceListCard;

import java.util.Collections;
import java.util.List;

/**
 * Created by Darshan on 14-06-2016.
 */
public class ServiceListingAdapter extends RecyclerView.Adapter<ServiceListingAdapter.ViewHolder> {

    List<ServiceListCard> list = Collections.emptyList();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private int mDataSetTypes;

    public String type = "";

    public static final int HOMECARD = 0, ORDERSP = 1, ORDERU = 2, SERVICELIST = 3, NOTIFICATION = 4;

    public ServiceListingAdapter(List data, int mDatasetType) {
        if (mDatasetType == 0) {
            this.list = (List<ServiceListCard>) data;
        }
        this.mDataSetTypes = mDatasetType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }

    public class CardServiceList extends ViewHolder {
        CardView cv;
        TextView servicename, review;
        LinearLayout bgimage;
        public ImageView status_icon, favourite_icon, stars[] = new ImageView[5];

        public CardServiceList(View v) {
            super(v);
            this.servicename = (TextView) itemView.findViewById(R.id.SL_servicename);
            this.review = (TextView) itemView.findViewById(R.id.SL_reviews);
            this.cv = (CardView) itemView.findViewById(R.id.Service_List_cardView);
            this.bgimage = (LinearLayout) itemView.findViewById(R.id.SL_backgroundimage);
            this.favourite_icon = (ImageView) itemView.findViewById(R.id.SL_favourite);
            this.status_icon = (ImageView) itemView.findViewById(R.id.SL_check);
            this.stars[0] = (ImageView) itemView.findViewById(R.id.SL_star_1);
            this.stars[1] = (ImageView) itemView.findViewById(R.id.SL_star_2);
            this.stars[2] = (ImageView) itemView.findViewById(R.id.SL_star_3);
            this.stars[3] = (ImageView) itemView.findViewById(R.id.SL_star_4);
            this.stars[4] = (ImageView) itemView.findViewById(R.id.SL_star_5);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == HOMECARD) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_listing_card, viewGroup, false);

            return new CardServiceList(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == HOMECARD) {
            CardServiceList holder = (CardServiceList) viewHolder;
            holder.servicename.setText(String.valueOf(list.get(position).service_name));
            holder.review.setText(String.valueOf(list.get(position).total_reviews) + " Reviews");

            String urlThumbnail = list.get(position).banner_picture;
            if (!urlThumbnail.equals("")) {
                loadImages(urlThumbnail, holder, viewHolder.getItemViewType());
            }

            if (list.get(position).favourite.equals("1")) {
                holder.favourite_icon.setImageResource(R.mipmap.ic_like_selected);
            } else {
                holder.favourite_icon.setImageResource(R.mipmap.ic_like_deselected);
            }

        }
    }

    private void loadImages(String urlThumbnail, final ViewHolder holder, final int itemViewType) {

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (itemViewType == HOMECARD) {
                        ((CardServiceList) holder).bgimage.setBackground(new BitmapDrawable(response.getBitmap()));
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            //  Log.d(TAG, "constructor invoked");
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //       Log.d(TAG, "onSingleTap " + e);
                    return true;
                }

                @SuppressWarnings("deprecation")
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                    //   Log.d(TAG, "onLongPress " + e);
                }
            });
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onLongClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            //   Log.d(TAG, "constructor invoked");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}