package com.leeeyf.yiyipsdmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.leeeyf.yiyipsdmanager.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

//适配器类
public class MyAdapter extends ArrayAdapter<AppInfo> implements Filterable {

    private Context mContext;
    private int mresource;
    private MyFilter mFilter;
    private List<AppInfo> list;
    private List<AppInfo> backlist;
    private Activity selectmain;

    public MyAdapter(Context context,int resource,List<AppInfo> data,Activity selectmain){
        super(context,resource,data);
        this.mContext=context;
        this.mresource=resource;
        this.list=data;
        this.selectmain=selectmain;
        backlist=list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return list.get(position);
    }//这个必须有，否则notifyDataSetChanged();不更新

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(mresource,null);
            holder.image=(ImageView) convertView.findViewById(R.id.app_image);
            holder.title=(TextView) convertView.findViewById(R.id.app_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.image.setImageDrawable(getItem(position).mappimage);
        ((ViewGroup.MarginLayoutParams)holder.image.getLayoutParams()).setMargins(40,40,40,40);
        final Drawable mapp_image = holder.image.getDrawable();
        holder.title.setText(getItem(position).mappname);
        holder.title.setTextSize(20);
        ((ViewGroup.MarginLayoutParams)holder.title.getLayoutParams()).setMargins(40,40,40,40);
        final String m_appname = getItem(position).mappname;
//        holder.text.setText(getItem(position).mpackagename);
//        final String m_packagename = getItem(position).mpackagename;
//        final int appcpu = getAppCpuUsedPercent(getItem(position).mpackagename);
//        holder.button.setOnClickListener(new View.OnClickListener() {
//                                             @Override
//                                             public void onClick(View v) {
//                                                 Toast.makeText(mContext,"已选择: "+m_appname,Toast.LENGTH_SHORT).show();
//                                                 //int nowpid = android.os.Process.myPid();//得到正在运行的应用的pid
//                                                 //Log.e("TagButton","当前应用消耗率:"+appcpu);
//                                                 launch(m_appname,mapp_image);
//                                             }
//                                         }
//
//        );

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<AppInfo> applist;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                applist = backlist;
            } else {//否则把符合条件的数据对象添加到集合中
                applist = new ArrayList<>();
                for (AppInfo appinfo : backlist) {
                    if (appinfo.getMappname().contains(charSequence)) {
                        applist.add(appinfo);
                        Log.d("list", "performFiltering: "+appinfo.getMappname());
                    }

                }
            }
            result.values = applist; //将得到的集合保存到FilterResults的value变量中
            result.count = applist.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list = (List<AppInfo>)filterResults.values;
            if (filterResults.count>0){
                Log.d("list", "publishResults: changed");
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }



    public class ViewHolder{
        ImageView image;
        TextView title;
        TextView text;
        Button button;
    }

    //跳转进入应用
    private void launch(String app_name,Drawable app_image) {
//        Intent intent = selectmain.getPackageManager().getLaunchIntentForPackage(packagename);
//        //非空跳转
//        if (intent != null) {
//            selectmain.startActivity(intent);
//        } else {
//            // 没有安装要跳转的应用
//            Toast.makeText(selectmain.getApplicationContext(), "没有找到app", Toast.LENGTH_LONG).show();
//        }
    }

}

