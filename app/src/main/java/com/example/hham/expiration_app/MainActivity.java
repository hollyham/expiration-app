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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Activity class
 */
public class MainActivity extends AppCompatActivity {
    // List of objectClass objects (the items added to list)
    ArrayList<objectClass> items = new ArrayList<>();
    // List of Maps containing the name and expiration of each item
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    // Adapter for items to be displayed in ListView
    SimpleAdapter adapter;
    // View displaying list of items
    ListView lvItems;
    // Button to select date
    Button dateButton;
    // Selected data
    Date newDate = new Date();
    // Image view (for camera)
    ImageView imageView;
    // Permission code for camera access
    public  static final int RequestPermissionCode  = 1 ;

    /**
     * Initiates program
     * @param savedInstanceState Used to save and recover state information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets ListView (item list) by id
        lvItems = (ListView) findViewById(R.id.lvItems);
        // Gets date selection button by id
        dateButton = (Button) findViewById(R.id.dateButton);
        //readItems();

        // Enables permission to access camera
        EnableRuntimePermission();
        // Open camera upon button click
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        // Creates an adapter for ListView (list of items)
        adapter = new SimpleAdapter(this, data,
                R.layout.listview_row,
                new String[] {"title", "date"},
                new int[] {R.id.nameID,
                        R.id.dateID});
        // Sets ListView to use adapter
        lvItems.setAdapter(adapter);
        setUpListViewListener();
    }

    /**
     * Set up for camera upon activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Setup for onClickListener for ListView (list of items)
     */
    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        final Adapter currentAdapter = adapter.getAdapter();
                        data.remove(pos);
                        ((BaseAdapter) currentAdapter).notifyDataSetChanged();
                        //writeItems();
                        return true;
                    }
                });
    }

    /**
     * Asks for permission to access camera
     */
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

    /**
     * Displays result of permisson request
     * @param RC
     * @param per
     * @param PResult
     */
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Occurs after add item button is pressed
     * @param v Current view
     */
    public void onAddItem(View v) {
        // Checks if date was selected before adding item
        if(!dateButton.getText().toString().equals("Date")){
            // Gets user input text box
            EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
            // Gets user's input
            String itemText = etNewItem.getText().toString();
            // Creates new objectClass object with new item information
            objectClass newObject = new objectClass (itemText, newDate, null);
            // Adds item to list of items
            items.add(newObject);

            // Creates a HashMap to contain item name and expiration date
            Map<String, String> datum = new HashMap<String, String>(2);
            // Adds name to HashMap
            datum.put("title", newObject.getName());
            // Gets expiration date and builds string date
            Date date = newObject.getDate();
            datum.put("date",date.getMonth() + "/" + date.getDate() + "/" + date.getYear());

            // TODO: Change so item is added in order to the list
            // Adds HashMap to List of all items
           //***** data.add(datum);

            // Reset date button and input text box
            dateButton.setText("Date");
            etNewItem.setText("");

            adapter.notifyDataSetChanged();
            //writeItems();
        }
    }

    /**
     * Sets default for date picker to be current date and saves selected date.
     * @param v Current view
     */
    public void onSelectDate(View v) {
        // Gets today's date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    /**
                     * Saves selected date and changes button to display selected date
                     * @param datePicker
                     * @param year Selected year
                     * @param month Selected month
                     * @param day Selected day
                     */
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Creates string of selected date
                        String inputDate = month + "/" + day + "/" + year;

                        // saves selected year, month, and day in instance variable
                        newDate.setYear(year);
                        newDate.setMonth(month);
                        newDate.setDate(day);

                        // sets button text as selected date
                        dateButton.setText(inputDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    /**
     * TODO: Finish color changes
     */
    public void updateItemColors(){
        // Gets today's date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        for(int i=0; i<lvItems.getChildCount(); i++){

        }
    }

    /**
     * NOT UPDATED: Reads items from todo.txt
     */
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

    /**
     * NOT UPDATED: Writes items to todo.txt
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

    /**
     * Parses the date components
     * first element is year, followed by month and date
     * @param dateParse; the date string to be parsed
     *
     */
    public int [] parseDate (String dateParse){
        int [] result = new int[3];
        int index = dateParse.indexOf("/");
        result[0] = Integer.parseInt(dateParse.substring(0, index));
        dateParse = dateParse.substring(index+1);
        index = dateParse.indexOf("/");
        result[1] = Integer.parseInt(dateParse.substring(0, index));
        dateParse = dateParse.substring(index+1);
        result[2] = Integer.parseInt(dateParse);
        return result;
    }

    /**
     * TODO: Put new item in correct location in list
     *
     * @param newItem Item (HashMap) to be inserted into the data (first value is name, second is date)
     */
    public void insertItem(HashMap<String, String> newItem){

    }

}
