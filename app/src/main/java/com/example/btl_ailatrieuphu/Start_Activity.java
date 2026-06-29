package com.example.btl_ailatrieuphu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.progressview.ProgressView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Class.Question;
import Database.DAO_Question;

public class Start_Activity extends AppCompatActivity {
    private MediaPlayer voiceInGame;
    public static int TongSoLanChoi = 0;
    public void updateTongSoLanChoi(){
        TongSoLanChoi++;
    }
    public static int TongTienThuong = 0;
    public void updateTongTienThuong(){
        TongTienThuong += TienThuong;
    }
    public static int KyLuc = 0;
    public void updateKyLuc(){
        if (currentQuestion>KyLuc){
            KyLuc = currentQuestion;
        }
    }
    DAO_Question daoQuestion;
    TextView tvTime, tvTienThuong, tvQuestion;
    Button btn5050;
    ImageButton imgbtnCall, imgbtnAskTheAudience, imgbtnOnSiteAdvisory, imgbtnEndGame;
    private Handler handler;
    //biến kiểm tra trạng thái quyền trợ giúp xem đã dùng chưa
    private boolean state5050 = true;
    private boolean stateCall = true;
    private boolean stateAskTheAudience = true;
    private boolean stateOnSiteAdvisory = true;
    private void resetQuyenTroGiup(){
        state5050 = true;
        stateCall = true;
        stateAskTheAudience = true;
        stateOnSiteAdvisory = true;
    }

    //tạo biến lưu trữ tiền thưởng trong phiên game, mỗi vòng được tăng 1000 =))
    private int TienThuong = 0;
    private void updateTienThuong(){
        TienThuong+=1000;
        // Sử dụng NumberFormat để định dạng tiền thưởng với dấu phân cách hàng nghìn
        NumberFormat numberFormat = NumberFormat.getInstance();
        tvTienThuong.setText(numberFormat.format(TienThuong));
    }
    private String getTienThuongString(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(TienThuong)+"$";
    }
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    private List<Question> mListQuestion_All = new ArrayList<>();
    private List<Question> mListQuestion_De = new ArrayList<>();
    private List<Question> mListQuestion_Thuong = new ArrayList<>();
    private List<Question> mListQuestion_Kho = new ArrayList<>();
    private static final int MAX_QUESTIONS = 15;  //số lượng câu hỏi tối đa của 1 lần chơi
    private Question mQuestion;
    private int currentQuestion = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        initUi();

        updateTongSoLanChoi();

        resetQuyenTroGiup();
        currentQuestion = 0;

        //xoá sạch ds cho chắc mặc dù là nó trống =))
        mListQuestion_All.clear();
        mListQuestion_De.clear();
        mListQuestion_Thuong.clear();
        mListQuestion_Kho.clear();


        //lấy toàn bộ câu hỏi vào mListQuestion_All
        daoQuestion = new DAO_Question(this);
        mListQuestion_All = daoQuestion.GetAllQuestions();

        //kiểm tra xem mListQuestion_All có câu hỏi không
        if (mListQuestion_All == null || mListQuestion_All.isEmpty()) {
            showDialog("Không có câu hỏi nào!");
            return;
        }


        //phân loại câu hỏi theo độ khó và gán vào 3 list riêng
        for (Question question : mListQuestion_All) {
            switch (question.getDifficulty_level()) {
                case "Dễ":
                    mListQuestion_De.add(question);
                    break;
                case "Thường":
                    mListQuestion_Thuong.add(question);
                    break;
                case "Khó":
                    mListQuestion_Kho.add(question);
                    break;
            }
        }

        //dùng Collections.shuffle để trộn ngẫu nhiên 1 list
        Collections.shuffle(mListQuestion_De);
        Collections.shuffle(mListQuestion_Thuong);
        Collections.shuffle(mListQuestion_Kho);

        //gán giá trị vào câu hỏi đầu tiên để bắt đầu chơi
       setDataQuestion();

       //bắt đầu chạy bộ đếm thời gian
        boDemThoiGian(60);

