package com.example.mom.atm;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public Boolean logon = false ;
    public static final int FUNC_LOGIN=1 ; // 代表 登入功能 的常數

    //MainActivity的屬性 func陣列 為字串陣列
    String[] func = {"餘額查詢", "交易明細" , "最新消息" , "投資理財" , "離開"};

    //以 int陣列 將icon的id存入 MainActivity的屬性 icons陣列中
    int[] icons = {R.drawable.func_bank1,
                   R.drawable.func_history1,
                   R.drawable.func_news1,
                   R.drawable.func_finance1,
                   R.drawable.func_exit1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!logon){
            Intent intent = new Intent(this , LoginActivity.class);
            //startActivity(intent);
            startActivityForResult(intent , FUNC_LOGIN); //切換功能後,回到主功能畫面需取得特定的結果
        }
        /*
        //使用ListView
        ListView list = (ListView) findViewById(R.id.List);
        //ArrayAdapter(Context , 清單顯示的版面配置 , 陣列資源 )
        ArrayAdapter adapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , func);
        list.setAdapter(adapter); //將adapter物件 設定至 ListView元件中
        //使用Spinner
        Spinner notify = (Spinner) findViewById(R.id.spinner);
        //因陣列放在resource中,可用ArrayAdapter.createFromResource(Context , 陣列資源ID , 清單顯示的版面配置)
        final ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(this , R.array.notify_array , android.R.layout.simple_spinner_item);
        //清單中會有點壅擠, 可使用ArrayAdapter的 setDropDownViewResource ,會較美觀 不壅擠
        nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notify.setAdapter(nAdapter); //將nAdapter物件 設定至 Spinner元件中
        //選擇項目的事件處理 , 加入 AdapterView.OnItemSelectedListener
        notify.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this , nAdapter.getItem(position).toString() , Toast.LENGTH_LONG).show();
                        //若需要取得目前Spinner選擇的項目 , 可使用 Spinner的getSelectedItem
                        //String text = notify.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        //使用GridView
        GridView grid = findViewById(R.id.grid);
        ArrayAdapter gAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , func );
        grid.setAdapter(gAdapter);
        grid.setOnItemClickListener( this ); // MainActivity Implement AdapterView.OnItemClickListener , 實作傾聽者介面
        */
        //使用IconAdapter 建立 有圖文的 GridView , IconAdapter extends BaseAdapter
        GridView grid = findViewById(R.id.grid);
        IconAdapter gAdapter = new IconAdapter();
        grid.setAdapter(gAdapter);
        grid.setOnItemClickListener( this );
    }

    class IconAdapter extends BaseAdapter {         //alt+Enter implement自動產生四個必須實作的方法

        @Override
        public int getCount() {     //需回傳GridView中項目的個數
            return func.length;
        }

        @Override
        public Object getItem(int position) {       //回傳參數position所對應到的資源
            return func[position];
        }

        @Override
        public long getItemId(int position) {        //回傳position所對應到的id值 , 值 可供辨識 不可重複
            return icons[position];
        }
        //GridView欲在畫面中欲展示一個項目給使用者時，會呼叫此方法
        //convertView 即是目前呼叫方手上有的 View 元件。當然，在第一次呼叫時，傳入的 convertView 是 null 值
        //應該在 convertView 是 null 值時產生一個合適的 View 元件給呼叫方。
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                //取得LayoutInflater物件 在使用它的方法 inflate() 由版面檔 item_row.xml 建立一個實際的View物件
                row = getLayoutInflater().inflate(R.layout.item_row ,null);
                ImageView image = row.findViewById(R.id.item_image);
                TextView text = row.findViewById(R.id.item_text);
                image.setImageResource(icons[position]);
                text.setText(func[position]);
            }
            return row;
        }
    }
    //startActivityForResult 接收回傳值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // requestCode ＝ FUNC_LOGIN , resultCode = RESULT_OK , data = intent
        if (requestCode == FUNC_LOGIN){
            if (resultCode == RESULT_OK){
                String ID = data.getStringExtra("LOGIN_USERID");
                String PW = data.getStringExtra("LOGIN_PASSWORD");
                Log.d("RESULT", ID +"/"+PW); // 顯示於Logcat中 , Debug用
            }
            else {
                finish();
            }
        }
    }
    //Button JumptoLogin
    public void JumptoLogin(View view ){
        Intent intent = new Intent(this , LoginActivity.class);
        startActivity(intent);
    }
    //主畫面 上方的Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // MenuInflater 產生選單的類別 , inflate 讀取一個Menu.xml , 依照設計圖xml產生menu物件
        return super.onCreateOptionsMenu(menu);
    }
    // 選單的事件處理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting){  //選單中Settings的事件處理

            //Toast 用法 ： Toast.makeText(Context , String , Toast.LENGTH)
            Toast.makeText(this , "設定" , Toast.LENGTH_LONG).show();

            return true ;
        }
        else if (id == R.id.action_help){ //選單中help的事件處理

            //AlertDialog 用法 : AlertDialog.Builder(Context).setTitle(標題).setMessage(內容).setPositiveButton(Button文字,listener反應).show();
            new AlertDialog.Builder(this )
                    .setTitle("Help")
                    .setMessage("Help content")
                    .setPositiveButton("OK",null)
                    .show();

            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
    //grid.setOnItemClickListener( this ) 所對應到的onItemClick()
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) id) { //position是按下的項目之索引值
            case R.drawable.func_bank1:
                break;
            case R.drawable.func_history1:  //跳到TransActivity
                startActivity( new Intent (this , TransActivity.class));
                break;
            case R.drawable.func_news1:
                break;
            case R.drawable.func_finance1:  //跳到FinanceActivity
                startActivity( new Intent(this , FinanceActivity.class));
                break;
            case R.drawable.func_exit1:    //結束
                finish();
                break;
        }
    }
}
