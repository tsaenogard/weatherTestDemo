package com.masb1ue.app.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List mList;
    private final LayoutInflater mLayoutInflater;

    public static enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT
    }

    MyRecycleViewAdapter(List list, Context context) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.rv_image, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.rv_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder)._textView.setText(mList.get(position / 2).toString());
        } else if (holder instanceof ImageViewHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? ITEM_TYPE.ITEM_TYPE_TEXT.ordinal() : ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal();
    }

    @Override
    public int getItemCount() {
        return mList.size() + mList.size() - 1 ;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView _textView;

        TextViewHolder(View itemView) {
            super(itemView);
            _textView = itemView.findViewById(R.id.contentTV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TextViewHolder", "text" + getAdapterPosition());
                    Intent intent = new Intent (v.getContext(), Main2Activity.class);
                    intent.putExtra("data", _textView.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView _imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            _imageView = itemView.findViewById(R.id.contentIV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ImageViewHolder", "image" + getAdapterPosition());
                }
            });
        }
    }

}
