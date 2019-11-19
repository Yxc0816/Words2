package com.example.words2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatebase extends SQLiteOpenHelper {

    //带全部参数的构造函数，此构造函数必不可少
    public MyDatebase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    public static final String CREATE_WORD="create table word("
            +"id varchar(20) primary key,"
            +"exl varchar(20),"
            +"exp char(30))";
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执
        db.execSQL(CREATE_WORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}