package com.example.mom.atm;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //記住上次的帳號 利用 偏好設定SharedPreferences 存取設定資料
        EditText edID = (EditText) findViewById(R.id.ID); //取得EditText ID元件
        setting = getSharedPreferences("ATM",MODE_PRIVATE);
        //取得儲存設定物件 setting , ATM為設定檔名 ATM.xml , MODE_PRIVATE指設定值只能讓這app使用
        edID.setText(setting.getString("Pref_Userid","")); //設定edID預設值，顯示上次記住的登入帳號

        //顯示密碼與不顯示密碼 CheckBox 設定
        final EditText edPW = (EditText) findViewById(R.id.PW);
        final CheckBox checkBox =  (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(       //(打new OnCheck後 按enter 自動產生) Check change listener
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //顯示密碼
                        if (checkBox.isChecked()){
                            edPW.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        else{
                            edPW.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }

        );
    }
    //View元件 android.view.View ,onClick方法的傳入值一定要有一個View元件
    public void Login (View view){
        EditText edID = (EditText) findViewById(R.id.ID);
        EditText edPW = (EditText) findViewById(R.id.PW);
        String ID = edID.getText().toString();
        String PW = edPW.getText().toString();

        //實作AsyncTask LoginTask() 網路連線登入
        String url = new StringBuilder(     //利用StringBuilder產生對應的字串物件 避免不必要的參照與資源回收
                "http://atm201605.appspot.com/login?uid=")
                .append(ID)
                .append("&pw=")
                .append(PW)
                .toString();
        new LoginTask().execute(url);

        /*  一開始只用if else判斷是否登入成功
        if (ID.equals("Jack") && PW.equals("1234")){ //假設登入成功
            setting = getSharedPreferences("ATM",MODE_PRIVATE); //記住登入帳號
            setting.edit() //呼叫.edit()取得編輯物件
                    .putString("Pref_Userid",ID)  //資料為 Sting ID , 標籤為 Pref_Userid
                    .apply(); //commit()會直接執行 , apply()會盡快執行

            Toast.makeText(this , "登入成功", Toast.LENGTH_LONG).show();
            //設定返回值
            getIntent().putExtra("LOGIN_USERID", ID);
            getIntent().putExtra("LOGIN_PASSWORD",PW);
            setResult(RESULT_OK , getIntent()); //回到MainActivity 仍可得到結果

            finish(); //Activity類別的finish()方法,回到前一個活動,這裡前一個活動是MainActivity
        }
        else{ //登入失敗
            new AlertDialog.Builder(this)
                    .setTitle("ATM")
                    .setMessage("登入失敗")
                    .setPositiveButton("OK",null)
                    .show();
        }
     */
    }
    public void Cancel (View view){
        finish();
    }

    //設計網路連線登入的AsyncTask內部類別
    class LoginTask extends AsyncTask< String , Void , Boolean >{  //傳入值為網址使用String  不需要更新所以第二參數為Void類別 回傳值為Boolean判斷登入成功失敗


        @Override
        protected Boolean doInBackground(String... strings) {
            boolean logon = false;

            try {
                URL url = new URL(strings[0]);         //可打完try的內容後 按alt+Enter 快速加入try...catch 再按alt+Enter加上 catch IOException
                InputStream is = url.openStream();
                int data = is.read();
                Log.d("HTTP" , String.valueOf(data));
                if(data==49){  //連線讀取一個字元 因讀到 1 字元 Unicode值為49 代表登入成功
                    logon =true;
                }
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return logon;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {  //連線完成後 給使用者回應
            super.onPostExecute(aBoolean);
            if (aBoolean){
                //登入成功
                //儲存帳號至SharedPreferences , 結束本畫面回到MainActivity
                Toast.makeText(LoginActivity.this , "登入成功" , Toast.LENGTH_LONG ).show();
                //儲存帳號至SharedPreferences
                EditText edID = (EditText) findViewById(R.id.ID);
                EditText edPW = (EditText) findViewById(R.id.PW);
                String ID = edID.getText().toString();
                String PW = edPW.getText().toString();
                setting = getSharedPreferences("ATM",MODE_PRIVATE); //記住登入帳號
                setting.edit() //呼叫.edit()取得編輯物件
                        .putString("Pref_Userid",ID)  //資料為 Sting ID , 標籤為 Pref_Userid
                        .apply(); //commit()會直接執行 , apply()會盡快執行
                getIntent().putExtra("LOGIN_USERID", ID);
                getIntent().putExtra("LOGIN_PASSWORD",PW);
                setResult(RESULT_OK , getIntent());
                finish();
            }
            else{
                //登入失敗
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("ATM")
                        .setMessage("登入失敗")
                        .setPositiveButton("OK",null)
                        .show();
            }
        }
    }
}
