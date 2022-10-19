package com.xiaobo.smartcalendar.MyDialog;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaobo.smartcalendar.R;

import java.util.List;

public class MyDialogCustiomizedRuleAdapter extends RecyclerView.Adapter<MyDialogCustiomizedRuleAdapter.MyHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<String> data;
    private List<String> dataSub;

    public MyDialogCustiomizedRuleAdapter(List<String> data, List<String> dataSub) {
        this.data = data;
        this.dataSub = dataSub;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_dilog_customizedrule_item, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.fullDialogCustomizedRuleItem.setText(data.get(position));
        holder.itemDescribe.setText(dataSub.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    
    class MyHolder extends RecyclerView.ViewHolder {
        TextView fullDialogCustomizedRuleItem;
        TextView itemDescribe;
        public MyHolder(View itemView) {
            super(itemView);

            fullDialogCustomizedRuleItem = itemView.findViewById(R.id.fulldialog_customizedrule_item);
            itemDescribe = itemView.findViewById(R.id.fulldialog_customizedrule_describe);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
