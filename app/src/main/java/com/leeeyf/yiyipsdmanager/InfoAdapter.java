package com.leeeyf.yiyipsdmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leeeyf.yiyipsdmanager.entity.Account;

import java.util.ArrayList;
import java.util.List;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder>  implements Filterable{

    private LayoutInflater mInflater;
    private Context mContext;
    private MyFilter mFilter;

    private List<Account> Accounts = new ArrayList<>();
    private List<Account> backAccounts = new ArrayList<>();
    public InfoAdapter(Context context,List<Account> Accounts){
        this.Accounts=Accounts;
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        backAccounts=Accounts;
    }

    public void setAccounts(List<Account> accounts) {
        Accounts = accounts;
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
            List<Account> accountlist;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                accountlist = backAccounts;
            } else {//否则把符合条件的数据对象添加到集合中
                accountlist = new ArrayList<>();
                for (Account accountinfo : backAccounts) {
                    if (accountinfo.getAccountName().contains(charSequence)) {
                        accountlist.add(accountinfo);
                        Log.d("list", "performFiltering: "+accountinfo.getAccountName());
                    }

                }
            }
            result.values = accountlist; //将得到的集合保存到FilterResults的value变量中
            result.count = accountlist.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Accounts = (List<Account>)filterResults.values;
            for(int i=0;i<Accounts.size();i++)
                Log.d("wacher", "publishResults: "+Accounts.get(i).getAccountName());
            if (filterResults.count>0){
                Log.d("list", "publishResults: changed");
                notifyDataSetChanged();//通知数据发生了改变
            }else {
               // notifyDataSetInvalidated();//通知数据失效
                notifyDataSetChanged();
            }
        }
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CardView item_cardview;
        private TextView item_tv;
        private TextView time;
        private ImageView item_icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            item_cardview=(CardView)itemView.findViewById(R.id.item_cardview);
            item_tv=(TextView)itemView.findViewById(R.id.item_tv);
            item_icon=(ImageView)itemView.findViewById(R.id.item_icon);
            time=itemView.findViewById(R.id.item_time);

        }
    }


    @NonNull
    @Override
    public InfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.cardviewlayout,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {//recyclerview点击事件

       holder.item_tv.setText(Accounts.get(position).getAccountName());
       // Log.d("tag", "onBindViewHolder: "+position+Accounts.get(position).getAccountName());

       Bitmap icon = BitmapFactory.decodeByteArray(Accounts.get(position).getIcon(),0,Accounts.get(position).getIcon().length);
//       if(icon!=null)
//           Log.d("datawacher", "onBindViewHolder: "+icon);
//       else
//           Log.d("datawacher", "onBindViewHolder: "+Accounts.get(position).getIcon()+Accounts.get(position).getIcon().length);
       Drawable drawable =new BitmapDrawable(icon);
       holder.item_icon.setImageDrawable(drawable);
       holder.time.setText(Accounts.get(position).getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(Accounts,position);
                    notifyDataSetChanged();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return Accounts.size();
    }



    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(List<Account> Accounts,int position);
        //！！！！！！！
        // ！！！！！！
        // 重要突破，加入第一个Accounts参数，才可以将搜索后新的Accounts列表传过去，点击的才是新的Accounts列表，否则是主界面的Accounts列表。
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private  int mPosition;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

}
