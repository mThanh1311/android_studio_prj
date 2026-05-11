package com.example.hotelmanagementsystem.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private TextView tvRoomName, tvBranch, tvPrice, tvTotalPreview;
    private EditText edtCustomerName, edtEmail, edtPhone, edtCheckIn, edtCheckOut, edtTotalDays;
    private Button btnConfirmBooking;

    private DatabaseHelper db;
    private SessionManager sessionManager;

    private Room room;
    private int roomId;

    private Calendar checkInCalendar;
    private Calendar checkOutCalendar;

    private NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();

        roomId = getIntent().getIntExtra("room_id", -1);
        room = db.getRoomById(roomId);

        initViews();

        if (room == null) {
            Toast.makeText(this, "Không tìm thấy thông tin phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRoomInfo();
        autoFillUserInfo();
        setupDatePickers();
        setupTotalPreview();
        setupConfirmButton();
    }

    private void initViews() {
        tvRoomName = findViewById(R.id.tvRoomName);
        tvBranch = findViewById(R.id.tvBranch);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotalPreview = findViewById(R.id.tvTotalPreview);

        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtCheckIn = findViewById(R.id.edtCheckIn);
        edtCheckOut = findViewById(R.id.edtCheckOut);
        edtTotalDays = findViewById(R.id.edtTotalDays);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
    }

    private void loadRoomInfo() {
        tvRoomName.setText(room.getName());
        tvBranch.setText(room.getBranchName());
        tvPrice.setText(formatter.format(room.getPrice()) + " VNĐ / đêm");
        tvTotalPreview.setText("Tổng tiền sẽ được tính theo số đêm");
    }

    private void autoFillUserInfo() {
        edtCustomerName.setText(sessionManager.getUserName());
        edtEmail.setText(sessionManager.getUserEmail());
    }

    private void setupDatePickers() {
        edtCheckIn.setOnClickListener(v -> showDatePicker(true));
        edtCheckOut.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isCheckIn) {
        Calendar selectedCalendar = isCheckIn ? checkInCalendar : checkOutCalendar;

        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedCalendar.set(Calendar.YEAR, selectedYear);
                    selectedCalendar.set(Calendar.MONTH, selectedMonth);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    String dateText = formatDate(selectedCalendar);

                    if (isCheckIn) {
                        edtCheckIn.setText(dateText);
                    } else {
                        edtCheckOut.setText(dateText);
                    }

                    calculateDaysFromDates();
                    updateTotalPreview();
                },
                year,
                month,
                day
        );

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private String formatDate(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
    }

    private void calculateDaysFromDates() {
        String checkIn = edtCheckIn.getText().toString().trim();
        String checkOut = edtCheckOut.getText().toString().trim();

        if (checkIn.isEmpty() || checkOut.isEmpty()) {
            return;
        }

        long diffMillis = checkOutCalendar.getTimeInMillis() - checkInCalendar.getTimeInMillis();
        long diffDays = diffMillis / (24 * 60 * 60 * 1000);

        if (diffDays <= 0) {
            Toast.makeText(this, "Ngày trả phòng phải sau ngày nhận phòng", Toast.LENGTH_SHORT).show();
            edtTotalDays.setText("");
            return;
        }

        edtTotalDays.setText(String.valueOf(diffDays));
    }

    private void setupTotalPreview() {
        edtTotalDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotalPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateTotalPreview() {
        String daysText = edtTotalDays.getText().toString().trim();

        if (daysText.isEmpty()) {
            tvTotalPreview.setText("Tổng tiền sẽ được tính theo số đêm");
            return;
        }

        try {
            int totalDays = Integer.parseInt(daysText);

            if (totalDays <= 0) {
                tvTotalPreview.setText("Số đêm phải lớn hơn 0");
                return;
            }

            int totalPrice = totalDays * room.getPrice();
            tvTotalPreview.setText("Tổng thanh toán: " + formatter.format(totalPrice) + " VNĐ");
        } catch (NumberFormatException e) {
            tvTotalPreview.setText("Số đêm không hợp lệ");
        }
    }

    private void setupConfirmButton() {
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void confirmBooking() {
        String customerName = edtCustomerName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String checkIn = edtCheckIn.getText().toString().trim();
        String checkOut = edtCheckOut.getText().toString().trim();
        String daysText = edtTotalDays.getText().toString().trim();

        if (customerName.isEmpty() || email.isEmpty() || phone.isEmpty()
                || checkIn.isEmpty() || checkOut.isEmpty() || daysText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalDays;

        try {
            totalDays = Integer.parseInt(daysText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số đêm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalDays <= 0) {
            Toast.makeText(this, "Số đêm phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalPrice = totalDays * room.getPrice();

        int bookingId = db.createBookingAndReturnId(
                sessionManager.getUserId(),
                roomId,
                customerName,
                phone,
                checkIn,
                checkOut,
                totalDays,
                totalPrice
        );

        if (bookingId != -1) {
            sessionManager.saveLastBookingId(bookingId);
            showBookingSuccessDialog(bookingId, customerName, phone, checkIn, checkOut, totalDays, totalPrice);
        } else {
            Toast.makeText(this, "Đặt phòng thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBookingSuccessDialog(int bookingId, String customerName, String phone,
                                          String checkIn, String checkOut, int totalDays, int totalPrice) {
        String message =
                "Mã booking: #" + bookingId + "\n" +
                        "Phòng: " + room.getName() + "\n" +
                        "Chi nhánh: " + room.getBranchName() + "\n" +
                        "Người đặt: " + customerName + "\n" +
                        "Số điện thoại: " + phone + "\n" +
                        "Check-in: " + checkIn + "\n" +
                        "Check-out: " + checkOut + "\n" +
                        "Số đêm: " + totalDays + "\n" +
                        "Tổng tiền: " + formatter.format(totalPrice) + " VNĐ";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đặt phòng thành công!");
        builder.setMessage(message);

        builder.setPositiveButton("Xem booking", (dialog, which) -> {
            Intent intent = new Intent(BookingActivity.this, BookingDetailActivity.class);
            intent.putExtra("booking_id", bookingId);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Về trang chủ", (dialog, which) -> {
            Intent intent = new Intent(BookingActivity.this, UserHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        builder.setNeutralButton("Thông báo", (dialog, which) -> {
            Intent intent = new Intent(BookingActivity.this, NotificationActivity.class);
            startActivity(intent);
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}