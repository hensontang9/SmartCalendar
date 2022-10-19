package com.xiaobo.smartcalendar.activity.AddRuleActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaobo.smartcalendar.Model.Events.Periodicity;
import com.xiaobo.smartcalendar.Model.Events.TypeOfEvent;
import com.xiaobo.smartcalendar.R;

import java.util.ArrayList;

public class AddRuleActivity extends AppCompatActivity {

    EditText event1Title;
    Button event1Type;
    EditText event1Location;
    EditText event1Host;
    EditText event1Participant;
    Button event1Periodicity;
    EditText event2Title;
    Button event2Type;
    EditText event2Location;
    EditText event2Host;
    EditText event2Participant;
    Button event2Periodicity;
    CheckBox orderImpart;
    Button ruleAction1;
    Button ruleAction2;
    Button ruleAction3;
    Button ruleAction4;

    CustomizedRuleModel model;

    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<String> options11Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private Button conflictType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        
        initView();
        
        getOptionData();
        
        makeButtonClicked();
    }

    private void initView() {
        event1Title = findViewById(R.id.event1title_for_rule);
        event1Type = findViewById(R.id.event1type_for_rule);
        event1Location = findViewById(R.id.event1location_for_rule);
        event1Host = findViewById(R.id.event1host_for_rule);
        event1Participant = findViewById(R.id.event1participant_for_rule);
        event1Periodicity = findViewById(R.id.event1periodicity_for_rule);
        event2Title = findViewById(R.id.event2title_for_rule);
        event2Type = findViewById(R.id.event2type_for_rule);
        event2Location = findViewById(R.id.event2location_for_rule);
        event2Host = findViewById(R.id.event2host_for_rule);
        event2Participant = findViewById(R.id.event2participant_for_rule);
        event2Periodicity = findViewById(R.id.event2periodicity_for_rule);
        orderImpart = findViewById(R.id.orderimpart_for_rule);
        ruleAction1 = findViewById(R.id.rule_action1);
        ruleAction2 = findViewById(R.id.rule_action2);
        ruleAction3 = findViewById(R.id.rule_action3);
        ruleAction4 = findViewById(R.id.rule_action4);
    }

    private void getOptionData() {
        options1Items.add(this.getString(R.string.incon_type));
        options1Items.add(this.getString(R.string.incon_type_first_before));
        options1Items.add(this.getString(R.string.incon_type_first_meets));
        options1Items.add(this.getString(R.string.incon_type_first_overlaps));
        options1Items.add(this.getString(R.string.incon_type_first_starts));
        options1Items.add(this.getString(R.string.incon_type_first_equals));
        options1Items.add(this.getString(R.string.incon_type_first_during));
        options1Items.add(this.getString(R.string.incon_type_first_finishes));

        options11Items.add("");
        options11Items.add(this.getString(R.string.incon_type_first_before_describe));
        options11Items.add(this.getString(R.string.incon_type_first_meets_describe));
        options11Items.add(this.getString(R.string.incon_type_first_overlaps_describe));
        options11Items.add(this.getString(R.string.incon_type_first_starts_describe));
        options11Items.add(this.getString(R.string.incon_type_first_equals_describe));
        options11Items.add(this.getString(R.string.incon_type_first_during_describe));
        options11Items.add(this.getString(R.string.incon_type_first_finishes_describe));

        ArrayList<String> options2Items_01 = new ArrayList<>();
        ArrayList<String> options2Items_02 = new ArrayList<>();
        ArrayList<String> options2Items_03 = new ArrayList<>();
        ArrayList<String> options2Items_04 = new ArrayList<>();
        ArrayList<String> options2Items_05 = new ArrayList<>();
        ArrayList<String> options2Items_06 = new ArrayList<>();
        ArrayList<String> options2Items_07 = new ArrayList<>();

        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
        options2Items.add(options2Items_04);
        options2Items.add(options2Items_05);
        options2Items.add(options2Items_06);
        options2Items.add(options2Items_07);
    }

    public void makeButtonClicked() {
        event1Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        event1Periodicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        event2Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        event2Periodicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        ruleAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        ruleAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        ruleAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });
        ruleAction4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleElectionAlert(v);
            }
        });

        orderImpart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void showSingleElectionAlert(View view) {

        String[] mArray = null;
        String title = null;
        switch (view.getId()) {
            
            case R.id.event1type_for_rule:
                mArray = TypeOfEvent.Kind.KindStrings();
                if (mArray.length > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("CustomizedRule");

                    final String[] finalEvent1TypeArray = mArray;
                    builder.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            Log.d("CustomizedRuleAcitvity", "事件1的类型选择了第" + which + "项");
                            event1Type.setText(finalEvent1TypeArray[which]);
                        }
                    });

                    builder.show();
                }
                break;
            case R.id.event2type_for_rule:
                mArray = TypeOfEvent.Kind.KindStrings();
                if (mArray.length > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("CustomizedRule");

                    final String[] finalEvent2TypeArray = mArray;
                    builder.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            Log.d("CustomizedRuleAcitvity", "事件2的类型选择了第" + which + "项");
                            event2Type.setText(finalEvent2TypeArray[which]);
                        }
                    });

                    builder.show();
                }
                break;
            
            case R.id.event1periodicity_for_rule:
                mArray = Periodicity.TypeOfPeriodicity.TypesOfPeriodicityStrings();
                if (mArray.length > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("CustomizedRule");

                    final String[] finalEvent1PeriodicityArray = mArray;
                    builder.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            Log.d("CustomizedRuleAcitvity", "事件1的周期性选择了第" + which + "项");
                            event1Periodicity.setText(finalEvent1PeriodicityArray[which]);
                        }
                    });

                    builder.show();
                }
                break;
            case R.id.event2periodicity_for_rule:
                mArray = Periodicity.TypeOfPeriodicity.TypesOfPeriodicityStrings();
                if (mArray.length > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("CustomizedRule");

                    final String[] finalEvent2PeriodicityArray = mArray;
                    builder.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            Log.d("CustomizedRuleAcitvity", "事件2的周期性选择了第" + which + "项");
                            event2Periodicity.setText(finalEvent2PeriodicityArray[which]);
                        }
                    });

                    builder.show();
                }
                break;
            





