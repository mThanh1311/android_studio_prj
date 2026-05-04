package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.text.NumberFormat;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private TextView tvRoomName, tvPrice, tvTotalPreview;
    private EditText edtCustomerName, edtPhone, edtCheckIn, edtCheckOut, edtTotalDays;
    private Button btnConfirmBooking;

    private DatabaseHelper db;
    private SessionManager sessionManager;
    private int roomId;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        roomId = getIntent().getIntExtra("room_id", -1);
        room = db.getRoomById(roomId);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotalPreview = findViewById(R.id.tvTotalPreview);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtCheckIn = findViewById(R.id.edtCheckIn);
        edtCheckOut = findViewById(R.id.edtCheckOut);
        edtTotalDays = findViewById(R.id.edtTotalDays);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        if (room == null) {
            finish();
            return;
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvRoomName.setText(room.getName());
        tvPrice.setText(formatter.format(room.getPrice()) + " VNĐ / đêm");
        tvTotalPreview.setText("Tổng tiền sẽ được tính theo số đêm");

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void confirmBooking() {
        String customerName = edtCustomerName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String checkIn = edtCheckIn.getText().toString().trim();
        String checkOut = edtCheckOut.getText().toString().trim();
        String daysText = edtTotalDays.getText().toString().trim();

        if (customerName.isEmpty() || phone.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty() || daysText.isEmpty()) {
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

        boolean success = db.createBooking(
                sessionManager.getUserId(),
                roomId,
                customerName,
                phone,
                checkIn,
                checkOut,
                totalDays,
                totalPrice
        );

        if (success) {
            Toast.makeText(this, "Đặt phòng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đặt phòng thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}