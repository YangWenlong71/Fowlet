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
import com.fowlet.android.model.CollectBean;

import java.util.ArrayList;

public class CollectA extends BaseAdapter implements ListAdapter {

    private ArrayList<CollectBean> collectBeans;
    private int id;
    private Context mcontext;
    private LayoutInflater inflater;

    public CollectA(int sub_item, Context mcontext, ArrayList<CollectBean> collectBeans) {
        this.collectBeans = collectBeans;
        this.mcontext = mcontext;
        this.id = sub_item;
        inflater = LayoutInflater.from(mcontext);

    }

    @Override
    public int getCount() {
        return collectBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return collectBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("WrongConstant")
    @Override

    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv_movieid = null;
        TextView tv_moviename = null;
        TextView tv_movietype = null;
        TextView tv_movieurl = null;
        ViewHolder viewHolder;
        
        if (view == null) {
            view = inflater.inflate(id, null);
            tv_movieid = (TextView) view.findViewById(R.id.tv_movieid);
            tv_moviename = (TextView) view.findViewById(R.id.tv_moviename);
            tv_movietype = (TextView) view.findViewById(R.id.tv_movietype);
            tv_movieurl = (TextView) view.findViewById(R.id.tv_movieurl);

            view.setTag(new ViewHolder(tv_movieid,tv_moviename,tv_movietype,tv_movieurl));
        } else {
            ViewHolder viewHolder1 = (ViewHolder) view.getTag(); // 重新获取ViewHolder
            tv_movieid = viewHolder1.tv_movieid;
            tv_moviename = viewHolder1.tv_moviename;
            tv_movietype = viewHolder1.tv_movietype;
            tv_movieurl = viewHolder1.tv_movieurl;
        }

        CollectBean universal = (CollectBean) collectBeans.get(i); // 获取当前项的实例

        tv_movieid.setText(universal.getMovieId().toString());
        tv_moviename.setText(universal.getMovieName().toString());
        tv_movietype.setText(universal.getMovieType().toString());
        tv_movieurl.setText(universal.getMovieUrl().toString());


        return view;
    }
    private final class ViewHolder {
        TextView tv_movieid = null;
        TextView tv_moviename = null;
        TextView tv_movietype = null;
        TextView tv_movieurl = null;
        public ViewHolder(TextView tv_movieid,TextView tv_moviename,TextView tv_movietype,TextView tv_movieurl) {
            this.tv_movieid = tv_movieid;
            this.tv_moviename = tv_moviename;
            this.tv_movietype = tv_movietype;
            this.tv_movieurl = tv_movieurl;
        }
    }

}
