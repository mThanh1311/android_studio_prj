package com.example.hotelmanagementsystem.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.util.Calendar;

public class AccountInfoActivity extends AppCompatActivity {

    private TextView btnBackAccount;
    private EditText edtName, edtEmail, edtPhone, edtOldPassword, edtNewPassword;
    private RadioButton rbMale, rbFemale;
    private Button btnSaveAccount;
    private SessionManager sessionManager;
    private String selectedDob = "";
    private com.google.android.material.imageview.ShapeableImageView imgAccountAvatar;
    private TextView tvAccountNameHeader, tvAccountEmailHeader;
    private TextView btnEditInfo, tvDob;
    private RadioGroup rgAccountGender;
    private Button btnSaveInfo, btnChangePassword, btnSavePassword;
    private LinearLayout layoutPasswordFields;
    private EditText edtConfirmNewPassword;
    private boolean isEditingInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#21A8F3"));

        sessionManager = new SessionManager(this);

        initViews();
        setupData();
        setupEvents();

        BottomNavHelper.setup(this, "setting");
    }

    private void initViews() {
        btnBackAccount = findViewById(R.id.btnBackAccount);

        edtName = findViewById(R.id.edtAccountName);
        edtEmail = findViewById(R.id.edtAccountEmail);
        edtPhone = findViewById(R.id.edtAccountPhone);
        tvDob = findViewById(R.id.tvAccountDob);

        rbMale = findViewById(R.id.rbAccountMale);
        rbFemale = findViewById(R.id.rbAccountFemale);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);

        imgAccountAvatar = findViewById(R.id.imgAccountAvatar);
        tvAccountNameHeader = findViewById(R.id.tvAccountNameHeader);
        tvAccountEmailHeader = findViewById(R.id.tvAccountEmailHeader);

        btnEditInfo = findViewById(R.id.btnEditInfo);

        edtName = findViewById(R.id.edtAccountName);
        edtEmail = findViewById(R.id.edtAccountEmail);
        edtPhone = findViewById(R.id.edtAccountPhone);
        tvDob = findViewById(R.id.tvAccountDob);

        rgAccountGender = findViewById(R.id.rgAccountGender);
        rbMale = findViewById(R.id.rbAccountMale);
        rbFemale = findViewById(R.id.rbAccountFemale);

        btnSaveInfo = findViewById(R.id.btnSaveInfo);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        layoutPasswordFields = findViewById(R.id.layoutPasswordFields);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);
    }

    private void setupData() {
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        String gender = sessionManager.getGender();

        tvAccountNameHeader.setText(name);
        tvAccountEmailHeader.setText(email);

        edtName.setText(name);
        edtEmail.setText(email);

        if ("female".equalsIgnoreCase(gender)) {
            rbFemale.setChecked(true);
            imgAccountAvatar.setImageResource(R.drawable.avatar_female);
        } else {
            rbMale.setChecked(true);
            imgAccountAvatar.setImageResource(R.drawable.avatar_male);
        }

        // Nếu bạn chưa có phone/dob trong SessionManager thì để trống trước
        edtPhone.setText("");
        tvDob.setText("Chưa cập nhật");
        tvDob.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));

        setInfoEditing(false);
    }

    private void setupEvents() {
        btnBackAccount.setOnClickListener(v -> finish());

        btnEditInfo.setOnClickListener(v -> {
            setInfoEditing(!isEditingInfo);
        });

        tvDob.setOnClickListener(v -> {
            if (isEditingInfo) {
                showDatePicker();
            }
        });

        btnSaveInfo.setOnClickListener(v -> saveAccountInfo());

        btnChangePassword.setOnClickListener(v -> {
            if (layoutPasswordFields.getVisibility() == View.VISIBLE) {
                layoutPasswordFields.setVisibility(View.GONE);
            } else {
                layoutPasswordFields.setVisibility(View.VISIBLE);
            }
        });

        btnSavePassword.setOnClickListener(v -> saveNewPassword());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR) - 18;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDob = String.format(
                            java.util.Locale.getDefault(),
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                    );

                    tvDob.setText(selectedDob);
                    tvDob.setTextColor(android.graphics.Color.parseColor("#111827"));
                },
                year,
                month,
                day
        );

        dialog.show();
    }

    private void setInfoEditing(boolean enabled) {
        isEditingInfo = enabled;

        edtName.setEnabled(enabled);
        edtPhone.setEnabled(enabled);
        tvDob.setEnabled(enabled);

        rbMale.setEnabled(enabled);
        rbFemale.setEnabled(enabled);
        edtEmail.setEnabled(false);

        btnSaveInfo.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnEditInfo.setText(enabled ? "Đang sửa" : "Chỉnh sửa");
    }

    private void saveAccountInfo() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String gender = rbFemale.isChecked() ? "female" : "male";

        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập họ tên");
            edtName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return;
        }

        if (selectedDob.isEmpty() && tvDob.getText().toString().equals("Chưa cập nhật")) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }

        tvAccountNameHeader.setText(name);

        if ("female".equalsIgnoreCase(gender)) {
            imgAccountAvatar.setImageResource(R.drawable.avatar_female);
        } else {
            imgAccountAvatar.setImageResource(R.drawable.avatar_male);
        }

        Toast.makeText(this, "Đã cập nhật thông tin tài khoản", Toast.LENGTH_SHORT).show();

        setInfoEditing(false);
    }

    private void saveNewPassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            edtOldPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            edtOldPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            edtNewPassword.setError("Mật khẩu mới phải có ít nhất 6 ký tự");
            edtNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmNewPassword.setError("Mật khẩu xác nhận không khớp");
            edtConfirmNewPassword.requestFocus();
            return;
        }

        Toast.makeText(this, "Đã cập nhật mật khẩu mô phỏng", Toast.LENGTH_SHORT).show();

        edtOldPassword.setText("");
        edtNewPassword.setText("");
        edtConfirmNewPassword.setText("");
        layoutPasswordFields.setVisibility(View.GONE);
    }
}