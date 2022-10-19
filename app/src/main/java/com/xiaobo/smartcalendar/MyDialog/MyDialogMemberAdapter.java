package com.xiaobo.smartcalendar.MyDialog;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.R;

import java.util.List;

public class MyDialogMemberAdapter extends RecyclerView.Adapter<MyDialogMemberAdapter.MyHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<MyTemporalInconsistency> data;
    private MyEventManager myEventManager = MyEventManager.getInstance();

    public MyDialogMemberAdapter(List<MyTemporalInconsistency> data) {
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_dilog_recycler_item, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.fullDialogRecyclerItemEvent1.setText("事件1:" + myEventManager.getMyEvent(data.get(position).getUuid_event1()).getmActivityTitle().getmTitle());
        holder.fullDialogRecyclerItemEvent2.setText("事件2:" + myEventManager.getMyEvent(data.get(position).getUuid_event2()).getmActivityTitle().getmTitle());

        holder.fullDialogRecyclerInconIdentify.setText("系统建议");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });

        holder.fullDialogRecyclerIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MyDialog", "点击了忽略按钮");
                mOnItemClickListener.onIgnoreButtonClick(position);
            }
        });
        holder.fullDialogRecyclerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DialogMemberAdapter", "点击了编辑按钮");
                mOnItemClickListener.onEditButtonClick(position);
            }
        });

        int imageOfMyIdentify = R.drawable.wait;
        switch (data.get(position).getmCTC()) {
            case ITI:
                imageOfMyIdentify = R.drawable.iti_b1;
                break;
            case ITI_B1:
                imageOfMyIdentify = R.drawable.iti_b1;
                break;
            case ITI_B2:
                imageOfMyIdentify = R.drawable.iti_b2;
                break;
            case ITI_B3:
                imageOfMyIdentify = R.drawable.iti_b3;
                break;
            case ITI_M1:
                imageOfMyIdentify = R.drawable.iti_m1;
                break;
            case ITI_M2:
                imageOfMyIdentify = R.drawable.iti_m2;
                break;
            case ITI_M3:
                imageOfMyIdentify = R.drawable.iti_m3;
                break;
            case DTI_O1:
                imageOfMyIdentify = R.drawable.dti_o1;
                break;
            case DTI_O2:
                imageOfMyIdentify = R.drawable.dti_o2;
                break;
            case DTI_O3:
                imageOfMyIdentify = R.drawable.dti_o3;
                break;
            case DTI_S1:
                imageOfMyIdentify = R.drawable.dti_s1;
                break;
            case DTI_S2:
                imageOfMyIdentify = R.drawable.dti_s2;
                break;
            case DTI_S3:
                imageOfMyIdentify = R.drawable.dti_s3;
                break;
            case DTI_E1:
                imageOfMyIdentify = R.drawable.dti_e1;
                break;
            case DTI_E2:
                imageOfMyIdentify = R.drawable.dti_e2;
                break;
            case DTI_E3:
                imageOfMyIdentify = R.drawable.dti_e3;
                break;
            case DTI_C1:
                break;
            case DTI_C2:
                break;
            case DTI_C3:
                break;
            default:
                imageOfMyIdentify = R.drawable.wait;
                break;

        }
        holder.fullDialogRecyclerInconImageView.setImageResource(imageOfMyIdentify);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    
    class MyHolder extends RecyclerView.ViewHolder {

        TextView fullDialogRecyclerItemEvent1;
        TextView fullDialogRecyclerItemEvent2;
        TextView fullDialogRecyclerInconIdentify;
        Button fullDialogRecyclerIgnore;
        Button fullDialogRecyclerEdit;

        
        ImageView fullDialogRecyclerInconImageView;

        public MyHolder(View itemView) {
            super(itemView);

            fullDialogRecyclerItemEvent1 = itemView.findViewById(R.id.fulldialog_recycler_item_event1);
            fullDialogRecyclerItemEvent2 = itemView.findViewById(R.id.fulldialog_recycler_item_event2);
            fullDialogRecyclerInconIdentify = itemView.findViewById(R.id.fulldialog_recycler_incon_identify);
            fullDialogRecyclerIgnore = itemView.findViewById(R.id.fulldialog_recycler_ignore);
            fullDialogRecyclerEdit = itemView.findViewById(R.id.fulldialog_recycler_edit);
            fullDialogRecyclerInconImageView = itemView.findViewById(R.id.fulldialog_recycler_incon_imageview);
        }
    }

    
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onIgnoreButtonClick(int position);
        void onEditButtonClick(int position);
    }

}
