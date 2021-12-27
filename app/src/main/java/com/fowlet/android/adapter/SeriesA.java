package com.fowlet.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.fowlet.android.R;
import com.fowlet.android.model.Series;

import java.util.ArrayList;


public class SeriesA extends BaseAdapter implements ListAdapter {
    
    private ArrayList<Series> Seriesc;
    private int id;
    private Context context;
    public SeriesA(int item, Context context, ArrayList<Series> Seriesc) {
        this.Seriesc = Seriesc;

        if (context == null) {
            System.out.println("newestList_A：：：" + "取为null");
        } else {
            this.context = context;
        }
        this.id = item;
    }

    @Override
    public int getCount() {
        return Seriesc.size();
    }

    @Override
    public Object getItem(int i) {
        return Seriesc.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    
    @SuppressLint("WrongConstant")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        TextView tv_str = null;
        TextView tv_content = null;

        if (context != null) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_series, null);
                tv_str = (TextView) view.findViewById(R.id.tv_str);
                tv_content = (TextView) view.findViewById(R.id.tv_content);

                view.setTag(new ViewHolder(tv_str,tv_content));
            } else {
                ViewHolder viewHolder1 = (ViewHolder) view.getTag(); // 重新获取ViewHolder
                tv_str = viewHolder1.tv_str;
                tv_content = viewHolder1.tv_content;
            }
            Series seriesc = (Series) Seriesc.get(i); // 获取当前项的实例
            tv_str.setText(seriesc.getFlag());

            tv_content.setText(seriesc.getContent());
        }
            return view;
        }
        
        private final class ViewHolder {
            TextView tv_str = null;
            TextView tv_content = null;
            public ViewHolder(TextView tv_str,TextView tv_content) {
                this.tv_str = tv_str;
                this.tv_content = tv_content;
            }
         }
}
