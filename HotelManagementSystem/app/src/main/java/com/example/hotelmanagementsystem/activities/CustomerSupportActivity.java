package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class CustomerSupportActivity extends AppCompatActivity {

    private TextView btnBackSupport;
    private EditText edtSupportName, edtSupportPhone, edtSupportContent;
    private Button btnSubmitSupport;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#21A8F3"));

        sessionManager = new SessionManager(this);

        initViews();
        setupData();
        setupEvents();
        BottomNavHelper.setup(this, "home");
    }

    private void initViews() {
        btnBackSupport = findViewById(R.id.btnBackSupport);

        edtSupportName = findViewById(R.id.edtSupportName);
        edtSupportPhone = findViewById(R.id.edtSupportPhone);
        edtSupportContent = findViewById(R.id.edtSupportContent);

        btnSubmitSupport = findViewById(R.id.btnSubmitSupport);
    }

    private void setupData() {
        String name = sessionManager.getUserName();

        if (name != null && !name.trim().isEmpty()) {
            edtSupportName.setText(name);
        }
    }

    private void setupEvents() {
        btnBackSupport.setOnClickListener(v -> finish());

        btnSubmitSupport.setOnClickListener(v -> {
            String name = edtSupportName.getText().toString().trim();
            String phone = edtSupportPhone.getText().toString().trim();
            String content = edtSupportContent.getText().toString().trim();

            if (name.isEmpty()) {
                edtSupportName.setError("Vui lòng nhập họ tên");
                edtSupportName.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                edtSupportPhone.setError("Vui lòng nhập số điện thoại");
                edtSupportPhone.requestFocus();
                return;
            }

            if (content.isEmpty()) {
                edtSupportContent.setError("Vui lòng nhập nội dung cần hỗ trợ");
                edtSupportContent.requestFocus();
                return;
            }

            Toast.makeText(
                    this,
                    "Yêu cầu hỗ trợ đã được ghi nhận. CSKH sẽ liên hệ bạn sớm.",
                    Toast.LENGTH_LONG
            ).show();

            edtSupportContent.setText("");
        });
    }
}