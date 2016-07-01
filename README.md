# Android的各种坑

标签（空格分隔）： Android

---
## 易错点
1.注意Receive的intent所属

2.View在XML要大写

3.创建数据库getAppcationConext()；大多数用Activity.this。且对话框不能用getAppliactionContext()；因为父类的Context没有token.子类Activity才有

4.
```
<activity 
	android:name=".MainActivity"android:theme="@android:style/Theme.Translucent.NoTitleBar">
	JAVA MainActivity继承自Acitivity 设置为透明背景，可以用来演示动画
```
5.animation-list 设置在drawable中

6.Receiver无法直接打开Activity和Service，抛出以下异常：
```
	Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
```
activity继承了context重载了startActivity方法,如果使用acitvity中的startActivity，不会有任何限制。
而如果直接使用context的startActivity则会报上面的错误，根据错误提示信息,可以得知,如果要使用这种方式需要打开新的TASK。
	解决方法:
```
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
  	context.startActivity(intent);
```

7.使用widget的应用必须是安装在内存中而不是sd卡

8.listview中有checkbox，需要用设置item的onclick中setcheck，而是ViewHolder的onClick

9.使用AIDL文件 binder返回this而不是null

10.progressdialog要弹出对话框，progressbar不会

11.锁屏广播在代码中注册 

12.
```
protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);..}
```
只有一个参数，新加的PersistableBundle参数，令这些方法有了系统关机重启后数据恢复的能力

13.删除或者添加ListView数据：1.删除/添加数据库+删除/添加List+notifyDataSetChanged;
  如果是倒叙添加，且要添加的数据最近显示，info.add(0,javaBean);

14.建议在服务的内部类中声明广播接受者，且在onCreate中注册+onDestroy中取消注册

15.69.获取系统图片时<!--image设置固定值，解决大小不一的问题-->
	
## 知识点
1.注册表保存的是进程号和文件位置

2.TextView,ImageView设置drawable(text)Top/Right...可实现文字+图片

3.同等优先级，java注册拦截比Manifest文件拦截快，因为Manifest要额外打开一次广播

4.左移一位相当于该数乘以2，左移2位相当于该数乘以2^2=4。上面举的例子15<< 2=60，即乘了4

5.LinearLayout中使用
```
    gravity="center"可以使得子View在中间
```


6.TextView
```
    android:ellipsize="marquee"跑马灯
    android:singleLine="true"单行
```	
7.GridView 
```
	android:verticalSpacing="15dp"竖直方向的间隔
	android:horizontalSpacing="15dp"水平方向的间隔
	android:numColumns="3" 三列
```
8.加载界面
	用真布局包裹一个listView和一个LinearLayout(progressBar+Text)，让LinearLayout在上面先显示，
	然后ListView加载完毕后，LinearLayout设置为invisible

9.抽屉
	DrawerLayout（第一个子View是主页，第二子View是抽屉）
	

10.
```
    contentLl.addView(view);
```
contentLL为竖直方向的LinearLayout，view为子View，
这样可以动态加载view（ListView的刷新不能实现一个一个的添加），还可以在加载过程中给每一个view设置点击事件
  
11.设置style（parent与appTheme相同），dimon，color，重用布局，大小，颜色

12.制作动画多用真布局包裹

13.if （String1.contain（String2）），String1是否包含String2

14.屏幕适配：View：小屏幕+ScrollView

15.Logcat切换到NoFilters，可以查看Intent的隐式调用

## API
1.TrafficStats流量统计类
	
2.MD控件TextInputLyaout，用来做登录框
	
3.DrawerLayoutdrawer
```
	Layout.addDrawerListener(new DrawerLayout.DrawerListener(){..}
```
XML：DrawerLayout（Layout1内容+Layout2抽屉->必须设置layout_gravity）
	
4.ExpanDableListView扩展的ListView  
	Adapter：BaseExpandableListAdapter








