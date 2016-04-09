package com.hobbythai.android.ksrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //explicit
    private MyManage myManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request SQLite
        myManage = new MyManage(this);

        //test add value
        testAdd();

        //delete sqlite
        deleteSQLite();

        //synchronize JSON to SQLite
        synJSONtoSQLite();


    }//main method

    private void synJSONtoSQLite() {
        MyMainConnected myMainConnected = new MyMainConnected();
        myMainConnected.execute();

    }

    //inner class
    public class MyMainConnected extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                //syn server
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://swiftcodingthai.com/9Apr/php_get_user_ks.php").build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string(); //send data to strJSON

            } catch (Exception e) {
                return null;
            }

        }//do in background

        @Override
        protected void onPostExecute(String strJSON) {
            super.onPostExecute(strJSON);

            Log.d("Restaurant","strJSON"+strJSON); //show raw json

            try {

                //get JSON
                JSONArray jsonArray = new JSONArray(strJSON); //load data to new format use json array
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String strUser = jsonObject.getString(MyManage.column_user);
                    String strPassword = jsonObject.getString(MyManage.column_pass);
                    String strName = jsonObject.getString(MyManage.column_name);

                    myManage.addValueToSQLite(0, strUser, strPassword, strName);

                }//for

            } catch (Exception e) {
                Log.d("Restaurant", "Error--> " + e.toString());
            }

        }//on pos execute

    }//my main connected


    private void deleteSQLite() {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManage.food_table, null, null);
        sqLiteDatabase.delete(MyManage.user_table, null, null);
    }

    private void testAdd() {
        myManage.addValueToSQLite(0, "user", "pass", "name");
        myManage.addValueToSQLite(1, "food", "price", "source");
    }


}//main class