//









//








//









//








//









//








//









//



            
            case R.id.button_customizedrule_conflicttype:
                break;
            default:
                mArray = null;
                break;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("CustomizedRuleActivity", "点击了BarItem");
        switch (item.getItemId()) {
            case R.id.menu_item_new_event:

                Toast.makeText(this, "点击了确认按钮", Toast.LENGTH_SHORT).show();


                String v_event1Title = event1Title.getText().toString();
                String v_event1Type = event1Type.getText().toString();
                String v_event1Location = event1Location.getText().toString();
                String v_event1Host = event1Host.getText().toString();
                String v_event1Participant = event1Participant.getText().toString();
                String v_event1Periodicity = event1Periodicity.getText().toString();
                String v_event2title = event2Title.getText().toString();
                String v_event2Type = event2Type.getText().toString();
                String v_event2Location = event2Location.getText().toString();
                String v_event2Host = event2Host.getText().toString();
                String v_event2Participant = event2Participant.getText().toString();
                String v_event2Periodicity = event2Periodicity.getText().toString();





                boolean v_symmetric = orderImpart.isChecked();
                
                model = new CustomizedRuleModel(v_event1Title, v_event1Type, v_event1Location, v_event1Host, v_event1Participant, v_event1Periodicity
                        , v_event2title, v_event2Type, v_event2Location, v_event2Host, v_event2Participant, v_event2Periodicity
                        ,  v_symmetric);
                



                
                this.finish();

                return true;
            case android.R.id.home:
                Log.e("temp_back", "点击了返回按钮,虚拟按键");
                this.finish(); // back button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}