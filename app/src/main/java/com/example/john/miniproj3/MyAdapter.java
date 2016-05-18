package com.example.john.miniproj3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by John on 26/4/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static final String[] phrases={"see you tomorrow.", "今晚遲Ｄ返來食飯.", "have a nice day!", "today, the weather is good!", "今日天氣幾好."};
    private static final String[] descs={"Typically said to someone whose daily schedule is the same as one's own.", "今晚遲Ｄ返來食飯.", "have a nice day!", "today, the weather is good!", "今日天氣幾好."};

    public MyAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindModel(phrases[position], descs[position]);
    }

    @Override
    public int getItemCount() {
        return phrases.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView phrase;
        private TextView desc;

        public MyViewHolder(View itemView) {
            super(itemView);

            phrase = (TextView) itemView.findViewById(R.id.phrase);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }

        void bindModel(String phr, String des) {
            phrase.setText(phr);
            desc.setText(des);
        }
    }
}
