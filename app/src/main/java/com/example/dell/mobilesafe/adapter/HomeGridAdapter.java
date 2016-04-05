package com.example.dell.mobilesafe.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.activity.HomeActivity;

/**
 * Created by Q on 2016/4/5.
 */
public class HomeGridAdapter extends BaseAdapter {
    private final  static int ids[]={R.drawable.ico_shoujifangdao,R.drawable.ico_tongxunweishi,R.drawable.ico_yingyongguanli,
            R.drawable.ico_jinchengguanli,R.drawable.ico_liuliangtongji,R.drawable.ico_shoujishadu,
    R.drawable.ico_huancunqingli,R.drawable.ico_gaojigongju,R.drawable.ico_shezhizhongxin};
    private final static String names[]={"手机防盗","通讯卫士","应用管理","进程管理","流量管理","手机杀毒","缓存清理","高级工具","设置中心"};
    private Context context;
    public HomeGridAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view=View.inflate(context, R.layout.item_home,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.img_ico);
        TextView textView=(TextView)view.findViewById(R.id.txt_name);
        textView.setText(names[position]);
        imageView.setImageResource(ids[position]);
        return view;
    }
}
