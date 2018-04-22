package com.example.mom.atm;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {

    //使用 OkHttp 第三方函式庫  需先在build.gradle(Module:app) implement 'com.squareup.okhttp3:okhttp:3.9.1'
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        //new TransTask().execute("http://atm201605.appspot.com/h");  //使用AsyncTask方法
        //使用OKHttp第三方函式庫
        Request request = new Request.Builder()             //使用Request.Builder() 設定一個連線必要資訊
                .url("http://atm201605.appspot.com/h")      //如網址用url()方法定義
                .build();                                   //產生Http的 Request 讓為連接至Server
        Call call = client.newCall( request);               //使用 OkHttpClient 的 newCall() 建議呼叫物件 尚未連接至Server
        call.enqueue( new Callback() {                      //使用 Call 的 enqueue() 進行排程連線 此時才真正連接至Server  並在方法中準備一個回報的物件
                         @Override
                         public void onResponse(Call call, Response response) throws IOException {  //連線成功 自動執行onResponse()
                                String json = response.body().string();         //取得Server回應的資料
                                Log.d("OKHTTP" , json);
                                //解析JSON 3個不同方法
                                //parseJSON(json); //使用Android內建 JSON.org函式庫
                                parseGson(json); //使用Google提供 Gson第三方函式庫
                                //parseJackson(json);//使用Jackson第三方函式庫
                         }

                         @Override
                         public void onFailure(Call call, IOException e) {          //連線失敗 自動執行onFailure()
                                //告知使用者連線失敗
                        }
                }

        );
    }
    /*
    //任務過於繁雜 使用OkHttp 第三方函式庫 快速實作資料交換的工作 讓HTTP連線更有效率 避免人為失誤
    //使用AsyncTask類別 設計Http連線工作  讀取完成後回傳字串  doInBackground()設計連線並讀取Server回傳的所有字串
    class TransTask extends AsyncTask < String , Void ,String >{
        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader( new InputStreamReader( url.openStream()));
                String line = in.readLine();
                while (line != null){
                    Log.d("HTTP" , line);
                    sb.append(line);
                    line = in.readLine(); //讀下一行
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return sb.toString();
        }
        //在 doInBackground() 後自動執行
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("JSON" , s);
            parseJSON(s);
        }
    }

    //解析JSON資料
    //利用Android內建的 JSON.org函式庫  JSONArray,JSONObject...
    private void parseJSON(String s ){
        ArrayList<Transaction> trans = new ArrayList<>();   //ArrayList集合物件trans , 裡面只能存放Transaction物件
        try {
            JSONArray array = new JSONArray(s);   //需要加 JSONException  alt+enter加入try..catch  , 將傳入的字串s 交給JSONArray() 產生array物件
            for(int i =0 ; i <array.length() ; i++){   //迴圈取出物件
                JSONObject object = array.getJSONObject(i);  //以索引值取出物件
                String account = object.getString("account");  //JSONObject.getXX()取得各個屬性值
                String date = object.getString("date");
                int amount = object.getInt("amount");
                int type = object.getInt("type");
                Log.d("JSON" , account+"/"+date+"/"+amount+"/"+type);
                Transaction t = new Transaction(account , date ,amount ,type);    //產生Transaction物件 代表一筆交易紀錄
                trans.add(t);    //將物件加到集合中
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
    //解析JSON資料  ,  利用Google提供處理JSON格式的函式庫 Gson第三方函式庫
    private void parseGson(String s){
        Gson gson = new Gson();
        //Gson.fromJson() 從JSON資料轉為Java資料
        //第一個參數 JSON字串
        //第二個參數 想轉出的資料格式 資料型態使用Gson的TypeToken宣告目的型態為 ArrayList< Transaction> , Gson將JSON資料一次轉為Java集合 以list物件存取
        final ArrayList< Transaction> list = gson.fromJson( s, new TypeToken< List<Transaction>>(){}.getType());
        Log.d("GSON" , list.size() +"/"+ list.get(0).getAmount() ); //列出陣列內筆數 與第一筆交易金額 確認是否轉換成功
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupRecyclerView(list);
            }
        });
    }
    /*
    //解析JSON資料  ,  利用 Jackson(FasterXML Jackson)第三方函式庫 是目前開源專案中有著高效率特色的函式庫之一
    private void parseJackson(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //ObjectMapper.readValue() 進行轉換JSON資料為Java資料 , 第一個參數 JSON字串s
            //用Jackson的 TypeReference 宣告目的型態為ArrayList<Transaction> , ObjectMapper將JSON資料一次轉為Java集合 以list物件存取
            ArrayList< Transaction> list = objectMapper.readValue(s, new TypeReference<List<Transaction>>(){} );
            Log.d("JACKSON" , list.size() +"/"+ list.get(0).getAmount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    private void setupRecyclerView( List<Transaction> list){
        RecyclerView recyclerView = findViewById(R.id.recycler);  //取得畫面中的RecyclerView元件
        TransactionAdapter adapter = new TransactionAdapter(list);  //傳入交易集合list 並產生TransactionAdapter
        recyclerView.setAdapter(adapter);                           //設定Adapter
        recyclerView.setLayoutManager( new LinearLayoutManager(this)); //設定RecyclerView自己的LayoutManager物件
    }

}
