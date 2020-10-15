package com.cleaner.booster.phone.repairer.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

    public void deletePkgAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }

    public Cursor getAllPkg() {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery("select * from " + tableName, null, null);

    }

    public boolean isPkgAvail(String pkgPath) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+tableName,null,null);
        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                String s = cursor.getString(1);
                if (pkgPath.matches(s)) {
                    return true;
                }
            }
        }
            return false;
    }

    public Cursor getPkg(String pkgPath) {

        SQLiteDatabase database = getWritableDatabase();

        return database.rawQuery("select pkg_path from " + tableName + " WHERE pkg_path LIKE ?", new String[]{pkgPath});
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return count;
    }
}