        //sự kiện chọn đáp án a
        btnAnswerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("A",btnAnswerA);
            }
        });

        //sự kiện chọn đáp án b
        btnAnswerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("B",btnAnswerB);
            }
        });

        //sự kiện chọn đáp án c
        btnAnswerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("C",btnAnswerC);
            }
        });

        //sự kiện chọn đáp án d
        btnAnswerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("D",btnAnswerD);
            }
        });


        //sự kiện click quyền trợ giúp 50 50
        btn5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state5050 = false;
                btn5050.setEnabled(false);
                btn5050.setBackgroundResource(R.drawable.bg_oval_gray);

                //tạo list chứa các đáp án sai
                List<Button> lsDapAnSai = new ArrayList<>();
                if (!mQuestion.getCorrect_answer().equals("A")) {
                    lsDapAnSai.add(btnAnswerA);
                }
                if (!mQuestion.getCorrect_answer().equals("B")) {
                    lsDapAnSai.add(btnAnswerB);
                }
                if (!mQuestion.getCorrect_answer().equals("C")) {
                    lsDapAnSai.add(btnAnswerC);
                }
                if (!mQuestion.getCorrect_answer().equals("D")) {
                    lsDapAnSai.add(btnAnswerD);
                }

                //dùng Collections.shuffle để trộn ngẫu nhiên list đáp án sai
                Collections.shuffle(lsDapAnSai);
                lsDapAnSai.get(0).setText(""); //xoá text đáp án sai 1
                lsDapAnSai.get(0).setEnabled(false); //bỏ quyền click
                lsDapAnSai.get(1).setText(""); //xoá text đáp án sai 2
                lsDapAnSai.get(1).setEnabled(false); //bỏ quyền click
                lsDapAnSai.clear(); //dùng xong r thì xoá dữ liệu đi cho nhẹ =))
            }
        });


        //sự kiện click quyền trợ giúp gọi điện cho người thân
        imgbtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateCall = false;
                imgbtnCall.setEnabled(false);
                imgbtnCall.setBackgroundResource(R.drawable.bg_oval_gray);

                openDialogCall(1);
            }
        });

        //sự kiện click quyền trợ giúp hỏi ý kiến khán giả trong trường quay
        imgbtnAskTheAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateAskTheAudience = false;
                imgbtnAskTheAudience.setEnabled(false);
                imgbtnAskTheAudience.setBackgroundResource(R.drawable.bg_oval_gray);

                int phanTramChoDapAnDung = 40;
                int phanTramChoRandom = 60;

                //list chứa các đáp án enabled = true
                ArrayList<Integer> enabledAnswers = new ArrayList<>();

                //thêm các chỉ số đại diện cho đáp án thoả mãn enabled = true vào list
                if (btnAnswerA.isEnabled()) enabledAnswers.add(0);  // 0 - A
                if (btnAnswerB.isEnabled()) enabledAnswers.add(1);  // 1 - B
                if (btnAnswerC.isEnabled()) enabledAnswers.add(2);  // 2 - C
                if (btnAnswerD.isEnabled()) enabledAnswers.add(3);  // 3 - D

                int[] randomValues = new int[4]; //mảng kết quả cho các đáp án
                int size = enabledAnswers.size();

                if (size > 0) {
                    Random random = new Random();

                    //random giá trị ngẫu nhiên cho các đáp án
                    for (int i = 0; i < size - 1; i++) {
                        int randomValue = random.nextInt(phanTramChoRandom + 1);
                        randomValues[enabledAnswers.get(i)] = randomValue;
                        phanTramChoRandom -= randomValue;
                    }

                    //gán phần trăm còn lại cho đáp án cuối cùng
                    randomValues[enabledAnswers.get(size - 1)] = phanTramChoRandom;
                }

                if (mQuestion.getCorrect_answer().equals("A")){
                    randomValues[0] += phanTramChoDapAnDung;
                } else if (mQuestion.getCorrect_answer().equals("B")) {
                    randomValues[1] += phanTramChoDapAnDung;
                } else if (mQuestion.getCorrect_answer().equals("C")) {
                    randomValues[2] += phanTramChoDapAnDung;
                }else if (mQuestion.getCorrect_answer().equals("D")) {
                    randomValues[3] += phanTramChoDapAnDung;
                }

                openDialogAskTheAudience(randomValues[0],randomValues[1],randomValues[2],randomValues[3]);
            }
        });

        //sự kiện click quyền trợ giúp tư vấn tại chỗ
        imgbtnOnSiteAdvisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateOnSiteAdvisory = false;
                imgbtnOnSiteAdvisory.setEnabled(false);
                imgbtnOnSiteAdvisory.setBackgroundResource(R.drawable.bg_oval_gray);

                openDialogCall(3);
            }
        });

        //sự kiện click dừng cuộc chơi
        imgbtnEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Start_Activity.this); //tạo cấu hình
                builder.setMessage("Bạn muốn dừng cuộc chơi với tiền thưởng " + getTienThuongString() + " ?");  //gán thông báo
                builder.setCancelable(true);  //cho đóng bằng cách nhấn ra vùng overlay

                //khi nhấn thoát thì ra trang chủ
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateTongTienThuong();
                        updateKyLuc();
                        voiceEnd();
                        Intent intent = new Intent(Start_Activity.this, Home_Activity.class);
                        startActivity(intent);
                    }
                });

                // Khi nhấn huỷ thì không làm gì
                builder.setNegativeButton("Huỷ", null);

                AlertDialog alertDialog = builder.create(); //tạo ra dialog vừa cấu hình
                alertDialog.show();  //hiển thị nó lên
            }
        });
    }

    //khởi tạo các biến ở giao diện để dùng
    private void initUi(){
        tvTime = findViewById(R.id.tvTime);
        tvTienThuong = findViewById(R.id.tvTienThuong);
        btn5050 = findViewById(R.id.btn5050);
        imgbtnCall = findViewById(R.id.imgbtnCall);
        imgbtnAskTheAudience = findViewById(R.id.imgbtnAskTheAudience);
        imgbtnOnSiteAdvisory = findViewById(R.id.imgbtnOnSiteAdvisory);
        imgbtnEndGame = findViewById(R.id.imgbtnEndGame);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnAnswerA = findViewById(R.id.btnAnswerA);
        btnAnswerB = findViewById(R.id.btnAnswerB);
        btnAnswerC = findViewById(R.id.btnAnswerC);
        btnAnswerD = findViewById(R.id.btnAnswerD);
    }


    //tạo hàm đếm thời gian với tham số truyền vào là số giây
    private void boDemThoiGian(int giay) {
        //dùng handler để gửi công việc đến luồng chính
        handler = new Handler();

        //lưu trữ số giây còn lại
        //dùng mảng vì biến sử dụng trong Runnable phải là final (hằng), dùng mảng có thể thay đổi giá trị
        final int[] thgianConLai = {giay};

        //tạo Runnable để thực hiện công việc lặp lại
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (thgianConLai[0] >= 0) {
                    //cập nhật giá trị lên TextView
                    tvTime.setText(String.valueOf(thgianConLai[0]));

                    //giảm thời gian còn lại
                    thgianConLai[0]--;

                    //lặp lại công việc sau 1 giây (1000ms)
                    handler.postDelayed(this, 1000);
                } else {
                    //khi hết thời gian, hiển thị dialog game over
                    showDialog("Hết thời gian. Tiền thưởng: "+getTienThuongString());
                }
            }
        };

        //bắt đầu công việc vừa cấu hình ở trên
        handler.post(runnable);
    }


    // Hàm dừng bộ đếm và thực hiện công việc khác
    private void stopCountdown() {
        // Dừng việc gọi Runnable nữa
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);  // Hủy bỏ tất cả các công việc đã lên lịch
        }
    }


    //mở dialog quyền trợ giúp hỏi ý kiến khán giả trong trường quay
    private void openDialogAskTheAudience(int progress_a, int progress_b, int progress_c, int progress_d){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.help_khangiatruongquay);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);  //đặt click vào overlay không thoát được :))

        TextView tv_progress_a = dialog.findViewById(R.id.tv_progress_a);
        TextView tv_progress_b = dialog.findViewById(R.id.tv_progress_b);
        TextView tv_progress_c = dialog.findViewById(R.id.tv_progress_c);
        TextView tv_progress_d = dialog.findViewById(R.id.tv_progress_d);

        ProgressView progressView_a = dialog.findViewById(R.id.progress_a);
        ProgressView progressView_b = dialog.findViewById(R.id.progress_b);
        ProgressView progressView_c = dialog.findViewById(R.id.progress_c);
        ProgressView progressView_d = dialog.findViewById(R.id.progress_d);

        Button btnThanks = dialog.findViewById(R.id.btnThanks);

        tv_progress_a.setText(progress_a + "%");
        tv_progress_b.setText(progress_b + "%");
        tv_progress_c.setText(progress_c + "%");
        tv_progress_d.setText(progress_d + "%");

        progressView_a.setProgress(progress_a);
        progressView_b.setProgress(progress_b);
        progressView_c.setProgress(progress_c);
        progressView_d.setProgress(progress_d);

        btnThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //mở dialog quyền trợ giúp gọi điện thoại cho người thân hoặc tổ tư vấn tại chỗ :)) dùng chung:))))))
    private void openDialogCall(int soLuongToiDa){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.help_goidiennguoithan);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);  //đặt click vào overlay không thoát được :))

        ImageButton imgbtnNguoiThan1 = dialog.findViewById(R.id.imgbtnNguoiThan1);
        ImageButton imgbtnNguoiThan2 = dialog.findViewById(R.id.imgbtnNguoiThan2);
        ImageButton imgbtnNguoiThan3 = dialog.findViewById(R.id.imgbtnNguoiThan3);
        ImageButton imgbtnNguoiThan4 = dialog.findViewById(R.id.imgbtnNguoiThan4);
        ImageButton imgbtnNguoiThan5 = dialog.findViewById(R.id.imgbtnNguoiThan5);
        ImageButton imgbtnNguoiThan6 = dialog.findViewById(R.id.imgbtnNguoiThan6);

        TextView tvTraLoi1 = dialog.findViewById(R.id.tvTraLoi1);
        TextView tvTraLoi2 = dialog.findViewById(R.id.tvTraLoi2);
        TextView tvTraLoi3 = dialog.findViewById(R.id.tvTraLoi3);
        TextView tvTraLoi4 = dialog.findViewById(R.id.tvTraLoi4);
        TextView tvTraLoi5 = dialog.findViewById(R.id.tvTraLoi5);
        TextView tvTraLoi6 = dialog.findViewById(R.id.tvTraLoi6);

        Button btnThanks = dialog.findViewById(R.id.btnThanks);

        final int[] current = {0};  //truy cập vào lớp con bắt buộc phải là final nên phải dùng mảng :))
        Random random = new Random();

        imgbtnNguoiThan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi1.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi1.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi1.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi1.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi1.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi1.setVisibility(View.VISIBLE);
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });

        imgbtnNguoiThan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi2.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi2.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi2.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi2.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi2.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi2.setVisibility(View.VISIBLE);  // Hiển thị đáp án
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });

        imgbtnNguoiThan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi3.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi3.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi3.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi3.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi3.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi3.setVisibility(View.VISIBLE);  // Hiển thị đáp án
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });

        imgbtnNguoiThan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi4.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi4.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi4.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi4.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi4.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi4.setVisibility(View.VISIBLE);  // Hiển thị đáp án
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });

        imgbtnNguoiThan5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi5.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi5.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi5.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi5.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi5.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi5.setVisibility(View.VISIBLE);  // Hiển thị đáp án
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });

        imgbtnNguoiThan6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTraLoi6.setText(mQuestion.getCorrect_answer());
                int xs = random.nextInt(100) + 1;
                if (xs <= 70) {
                    tvTraLoi6.setText(mQuestion.getCorrect_answer());
                } else {
                    String[] all = {"A", "B", "C", "D"};
                    List<String> dapAnSai = new ArrayList<>();
                    for (String da : all) {
                        if (!da.equals(mQuestion.getCorrect_answer())) {
                            dapAnSai.add(da);
                        }
                    }
                    if(xs<=80){
                        tvTraLoi6.setText(dapAnSai.get(0));
                    } else if (xs<=90) {
                        tvTraLoi6.setText(dapAnSai.get(1));
                    } else if (xs<=100){
                        tvTraLoi6.setText(dapAnSai.get(2));
                    }
                }
                tvTraLoi6.setVisibility(View.VISIBLE);  // Hiển thị đáp án
                current[0]++;

                if(current[0] == soLuongToiDa){
                    imgbtnNguoiThan1.setEnabled(false);
                    imgbtnNguoiThan2.setEnabled(false);
                    imgbtnNguoiThan3.setEnabled(false);
                    imgbtnNguoiThan4.setEnabled(false);
                    imgbtnNguoiThan5.setEnabled(false);
                    imgbtnNguoiThan6.setEnabled(false);
                }
            }
        });


        btnThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    //gán giá trị và câu hỏi và đáp án
    private void setDataQuestion() {
        //list.remove xoá phần tử trong danh sách và trả về phần tử vừa xoá
        if (currentQuestion < 5) { //nếu đang ở 5 câu đầu
            if (!mListQuestion_De.isEmpty()) { //nếu ds câu hỏi dễ không trống
                mQuestion = mListQuestion_De.remove(0); //lấy câu hỏi index 0 ở ds dễ và xoá luôn câu đó khỏi ds
            } else if (!mListQuestion_Thuong.isEmpty()) {  //khi ds câu dễ trống và ds câu thường còn
                mQuestion = mListQuestion_Thuong.remove(0); //lấy câu hỏi index 0 ở ds thường và xoá luôn câu đó khỏi ds
            } else if (!mListQuestion_Kho.isEmpty()) {  //khi ds câu thường trống và ds câu khó còn
                mQuestion = mListQuestion_Kho.remove(0); //lấy câu hỏi index 0 ở ds khó và xoá luôn câu đó khỏi ds
            } else {
                voiceEnd();
                updateKyLuc();
                updateTienThuong();
                showDialog("Hết câu hỏi. Tiền thưởng: "+getTienThuongString());
                return;
            }
        } else if (currentQuestion < 10) { //nếu đang ở 5 câu tiếp theo
            if (!mListQuestion_Thuong.isEmpty()) {  //nếu ds thường không trống
                mQuestion = mListQuestion_Thuong.remove(0); //lấy câu hỏi index 0 ở ds thường và xoá luôn câu đó khỏi ds
            } else if (!mListQuestion_Kho.isEmpty()) {  //khi ds câu thường trống và ds câu khó còn
                mQuestion = mListQuestion_Kho.remove(0); //lấy câu hỏi index 0 ở ds khó và xoá luôn câu đó khỏi ds
            } else {
                voiceEnd();
                updateKyLuc();
                updateTienThuong();
                showDialog("Hết câu hỏi. Tiền thưởng: "+getTienThuongString());
                return;
            }
        } else if (currentQuestion < 15) { //khi đang ở 5 câu cuối
            if (!mListQuestion_Kho.isEmpty()) {  //nếu ds khó không trống
                mQuestion = mListQuestion_Kho.remove(0);  //lấy câu hỏi index 0 ở ds khó và xoá luôn câu đó khỏi ds
            } else {
                voiceEnd();
                updateKyLuc();
                updateTienThuong();
                showDialog("Hết câu hỏi. Tiền thưởng: "+getTienThuongString());
                return;
            }
        }

        //reset lại nút chọn câu hỏi (đổi lại thành màu xanh dương và trả quyền được click)
        resetButtons();

        //gán giá trị của câu hỏi và đáp án vào view
        String titleQuestion = "Câu " + (currentQuestion + 1);
        String contentQuestion = mQuestion.getQuestion();
        tvQuestion.setText(titleQuestion + ": " + contentQuestion);
        btnAnswerA.setText(mQuestion.getAnswer_a());
        btnAnswerB.setText(mQuestion.getAnswer_b());
        btnAnswerC.setText(mQuestion.getAnswer_c());
        btnAnswerD.setText(mQuestion.getAnswer_d());
    }


    //kiểm tra đáp án đúng hay sai sau khi chọn
    private void checkAnswer(String selectedAnswer, Button selectedButton) {
        //dừng bộ đếm thời gian nếu nó đang chạy
        stopCountdown();

        //sau khi chọn đáp án thì không cho quyền được click vào nữa
        disableButtons();

        //đổi màu cam đáp án vừa chọn
        selectedButton.setBackgroundResource(R.drawable.btn_answer_orange);

        //tạo độ trễ 1s
        new android.os.Handler().postDelayed(() -> {
            //nếu button chọn là đáp án đúng
            if (selectedAnswer.equals(mQuestion.getCorrect_answer())) {
                voiceTrue();
                selectedButton.setBackgroundResource(R.drawable.btn_answer_green);
                // Chờ 2 giây rồi thực hiện bước tiếp theo
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Thực hiện bước tiếp theo sau 3 giây
                        nextQuestion();
                    }
                }, 2000);
            }
            //nếu button chọn là đáp án sai
            else {
                //thì đổi màu thành màu đỏ
                selectedButton.setBackgroundResource(R.drawable.btn_answer_red);

                //và đồng thời đổi màu đáp án đúng thành màu xanh
                if (mQuestion.getCorrect_answer().equals("A")) {
                    btnAnswerA.setBackgroundResource(R.drawable.btn_answer_green);
                } else if (mQuestion.getCorrect_answer().equals("B")) {
                    btnAnswerB.setBackgroundResource(R.drawable.btn_answer_green);
                } else if (mQuestion.getCorrect_answer().equals("C")) {
                    btnAnswerC.setBackgroundResource(R.drawable.btn_answer_green);
                } else if (mQuestion.getCorrect_answer().equals("D")) {
                    btnAnswerD.setBackgroundResource(R.drawable.btn_answer_green);
                }

                updateTongTienThuong();
                updateKyLuc();
                voiceEnd();
                //hiển thị thông báo thua và trở về trang chủ
                showDialog("You loss. Tiền thưởng: "+getTienThuongString());
            }

        }, 1000);
    }


    //Enabled = false để cho chỉ xem, không cho chọn nhiều đáp án cùng lúc:))
    private void disableButtons() {
        btn5050.setEnabled(false);
        imgbtnCall.setEnabled(false);
        imgbtnAskTheAudience.setEnabled(false);
        imgbtnOnSiteAdvisory.setEnabled(false);
        imgbtnEndGame.setEnabled(false);

        btnAnswerA.setEnabled(false);
        btnAnswerB.setEnabled(false);
        btnAnswerC.setEnabled(false);
        btnAnswerD.setEnabled(false);
    }


    //trả lại quyền click và đổi lại màu nền thành xanh dương ban đầu
    private void resetButtons() {
        btnAnswerA.setBackgroundResource(R.drawable.btn_answer);
        btnAnswerB.setBackgroundResource(R.drawable.btn_answer);
        btnAnswerC.setBackgroundResource(R.drawable.btn_answer);
        btnAnswerD.setBackgroundResource(R.drawable.btn_answer);

        btnAnswerA.setEnabled(true);
        btnAnswerB.setEnabled(true);
        btnAnswerC.setEnabled(true);
        btnAnswerD.setEnabled(true);

        if (state5050){
            btn5050.setEnabled(true);
        }
        if (stateCall){
            imgbtnCall.setEnabled(true);
        }
        if (stateAskTheAudience){
            imgbtnAskTheAudience.setEnabled(true);
        }
        if (stateOnSiteAdvisory){
            imgbtnOnSiteAdvisory.setEnabled(true);
        }
        imgbtnEndGame.setEnabled(true);
    }


    //chuyển câu hỏi
    private void nextQuestion() {
        //cập nhật tiền thưởng (+1000)
        updateTienThuong();

        //dừng bộ đếm thời gian nếu nó đang chạy
        stopCountdown();

        //nếu current (biến đếm ý :)) ) bằng MAX_QUESTIONS (gán là 15 ở đầu) -1 thì dừng và báo win
        //-1 vì current chạy từ 0 :))
        if (currentQuestion == MAX_QUESTIONS - 1) {
            updateTongTienThuong();
            updateKyLuc();
            voiceEnd();
            showDialog("You win! Tiền thưởng: "+getTienThuongString());
        }
        else {
            //tăng biến đếm lên 1 và gán giá trị câu hỏi mới vào giao diện sau 1s delay
            currentQuestion++;
            //cập nhật lại bộ đếm thời gian
            boDemThoiGian(60);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDataQuestion();
                }
            }, 1000);
        }
    }


    //hiện dialog thông báo
    private void showDialog(String message){
        //dừng bộ đếm thời gian nếu nó đang chạy
        stopCountdown();

        //thông báo xảy ra khi hết câu hỏi, thắng, thua nên sau đó sẽ phải gán lại biến đếm = 0
        currentQuestion = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this); //tạo cấu hình
        builder.setMessage(message);  //gán thông báo
        builder.setCancelable(false);  //không cho đóng bằng cách nhấn ra vùng overlay, phải nhấn Thoát :))

        //khi nhấn thoát thì ra trang chủ
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Start_Activity.this, Home_Activity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create(); //tạo ra dialog vừa cấu hình
        alertDialog.show();  //hiển thị nó lên
    }

    //phát nhạc khi trả lời đúng
    private void voiceTrue() {
        if(Home_Activity.dangPhatNhacNen){
            if (voiceInGame == null) {
                voiceInGame = MediaPlayer.create(this, R.raw.cautraloidung);
            }

            voiceInGame.start();

            // dừng và giải phóng MediaPlayer sau khi nhạc phát xong
            voiceInGame.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    voiceInGame.release();
                    voiceInGame = null;
                }
            });
        }
    }

    //phát nhạc khi trả lời sai
    private void voiceEnd() {
        if (Home_Activity.dangPhatNhacNen){
            if (voiceInGame == null) {
                voiceInGame = MediaPlayer.create(this, R.raw.ra_ve);
            }
            voiceInGame.start();

            // dừng và giải phóng MediaPlayer sau khi nhạc phát xong
            voiceInGame.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    voiceInGame.release();
                    voiceInGame = null;
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceInGame != null) {
            voiceInGame.release();
            voiceInGame = null;
        }
    }

}