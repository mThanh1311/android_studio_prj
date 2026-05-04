package com.example.hotelmanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private ArrayList<Booking> bookingList;
    private boolean showCancelButton;
    private DatabaseHelper db;

    public BookingAdapter(Context context, ArrayList<Booking> bookingList, boolean showCancelButton) {
        this.context = context;
        this.bookingList = bookingList;
        this.showCancelButton = showCancelButton;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        holder.tvRoomName.setText(booking.getRoomName());
        holder.tvCustomerName.setText("Khách: " + booking.getCustomerName());
        holder.tvPhone.setText("SĐT: " + booking.getPhone());
        holder.tvDate.setText("Ngày: " + booking.getCheckIn() + " - " + booking.getCheckOut());
        holder.tvTotal.setText("Tổng tiền: " + formatter.format(booking.getTotalPrice()) + " VNĐ");
        holder.tvStatus.setText("Trạng thái: " + booking.getStatus());

        if (showCancelButton && booking.getStatus().equals("booked")) {
            holder.btnCancel.setVisibility(View.VISIBLE);

            holder.btnCancel.setOnClickListener(v -> {
                boolean success = db.cancelBooking(booking.getId());

                if (success) {
                    Booking updatedBooking = new Booking(
                            booking.getId(),
                            booking.getUserId(),
                            booking.getRoomId(),
                            booking.getRoomName(),
                            booking.getCustomerName(),
                            booking.getPhone(),
                            booking.getCheckIn(),
                            booking.getCheckOut(),
                            booking.getTotalDays(),
                            booking.getTotalPrice(),
                            "cancelled"
                    );

                    bookingList.set(position, updatedBooking);
                    notifyDataSetChanged();

                    Toast.makeText(context, "Đã huỷ booking", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Huỷ booking thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvCustomerName, tvPhone, tvDate, tvTotal, tvStatus;
        Button btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}