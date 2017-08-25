package com.xingbo.live.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

//import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xingbo_szd on 2016/7/11.
 */
public class VisitorAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MsgFUser> fUserList;

    public VisitorAdapter(Context context, List<MsgFUser> fUserList) {
        this.context = context;
        this.fUserList = fUserList;
    }

    public void setData(List<MsgFUser> fUserList){
        this.fUserList=fUserList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_visitors, null);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VisitorViewHolder viewHolder = (VisitorViewHolder) holder;
        viewHolder.position = position;
        MsgFUser fUser = fUserList.get(position);
        viewHolder.imageview.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+fUser.getAvatar()));
    }

    @Override
    public int getItemCount() {
        return fUserList.size();

    }

    class VisitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public FrescoImageView imageview;
        public int position;

        public VisitorViewHolder(View itemView) {
            super(itemView);
            imageview = (FrescoImageView) itemView.findViewById(R.id.civ_item_visitor);
            rootView = itemView.findViewById(R.id.civ_item_visitor);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onItemClick(position,fUserList.get(position).getId());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private VisitorClickCallback callback;
    public interface VisitorClickCallback{
        public void onItemClick(int position,String uid);
    }

    public void setVisitorsClickLietener(VisitorClickCallback callback){
        this.callback=callback;
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

}
