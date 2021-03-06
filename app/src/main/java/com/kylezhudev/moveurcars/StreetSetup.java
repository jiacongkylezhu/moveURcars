package com.kylezhudev.moveurcars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    private String streetSP = "streetSP", numSP = "numSP";
    private int numStored;
    private DatabaseHelper dbHelper;

    private static final String SP_STREET_KEY = "street";
    private static final String SP_SIDE_KEY = "side";
    private static final String SP_STREET_ID_KEY = "side";
    private static int id;
    private static final String ID_KEY = "ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_setup);
        sideOfStreet = (Spinner) findViewById(R.id.sp_side_of_street);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.side_of_street_spinner, android.R.layout.simple_spinner_item);
        dbHelper = DatabaseHelper.getInstance(this);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                saveStreet(selectedItem, street);
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StreetSetup.this, AlarmSetupActivity.class);
                intent.putExtra(ID_KEY,id);
                startActivity(intent);
//                MainActivity.getAlarmFragment().updateStreet();
                finish();
            }
        });

    }

    private void saveStreet(String side, String street) {
        dbHelper = DatabaseHelper.getInstance(this);
        id = dbHelper.getItemIndex();
        if(id == -1){
            id = 1;
        }else {
            id++;
        }
        Log.i("check inAlarmId", "intAlarmId = " + id);

        boolean isInserted = dbHelper.insertData(street,side,id);
        if (isInserted) {
            Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Data does not Inserted", Toast.LENGTH_SHORT);
        }
        dbHelper.closeDB();
        dbHelper.close();
    }




        //TODO 4/16/2017 delete below if the above works

//        SharedPreferences spStreetID = getSharedPreferences(numSP, MODE_PRIVATE);
//        if(Integer.toString(numStored) != null) {
//            numStored = spStreetID.getInt(SP_STREET_ID_KEY, -1);
//        }
//
//        if(numStored == -1){
//            numStored = 1;
//        }else{
//            numStored ++;
//        }
//        Log.i("check streetID", "streetID = " + numStored);
//
//
//        SharedPreferences.Editor idEditor = spStreetID.edit();
//        idEditor.putInt(SP_STREET_ID_KEY, numStored);
//        idEditor.commit();
//
//        SharedPreferences spStreet = getSharedPreferences(streetSP + Integer.toString(numStored),MODE_PRIVATE);
//        SharedPreferences.Editor streetEditor = spStreet.edit();
//        streetEditor.putString(SP_STREET_KEY, street);
//        streetEditor.putString(SP_SIDE_KEY, side);
//        streetEditor.commit();
//
//

}
