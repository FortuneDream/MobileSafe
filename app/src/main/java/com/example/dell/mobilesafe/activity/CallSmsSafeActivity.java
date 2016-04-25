package com.example.dell.mobilesafe.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.BlackNumberInfo;
import com.example.dell.mobilesafe.db.BlackNumberDAO;
import com.example.dell.mobilesafe.utils.LogUtil;

import java.util.List;


public class CallSmsSafeActivity extends AppCompatActivity {
    private static final String TAG = "CallSmsSafeActivity";
    private LinearLayout loadingLL;
    private ListView listView;
    private BlackNumberDAO blackNumberDAO;
    private int index=0;
    private int count=0;
    private CallSmsSafeAdapter adapter;
    private List<BlackNumberInfo> infos;
    private boolean isLoading=false;
    private AlertDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isLoading=false;
            if (adapter == null) {
                adapter = new CallSmsSafeAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            loadingLL.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        initView();
        blackNumberDAO = BlackNumberDAO.getDBInstance(this);
//        Random random=new Random();
//        for (int i=0;i<10;i++){
//            blackNumberDAO.add("00000"+i, String.valueOf(random.nextInt(3)));
//        }
        count=blackNumberDAO.queryCount();
        //加载大量数据，用子线程
        loadingPartList();
    }

    private void loadingPartList() {
        loadingLL.setVisibility(View.INVISIBLE);//表示正在加载数据
        new Thread() {
            @Override
            public void run() {
                if (infos==null){
                    infos = blackNumberDAO.queryPart(index);//部分加载，防止过得加载
                }else {
                    //addALL在原来的基础上，再添加数据
                    infos.addAll(blackNumberDAO.queryPart(index));
                }
                mHandler.sendEmptyMessage(0);//子线程加载完毕后，发消息，然后设置adapter,
                //只是利用子线程优化，仅仅只能让用户早一点进入黑名单Activity，但是此时还不能得到黑名单的列表，需要等待子线程加载完毕了才可以看到黑名单列表
                //可以使用显示一个加载样式，来提示用户正在加载，然后加载完毕后，样式消失
            }
        }.start();
    }


    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        loadingLL = (LinearLayout) findViewById(R.id.ll_loading);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当状态发生变化：静止<->滑动,手指滑动->惯性滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    //静止状态（滑到底部停止）
                    case SCROLL_STATE_IDLE:
                        if(isLoading){
                            Toast.makeText(getApplicationContext(),"正在加载",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //得到最后看到的位置
                        int lastPosition=listView.getLastVisiblePosition();//从0开始->19
                        int totalSize=infos.size();//此时数据源的大小,最初为20
                        //加载完毕
                        if (index>=count){
                            Toast.makeText(getApplicationContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (lastPosition==(totalSize-1)){
                            index+=20;//再加载20条
                            isLoading=true;//只能在这个if条语句中,如果写在外面，那么当没有滑动到最后一条时，is=true，此时，再滑动就一直在前面的if条件下return
                            loadingPartList();
                            Toast.makeText(getApplicationContext(),"加载更多数据",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //滑动状态
                    case SCROLL_STATE_FLING:
                        break;
                    //触摸滑动状态
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            //滚动
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private class CallSmsSafeAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //每显示一行就执行一次
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(CallSmsSafeActivity.this, R.layout.item_call_sms_safe, null);
                //第一次加载布局的时候，将控件的位置保存下来（findViewById会用先序遍历整个二叉树，假如缓存了节点位置，就不需要再次遍历，直接取出位置上的控件）
                viewHolder = new ViewHolder();
                viewHolder.deleteImg= (ImageView) view.findViewById(R.id.img_delete);
                viewHolder.numberTxt = (TextView) view.findViewById(R.id.txt_number);
                viewHolder.modeTxt = (TextView) view.findViewById(R.id.txt_mode);//
                //view对象和viewHolder进行关联，给view添加一个额外的数据->viewholder.viewholder中保存了2个控件的位置，。
                view.setTag(viewHolder);

                LogUtil.v(TAG,"重新创建view");
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();//直接view的tag中的viewholder，不用遍历view树
                LogUtil.v(TAG,"使用历史缓存的view");
            }

            final BlackNumberInfo info = infos.get(position);
            viewHolder.numberTxt.setText(info.getNumber());
            String mode = info.getMode();
            if ("0".equals(mode)) {
                //电话拦截
                viewHolder.modeTxt.setText("电话拦截");
            } else if ("1".equals(mode)) {
                //短信拦截
                viewHolder.modeTxt.setText("短信拦截");
            } else if ("2".equals(mode)){
                viewHolder.modeTxt.setText("电话+短信拦截");
                //全部拦截
            }
            //设置图片的点击事件，（只是img，不是整个item）
            viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.删除数据库数据
                    blackNumberDAO.delete(info.getNumber());
                    infos.remove(info);
                    adapter.notifyDataSetChanged();
                    //2.当前列表删除
                    //3.刷新
                }
            });
            LogUtil.v(TAG,"mode:"+info.getMode()+"  "+info.getNumber());
            return view;
        }
    }

    static class ViewHolder {//用静态内部类，只加载一次类
        TextView numberTxt;
        TextView modeTxt;
        ImageView deleteImg;
    }

    //点击事件，弹出对话框，添加黑名单
    public void addBlackNumber(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        dialog=builder.create();
        View contentView=View.inflate(this,R.layout.dialog_add_black_number,null);
        dialog.setView(contentView,0,0,0,0);
        dialog.show();
        final EditText numberEdt= (EditText) contentView.findViewById(R.id.edt_phone);
        final RadioGroup modeRg= (RadioGroup) contentView.findViewById(R.id.rg_mode);
        Button okBtn= (Button) contentView.findViewById(R.id.btn_ok);
        Button cancelBtn= (Button) contentView.findViewById(R.id.btn_cancel);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=numberEdt.getText().toString().trim();
                int checkId=modeRg.getCheckedRadioButtonId();//得到选中id
                String mode="0";
                switch (checkId){
                    case R.id.rd_phone:
                        mode="0";
                        break;
                    case R.id.rd_sms:
                        mode="1";
                        break;
                    case R.id.rd_all:
                        mode="2";
                        break;
                }
                LogUtil.v(TAG,"新添加的mode:"+mode);
                if (TextUtils.isEmpty(number)){
                    Toast.makeText(CallSmsSafeActivity.this,"电话号码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
                    blackNumberInfo.setNumber(number);
                    blackNumberInfo.setMode(mode);
                    infos.add(0,blackNumberInfo);
                    blackNumberDAO.add(number,mode);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
