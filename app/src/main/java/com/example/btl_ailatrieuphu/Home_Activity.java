package com.example.btl_ailatrieuphu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class Home_Activity extends AppCompatActivity {

    private MediaPlayer nhacNen;
    public static boolean dangPhatNhacNen = true;
    Button btnStart, btnVote, btnThongKe, btnInfo;
    ImageButton imgbtnLoginAdmin, imgbtnVolume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        imgbtnVolume = findViewById(R.id.imgbtnVolume);
        if (dangPhatNhacNen){
            nhacNen =MediaPlayer.create(Home_Activity.this, R.raw.nhacnen);
            nhacNen.start();
            imgbtnVolume.setBackgroundResource(R.drawable.bg_oval_blue);
        } else {
            imgbtnVolume.setBackgroundResource(R.drawable.bg_oval_gray);
        }

        //sự kiện khi nhấn vào nút âm thanh
        imgbtnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nhacNen == null){
                    nhacNen =MediaPlayer.create(Home_Activity.this, R.raw.nhacnen);
                }

                if (dangPhatNhacNen){
                    nhacNen.pause();
                    nhacNen.seekTo(0);  //đưa nhạc về giây 0
                    dangPhatNhacNen = false;
                    imgbtnVolume.setBackgroundResource(R.drawable.bg_oval_gray);
                }
                else {
                    nhacNen.start();
                    imgbtnVolume.setBackgroundResource(R.drawable.bg_oval_blue);
                    dangPhatNhacNen=true;
                }
            }
        });

        //sự kiện khi nhấn bắt đầu
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Activity.this, Start_Activity.class);
            startActivity(intent);
        });

        //sự kiện khi click imgbtnloginadmin
        imgbtnLoginAdmin = findViewById(R.id.imgbtnLoginAdmin);
        imgbtnLoginAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Activity.this, Login_Activity.class);
            startActivity(intent);
        });

        //sự kiện khi click btnvote
        btnVote = findViewById(R.id.btnVote);
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Home_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.home_danhgia);

                // Thiết lập chiều rộng và chiều cao cho Dialog
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }

                Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                        Toast.makeText(Home_Activity.this,
                                "Cảm ơn bạn đã đánh giá " + ratingBar.getRating() + "⭐ cho chúng tôi!",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        //sự kiện khi click btnthongke
        btnThongKe = findViewById(R.id.btnThongKe);
        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Home_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.home_thongke);

                // Thiết lập chiều rộng và chiều cao cho Dialog
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }

                TextView tvLanChoi = dialog.findViewById(R.id.tvLanChoi);
                TextView tvTienThuong = dialog.findViewById(R.id.tvTienThuong);
                TextView tvKyLuc = dialog.findViewById(R.id.tvKyLuc);

                tvLanChoi.setText(Start_Activity.TongSoLanChoi + " lần");

                NumberFormat numberFormat = NumberFormat.getInstance();
                tvTienThuong.setText(numberFormat.format(Start_Activity.TongTienThuong) + "$");

                tvKyLuc.setText(Start_Activity.KyLuc + "/15 câu");

                Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        //sự kiện khi click btninfo
        btnInfo =findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/watch/giaitrivtv3/329890848072212/"));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nhacNen != null) {
            nhacNen.release(); //giải phóng tài nguyên
            nhacNen = null;    //đặt về null
        }
    }
}