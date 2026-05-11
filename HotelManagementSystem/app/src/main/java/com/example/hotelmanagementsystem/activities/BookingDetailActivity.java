package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.models.Room;

import java.text.NumberFormat;
import java.util.Locale;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView btnBack;

    private ImageView imgBookingHeader;

    private TextView tvBookingCode;
    private TextView tvCustomerName, tvCustomerEmail, tvCustomerPhone;
    private TextView tvRoomName, tvBranchName, tvCheckIn, tvCheckOut, tvNights;
    private TextView tvTotalPrice, tvBookingStatus;

    private DatabaseHelper db;
    private Booking booking;
    private Room room;

    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#21A8F3"));

        db = new DatabaseHelper(this);
        bookingId = getIntent().getIntExtra("booking_id", -1);

        initViews();
        setupEvents();
        loadBookingDetail();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        imgBookingHeader = findViewById(R.id.imgBookingHeader);

        tvBookingCode = findViewById(R.id.tvBookingCode);

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvBranchName = findViewById(R.id.tvBranchName);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        tvNights = findViewById(R.id.tvNights);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvBookingStatus = findViewById(R.id.tvBookingStatus);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadBookingDetail() {
        if (bookingId == -1) {
            Toast.makeText(this, "Không tìm thấy mã booking", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        booking = db.getBookingById(bookingId);

        if (booking == null) {
            Toast.makeText(this, "Không tìm thấy thông tin booking", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        room = db.getRoomById(booking.getRoomId());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        tvBookingCode.setText("Mã booking: #" + booking.getId());

        tvCustomerName.setText("Người đặt: " + booking.getCustomerName());
        tvCustomerEmail.setText("Email: " + booking.getCustomerEmail());
        tvCustomerPhone.setText("Số điện thoại: " + booking.getPhone());

        tvRoomName.setText("Phòng: " + booking.getRoomName());

        if (room != null) {
            tvBranchName.setText("Chi nhánh: " + room.getBranchName());
            imgBookingHeader.setImageResource(getRoomImageRes(room.getType(), room.getName()));
        } else {
            tvBranchName.setText("Chi nhánh: ABC Hotel");
            imgBookingHeader.setImageResource(getRoomImageRes("", booking.getRoomName()));
        }

        tvCheckIn.setText("Ngày nhận phòng: " + booking.getCheckIn());
        tvCheckOut.setText("Ngày trả phòng: " + booking.getCheckOut());
        tvNights.setText("Số đêm: " + booking.getTotalDays());

        tvTotalPrice.setText("Tổng thanh toán: " + formatter.format(booking.getTotalPrice()) + " VNĐ");
        tvBookingStatus.setText(getStatusText(booking.getStatus()));
    }

    private int getRoomImageRes(String roomType, String roomName) {
        String text = "";

        if (roomType != null) {
            text += roomType.toLowerCase() + " ";
        }

        if (roomName != null) {
            text += roomName.toLowerCase();
        }

        if (text.contains("standard")) {
            return R.drawable.room_standard;
        }

        if (text.contains("deluxe")) {
            return R.drawable.room_deluxe;
        }

        if (text.contains("suite")) {
            return R.drawable.room_suite;
        }

        if (text.contains("family")) {
            return R.drawable.room_family;
        }

        return R.drawable.room_deluxe;
    }

    private String getStatusText(String status) {
        if ("booked".equals(status)) {
            return "Đã xác nhận";
        }

        if ("cancelled".equals(status)) {
            return "Đã huỷ";
        }

        if ("completed".equals(status)) {
            return "Hoàn thành";
        }

        return status;
    }
}