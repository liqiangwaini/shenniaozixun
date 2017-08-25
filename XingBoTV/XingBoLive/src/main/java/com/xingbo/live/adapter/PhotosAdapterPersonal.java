package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.http.HttpConfig;
import java.util.Arrays;
import java.util.List;
import com.xingbo.live.util.FrescoUtils;
import com.xingbo.live.util.TimeUtils;
import com.xingbobase.extra.sticky.StickyGridHeadersSimpleAdapter;
import com.xingbobase.view.FrescoImageView;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/1
 */
public class PhotosAdapterPersonal extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {
    protected static final String TAG = PhotosAdapter.class.getSimpleName();

    private int mHeaderResId;

    private LayoutInflater mInflater;

    private int mItemResId;

    private List<UserPhotos> mItems;
    private Context context;

    public PhotosAdapterPersonal(Context context, List<UserPhotos> items, int headerResId,
                         int itemResId) {
        init(context, items, headerResId, itemResId);
    }

    public void upDataAdapter(List<UserPhotos> items) {
        this.mItems = items;
    }

    public PhotosAdapterPersonal(Context context, UserPhotos[] items, int headerResId,
                         int itemResId) {
        init(context, Arrays.asList(items), headerResId, itemResId);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public long getHeaderId(int position) {
        UserPhotos item = getItem(position);
        String value = item.getUptime();
        // SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
        String date = value;
        return TimeUtils.convertTime2Int(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.header2, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        UserPhotos item = getItem(position);
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String date = item.getUptime();
        holder.textView.setText(date);
        return convertView;
    }

    @Override
    public UserPhotos getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item2, parent, false);
            holder = new ViewHolder();
            holder.imageView = (FrescoImageView) convertView.findViewById(R.id.image_item1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserPhotos item = getItem(position);
        String url = item.getUrl();

        if (!TextUtils.isEmpty(url) && !"".equals(url)) {
            FrescoUtils.showThumb(Uri.parse(HttpConfig.FILE_SERVER + url), holder.imageView, 120,context);
//            holder.imageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + url));
        }
//        else{
//        holder.imageView.setImageResource(R.mipmap.ic_launcher);
//            //holder.imageView.setVisibility(View.GONE);
//    }
        return convertView;
    }

    private void init(Context context, List<UserPhotos> items, int headerResId, int itemResId) {
        this.context=context;
        this.mItems = items;
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public FrescoImageView imageView;
    }
}
