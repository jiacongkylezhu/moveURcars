package com.kylezhudev.moveurcars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kylezhudev.moveurcars.model.DatabaseHelper;

public class StreetSetup extends AppCompatActivity {
    private Spinner sideOfStreet;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private String selectedItem;
    private String street;
    private Button btnSave;
    private EditText editText;
    private DatabaseHelper dbHelper;
    private static int id;
    private static final String ID_KEY = "ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_setup);
        transparentStatusBar();
        sideOfStreet = (Spinner) findViewById(R.id.sp_side_of_street);
//        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.side_of_street_spinner, android.R.layout.simple_spinner_item);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.side_of_street_spinner, R.layout.spinner_items);
        dbHelper = DatabaseHelper.getInstance(this);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_items);
        sideOfStreet.setAdapter(arrayAdapter);
        sideOfStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Please select which side you park the car on", Toast.LENGTH_SHORT).show();
            }
        });

        editText = (EditText) findViewById(R.id.et_street);
        btnSave = (Button) findViewById(R.id.btn_save_street);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                street = editText.getText().toString();

                if (selectedItem == null) {
                    Toast.makeText(getBaseContext(), "Please select which side you park the car on", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(street)) {
                    Log.i("savedStreet", "Street: " + street);
                    Toast.makeText(getBaseContext(), "Please enter street name", Toast.LENGTH_SHORT).show();
                }else{
                    saveStreet(selectedItem, street);
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StreetSetup.this, AlarmSetupActivity.class);
                    intent.putExtra(ID_KEY, id);
                    startActivity(intent);
//                MainActivity.getAlarmFragment().updateStreet();
                    finish();
                }


            }
        });

    }

    private void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <21){
            setWindowFlag(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,true);
        }
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if(Build.VERSION.SDK_INT >= 21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on){
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if(on){
            winParams.flags |= bits;
        }else{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void saveStreet(String side, String street) {
        dbHelper = DatabaseHelper.getInstance(this);
        id = dbHelper.getItemIndex();
        if (id == -1) {
            id = 1;
        } else {
            id++;
        }

        Log.i("check inAlarmId", "intAlarmId = " + id);

        boolean isInserted = dbHelper.insertData(street, side, id);
        if (isInserted) {
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Data does not Inserted", Toast.LENGTH_SHORT);
        }
        dbHelper.closeDB();
        dbHelper.close();
    }
}
