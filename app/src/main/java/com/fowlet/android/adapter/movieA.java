package com.fowlet.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.fowlet.android.R;
import com.fowlet.android.model.movie;

import java.util.ArrayList;

public class movieA extends BaseAdapter implements ListAdapter {
    
    private ArrayList<movie> movies;
    private int id;
    private Context context;
    public movieA(int item, Context context, ArrayList<movie> movies) {
        this.movies = movies;

        if (context == null) {
            System.out.println("movies：：：" + "取为null");
        } else {
            this.context = context;
        }
        this.id = item;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    
    @SuppressLint("WrongConstant")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        TextView tv_last= null ;
        TextView tv_id= null;
        TextView tv_tid= null;
        TextView tv_name= null;
        TextView tv_type= null;
        TextView tv_dt= null;
        TextView tv_note= null;

        if (context != null) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_search, null);

                tv_last = (TextView) view.findViewById(R.id.tv_last);
                tv_id = (TextView) view.findViewById(R.id.tv_id);
                tv_tid = (TextView) view.findViewById(R.id.tv_tid);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_type = (TextView) view.findViewById(R.id.tv_type);
                tv_dt = (TextView) view.findViewById(R.id.tv_dt);
                tv_note = (TextView) view.findViewById(R.id.tv_note);

                view.setTag(new ViewHolder(tv_last,tv_id,tv_tid,tv_name,tv_type,tv_dt,tv_note));
            } else {
                ViewHolder viewHolder1 = (ViewHolder) view.getTag(); // 重新获取ViewHolder
                tv_last = viewHolder1.tv_last;
                tv_id = viewHolder1.tv_id;
                tv_name = viewHolder1.tv_name;
                tv_type = viewHolder1.tv_type;
                tv_note = viewHolder1.tv_note;
            }
            movie movie_c = (movie) movies.get(i); // 获取当前项的实例

            tv_last.setText("最后更新:"+movie_c.getLast());
            tv_id.setText(movie_c.getId());
            //tv_name.setText(movie_c.getName());

            TextToHtml(tv_name,movie_c.getName().split("#")[0],movie_c.getName().split("#")[1]);

            tv_type.setText(movie_c.getType());
            tv_note.setText(movie_c.getNote());
        }
            return view;
        }
        
        private final class ViewHolder {
            TextView tv_last= null ;
            TextView tv_id= null;
            TextView tv_tid= null;
            TextView tv_name= null;
            TextView tv_type= null;
            TextView tv_dt= null;
            TextView tv_note= null;
            public ViewHolder(TextView tv_last,TextView tv_id,TextView tv_tid,TextView tv_name,TextView tv_type,TextView tv_dt,TextView tv_note) {
                this.tv_last = tv_last;
                this.tv_id = tv_id;
                this.tv_tid = tv_tid;
                this.tv_name = tv_name;
                this.tv_type = tv_type;
                this.tv_dt = tv_dt;
                this.tv_note = tv_note;
            }
         }


    /*
     * 设置部分字体红色
     * */
    private void TextToHtml(TextView tv_name,String str,String keyword){

        //这个是拼接出来的
        String middle =  str.replace(keyword,"</font><font color='#e93323'>"+keyword+"</font><font color='#2c2c2c'>");
        //String keyword="普罗米修斯";
        String ultimately = "<font color='#2c2c2c'>"+middle+"</font>";
        tv_name.setText(Html.fromHtml(ultimately));
    }
}
