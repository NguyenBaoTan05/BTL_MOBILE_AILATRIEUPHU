package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    //TẠO CSDL
    public SQLiteHelper(Context context){
        super(context, "btl_ailatrieuphu.db", null,2);
    }
    //-----------------------------------------------------------------------------------------
    //QUERY TẠO BẢNG
    public static final String SQL_QUESTION = "create table Question (\n" +
            "    id text primary key,\n" +
            "    question text not null,\n" +
            "    answer_a text not null,\n" +
            "    answer_b text not null,\n" +
            "    answer_c text not null,\n" +
            "    answer_d text not null,\n" +
            "    correct_answer text not null,\n" +
            "    difficulty_level text not null\n" +
            "    )";

    public static final String SQL_ACCOUNT = "create table Account(\n" +
            "   username text primary key,\n" +
            "   password text not null\n" +
            "   )";


    //-----------------------------------------------------------------------------------------
    //CHẠY QUERY TẠO BẢNG
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_QUESTION);
        db.execSQL(SQL_ACCOUNT);
        db.execSQL("insert into Account (username, password) values ('admin', '123')");
    }

    //-----------------------------------------------------------------------------------------
    //XOÁ BẢNG DỮ LIỆU CŨ TẠO BẢNG DỮ LIỆU MỚI
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Question");  //xoá bảng cũ, tạo mới
        db.execSQL("DROP TABLE IF EXISTS Account");  //xoá bảng cũ, tạo mới
        onCreate(db); // Gọi lại onCreate() để tạo bảng mới
    }
}
