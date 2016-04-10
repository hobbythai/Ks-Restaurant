package com.hobbythai.android.ksrestaurant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //explicit
    private MyManage myManage;

    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind widget
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);

        //Request SQLite
        myManage = new MyManage(this);

        //test add value
        testAdd();

        //delete sqlite
        deleteSQLite();

        //synchronize JSON to SQLite
        synJSONtoSQLite();


    }//main method

    public void clickLogin(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //check space
        if (userString.equals("") || passwordString.equals("")) {
            //have space
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "มีช่องว่าง", "กรอกให้ครบ");
        } else {
            //no space
            checkUser();

        }

    }//click login

    private void checkUser() {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE WHERE User = " + "'" + userString + "'", null);
            cursor.moveToFirst();

            String[] resultStrings = new String[cursor.getColumnCount()]; //book mem for record x column
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                resultStrings[i] = cursor.getString(i);
            }//for
            cursor.close(); //turn back mem

            //check password
            if (passwordString.equals(resultStrings[2])) {
                //password true
                Toast.makeText(this, "ยินดีต้อนรับ " + resultStrings[3], Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                intent.putExtra("Officer", resultStrings[3]);
                startActivity(intent);
                finish();

            } else {
                //password false
                MyAlert myAlert = new MyAlert();
                myAlert.myDialog(this, "Password ผิด", "กรอกใหม่");
            }

        } catch (Exception e) {
            Log.d("test", "error = " + e.toString());
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "ผิดพลาด", "ไม่มี" + userString + "ในข้อมูลของเรา");
        }

    }//check user

    private void synJSONtoSQLite() {

        MyMainConnected myMainConnected = new MyMainConnected();
        myMainConnected.execute();

    }//sys json

    //inner class
    public class MyMainConnected extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                //syn server get data from server
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

            Log.d("Restaurant", "strJSON" + strJSON); //show raw json

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
