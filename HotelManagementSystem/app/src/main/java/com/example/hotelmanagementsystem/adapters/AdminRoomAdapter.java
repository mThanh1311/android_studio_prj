package com.example.hotelmanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.activities.AddEditRoomActivity;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdminRoomAdapter extends RecyclerView.Adapter<AdminRoomAdapter.AdminRoomViewHolder> {

    private Context context;
    private ArrayList<Room> roomList;
    private DatabaseHelper db;

    public AdminRoomAdapter(Context context, ArrayList<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public AdminRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_room, parent, false);
        return new AdminRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        holder.tvRoomName.setText(room.getName());
        holder.tvRoomType.setText("Loại: " + room.getType());
        holder.tvPrice.setText(formatter.format(room.getPrice()) + " VNĐ / đêm");
        holder.tvCapacity.setText("Sức chứa: " + room.getCapacity() + " người");
        holder.tvStatus.setText("Trạng thái: " + room.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditRoomActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("room_id", room.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            boolean success = db.deleteRoom(room.getId());

            if (success) {
                roomList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xoá phòng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xoá phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class AdminRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomType, tvPrice, tvCapacity, tvStatus;
        Button btnEdit, btnDelete;

        public AdminRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}