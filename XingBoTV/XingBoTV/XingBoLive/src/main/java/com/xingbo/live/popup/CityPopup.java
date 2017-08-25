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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class CityPopup extends PopupWindow implements AdapterView.OnItemClickListener{
    private static final String TAG="BankPopup";
    private Context context;

    public CityPopup(Context context){
        this.context=context;
        View rootView = View.inflate(context, R.layout.city_popup, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);

        ListView listview= (ListView) rootView.findViewById(R.id.listview_city);
        listview.setOnItemClickListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.item_area,Arrays.asList(context.getResources().getStringArray(R.array.province_item)));
        listview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        callback.getCityName(position,Arrays.asList(context.getResources().getStringArray(R.array.province_item)).get(position));
    }

    private OnCityAreaNameCallback callback;
    public void setCityNameCallback(OnCityAreaNameCallback callback){
        this.callback=callback;
    }
    public interface OnCityAreaNameCallback{
        public void getCityName(int position,String cityname);
    }

}
