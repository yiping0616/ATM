package com.example.mom.atm;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private  MyDBHelper helper;
    private EditText edDate;
    private EditText edInfo;
    private EditText edAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findValue();

        //MyDBHelper(Context , String name 自訂資料庫檔案名稱 .db or .sqlite ,Cursor factory null是以標準模式SQLiteCursor , int version目前資料庫版本 )
        //系統發現不存在expense.db 會自動呼叫MyDBHelper的onCreate()建立expense.db
        //helper = new MyDBHelper (this , "expense2.db", null , 1 ); //沒做SQLiteOpenHelper Singleton
        helper = MyDBHelper.getInstance(this); //有做SQLiteOpenHelper Singleton  ,  getInstance宣告為 public static My
    }

    private void findValue() {
        edDate = (EditText) findViewById(R.id.ed_date);
        edInfo = (EditText) findViewById(R.id.ed_info);
        edAmount = (EditText) findViewById(R.id.ed_amount);
    }
    //onClick add
    //SQLite 實作
    //把值讀出來 再放到 ContentValues 再insert到table裏
    //MyDBHelper.getWritableDatabase().insert( table "exp", nullColumnHack , ContentValues values )
    public void add(View view){
        String cdate = edDate.getText().toString();
        String info = edInfo.getText().toString();
        int amount = Integer.parseInt( edAmount.getText().toString() );
        ContentValues values = new ContentValues();
        values.put("cdate" , cdate);
        values.put("info" , info );
        values.put("amount" , amount);
        //取得資料庫物件後 呼叫insert()方法 傳入表格名稱與values集合 以新增這筆紀錄 若成功會return 新增資料的id值
        //nullColumnHack參數 可填入一欄位名稱 當values無任何資料時 會在該欄位給予空值，不多見 可直接給予第二參數null
        helper.getWritableDatabase().insert("exp" , null , values) ;
        finish();
    }

}
