package com.example.contacts_demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts_demo.R;
import com.example.contacts_demo.entity.ContactList;

import java.util.ArrayList;

public class AlphaListAdapter extends
        RecyclerView.Adapter<AlphaListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView charTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            charTextView = (TextView) itemView.findViewById(R.id.alpha_txtview);
        }
    }

    private ArrayList<String> mChars;
    private RecyclerView listRV;
    private TextView alphaTV;
    private int prevPos=0;
    public AlphaListAdapter(ArrayList<String> chars, TextView alphatv, RecyclerView listrv) {
        this.mChars=chars;
        this.alphaTV=alphatv;
        this.listRV=listrv;
    }

    public void scrollList(int pos,int prevpos ,int height)
    {
        int a=height/234;
        if(pos-prevpos<0)
            listRV.smoothScrollToPosition(ContactList.getInstance().getPositions().getValue().get(pos));
        else
            listRV.smoothScrollToPosition(ContactList.getInstance().getPositions().getValue().get(pos)+a);
    }
    @Override
    public AlphaListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alphaView = inflater.inflate(R.layout.alpha_list_adapter_item, parent, false);
        AlphaListAdapter.ViewHolder viewHolder = new AlphaListAdapter.ViewHolder(alphaView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final AlphaListAdapter.ViewHolder viewHolder, final int position) {
        final String state=mChars.get(position);

        TextView textView = viewHolder.charTextView;
        textView.setText(state);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                alphaTV.setText(state);
                scrollList(position,prevPos,v.getDisplay().getHeight());
                prevPos=position;
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChars.size();
    }
}

