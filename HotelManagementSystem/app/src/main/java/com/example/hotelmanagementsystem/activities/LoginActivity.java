package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private DatabaseHelper db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        boolean isValid = db.checkUser(email, password);

        if (isValid) {
            int userId = db.getUserId(email);
            String userName = db.getUserName(email);
            String role = db.getUserRole(email);
            String gender = db.getUserGender(email);

            sessionManager.saveLogin(userId, userName, email, role, gender);

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            if ("admin".equals(role)) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }

            finish();
        } else {
            showRegisterSuggestionDialog();
        }
    }

    private void showRegisterSuggestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bạn chưa có tài khoản?");
        builder.setMessage(
                "Email hoặc mật khẩu chưa chính xác. Nếu bạn chưa phải là thành viên, hãy đăng ký tài khoản để nhận ưu đãi hấp dẫn, tích điểm thành viên và quản lý lịch sử đặt phòng dễ dàng hơn."
        );

        builder.setPositiveButton("Đăng ký ngay", (dialog, which) -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        builder.setNegativeButton("Thử lại", (dialog, which) -> {
            dialog.dismiss();
            edtPassword.setText("");
            edtPassword.requestFocus();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}