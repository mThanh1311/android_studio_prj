package com.example.hotelmanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.activities.RoomDetailActivity;
import com.example.hotelmanagementsystem.models.Room;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private ArrayList<Room> roomList;

    public RoomAdapter(Context context, ArrayList<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        holder.imgRoom.setImageResource(getRoomImage(room));
        holder.tvRoomName.setText(room.getName());
        holder.tvBranch.setText(room.getBranchName());
        holder.tvRating.setText(getRatingText(room));
        holder.tvPrice.setText(formatter.format(room.getPrice()) + "đ / đêm");
        holder.tvRoomMeta.setText(room.getType() + " • " + room.getCapacity() + " người • " + room.getStatus());

        holder.cardRoomRoot.setOnClickListener(v -> openRoomDetail(room));

        holder.tvFavorite.setOnClickListener(v -> {
            holder.tvFavorite.setText("♥");
            Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        });
    }

    private void openRoomDetail(Room room) {
        Intent intent = new Intent(context, RoomDetailActivity.class);
        intent.putExtra("room_id", room.getId());
        context.startActivity(intent);
    }

    private int getRoomImage(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("standard")) {
            return R.drawable.room_standard;
        } else if (type.contains("deluxe")) {
            return R.drawable.room_deluxe;
        } else if (type.contains("suite")) {
            return R.drawable.room_suite;
        } else if (type.contains("family")) {
            return R.drawable.room_family;
        }

        return R.drawable.room_deluxe;
    }

    private String getRatingText(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            return "⭐⭐⭐⭐⭐ 4.9";
        } else if (type.contains("family")) {
            return "⭐⭐⭐⭐⭐ 4.8";
        } else if (type.contains("deluxe")) {
            return "⭐⭐⭐⭐⭐ 4.7";
        }

        return "⭐⭐⭐⭐ 4.5";
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        FrameLayout cardRoomRoot;
        ImageView imgRoom;
        TextView tvFavorite, tvRoomName, tvBranch, tvRating, tvPrice, tvRoomMeta;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            cardRoomRoot = itemView.findViewById(R.id.cardRoomRoot);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            tvFavorite = itemView.findViewById(R.id.tvFavorite);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRoomMeta = itemView.findViewById(R.id.tvRoomMeta);
        }
    }
}