package com.example.words2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class searchActivity extends AppCompatActivity {
    private MyDatebase dbHelper;
    private NewsTitleFragment.NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbHelper=new MyDatebase(this,"text_db",null,2);


        Button search=(Button)findViewById(R.id.search);
        final EditText editText = (EditText)findViewById(R.id.search_edittext);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1=editText.getText().toString();
                String selectionArgs[] = new String[] { "%"+str1+"%" };
                String selection = "Mean LIKE ?" ;//Word LIKE ?
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("word",null,selection,selectionArgs,null,null,null);
                if(str1.length()>0) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("result",str1);
                    Intent intent=new Intent(searchActivity.this,MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else
                    Toast.makeText(searchActivity.this,"没有找到",Toast.LENGTH_LONG).show();
            }
        });
        Button button1=(Button)findViewById(R.id.back);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(searchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
