package com.itheima105.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    // A   1000      900    //事物回滚
    //int i = 1/0;
    //B    500       600
    @BindView(R.id.btn_insert)
    Button btnInsert;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.btn_insert_t)
    Button btnInsertT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_query, R.id.btn_insert_t})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                insert();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_insert_t:
                insertTransaction();
                break;
        }
    }

    //数据库绑定事物
    private void insertTransaction() {
        SQLiteDatabase db = null;
        try{
            DBHelper dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();

            long startTime = System.currentTimeMillis();
            //开启事物
            db.beginTransaction();
            for (int i = 0; i < 1000; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("username","aaa"+i);
                //一旦循环到5的时候,则后续的代码会因为捕获异常而无法执行
                /*if (i == 5){
                    //模拟一个错误
                    int a = 1/0;
                }*/
                db.insert("t_user",null,contentValues);
            }
            db.setTransactionSuccessful();//提交事物,如果循环过程没有异常,插入数据过程中没有异常,则可以提交事物
            long endTime = System.currentTimeMillis();
            long transactionTime = endTime - startTime;
            Log.i("","绑定事物花费时间 = "+transactionTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    private void delete() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("t_user",null,null);
        db.close();

    }

    private void query() {
        ArrayList<String> stringList = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询表中的所有数据
        Cursor cursor = db.query("t_user", new String[]{"username"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            stringList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        for (int i = 0; i < stringList.size(); i++) {
            String content = stringList.get(i);
            Log.i("","content = "+content);
        }
    }

    private void insert() {
        try{
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //插入数据前时间戳
            long timeStart = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("username","aaa"+i);


                //一旦循环到5的时候,则后续的代码会因为捕获异常而无法执行
                /*if (i == 5){
                    //模拟一个错误
                    int a = 1/0;
                }*/

                db.insert("t_user",null,contentValues);
            }
            //插入数据后时间戳
            long timeEnd = System.currentTimeMillis();
            long disTime = timeEnd - timeStart;

            Log.i("","disTime = "+disTime);//17000    153
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
