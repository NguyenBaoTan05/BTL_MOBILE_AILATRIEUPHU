package Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.example.btl_ailatrieuphu.Login_Activity;
import java.util.ArrayList;
import java.util.List;
import Class.Account;

public class DAO_Account {
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private Context context;

    public DAO_Account(Context context) {
        this.context = context;
        dbHelper = new SQLiteHelper(context);  //thực thi tạo database
        db = dbHelper.getWritableDatabase(); //cho phép ghi dữ liệu vào database
    }

    //hiển thị dữ liệu dạng chuỗi
    public int Login(String username, String password) {
        //Tạo con trỏ đọc bảng dữ liệu
        Cursor c = db.query("Account",
                null,
                null,
                null,
                null,
                null,
                null);
        c.moveToFirst(); //di chuyển con trỏ về bản ghi đầu tiên
        //đọc
        while (c.isAfterLast() == false) //isAfterLast trả về true khi vượt quá dòng cuối cùng
        {
            if (c.getString(0).equals(username) && c.getString(1).equals(password))
            {
                return 1;
            }
            c.moveToNext();  //chuyển con trỏ sang dòng tiếp theo
        }
        c.close();
        return -1;
    }
}