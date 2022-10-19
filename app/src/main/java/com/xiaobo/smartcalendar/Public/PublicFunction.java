package com.xiaobo.smartcalendar.Public;

import android.content.Context;

import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.SearchLocationActivity.Constants;

import java.util.Date;
import java.util.Locale;


public class PublicFunction {
    
    public static String formatDate(long date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((int)(date / 1000 / 60 / 60) + "小时");
        stringBuilder.append((int)((date/ 1000 % 3600)) / 60 + "分钟");

        return stringBuilder.toString();
    }
    public static String formatDate(Context mContext, long date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((int)(date / 1000 / 60 / 60) + mContext.getResources().getString(R.string.HOUR) + " ");
        stringBuilder.append((int)((date/ 1000 % 3600)) / 60 + mContext.getResources().getString(R.string.MINUTE));

        return stringBuilder.toString();
    }

    public static String formatDate(Context mContext, int month, int day) {
        StringBuilder str = new StringBuilder();
        if (isZH(mContext)) {
            str.append(String.format("%d月%d日", month, day));
        }
        else {
            switch (month) {
                case 1:
                    str.append("Jan.");
                    break;
                case 2:
                    str.append("Feb.");
                    break;
                case 3:
                    str.append("Mar.");
                    break;
                case 4:
                    str.append("Apr.");
                    break;
                case 5:
                    str.append("May");
                    break;
                case 6:
                    str.append("Jun.");
                    break;
                case 7:
                    str.append("Jul.");
                    break;
                case 8:
                    str.append("Aug.");
                    break;
                case 9:
                    str.append("Sept");
                    break;
                case 10:
                    str.append("Oct.");
                    break;
                case 11:
                    str.append("Nov.");
                    break;
                case 12:
                    str.append("Dec.");
                    break;
                default:
                    str.append("");
                    break;
            }
            str.append(day);
        }

        return str.toString();
    }

    public static boolean isZH(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }


}
