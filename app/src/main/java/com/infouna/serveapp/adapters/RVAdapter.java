package com.infouna.serveapp.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.infouna.serveapp.datamodel.HomeCardData;
import com.infouna.serveapp.datamodel.OrderListCardSP;
import com.infouna.serveapp.datamodel.OrderListCardUser;

import java.util.Collections;
import java.util.List;

/**
 * Created by Darshan on 14-06-2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    List<HomeCardData> list = Collections.emptyList();
    List<OrderListCardSP> listODSP = Collections.emptyList();
    List<OrderListCardUser> listODSU = Collections.emptyList();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private int mDataSetTypes;

    public static final int HOMECARD = 0, ORDERSP = 1, ORDERU = 2;

    public RVAdapter(List data, int mDatasetType) {
        if (mDatasetType == 0) {
            this.list = (List<HomeCardData>) data;
        } else if (mDatasetType == 1) {
            this.listODSP = (List<OrderListCardSP>) data;
        } else if (mDatasetType == 2) {
            this.listODSU = (List<OrderListCardUser>) data;
        }
        this.mDataSetTypes = mDatasetType;
    }

/*
    public RVAdapter(List<OrderListCardSP> data, int mDatasetType) {
        this.listODSP = data;
        this.mDataSetTypes = mDatasetType;
    }
*/

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }

    public class CardHomeCategory extends ViewHolder {
        CardView cv;
        TextView servicename;
        LinearLayout parentlayout;

        public CardHomeCategory(View v) {
            super(v);
            this.cv = (CardView) itemView.findViewById(R.id.cardViewHomeCategory);
            this.servicename = (TextView) itemView.findViewById(R.id.sname);
            this.parentlayout = (LinearLayout) itemView.findViewById(R.id.llimage);
        }
    }

    public class CardOrderListSP extends ViewHolder {
        CardView cv;
        TextView username, date, time, status;
        ImageButton status_icon;

        public CardOrderListSP(View v) {
            super(v);
            this.cv = (CardView) itemView.findViewById(R.id.cardorderlistingsp);
            this.username = (TextView) itemView.findViewById(R.id.orderlistingSP_uname);
            this.date = (TextView) itemView.findViewById(R.id.orderlistingSP_date);
            this.time = (TextView) itemView.findViewById(R.id.orderlistingSP_time);
            this.status = (TextView) itemView.findViewById(R.id.orderlistingSP_status);
            this.status_icon = (ImageButton) itemView.findViewById(R.id.odsp_status_image);
        }

    }

    public class CardOrderListUser extends ViewHolder {
        CardView cv;
        TextView servicename, date, time, status;
        ImageButton status_icon;

        public CardOrderListUser(View v) {
            super(v);
            this.cv = (CardView) itemView.findViewById(R.id.cardorderlistinguser);
            this.servicename = (TextView) itemView.findViewById(R.id.orderlistinguser_servicename);
            this.date = (TextView) itemView.findViewById(R.id.orderlistingUser_date);
            this.time = (TextView) itemView.findViewById(R.id.orderlistingUser_date);
            this.status = (TextView) itemView.findViewById(R.id.orderlistingUser_status);
            this.status_icon = (ImageButton) itemView.findViewById(R.id.oduser_status_image);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == HOMECARD) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_category_card, viewGroup, false);

            return new CardHomeCategory(v);
        } else if (viewType == ORDERSP) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_listing_sp_card, viewGroup, false);

            return new CardOrderListSP(v);
        } else if (viewType == ORDERU) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_listing_user_card, viewGroup, false);

            return new CardOrderListSP(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == HOMECARD) {
            CardHomeCategory holder = (CardHomeCategory) viewHolder;
            holder.servicename.setText(String.valueOf(list.get(position).servicename));

            String urlThumbnail = list.get(position).service_image_url;
            loadImages(urlThumbnail, holder, viewHolder.getItemViewType());
        } else if (viewHolder.getItemViewType() == ORDERSP) {
            CardOrderListSP holder = (CardOrderListSP) viewHolder;
            holder.username.setText(String.valueOf(listODSP.get(position).username));
            holder.date.setText(String.valueOf(listODSP.get(position).requested_date_time));
            holder.time.setText(String.valueOf(listODSP.get(position).requested_date_time));

            if (String.valueOf(listODSP.get(position).accepted).equals("1")) {
                holder.status.setText("Service accepted");
                holder.status_icon.setImageResource(R.mipmap.ic_check);

            } else if (String.valueOf(listODSP.get(position).accepted).equals("0")) {
                holder.status.setText("Pending approval");
                holder.status_icon.setImageResource(R.mipmap.ic_warning_notification);
            }
        } else if (viewHolder.getItemViewType() == ORDERU) {
            CardOrderListUser holder = (CardOrderListUser) viewHolder;
            holder.servicename.setText(String.valueOf(listODSP.get(position).service_name));
            holder.date.setText(String.valueOf(listODSP.get(position).requested_date_time));
            holder.time.setText(String.valueOf(listODSP.get(position).requested_date_time));

            if (String.valueOf(listODSU.get(position).accepted).equals("1")) {
                holder.status.setText("Service accepted");
                holder.status_icon.setImageResource(R.mipmap.ic_check);

            } else if (String.valueOf(listODSU.get(position).accepted).equals("0")) {
                holder.status.setText("Pending approval");
                holder.status_icon.setImageResource(R.mipmap.ic_warning_notification);
            }
        }
    }

    private void loadImages(String urlThumbnail, final ViewHolder holder, final int itemViewType) {

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (itemViewType == HOMECARD) {
                        ((CardHomeCategory) holder).parentlayout.setBackground(new BitmapDrawable(response.getBitmap()));
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

    // public void animate(RecyclerView.ViewHolder viewHolder)
//   {
//       final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.trans);
//       viewHolder.itemView.setAnimation(animAnticipateOvershoot);
//   }

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