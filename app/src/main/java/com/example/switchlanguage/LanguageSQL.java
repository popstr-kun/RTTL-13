package com.example.switchlanguage;


import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;

public class LanguageSQL extends SQLiteOpenHelper {

    public LanguageSQL(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 產生資料表的 SQL 寫在這 onCreate
        // 如果 Android 載入時找不到生成的資料庫檔案，就會觸發 onCreate
        //String SQLTable = "CREATE TABLE IF NOT EXISTS" + TableName + "("+""+");";
        //db.execSQL(SQLTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade 則是如果資料庫結構有改變了就會觸發 onUpgrade

    }
}