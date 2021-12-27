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
import com.fowlet.android.model.dramaSeries_C;

import java.util.ArrayList;


public class dramaSeries_A extends BaseAdapter implements ListAdapter {


        private ArrayList<dramaSeries_C> dramaSeries_cs;
        private int id;
        private Context context;
        private LayoutInflater inflater;

        public dramaSeries_A(int item, Context context, ArrayList<dramaSeries_C> dramaSeries_cs) {
            this.dramaSeries_cs = dramaSeries_cs;
            this.context = context;
            this.id = item;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return dramaSeries_cs.size();
        }

        @Override
        public Object getItem(int i) {
            return dramaSeries_cs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @SuppressLint("WrongConstant")
        @Override

        public View getView(final int i, View view, ViewGroup viewGroup) {
            //episode,playerurl
            TextView episode = null;
            TextView playerurl = null;
            ViewHolder viewHolder;
            if (view == null) {
                view = inflater.inflate(id, null);
                episode = (TextView) view.findViewById(R.id.episode);
                playerurl = (TextView) view.findViewById(R.id.playerurl);
                view.setTag(new ViewHolder(episode,playerurl));
            } else {
                ViewHolder viewHolder1 = (ViewHolder) view.getTag(); // 重新获取ViewHolder
                episode = viewHolder1.episode;
                playerurl = viewHolder1.playerurl;
            }
            dramaSeries_C dsc = (dramaSeries_C) dramaSeries_cs.get(i); // 获取当前项的实例
            episode.setText(dsc.getEpisode().toString());//对象为空
            playerurl.setText(dsc.getPlayerurl().toString());
            return view;
        }
        private final class ViewHolder {
            TextView episode = null;
            TextView playerurl = null;
            public ViewHolder(TextView episode,TextView playerurl) {
                this.episode = episode;
                this.playerurl = playerurl;
            }
        }
    }