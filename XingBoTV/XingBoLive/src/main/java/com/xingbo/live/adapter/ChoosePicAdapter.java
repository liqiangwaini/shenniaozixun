package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.xingbo.live.R;
import com.xingbo.live.entity.ChoosePic;

import java.util.List;


/**
 * Created by xingbo_szd on 2016/8/2.
 */
public class ChoosePicAdapter extends BaseAdapter {

    private Context context;
    private List<ChoosePic> list;

    public ChoosePicAdapter(Context context, List<ChoosePic> list) {
        this.context = context;
        this.list=list;
    }

    public void setList(List<ChoosePic> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view= View.inflate(context, R.layout.item_choose_pic, null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) view.findViewById(R.id.image);
            viewHolder.checkBox= (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.setOnPicClick(i);
                }
            });
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        ChoosePic choosePic= (ChoosePic) getItem(i);
        viewHolder.imageView.setImageURI(Uri.parse("file://" + choosePic.getUrl()));
        viewHolder.checkBox.setChecked(choosePic.isChecked());
        return view;
    }

    private OnPictureClickListener listener;
    public void setOnPicsClicked(OnPictureClickListener listener){
        this.listener=listener;
    }
    public interface OnPictureClickListener{
        public void setOnPicClick(int position);
    }

    class ViewHolder{
        private ImageView imageView;
        private CheckBox checkBox;
    }
}
