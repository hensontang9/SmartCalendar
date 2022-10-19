package com.xiaobo.smartcalendar.Public;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

import com.xiaobo.smartcalendar.R;



public class VerifyCodeButton extends Button {
    private final Context mContext;

    private int mClickAfterBackground;
    private String mAfterCountDownText;
    private int mCountdownTime = 60;
    private TimeCount mTimeCount;
    private int mBackground;

    public VerifyCodeButton(Context context) {
        this(context, null);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.VerifyCodeButton);
        mBackground = a.getResourceId(R.styleable.VerifyCodeButton_android_background, mBackground);
        mClickAfterBackground = a.getResourceId(R.styleable.VerifyCodeButton_vcb_clickAfterBackground, mClickAfterBackground);
        mCountdownTime = a.getInteger(R.styleable.VerifyCodeButton_vcb_countdownTime, mCountdownTime);
        mAfterCountDownText = a.getString(R.styleable.VerifyCodeButton_vcb_afterCountdownText);
        a.recycle();
    }

    private void init() {
        setBackgroundResource(mBackground);
        mTimeCount = new TimeCount(mCountdownTime * 1000, 1000);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setClickable(false);
            setText(String.valueOf(millisUntilFinished / 1000 + "s"));
            setBackgroundResource(mClickAfterBackground);
        }

        @Override
        public void onFinish() {
            setClickable(true);
            setText(mAfterCountDownText != null ? mAfterCountDownText : "");
            setBackgroundResource(mBackground);
        }
    }

    
    public void start(){
        mTimeCount.start();
    }
}
