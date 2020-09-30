package com.cleaner.booster.phone.repairer.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class Db extends SQLiteOpenHelper {

    private static final String databaseName = "myDb";
    private static final String tableName = "apps_table";
    private static final String id = "id";
    private static final String pkgPath = "pkg_path";
    Context context;

    public Db(@Nullable Context context) {
        super(context, databaseName, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT,pkg_path TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    public boolean insertPkgPath(String pkgPath) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Db.pkgPath, pkgPath);

        long isInsert = db.insert(tableName, null, values);
        return isInsert != -1;
    }

    public boolean deletePkgPath(String pkgPath) {
        SQLiteDatabase db = getWritableDatabase();

        String[] args = new String[]{pkgPath};
        long isDeleted = db.delete(tableName, Db.pkgPath + "=?", args);

        return isDeleted != -1;
    }

    public Cursor getAllPkg() {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("select * from " + tableName, null, null);

    }

    public Cursor getPkg(String pkgPath) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("select pkg_path from " + tableName + " WHERE pkg_path LIKE ?", new String[]{pkgPath});
    }

}
