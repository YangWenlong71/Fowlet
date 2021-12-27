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
import com.fowlet.android.model.source;

import java.util.ArrayList;

public class sourceA extends BaseAdapter implements ListAdapter {


    private ArrayList<source> sourceArrayList;
    private int id;
    private Context mcontext;
    private LayoutInflater inflater;


    public sourceA(int sub_item, Context mcontext, ArrayList<source> sourceArrayList) {
        this.sourceArrayList = sourceArrayList;
        this.mcontext = mcontext;
        this.id = sub_item;
        inflater = LayoutInflater.from(mcontext);

    }

    @Override
    public int getCount() {
        return sourceArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return sourceArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("WrongConstant")
    @Override

    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv_player_name = null;
        TextView tv_player_type = null;
        TextView tv_player_search = null;
        TextView tv_player_detail = null;

        ViewHolder viewHolder;
        
        if (view == null) {
            view = inflater.inflate(id, null);
            tv_player_name = (TextView) view.findViewById(R.id.tv_player_name);
            tv_player_type = (TextView) view.findViewById(R.id.tv_player_type);
            tv_player_search = (TextView) view.findViewById(R.id.tv_player_search);
            tv_player_detail = (TextView) view.findViewById(R.id.tv_player_detail);

            view.setTag(new ViewHolder(tv_player_name,tv_player_type,tv_player_search,tv_player_detail));
        } else {
            ViewHolder viewHolder1 = (ViewHolder) view.getTag(); // 重新获取ViewHolder
            tv_player_name = viewHolder1.tv_player_name;
            tv_player_type = viewHolder1.tv_player_type;
            tv_player_search = viewHolder1.tv_player_search;
            tv_player_detail = viewHolder1.tv_player_detail;
        }

        source universal = (source) sourceArrayList.get(i); // 获取当前项的实例

        tv_player_name.setText(universal.getPlayerName().toString());
        tv_player_type.setText(universal.getPlayerType().toString());
        tv_player_search.setText(universal.getPlayerSearch().toString());
        tv_player_detail.setText(universal.getPlayerDetail().toString());


        return view;
    }
    private final class ViewHolder {
        TextView tv_player_name = null;
        TextView tv_player_type = null;
        TextView tv_player_search = null;
        TextView tv_player_detail = null;
        public ViewHolder(TextView tv_player_name,TextView tv_player_type,TextView tv_player_search,TextView tv_player_detail) {
            this.tv_player_name = tv_player_name;
            this.tv_player_type = tv_player_type;
            this.tv_player_search = tv_player_search;
            this.tv_player_detail = tv_player_detail;
        }
    }

}
