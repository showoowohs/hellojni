package demo.ooieueioo.db;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class DbAdapter {

	public static String DirPath = "/A/";
	public static String tSDCardPath;
	public static File tDataPath;
	public static final String DATABASE_NAME = "hi_test";
	public static final int DATABASE_VERSION = 17;
	public static Context context;
	public DatabaseHelper mDbHelper;
	public SQLiteDatabase mDb;

	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
//			tSDCardPath = android.os.Environment.getExternalStorageDirectory()
//					.getAbsolutePath();
//			tDataPath = new File(tSDCardPath + DirPath);
			// File tDataPath2 = new File(tDataPath+"hi_test.db");
			// db_main db = new db_main(this);
//			if (tDataPath.exists() == false) {
//				tDataPath.mkdirs();
//				System.out.println("new a DBdir");
//				//fnCreateDB(db);
//				System.out.println("Run fnCreateDB");
//			} else {
//				System.out.println("hava SDCardPath");
//			}
			fnCreateDB(db);
			System.out.println("000000000------------");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS touch_log");
            onCreate(db);
		}
	}

	public DbAdapter(Context context) {
		this.context = context; // new這個class時，一定要傳入context，因為openOrCreateDatabase是由context底下的method
	}

	public static final void fnCreateDB(SQLiteDatabase db) {
		// MODE_WORLD_WRITEABLE，只能有寫的權限
//		SQLiteDatabase db = context.openOrCreateDatabase(tSDCardPath
//				+ DirPath + "hi_test.db", context.MODE_WORLD_WRITEABLE, null);
		System.out.println("new db ing***************");
		// 建立table
		String sql = "CREATE TABLE touch_log (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
				+ "           touch_time TEXT," + "           message TEXT)";
		db.execSQL(sql);
	}

	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(this.context);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long addlog(String time, String msg) {
		Log.i("addlog()", "time " + time + " message " + msg);
		ContentValues cv = new ContentValues();
		cv.put("touch_time", time);
		cv.put("message", msg);
		return mDb.insert("touch_log", null, cv);
	}

}
