package com.example.btl_ailatrieuphu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import Database.DAO_Account;

public class Login_Activity extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    ImageButton imgbtnEye;
    Button btnLogin;
    DAO_Account daoAccount;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //ánh xạ
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        imgbtnEye = findViewById(R.id.imgbtnEye);
        btnLogin = findViewById(R.id.btnLogin);

        context = this;
        daoAccount = new DAO_Account(context);

        //sự kiện ẩn hiện mật khẩu
        imgbtnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái hiện tại của kiểu nhập
                if (txtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Nếu mật khẩu đang bị ẩn, hiển thị mật khẩu
                    txtPassword.setTransformationMethod(null); // Loại bỏ chế độ mật khẩu
                    imgbtnEye.setBackgroundResource(R.drawable.icon_eye); // Đổi icon sang mắt mở
                } else {
                    // Nếu mật khẩu đang hiển thị, ẩn mật khẩu
                    txtPassword.setTransformationMethod(new PasswordTransformationMethod()); // Bật chế độ mật khẩu
                    imgbtnEye.setBackgroundResource(R.drawable.icon_eye_closed); // Đổi icon sang mắt đóng
                }

                // Đặt con trỏ vào cuối văn bản sau khi thay đổi kiểu
                txtPassword.setSelection(txtPassword.getText().length());
            }
        });

        //sự kiện btn đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                //kiểm tra trường dữ liệu nhập vào
                if (validateLogin(username, password)) {
                    // hợp lệ--> gọi phương thức đăng nhập
                    int loginResult = daoAccount.Login(username, password);
                    if (loginResult == 1) {
                        Toast.makeText(Login_Activity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Activity.this, Admin_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login_Activity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Phương thức kiểm tra username và password
    private boolean validateLogin(String username, String password) {
        if (TextUtils.isEmpty(username)) {//kiểm tra rỗng và khoảng trắng
            txtUsername.setError("Vui lòng nhập tên đăng nhập!");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Vui lòng nhập mật khẩu!");
            return false;
        }

        // Kiểm tra không có ký tự đặc biệt trong username
        if (!username.matches("[a-zA-Z0-9]+")) {
            txtUsername.setError("Tên đăng nhập không được chứa ký tự đặc biệt!");
            return false;
        }

        return true;
    }
}