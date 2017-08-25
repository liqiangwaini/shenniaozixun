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
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class BankPopup extends PopupWindow implements AdapterView.OnItemClickListener{
    private static final String TAG="BankPopup";
    public BankPopup(Context context){
        View rootView = View.inflate(context, R.layout.bank_popup, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);

        ListView listview= (ListView) rootView.findViewById(R.id.listview_bank);
        listview.setOnItemClickListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.item_bank,getListData());
        listview.setAdapter(adapter);
    }

    public List<String> getListData(){
        List<String> list=new ArrayList<>();
        list.add("中国银行");
        list.add("中国农业银行");
        list.add("中国工商银行");
        list.add("中国建设银行");
        list.add("招商银行");
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        callback.getBankname(getListData().get(position));
    }


    private OnBankNameCallback callback;
    public void setBankNameCallback(OnBankNameCallback callback){
        this.callback=callback;
    }
    public interface OnBankNameCallback{
        public void getBankname(String bankname);
    }

}
