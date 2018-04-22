package com.example.mom.atm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//為TransactionAdapter加上繼承RecyclerView.Adapter類別 宣告裡面使用的ViewHolder
public class TransactionAdapter extends RecyclerView.Adapter< TransactionAdapter.ViewHolder > { //Adapter是抽象類別 需實作三個必要方法 按alt+enter快速解決

    private List<Transaction> trans;  //集合屬性

    public TransactionAdapter( List<Transaction> trans){   //Constructor , 將傳送的trans參數 存在屬性trans中
        this.trans = trans;
    }
    //在TransactionAdapter中設計一個類別層級的ViewHolder類別 , 繼承RecyclerView.ViewHolder
    //在此類別中 設計一筆交易在畫面上的元件 dateTextView , amountTextView , typeTextView
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dateTextView;
        private final TextView amountTextView;
        private final TextView typeTextView;
        public ViewHolder(View itemView) { //內部類別constructor
            super(itemView);
            dateTextView = itemView.findViewById(R.id.col_date);
            amountTextView = itemView.findViewById(R.id.col_amount);
            typeTextView = itemView.findViewById(R.id.col_type);
        }
    }
    //實作三個方法
    //當RecyclerView需要顯示一列資料 會呼叫Adapter內的這個方法 先取得一個ViewHolder物件
    //此方法要提供可以展示資料的View物件  產生的ViewHolder會在onBindViewHolder()使用
    //透過ViewGroup parent參數取得LayoutInflater來產生View
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate( R.layout.row_trans , parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    //當RecyclerView準備要展示一列特定位置(position)的紀錄時 會呼叫onBindViewHolder()
    //holder物件就是onCreateViewHolder()取得的viewHolder , 此方法只需要將holder物件中的元件設定為想要的內容
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("TRANS" , position +"");
        Transaction tran = trans.get(position);
        holder.dateTextView.setText(tran.getDate());
        holder.amountTextView.setText(tran.getAmount()+"");
        holder.typeTextView.setText(tran.getType()+"");
    }
    //取得目前資料筆數 回傳List<Transaction> trans集合筆數
    @Override
    public int getItemCount() {
        if(trans != null){
            return trans.size();
        }
        else  return 0;
    }
}
