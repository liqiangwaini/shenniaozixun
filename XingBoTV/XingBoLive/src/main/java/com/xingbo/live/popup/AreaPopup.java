package com.xingbo.live.popup;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class AreaPopup extends PopupWindow implements AdapterView.OnItemClickListener{
    private static final String TAG="BankPopup";
    private Context context;
    private int cityId;

    public AreaPopup(Context context,int cityId){
        this.context=context;
        this.cityId=cityId;
        View rootView = View.inflate(context, R.layout.area_popup, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);

        ListView listview= (ListView) rootView.findViewById(R.id.listview_area);
        listview.setOnItemClickListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.item_area,getListData());
        listview.setAdapter(adapter);
    }

    public List<String> getListData(){
        List<String> list=Arrays.asList(context.getResources().getStringArray(XingBoConfig.ARRAY_CITY[cityId]));
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        callback.getAreaName(getListData().get(position));
    }


    private OnAreaNameCallback callback;
    public void setAreaNameCallback(OnAreaNameCallback callback){
        this.callback=callback;
    }
    public interface OnAreaNameCallback{
        public void getAreaName(String areaname);
    }

}
