package com.example.mom.atm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class FinanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //浮動按鈕 動作事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(FinanceActivity.this , AddActivity.class)
                );
            }
        });
        //SimpleCursorAdapter 和 SQLite的Query資料查詢 實作
        ListView list = findViewById(R.id.list); //取得ListView
        //MyDBHelper helper = new MyDBHelper(this , "expense2.db" , null , 1 ); //沒做SQLiteOpenHelper Singleton
        MyDBHelper helper = MyDBHelper.getInstance(this ); //有做SQLiteOpenHelper Singleton

        // Cursor(指標) query 查詢
        Cursor c = helper.getReadableDatabase().query(
                "exp" , null , null , null , null , null , null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this ,
                //android.R.layout.simple_expandable_list_item_2 ,    //一列中有兩個TextView
                R.layout.finance_row ,   //自訂的finance_row.xml 一列中有三個TextView , 需要調字體顏色or大小 直接在finance_row調整即可
                c,   //查詢的Cursor物件
                new String[] {"cdate" , "info" , "amount"},  //顯示 "cdate" "info" "amount" 三個欄位
                new int[] { R.id.item_cdate , R.id.item_info , R.id.item_amount}, //三欄所對應版面id值
                0);  //flags參數=0 : ListView 在展示過程中資料庫中的記錄如果被更動了，ListView 將不自動重新查詢並更動畫面中的資料
        list.setAdapter(adapter);  //將adapter設定給list

    }

}
