package com.z.camera.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Author:
 * Time:2018/7/19 10:52
 * Description:This is MySuggestionAdapter
 */
public class ShowSelectedPicsAdapter extends RecyclerView.Adapter {

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    //定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }
    // 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ShowSelectedPicsAdapter(int layoutResId) {
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }


//    @Override
//    protected void convert(BaseViewHolder helper, String item) {
//        if ("-1".equals(item)) {
//            Glide.with(mContext).load(R.mipmap.suggestion_feedback_photo_default)
////                    .skipMemoryCache(false)
////                    .transform(new CropSquareTransformation(mContext))
////                    .bitmapTransform(new CropSquareTransformation(mContext))
//                    .into((ImageView) helper.getView(R.id.mine_sugguest_icon_iv));
//            helper.setVisible(R.id.mine_sugguest_delete_iv,false);
//
//        }else{
//            Glide.with(mContext).load(item)
//                    .skipMemoryCache(false)
////                    .transform(new CropSquareTransformation(mContext))
////                    .bitmapTransform(new CropSquareTransformation(mContext))
//                    .into((ImageView) helper.getView(R.id.mine_sugguest_icon_iv));
//            helper.setVisible(R.id.mine_sugguest_delete_iv,true);
//        }
//        helper.addOnClickListener(R.id.mine_sugguest_icon_iv);
//        helper.addOnClickListener(R.id.mine_sugguest_delete_iv);
//    }
}