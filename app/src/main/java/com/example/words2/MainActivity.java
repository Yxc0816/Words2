package com.example.words2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDatebase dbHelper;
    private List<News> wordsList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dbHelper=new MyDatebase(this,"text_db",null,2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.insert_menu:
                insert();
                return true;
            case R.id.update_menu:
                update();
                return true;

            case R.id.delate_menu:
                delete();
                return true;
            case R.id.search_menu:
                search();
                return true;
            case R.id.help_menu:
                help();
                return true;

        }
        return true;
    }

    private void help() {
        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("帮助：");
        dialog.setMessage("单词本进行增删改查"+"\n"+"横屏也可使用");
        dialog.setPositiveButton("确定",null).show();
    }


    public void initWords(){
        wordsList.clear();
       SQLiteDatabase db=dbHelper.getWritableDatabase();
        String ins="select * from word";
        Cursor cursor=db.rawQuery(ins,null);
        if(cursor.moveToFirst()){
            do{
                News words=new News();
                String id=cursor.getString(cursor.getColumnIndex("id"));
                words.setTitle(id);
                String exl=cursor.getString(cursor.getColumnIndex("exl"));
                words.setContent(exl);
                String exp=cursor.getString(cursor.getColumnIndex("exp"));
                words.setContent(exp);
                wordsList.add(words);
            }while (cursor.moveToNext());
        }cursor.close();
    }
    private void queryAll() {
        initWords();
    }
    private void delete() {
            LayoutInflater factory= LayoutInflater.from(MainActivity.this);
            final View textEntryView = factory.inflate(R.layout.dialog1,null);
            final EditText editText= (EditText) textEntryView.findViewById(R.id.delete_edittext);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("删除单词");
            builder.setView(textEntryView);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    //既可以使用Sql语句删除，也可以使用使用delete方法删除
                    String strId = editText.getText().toString();
                    String sql = "delete from word where id='" + strId + "'";
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    db.execSQL(sql);
                    //Delete(strId);
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                    queryAll();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void update() {
        LayoutInflater factory= LayoutInflater.from(MainActivity.this);
        final View textEntryView = factory.inflate(R.layout.dialog2,null);
        final EditText editText=(EditText)findViewById(R.id.update_edittext);
        final EditText editTextexl=(EditText)findViewById(R.id.update_exl);
        final EditText editTextexp=(EditText)findViewById(R.id.update_exp);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("修改单词");
        builder.setView(textEntryView);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String string1= editText.getText().toString();
                    String string2= editTextexl.getText().toString();
                    String string3= editTextexp.getText().toString();
                    ContentValues values = new ContentValues();
                    values.put("id", editText.getText().toString());
                    values.put("exl", editTextexl.getText().toString());
                    values.put("exl", editTextexp.getText().toString());
                    db.update("Word", values, "id = ?", new String[]{string1,string2,string3});
                    Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    queryAll();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void insert() {
        LayoutInflater factory= LayoutInflater.from(MainActivity.this);
        final View textEntryView = factory.inflate(R.layout.dialog,null);
        final EditText editTextid = (EditText) textEntryView.findViewById(R.id.inset_edittext);
        final EditText editTextexl = (EditText) textEntryView.findViewById(R.id.inset_exl);
        final EditText editTextexp = (EditText) textEntryView.findViewById(R.id.inset_exp);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("增加单词");
        builder.setView(textEntryView);
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            try{
                String string1= editTextid.getText().toString();
                String string2= editTextexl.getText().toString();
                String string3= editTextexp.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String ins = "insert into word values(?,?,?)";
                db.execSQL(ins,new  String[]{string1,string2,string3});
                Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_LONG).show();
                queryAll();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"添加失败",Toast.LENGTH_LONG).show();
            }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void search() {
        setContentView(R.layout.activity_search);
    }



}
