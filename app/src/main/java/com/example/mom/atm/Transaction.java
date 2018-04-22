package com.example.mom.atm;

public class Transaction {  //交易類別
    String account;
    String date;
    int amount;
    int type;
    //Constructor  按command+N 選擇Constructor 並選擇四個屬性(attribute)
    public Transaction(String account, String date, int amount, int type) {
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }
    //一個合法的JavaBean類別 需要為屬性設計setter 跟 getter方法  按command+N 選擇setter 跟 Getter
    public String getAccount() {
        return account;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setType(int type) {
        this.type = type;
    }
}
