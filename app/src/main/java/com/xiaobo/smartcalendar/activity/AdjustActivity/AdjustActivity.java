package com.xiaobo.smartcalendar.activity.AdjustActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.Service.BackgroundService.ReceivedBroadcastService;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AdjustActivity extends AppCompatActivity {

    private static final String TAG = "AdjustActivity";

    TextView commutingTimeTitle;
    TextView commutingTimeTextView;
    long commutingTime;

    TextView event1Title;
    TextView event1StartTextView;
    SeekBar event1StartTimeSeekBar;
    TextView event1EndTextView;
    SeekBar event1EndTimeSeekBar;
    NumberPicker event1HourPicker;
    NumberPicker event1MinutePicker;
    CheckBox event1Abandon;

    TextView event2Title;
    TextView event2StartTextView;
    SeekBar event2StartTimeSeekBar;
    TextView event2EndTextView;
    SeekBar event2EndTimeSeekBar;
    NumberPicker event2HourPicker;
    NumberPicker event2MinutePicker;
    CheckBox event2Abandon;

    String myInconID;
    UUID event1ID;
    UUID event2ID;
    MyEventManager myEventManager = MyEventManager.getInstance();
    MyTemporalInconsistencyManager myTemporalInconsistencyManager = MyTemporalInconsistencyManager.getInstance();
    long event1Duration;
    long event2Duration;
    
    boolean finishEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust);

        commutingTimeTitle = findViewById(R.id.adjust_commuting_time_title);
        commutingTimeTextView = findViewById(R.id.adjust_commuting_time);

        event1Title = findViewById(R.id.adjust_event1_title);
        event1StartTextView = findViewById(R.id.adjust_event1_starttime);
        event1StartTimeSeekBar = findViewById(R.id.adjust_event1_starttime_seekbar);
        event1EndTextView = findViewById(R.id.adjust_event1_endtime);
        event1EndTimeSeekBar = findViewById(R.id.adjust_event1_endtime_seekbar);
        event1HourPicker = findViewById(R.id.adjust_event1_hourpicker);
        event1MinutePicker = findViewById(R.id.adjust_event1_minutepicker);
        event1Abandon = findViewById(R.id.adjust_event1_abandon);

        event2Title = findViewById(R.id.adjust_event2_title);
        event2StartTextView = findViewById(R.id.adjust_event2_starttime);
        event2StartTimeSeekBar = findViewById(R.id.adjust_event2_starttime_seekbar);
        event2EndTextView = findViewById(R.id.adjust_event2_endtime);
        event2EndTimeSeekBar = findViewById(R.id.adjust_event2_endtime_seekbar);
        event2HourPicker = findViewById(R.id.adjust_event2_hourpicker);
        event2MinutePicker = findViewById(R.id.adjust_event2_minutepicker);
        event2Abandon = findViewById(R.id.adjust_event2_abandon);

        setTimeSeekBar(event1StartTimeSeekBar, event1StartTextView);
        setTimeSeekBar(event1EndTimeSeekBar, event1EndTextView);
        setTimeSeekBar(event2StartTimeSeekBar, event2StartTextView);
        setTimeSeekBar(event2EndTimeSeekBar, event2EndTextView);
        setTimePicker();

        
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        try {
            myInconID = bundle.getString("InconID");


            event1ID = myTemporalInconsistencyManager.getMyIncon(myInconID).getUuid_event1();
            event2ID = myTemporalInconsistencyManager.getMyIncon(myInconID).getUuid_event2();
            
            setData();
            Log.d("AdjustActivity_event1->", myEventManager.getMyEvent(event1ID).describeMyEvent());
            Log.d("AdjustActivity_event2->", myEventManager.getMyEvent(event2ID).describeMyEvent());
        }
        catch (Exception e) {
            Log.e("AdjustActivity", "获取上级信息错误");
        }
    }

    private void setData() {

        commutingTime = myEventManager.getMyEvent(event2ID).getCommutingTime();
        commutingTimeTextView.setText(myEventManager.getMyEvent(event2ID).getCommutingDate());

        event1Title.setText(myEventManager.getMyEvent(event1ID).getmActivityTitle().getmTitle());
        event2Title.setText(myEventManager.getMyEvent(event2ID).getmActivityTitle().getmTitle());

        event1Duration = myEventManager.getMyEvent(event1ID).getmDateOfEvent().getDuration();
        event2Duration = myEventManager.getMyEvent(event2ID).getmDateOfEvent().getDuration();

        int date1 = (int)myEventManager.getMyEvent(event1ID).getmDateOfEvent().getDuration();
        int h1 = date1 / 1000/60/60;
        int m1 = (date1 % (1000*60*60)) / (1000*60);
        event1HourPicker.setValue(h1);
        event1MinutePicker.setValue(m1);

        int date2 = (int)myEventManager.getMyEvent(event2ID).getmDateOfEvent().getDuration();
        int h2 = date2 / 1000/60/60;
        int m2 = (date2 % (1000*60*60)) / (1000*60);
        event2HourPicker.setValue(h2);
        event2MinutePicker.setValue(m2);

        setDateOfSeekBar(event1ID, event1StartTimeSeekBar, event1EndTimeSeekBar);
        setDateOfSeekBar(event2ID, event2StartTimeSeekBar, event2EndTimeSeekBar);
    }

    private void setTimeSeekBar(SeekBar seekBar, final TextView barText) {
        seekBar.setMax(288);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                barText.setText(transToTime(i));

                
                float textWidth = barText.getWidth();
                
                float left = seekBar.getLeft();
                
                float max =Math.abs(seekBar.getMax());

                
                float thumb = dip2px(AdjustActivity.this,15);

                
                float average = (((float) seekBar.getWidth())-2*thumb)/max;

                
                float currentProgress = i;

                
                float pox = left - textWidth/2 +thumb + average * currentProgress;
                barText.setX(pox);

                switch (seekBar.getId()) {
                    case R.id.adjust_event1_starttime_seekbar:
                        event1EndTimeSeekBar.setProgress((event1StartTimeSeekBar.getProgress() * 5 + (int)event1Duration/1000/60) / 5);
                        break;
                    case R.id.adjust_event2_starttime_seekbar:
                        event2EndTimeSeekBar.setProgress((event2StartTimeSeekBar.getProgress() * 5 + (int)event2Duration/1000/60) / 5);
                        break;

                    case R.id.adjust_event1_endtime_seekbar:
                        event1Duration = (event1EndTimeSeekBar.getProgress() - event1StartTimeSeekBar.getProgress())*5*1000*60;
                        int h = (int)event1Duration / 1000/60/60;
                        int m = (int)(event1Duration % (1000*60*60)) / (1000*60);
                        event1HourPicker.setValue(h);
                        event1MinutePicker.setValue(m);
                        break;
                    case R.id.adjust_event2_endtime_seekbar:
                        event2Duration = (event2EndTimeSeekBar.getProgress() - event2StartTimeSeekBar.getProgress())*5*1000*60;
                        int h2 = (int)event2Duration / 1000/60/60;
                        int m2 = (int)(event2Duration % (1000*60*60)) / (1000*60);
                        event2HourPicker.setValue(h2);
                        event2MinutePicker.setValue(m2);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setDateOfSeekBar(UUID eventID, SeekBar startSeekBar, SeekBar endSeekBar) {
        SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
        String[] strDate = HHmm.format(myEventManager.getMyEvent(eventID).getmDateOfEvent().getDate()).split(":");
        int startPoint = Integer.parseInt(strDate[0]) * 60 + Integer.parseInt(strDate[1]);
        startSeekBar.setProgress(startPoint / 5);
        endSeekBar.setProgress((startPoint + (int)myEventManager.getMyEvent(eventID).getmDateOfEvent().getDuration()/1000/60) / 5);
    }

    private void setTimePicker() {
        event1HourPicker.setMaxValue(23);
        event1HourPicker.setMinValue(0);




        event1MinutePicker.setMaxValue(59);
        event1MinutePicker.setMinValue(0);

        event1HourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                event1Duration = 0;
                event1Duration = newValue*1000*60*60 + event1MinutePicker.getValue()*1000*60;
                event1EndTimeSeekBar.setProgress((event1StartTimeSeekBar.getProgress() * 5 + (int)event1Duration/1000/60) / 5);
            }
        });
        event1MinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                event1Duration = 0;
                event1Duration = newValue*1000*60 + event1HourPicker.getValue()*1000*60*60;
                event1EndTimeSeekBar.setProgress((event1StartTimeSeekBar.getProgress() * 5 + (int)event1Duration/1000/60) / 5);
            }
        });

        event2HourPicker.setMaxValue(23);
        event2HourPicker.setMinValue(0);
        event2MinutePicker.setMaxValue(59);
        event2MinutePicker.setMinValue(0);

        event2HourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                event2Duration = 0;
                event2Duration = newValue*1000*60*60 + event2MinutePicker.getValue()*1000*60;
                event2EndTimeSeekBar.setProgress((event2StartTimeSeekBar.getProgress() * 5 + (int)event2Duration/1000/60) / 5);
            }
        });
        event2MinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                event2Duration = 0;
                event2Duration = newValue*1000*60 + event2HourPicker.getValue()*1000*60*60;
                event2EndTimeSeekBar.setProgress((event2StartTimeSeekBar.getProgress() * 5 + (int)event2Duration/1000/60) / 5);
            }
        });
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    
    private String transToTime(int i) {
        return String.format("%02d:%02d", 5*i/60, 5*i%60);
    }

    public void AdjustCheckBoxClicked(View clickbox) {
        switch (clickbox.getId()) {
            case R.id.adjust_event1_abandon:

                LinearLayout event1DateLayout = findViewById(R.id.adjust_event1_date);
                Button maskView1 = findViewById(R.id.adjust_event1_maskview);

                if (event1Abandon.isChecked()) {
                    event1DateLayout.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    event1HourPicker.setClickable(false);
                    event1MinutePicker.setClickable(false);
                    event1StartTimeSeekBar.setClickable(false);
                    event1EndTimeSeekBar.setClickable(false);

                    maskView1.setVisibility(View.VISIBLE);
                }
                else {
                    event1DateLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    maskView1.setVisibility(View.GONE);
                }

                break;
            case R.id.adjust_event2_abandon:

                LinearLayout event2DateLayout = findViewById(R.id.adjust_event2_date);
                Button maskView2 = findViewById(R.id.adjust_event2_maskview);

                if (event2Abandon.isChecked()) {
                    event2DateLayout.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    event2HourPicker.setClickable(false);
                    event2MinutePicker.setClickable(false);
                    event2StartTimeSeekBar.setClickable(false);
                    event2EndTimeSeekBar.setClickable(false);

                    maskView2.setVisibility(View.VISIBLE);
                }
                else {
                    event2DateLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    maskView2.setVisibility(View.GONE);
                }

                break;
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setFinishEditing(boolean slogan) {
        this.finishEditing = slogan;
        Log.d("AdjustActivity", "修改了上传冲突标识为:" + slogan);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("AdjustActivity", "点击了BarItem");
        switch (item.getItemId()) {
            case R.id.menu_item_new_event:

                Log.e("AdjustActivity", "点击了保存按钮");

                MyEvent event1 = myEventManager.getMyEvent(event1ID);
                MyEvent event2 = myEventManager.getMyEvent(event2ID);

                SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat y = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date calendar1 = null;
                try {
                    calendar1 = yyyyMMdd.parse(event1.getmDateOfEvent().getCalendar().toString());
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                Date event1StartDate = null;
                Date event2StartDate = null;
                try {
                    Date event1StartTime = HHmm.parse(event1StartTextView.getText().toString());
                    
                    event1StartDate = new Date(calendar1.getTime() + event1StartTime.getTime() + 8*60*60*1000);

                    Date event2StartTime = HHmm.parse(event2StartTextView.getText().toString());
                    event2StartDate = new Date(calendar1.getTime() + event2StartTime.getTime() + 8*60*60*1000);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("adjust1", "" + y.format(event1StartDate));
                Log.d("adjust2", "" + y.format(event2StartDate));

                event1.getmDateOfEvent().setDate(event1StartDate);
                event2.getmDateOfEvent().setDate(event2StartDate);
                event1.getmDateOfEvent().setDuration(event1Duration);
                event2.getmDateOfEvent().setDuration(event2Duration);

                
                if (event1.getmDateOfEvent().getEndPoint().getTime() + (commutingTime * 1000) <= event2.getmDateOfEvent().getStartPoint().getTime()) {
                    Log.d("AdjustActivity", "事件1的结束时间+通勤时间早于事件2");
                    setFinishEditing(true);
                }
                if (event2.getmDateOfEvent().getEndPoint().getTime() + (commutingTime * 1000) <= event1.getmDateOfEvent().getStartPoint().getTime()) {
                    Log.d("AdjustActivity", "事件2的结束时间+通勤时间早于事件1");
                    setFinishEditing(true);
                }

                
                String e1_sT = String.valueOf(event1.getmDateOfEvent().getStartPoint().getTime());
                String e1_eT = String.valueOf(event1.getmDateOfEvent().getEndPoint().getTime());
                String e2_sT = String.valueOf(event2.getmDateOfEvent().getStartPoint().getTime());
                String e2_eT = String.valueOf(event2.getmDateOfEvent().getEndPoint().getTime());

                if (event1Abandon.isChecked()) {
                    Log.d("AdjustAcctivity", "放弃了事件1:" + event1ID);
                    e1_sT = "0";
                    e1_eT = "0";
                    myEventManager.deleteMyEvent(event1ID);
                    finishEditing = true;
                }
                if (event2Abandon.isChecked()) {
                    Log.d("AdjustAcctivity", "放弃了事件2:" + event1ID);
                    e2_sT = "0";
                    e2_eT = "0";
                    myEventManager.deleteMyEvent(event2ID);
                    finishEditing = true;
                }

                if (finishEditing) {

                    if (event1Abandon.isChecked() && !event2Abandon.isChecked()) {

                        myEventManager.resetMyEvent(event2ID, event2);
                    }
                    if (event2Abandon.isChecked() && !event1Abandon.isChecked()) {

                        myEventManager.resetMyEvent(event1ID, event1);
                    }
                    if (!event1Abandon.isChecked() && !event2Abandon.isChecked()){
                        
                        MyTemporalInconsistencyManager.getInstance().setInconHandled(myInconID, MyTemporalInconsistency.Handled.settled);
                        
                        MyTemporalInconsistencyManager.getInstance().deleteMyInconWithInconID(myInconID);

                        myEventManager.resetMyEvent(event1ID, event1);
                        myEventManager.resetMyEvent(event2ID, event2);
                    }



                    
                    
                    Log.d(TAG, "修改后的冲突");
                    Intent i = HttpRequestIntentService.newIntent(AdjustActivity.this);
                    Bundle httpBundle = new Bundle();
                    httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.REVISE_TI));
                    httpBundle.putString("INCON_ID", myInconID);
                    httpBundle.putString("e1_sT", e1_sT);
                    httpBundle.putString("e1_eT", e1_eT);
                    httpBundle.putString("e2_sT", e2_sT);
                    httpBundle.putString("e2_eT", e2_eT);
                    httpBundle.putString("handled", "false");
                    i.putExtras(httpBundle);
                    startService(i);

                    
                    this.finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "冲突没有解决", Toast.LENGTH_SHORT).show();
                    Log.d("AdjustActivity", "不满足上传条件, 不上传这个冲突");
                }

                return true;
            case android.R.id.home:
                Log.e("temp_back", "点击了返回按钮,虚拟按键");
//                destroyEvent();
                this.finish(); // back button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}