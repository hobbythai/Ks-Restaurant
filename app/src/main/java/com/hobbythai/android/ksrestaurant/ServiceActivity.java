package com.hobbythai.android.ksrestaurant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity {

    //explicit
    private TextView textView;
    private Spinner spinner;
    private ListView listView;

    private String officerString, deskString, foodString, amountString;

    private String[] foodStrings,priceStrings, iconStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //bind widget
        textView = (TextView) findViewById(R.id.textView2);
        spinner = (Spinner) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.listView);

        //show officer
        officerString = getIntent().getStringExtra("Officer");
        textView.setText(officerString);

        //create spinner
        createSpinner();

        //test
        MyConnectedFood myConnectedFood = new MyConnectedFood();
        myConnectedFood.execute();

    }//main method

    public class MyConnectedFood extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            //direct get from server
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://swiftcodingthai.com/9Apr/php_get_food.php").build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string(); // send to

            } catch (Exception e) {
                Log.d("10Aril", "Connected Error --> " + e.toString());
                return null;
            }
        }//do in

        @Override
        protected void onPostExecute(String strJSON) {
            super.onPostExecute(strJSON);

            Log.d("10Aprill", "strJSON --> " + strJSON);

            try {

                //set data for list view
                JSONArray jsonArray = new JSONArray(strJSON);

                foodStrings = new String[jsonArray.length()]; //book mem
                priceStrings = new String[jsonArray.length()];
                iconStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    foodStrings[i] = jsonObject.getString(MyManage.column_food);
                    priceStrings[i] = jsonObject.getString(MyManage.column_price);
                    iconStrings[i] = jsonObject.getString(MyManage.column_source);

                }// for

                //create list view
                MyAdapter myAdapter = new MyAdapter(ServiceActivity.this,
                        foodStrings, priceStrings, iconStrings);
                listView.setAdapter(myAdapter);


            } catch (Exception e) {
                Log.d("10Aprill", "Error strJSON --> " + e.toString());
            }

        }

    }//my connected food


    private void createSpinner() {

        final String[] deskStrings = {"โต๊ะที่ 1", "โต๊ะที่ 2", "โต๊ะที่ 3", "โต๊ะที่ 4",
                "โต๊ะที่ 5", "โต๊ะที่ 6", "โต๊ะที่ 7", "โต๊ะที่ 8", "โต๊ะที่ 9", "โต๊ะที่ 10",};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, deskStrings
        );

        spinner.setAdapter(stringArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deskString = deskStrings[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deskString = deskStrings[0];
            }
        });

    }

}//main class


















