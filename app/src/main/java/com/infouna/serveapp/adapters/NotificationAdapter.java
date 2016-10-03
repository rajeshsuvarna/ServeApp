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
import com.infouna.serveapp.datamodel.NotificationCard;
import com.infouna.serveapp.datamodel.OrderListCardSP;

import java.util.Collections;
import java.util.List;

/**
 * Created by Darshan on 14-06-2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<NotificationCard> list = Collections.emptyList();

    private int mDataSetTypes;

    public String type = "";

    public static final int HOMECARD = 0, ORDERSP = 1, ORDERU = 2, SERVICELIST = 3, NOTIFICATION = 4;

    public NotificationAdapter(List data, int mDatasetType) {
        if (mDatasetType == 0) {
            this.list = (List<NotificationCard>) data;
        }
        this.mDataSetTypes = mDatasetType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);
        }
    }


    public class CardNotification extends ViewHolder {
        CardView cv;
        TextView sname, title, notif_status;
        public ImageButton status_icon;

        public CardNotification(View v) {
            super(v);
            this.sname = (TextView) itemView.findViewById(R.id.notif_text_sname);
            this.title = (TextView) itemView.findViewById(R.id.notif_title);
            this.notif_status = (TextView) itemView.findViewById(R.id.notif_status);
            this.cv = (CardView) itemView.findViewById(R.id.cardViewNotification);
            this.status_icon = (ImageButton) itemView.findViewById(R.id.notif_status_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == HOMECARD) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_card, viewGroup, false);

            return new CardNotification(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == NOTIFICATION) {
            CardNotification holder = (CardNotification) viewHolder;
            if (type.equals("user")) {
                holder.sname.setText(String.valueOf(list.get(position).user_service_name));
                if (String.valueOf(list.get(position).user_sp_accepted).equals("1")) {
                    holder.title.setText("Service request accepted");
                    holder.notif_status.setText("was accepted");
                    holder.status_icon.setImageResource(R.mipmap.ic_check);
                } else {
                    holder.title.setText("Service request declined");
                    holder.notif_status.setText("was declined");
                    holder.status_icon.setImageResource(R.mipmap.ic_warning_notification);
                }

            } else if (type.equals("sp")) {

                holder.sname.setText(String.valueOf(list.get(position).sp_service_name));

                holder.title.setText("sp");
                holder.notif_status.setText("sp");
            }


            if (String.valueOf(list.get(position).user_sp_accepted).equals("1")) {

                holder.status_icon.setImageResource(R.mipmap.ic_check);

            } else if (String.valueOf(list.get(position).user_sp_accepted).equals("0")) {

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