package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Class.Question;

public class DAO_Question {
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;
    private Context context;

    public DAO_Question(Context context) {
        this.context = context;
        dbHelper = new SQLiteHelper(context);  //thực thi tạo database
        db = dbHelper.getWritableDatabase(); //cho phép ghi dữ liệu vào database
    }

    //thêm dữ liệu
    public int InsertQuestion(Question question){
        ContentValues values = new ContentValues();  //tạo đối tượng chứa dữ liệu
        //đưa dữ liệu vào đối tượng chứa
        values.put("id", question.getId());
        values.put("question", question.getQuestion());
        values.put("answer_a", question.getAnswer_a());
        values.put("answer_b", question.getAnswer_b());
        values.put("answer_c", question.getAnswer_c());
        values.put("answer_d", question.getAnswer_d());
        values.put("correct_answer", question.getCorrect_answer());
        values.put("difficulty_level", question.getDifficulty_level());

        //thực thi insert
        long ketqua = db.insert("Question",null,values);

        //kiểm tra kết quả insert
        if(ketqua <= 0){
            return -1; //insert thất bại
        }
        return 1; //insert thành công
    }

    // Lấy toàn bộ câu hỏi dưới dạng List<Question>
    public List<Question> GetAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        // Tạo con trỏ đọc bảng dữ liệu
        Cursor c = db.query("Question",
                null,
                null,
                null,
                null,
                null,
                null);
        c.moveToFirst(); // Di chuyển con trỏ về bản ghi đầu tiên
        // Đọc dữ liệu
        while (!c.isAfterLast()) { // isAfterLast trả về true khi vượt quá dòng cuối cùng
            Question question = new Question(); // Tạo đối tượng mới để chứa dữ liệu
            question.setId(c.getString(0)); // Đọc dữ liệu trường id
            question.setQuestion(c.getString(1)); // Đọc dữ liệu trường question
            question.setAnswer_a(c.getString(2)); // Đọc dữ liệu trường answer_a
            question.setAnswer_b(c.getString(3)); // Đọc dữ liệu trường answer_b
            question.setAnswer_c(c.getString(4)); // Đọc dữ liệu trường answer_c
            question.setAnswer_d(c.getString(5)); // Đọc dữ liệu trường answer_d
            question.setCorrect_answer(c.getString(6)); // Đọc dữ liệu trường correct_answer
            question.setDifficulty_level(c.getString(7)); // Đọc dữ liệu trường difficulty_level

            // Thêm đối tượng Question vào danh sách
            questionList.add(question);

            c.moveToNext(); // Chuyển con trỏ sang dòng tiếp theo
        }
        c.close(); // Đóng con trỏ
        return questionList; // Trả về danh sách
    }


    //hiển thị dữ liệu dạng chuỗi
    public List<String> GetAllQuestionToString(){
        List<String> ls = new ArrayList<>();
        //Tạo con trỏ đọc bảng dữ liệu
        Cursor c = db.query("Question",
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
            Question question = new Question(); //tạo đối tượng mới để chứa dữ liệu
            question.setId(c.getString(0)); //đọc dữ liệu trường id và đưa vào đối tượng
            question.setQuestion(c.getString(1)); //đọc dữ liệu trường question và đưa vào đối tượng
            question.setAnswer_a(c.getString(2)); //đọc dữ liệu trường answer_a và đưa vào đối tượng
            question.setAnswer_b(c.getString(3)); //đọc dữ liệu trường answer_b và đưa vào đối tượng
            question.setAnswer_c(c.getString(4)); //đọc dữ liệu trường answer_c và đưa vào đối tượng
            question.setAnswer_d(c.getString(5)); //đọc dữ liệu trường answer_d và đưa vào đối tượng
            question.setCorrect_answer(c.getString(6)); //đọc dữ liệu trường correct_answer và đưa vào đối tượng
            question.setDifficulty_level(c.getString(7)); //đọc dữ liệu trường difficulty_level và đưa vào đối tượng

            String text = "Mã câu hỏi: " + question.getId() + "\n"
                        + "Câu hỏi: " + question.getQuestion() + "\n"
                        + "Đáp án A: " + question.getAnswer_a() + "\n"
                        + "Đáp án B: " + question.getAnswer_b() + "\n"
                        + "Đáp án C: " + question.getAnswer_c() + "\n"
                        + "Đáp án D: " + question.getAnswer_d() + "\n"
                        + "Đáp án đúng: " + question.getCorrect_answer() + "\n"
                        + "Độ khó: " + question.getDifficulty_level();
            ls.add(text);
            c.moveToNext();  //chuyển con trỏ sang dòng tiếp theo
        }
        c.close();
        return ls;
    }


    //xoá câu hỏi
    public int DeleteQuestion(String id){
        //thực thi delete
        int ketqua = db.delete("Question","id=?", new String[]{id});

        //kiểm tra kết quả delete
        if(ketqua <= 0){
            return -1; //delete thất bại
        }
        return 1; //delete thành công
    }


    //sửa dữ liệu
    public int UpdateQuestion(Question question){
        ContentValues values = new ContentValues();  //tạo đối tượng chứa dữ liệu
        //đưa dữ liệu vào đối tượng chứa
        values.put("id", question.getId());
        values.put("question", question.getQuestion());
        values.put("answer_a", question.getAnswer_a());
        values.put("answer_b", question.getAnswer_b());
        values.put("answer_c", question.getAnswer_c());
        values.put("answer_d", question.getAnswer_d());
        values.put("correct_answer", question.getCorrect_answer());
        values.put("difficulty_level", question.getDifficulty_level());

        //thực thi update
        long ketqua = db.update("Question",values,"id=?",
                new String[]{question.getId()});

        //kiểm tra kết quả update
        if(ketqua <= 0){
            return -1; //update thất bại
        }
        return 1; //update thành công
    }

    // Tìm kiếm câu hỏi theo nội dung câu hỏi
    public List<String> SearchQuestions(String keyword) {
        List<String> ls = new ArrayList<>();

        // Câu truy vấn SQL với điều kiện LIKE để tìm kiếm theo nội dung câu hỏi
        String query = "SELECT * FROM Question WHERE question LIKE ?";

        // Tạo con trỏ và thực thi câu truy vấn
        Cursor c = db.rawQuery(query, new String[]{"%" + keyword + "%"});

        c.moveToFirst(); // Di chuyển con trỏ về bản ghi đầu tiên

        // Đọc dữ liệu
        while (!c.isAfterLast()) {
            Question question = new Question(); // Tạo đối tượng mới để chứa dữ liệu
            question.setId(c.getString(0)); // Đọc dữ liệu trường id
            question.setQuestion(c.getString(1)); // Đọc dữ liệu trường question
            question.setAnswer_a(c.getString(2)); // Đọc dữ liệu trường answer_a
            question.setAnswer_b(c.getString(3)); // Đọc dữ liệu trường answer_b
            question.setAnswer_c(c.getString(4)); // Đọc dữ liệu trường answer_c
            question.setAnswer_d(c.getString(5)); // Đọc dữ liệu trường answer_d
            question.setCorrect_answer(c.getString(6)); // Đọc dữ liệu trường correct_answer
            question.setDifficulty_level(c.getString(7)); // Đọc dữ liệu trường difficulty_level

            String text = "Mã câu hỏi: " + question.getId() + "\n"
                    + "Câu hỏi: " + question.getQuestion() + "\n"
                    + "Đáp án A: " + question.getAnswer_a() + "\n"
                    + "Đáp án B: " + question.getAnswer_b() + "\n"
                    + "Đáp án C: " + question.getAnswer_c() + "\n"
                    + "Đáp án D: " + question.getAnswer_d() + "\n"
                    + "Đáp án đúng: " + question.getCorrect_answer() + "\n"
                    + "Độ khó: " + question.getDifficulty_level();
            ls.add(text);

            c.moveToNext(); // Chuyển con trỏ sang dòng tiếp theo
        }

        c.close(); // Đóng con trỏ
        return ls; // Trả về danh sách câu hỏi tìm được
    }

}
