package com.infouna.serveapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.OrderDetailsSPActivity;
import com.infouna.serveapp.datamodel.OrderListCardSP;
import com.infouna.serveapp.datamodel.OrderListCardUser;

import java.util.Collections;
import java.util.List;

/**
 * Created by Darshan on 14-06-2016.
 */

public class ServiceOrdersAdapter extends RecyclerView.Adapter<ServiceOrdersAdapter.ViewHolder> {

    List<OrderListCardSP> list = Collections.emptyList();

    private int mDataSetTypes;

    public String type = "";

    public static final int HOMECARD = 0, ORDERSP = 1, ORDERU = 2, SERVICELIST = 3, NOTIFICATION = 4;

    public ServiceOrdersAdapter(List data, int mDatasetType) {
        if (mDatasetType == 0) {
            this.list = (List<OrderListCardSP>) data;
        }
        this.mDataSetTypes = mDatasetType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }


    public class CardOrderListSP extends ViewHolder {
        CardView cv;
        TextView username, date, time, status;
        ImageView status_icon;

        public CardOrderListSP(View v) {
            super(v);
            this.cv = (CardView) itemView.findViewById(R.id.cardorderlistingsp);
            this.username = (TextView) itemView.findViewById(R.id.orderlistingSP_uname);
            this.date = (TextView) itemView.findViewById(R.id.orderlistingSP_date);
            this.time = (TextView) itemView.findViewById(R.id.orderlistingSP_time);
            this.status = (TextView) itemView.findViewById(R.id.orderlistingSP_status);
            this.status_icon = (ImageView) itemView.findViewById(R.id.odsp_status_image);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == HOMECARD) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_listing_sp_card, viewGroup, false);

            return new CardOrderListSP(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == HOMECARD) {
            CardOrderListSP holder = (CardOrderListSP) viewHolder;
            holder.username.setText(String.valueOf(list.get(position).username));
            holder.date.setText(String.valueOf(list.get(position).requested_date_time));
            holder.time.setText(String.valueOf(list.get(position).requested_date_time));

            if (String.valueOf(list.get(position).accepted).equals("1")) {
                holder.status.setText("Service accepted");
                holder.status_icon.setImageResource(R.mipmap.ic_check);

            } else if (String.valueOf(list.get(position).accepted).equals("0")) {
                holder.status.setText("Pending approval");
                holder.status_icon.setImageResource(R.mipmap.ic_warning_notification);
            }
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