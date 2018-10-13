package com.example.hham.expiration_app;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ArrayList<objectClass> items = new ArrayList<>();
    ArrayList<String> listNames = new ArrayList<>();
    ArrayAdapter<String> itemAdapters;
    ListView lvItems;
    Button dateButton;
    Date newDate = new Date();

    ImageView imageView;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        dateButton = (Button) findViewById(R.id.dateButton);
        //readItems();
        itemAdapters = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);
        lvItems.setAdapter(itemAdapters);
        setUpListViewListener();

        EnableRuntimePermission();
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemAdapters.notifyDataSetChanged();
                        //writeItems();
                        return true;
                    }
                });
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA))
        {
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    public void onAddItem(View v) {
        if (dateButton.getText().toString().equals("Date")) {
        } else {
            EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
            String itemText = etNewItem.getText().toString();
            objectClass newObject = new objectClass(itemText, newDate, null);
            items.add(newObject);
            itemAdapters.add(newObject.getName());
            etNewItem.setText("");
            //writeItems();
        }
    }

    public void onSelectDate(View v) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String inputDate = month + "/" + day + "/" + year;

                        // saves selected year, month, and day
                        newDate.setYear(year);
                        newDate.setMonth(month);
                        newDate.setDate(day);

                        // sets button text as selected date
                        dateButton.setText(inputDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    /*
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e){
            items = new ArrayList<String>();
        }
    }
    */

    /*
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    */

    public ArrayList<Date> listDates() {
        ArrayList<Date> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            objectClass temp = items.get(i);
            result.add(temp.getDate());
        }
        return result;
    }

    /*
     * insert element to the right place
     */
    private ArrayList<objectClass> insertItem(ArrayList<objectClass> items, objectClass newItem) {
        // when newItem is the first item
        if (items.size() == 0) {
            items.add(newItem);
            return items;
        }

        ArrayList<objectClass> result = new ArrayList<objectClass>();
        for (int i = 0; i < items.size(); i++) {

        }
        return result;
    }

}
