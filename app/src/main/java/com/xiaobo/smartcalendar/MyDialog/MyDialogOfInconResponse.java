package com.xiaobo.smartcalendar.MyDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Response.MyTemporalInconsistencyResponse;
import com.xiaobo.smartcalendar.R;

import java.util.UUID;

public class MyDialogOfInconResponse extends Dialog {

    private Context mContext;
    private String myTemporalInconsistencyID;

    public MyDialogOfInconResponse(@NonNull Context context) {
        super(context);
        this.mContext = context;

        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    attributes.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                }
                else {
                    attributes.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
            }
            window.setAttributes(attributes);
        }

        this.setTitle(mContext.getString(R.string.SYSTEM_ADVICE));
    }

    
    public MyDialogOfInconResponse(Context context, String myTemporalInconsistencyID) {
        super(context);
        this.mContext = context;
        this.myTemporalInconsistencyID = myTemporalInconsistencyID;
    }

    public static class Builder {
        View mLayout;
        TextView itemEvent1;
        TextView itemEvent2;
        TextView inconIdentify;
        Button ignore;
        Button edit;
        ImageView inconImageView;
        MyDialogOfInconResponse mDialog;
        OnIgnoreClickListener mIgnoreClickListener;
        OnEditClickListener mEditClickListener;

        public Builder(Context context) {
            this.mDialog = new MyDialogOfInconResponse(context);
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            this.mLayout = inflater.inflate(R.layout.mydialog_of_incon_response, null, false);
            
            this.mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.itemEvent1 = mLayout.findViewById(R.id.mydialog_res_item_event1);
            this.itemEvent2 = mLayout.findViewById(R.id.mydialog_res_item_event2);
            this.inconIdentify = mLayout.findViewById(R.id.mydialog_res_incon_identify);
            this.ignore = mLayout.findViewById(R.id.mydialog_res_agree);
            this.edit = mLayout.findViewById(R.id.mydialog_res_edit);
            this.inconImageView = mLayout.findViewById(R.id.mydialog_res_incon_imageview);
            
            this.inconImageView.setVisibility(View.GONE);

        }

        
        public Builder setData(@NonNull MyTemporalInconsistencyResponse myTemporalInconsistencyResponse) {
            MyTemporalInconsistency myTemporalInconsistency = MyTemporalInconsistencyManager.getInstance().getMyIncon(myTemporalInconsistencyResponse.getData().getUuid());
            itemEvent1.setText(MyEventManager.getInstance().getMyEvent(myTemporalInconsistency.getUuid_event1()).getmActivityTitle().getmTitle());
            itemEvent2.setText(MyEventManager.getInstance().getMyEvent(myTemporalInconsistency.getUuid_event2()).getmActivityTitle().getmTitle());

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysActionType1() + ":");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysTimeAction1()).append("\n");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysActionType2() + ":");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysTimeAction2()).append("\n");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysActionType3() + ":");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysTimeAction3()).append("\n");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysActionType4() + ":");
            stringBuffer.append(myTemporalInconsistencyResponse.getData().getSysTimeAction4()).append("\n");
            inconIdentify.setText(stringBuffer);

            int imageOfMyIdentify = R.drawable.wait;
            switch (myTemporalInconsistency.getmCTC()) {
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
            inconImageView.setImageResource(imageOfMyIdentify);

            return this;
        }

        
        public Builder ignore(OnIgnoreClickListener onClickListener){
            this.mIgnoreClickListener = onClickListener;
            return this;
        }

        public Builder edit(OnEditClickListener onClickListener){
            this.mEditClickListener = onClickListener;
            return this;
        }

        public MyDialogOfInconResponse create() {
            ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIgnoreClickListener != null) {
                        mIgnoreClickListener.agree();
                    }
                    mDialog.dismiss();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditClickListener != null) {
                        mEditClickListener.edit();
                    }
                    mDialog.dismiss();
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                
            mDialog.setCanceledOnTouchOutside(false);   
            return mDialog;
        }

        public MyDialogOfInconResponse show() {
            if (mDialog != null) {
                mDialog.show();
            }
            return mDialog;
        }
    }

    public interface OnIgnoreClickListener {
        void agree();
    }

    public interface OnEditClickListener {
        void edit();
    }
}
