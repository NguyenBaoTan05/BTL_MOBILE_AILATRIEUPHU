package com.example.btl_ailatrieuphu;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Class.Question;
import Database.DAO_Question;

public class Admin_Activity extends AppCompatActivity {

    EditText et_IdCauHoi, et_ndungCauHoi, et_DapAnA, et_DapAnB, et_DapAnC, et_DapAnD, et_TimKiem;
    Spinner spinner_DapAnDung, spinner_DoKho;
    Button btn_LamMoi, btn_Them, btn_Sua, btn_Xoa;
    ListView lv_questions;
    ArrayAdapter<String> arrayAdapter;
    DAO_Question daoQuestion;
    List<String> list = new ArrayList<>();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        //ánh xạ
        et_IdCauHoi = findViewById(R.id.et_IdCauHoi);
        et_ndungCauHoi = findViewById(R.id.et_ndungCauHoi);
        et_DapAnA = findViewById(R.id.et_DapAnA);
        et_DapAnB = findViewById(R.id.et_DapAnB);
        et_DapAnC = findViewById(R.id.et_DapAnC);
        et_DapAnD = findViewById(R.id.et_DapAnD);
        spinner_DapAnDung = findViewById(R.id.spinner_DapAnDung);
        spinner_DoKho = findViewById(R.id.spinner_DoKho);
        btn_LamMoi = findViewById(R.id.btn_LamMoi);
        btn_Them = findViewById(R.id.btn_Them);
        btn_Sua = findViewById(R.id.btn_Sua);
        btn_Xoa = findViewById(R.id.btn_Xoa);
        et_TimKiem = findViewById(R.id.et_TimKiem);
        lv_questions = findViewById(R.id.lv_questions);

        //khởi tạo biến
        context = this;

        //hiển thị dữ liệu khi mới vào chương trình
        getAllQuestions();

        //btn làm mới
        btn_LamMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllQuestions();
            }
        });

        //btn thêm câu hỏis
        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question(); //tạo đối tượng chứa dữ liệu người dùng nhập
                //đưa dữ liệu vào đối tượng
                q.setId(et_IdCauHoi.getText().toString());
                q.setQuestion(et_ndungCauHoi.getText().toString());
                q.setAnswer_a(et_DapAnA.getText().toString());
                q.setAnswer_b(et_DapAnB.getText().toString());
                q.setAnswer_c(et_DapAnC.getText().toString());
                q.setAnswer_d(et_DapAnD.getText().toString());
                q.setCorrect_answer(spinner_DapAnDung.getSelectedItem().toString());
                q.setDifficulty_level(spinner_DoKho.getSelectedItem().toString());
                //gọi hàm insert
                int kq = daoQuestion.InsertQuestion(q);
                if (kq == -1){
                    Toast.makeText(context, "Insert thất bại",Toast.LENGTH_LONG).show();
                } else if (kq == 1) {
                    Toast.makeText(context, "Insert thành công",Toast.LENGTH_LONG).show();
                }
                getAllQuestions();
            }
        });

        //btn xoá câu hỏi
        btn_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_IdCauHoi.getText().toString();
                int kq = daoQuestion.DeleteQuestion(id);
                if (kq == -1){
                    Toast.makeText(context, "Delete thất bại",Toast.LENGTH_LONG).show();
                } else if (kq == 1) {
                    Toast.makeText(context, "Delete thành công",Toast.LENGTH_LONG).show();
                }
                getAllQuestions();
            }
        });

        //sk btn sửa thông tin câu hỏi
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question(); //tạo đối tượng chứa dữ liệu người dùng nhập
                //đưa dữ liệu vào đối tượng
                q.setId(et_IdCauHoi.getText().toString());
                q.setQuestion(et_ndungCauHoi.getText().toString());
                q.setAnswer_a(et_DapAnA.getText().toString());
                q.setAnswer_b(et_DapAnB.getText().toString());
                q.setAnswer_c(et_DapAnC.getText().toString());
                q.setAnswer_d(et_DapAnD.getText().toString());
                q.setCorrect_answer(spinner_DapAnDung.getSelectedItem().toString());
                q.setDifficulty_level(spinner_DoKho.getSelectedItem().toString());
                //gọi hàm update
                int kq = daoQuestion.UpdateQuestion(q);
                if (kq == -1){
                    Toast.makeText(context, "Update thất bại",Toast.LENGTH_LONG).show();
                } else if (kq == 1) {
                    Toast.makeText(context, "Update thành công",Toast.LENGTH_LONG).show();
                }
                getAllQuestions();
            }
        });

        //sự kiện khi thay đổi text trong et tìm kiếm
        et_TimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //lấy từ khoá tìm kiếm
                String keyword = charSequence.toString();

                //gọi phương thức tìm kiếm với từ khoá vừa lấy ở treên
                List<String> resultList = daoQuestion.SearchQuestions(keyword);

                //hiển thị ds mới lên listview
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Admin_Activity.this, android.R.layout.simple_list_item_1, resultList);
                lv_questions.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //sự kiện click vào item lấy data lên et
        lv_questions.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy item được chọn
            String selectedItem = list.get(position);

            // Tách từng dòng trong chuỗi bằng cách sử dụng ký tự xuống dòng "\n"
            String[] lines = selectedItem.split("\n");
            if (lines.length == 8) { // Đảm bảo đủ 8 dòng dữ liệu
                // Loại bỏ phần tiêu đề (e.g., "Mã câu hỏi: ") và chỉ lấy giá trị
                et_IdCauHoi.setText(lines[0].replace("Mã câu hỏi: ", "").trim());
                et_ndungCauHoi.setText(lines[1].replace("Câu hỏi: ", "").trim());
                et_DapAnA.setText(lines[2].replace("Đáp án A: ", "").trim());
                et_DapAnB.setText(lines[3].replace("Đáp án B: ", "").trim());
                et_DapAnC.setText(lines[4].replace("Đáp án C: ", "").trim());
                et_DapAnD.setText(lines[5].replace("Đáp án D: ", "").trim());
                spinner_DapAnDung.setSelection(getSpinnerIndex(spinner_DapAnDung, lines[6].replace("Đáp án đúng: ", "").trim()));
                spinner_DoKho.setSelection(getSpinnerIndex(spinner_DoKho, lines[7].replace("Độ khó: ", "").trim()));
            } else {
                // Nếu dữ liệu không đúng định dạng, thông báo lỗi
                Toast.makeText(context, "Dữ liệu không đúng định dạng!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAllQuestions(){
        //hiển thị dữ liệu
        list.clear();
        daoQuestion = new DAO_Question(context);  //tạo csdl và bảng dữ liệu
        list = daoQuestion.GetAllQuestionToString();  //lấy dữ liệu
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        lv_questions.setAdapter(arrayAdapter);
    }


    // Hàm hỗ trợ tìm index của giá trị trong Spinner
    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0; // Mặc định trả về vị trí đầu tiên nếu không tìm thấy
    }

}
