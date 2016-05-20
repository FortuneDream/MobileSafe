package com.example.dell.mobilesafe.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;

public class CommonNumberQuery extends AppCompatActivity {
    private ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number_query);
        initView();
        CommonNumberQueryAdapter adapter=new CommonNumberQueryAdapter();
        expandableListView.setAdapter(adapter);
    }

    private void initView() {
        expandableListView= (ExpandableListView) findViewById(R.id.elv_list);
    }

    private class CommonNumberQueryAdapter extends BaseExpandableListAdapter{

        //n个组
        @Override
        public int getGroupCount() {
            return 5;
        }

        //某个分组有多少个孩子
        @Override
        public int getChildrenCount(int groupPosition) {
            return groupPosition+1;
        }

        //分组的对象
        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        //孩子的对象
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        //分组的id
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //孩子的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        //是否允许id相同
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //每个组的view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView=new TextView(CommonNumberQuery.this);
            textView.setTextColor(Color.RED);
            textView.setTextSize(20);
            textView.setText("       "+"第"+groupPosition+"组");
            return  textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView=new TextView(CommonNumberQuery.this);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18);
            textView.setText("       "+"第"+groupPosition+"组第"+childPosition+"个孩子");
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
