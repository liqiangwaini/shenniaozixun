package com.xingbo.live.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.interfaces.DraweeController;
import com.xingbo.live.R;
import com.xingbo.live.entity.Guard;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/11.
 */
public class GuardAdapter extends RecyclerView.Adapter {
    private static final String TAG = "GuardAdapter";

    private Context context;
    private List<Guard> guardList;

    public GuardAdapter(Context context, List<Guard> guardList) {
        this.context = context;
        this.guardList = guardList;
    }

    public void setList(List<Guard> guardList) {
        this.guardList = guardList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contribution_avator, null);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VisitorViewHolder viewHolder = (VisitorViewHolder) holder;
        viewHolder.position = position;
        Guard guard = guardList.get(position);
        Log.e(TAG, HttpConfig.FILE_SERVER + guard.getAvatar());
        if (guard.getId() == null) {
            viewHolder.sub.setVisibility(View.INVISIBLE);
            viewHolder.time.setVisibility(View.INVISIBLE);
            viewHolder.isEmpty = true;
            viewHolder.imageview.setImageURI(Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.guard_empty));
            viewHolder.textview.setText("虚位以待");
        } else {
            viewHolder.isEmpty = false;
            if (guard.getAvatar() != null) {
                viewHolder.imageview.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + guard.getAvatar()));
            }
            viewHolder.textview.setText(guard.getNick());
            viewHolder.sub.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.time.setText(guard.getExpire());
            if (guard.getLvl().equals("2")) {
                viewHolder.sub.setBackgroundResource(R.mipmap.gold_guard_bg);
                viewHolder.sub.setText("金");
            } else {
                viewHolder.sub.setBackgroundResource(R.mipmap.normal_guard_bg);
                viewHolder.sub.setText("普");
            }
        }
    }

    @Override
    public int getItemCount() {
        return guardList.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textview;
        public FrescoImageView imageview;
        public boolean isEmpty;
        public int position;
        private final TextView sub;
        private final TextView time;

        public VisitorViewHolder(View itemView) {
            super(itemView);
            imageview = (FrescoImageView) itemView.findViewById(R.id.civ_item_contribution_avator);
            sub = (TextView) itemView.findViewById(R.id.civ_item_contribution_sub);
            textview = (TextView) itemView.findViewById(R.id.tv_item_contribution_nick);
            time = (TextView) itemView.findViewById(R.id.civ_item_contribution_time);
            imageview.setOnClickListener(this);
            imageview.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onItemClick(isEmpty, position);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private GuardClickCallback callback;

    public interface GuardClickCallback {
        public void onItemClick(boolean isEmpty, int position);
    }

    public void setGuardClickLietener(GuardClickCallback callback) {
        this.callback = callback;
    }


    /*public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }*/

}
