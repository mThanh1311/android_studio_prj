package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.text.NumberFormat;
import java.util.Locale;

public class RoomDetailActivity extends AppCompatActivity {

    private ImageView imgRoomCover, imgGallery1, imgGallery2, imgGallery3;
    private ImageView imgHostAvatar;

    private TextView btnBack, btnFavorite, tvPhotoCount;
    private TextView tvRoomName, tvBranch, tvRating, tvPrice, tvOffer;
    private TextView tvDescription, tvRoomType, tvCapacity, tvStatus;
    private TextView tvAmenity1, tvAmenity2, tvAmenity3, tvAmenity4;
    private ImageView imgAmenity1, imgAmenity2, imgAmenity3, imgAmenity4;
    private TextView tvPolicy;
    private TextView tvHostName, tvHostMeta;

    private ImageButton btnHostSupport;
    private Button btnSupport, btnBookNow;

    private DatabaseHelper db;
    private Room room;
    private int roomId;
    private boolean isFavorite = false;

    private NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        db = new DatabaseHelper(this);
        formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        roomId = getIntent().getIntExtra("room_id", -1);

        initViews();
        setupActions();
        loadRoomData();
    }

    private void initViews() {
        imgRoomCover = findViewById(R.id.imgRoomCover);
        imgGallery1 = findViewById(R.id.imgGallery1);
        imgGallery2 = findViewById(R.id.imgGallery2);
        imgGallery3 = findViewById(R.id.imgGallery3);

        imgHostAvatar = findViewById(R.id.imgHostAvatar);

        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        tvPhotoCount = findViewById(R.id.tvPhotoCount);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvBranch = findViewById(R.id.tvBranch);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvOffer = findViewById(R.id.tvOffer);
        tvDescription = findViewById(R.id.tvDescription);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvCapacity = findViewById(R.id.tvCapacity);
        tvStatus = findViewById(R.id.tvStatus);

        tvAmenity1 = findViewById(R.id.tvAmenity1);
        tvAmenity2 = findViewById(R.id.tvAmenity2);
        tvAmenity3 = findViewById(R.id.tvAmenity3);
        tvAmenity4 = findViewById(R.id.tvAmenity4);

        imgAmenity1 = findViewById(R.id.imgAmenity1);
        imgAmenity2 = findViewById(R.id.imgAmenity2);
        imgAmenity3 = findViewById(R.id.imgAmenity3);
        imgAmenity4 = findViewById(R.id.imgAmenity4);

        tvPolicy = findViewById(R.id.tvPolicy);

        tvHostName = findViewById(R.id.tvHostName);
        tvHostMeta = findViewById(R.id.tvHostMeta);
        btnHostSupport = findViewById(R.id.btnHostSupport);

        btnSupport = findViewById(R.id.btnSupport);
        btnBookNow = findViewById(R.id.btnBookNow);
    }

    private void setupActions() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;

            if (isFavorite) {
                btnFavorite.setText("♥");
                Toast.makeText(this, "Đã thêm phòng vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                btnFavorite.setText("♡");
                Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        btnHostSupport.setOnClickListener(v -> {
            Toast.makeText(this, "Đang mở hỗ trợ tư vấn phòng...", Toast.LENGTH_SHORT).show();
        });

        btnSupport.setOnClickListener(v -> {
            Toast.makeText(this, "Hotline hỗ trợ: 0123 456 789", Toast.LENGTH_LONG).show();
        });

        btnBookNow.setOnClickListener(v -> {
            if (room == null) {
                Toast.makeText(this, "Không tìm thấy thông tin phòng", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
            intent.putExtra("room_id", room.getId());
            startActivity(intent);
        });
    }

    private void loadRoomData() {
        room = db.getRoomById(roomId);

        if (room == null) {
            Toast.makeText(this, "Không tìm thấy thông tin phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvRoomName.setText(room.getName());
        tvBranch.setText(room.getBranchName());
        tvRating.setText(getRatingText(room));
        tvPrice.setText(formatter.format(room.getPrice()) + "đ / đêm");
        tvOffer.setText(getOfferText(room));
        tvDescription.setText(room.getDescription());
        tvRoomType.setText("Loại phòng: " + room.getType());
        tvCapacity.setText("Sức chứa: " + room.getCapacity() + " người");
        tvStatus.setText("Trạng thái: " + room.getStatus());
        tvPhotoCount.setText(getPhotoCountText(room));

        if ("available".equalsIgnoreCase(room.getStatus())) {
            tvStatus.setTextColor(0xFF16A34A);
        } else {
            tvStatus.setTextColor(0xFFDC2626);
        }

        int roomImage = getRoomImage(room);
        imgRoomCover.setImageResource(roomImage);
        imgGallery1.setImageResource(roomImage);

        bindAmenities(room);
        bindGallery(room);
        bindPolicy(room);
        bindHostInfo(room);
    }

    private int getRoomImage(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("standard")) {
            return R.drawable.room_standard;
        }

        if (type.contains("deluxe")) {
            return R.drawable.room_deluxe;
        }

        if (type.contains("suite")) {
            return R.drawable.room_suite;
        }

        if (type.contains("family")) {
            return R.drawable.room_family;
        }

        return R.drawable.room_deluxe;
    }

    private String getRatingText(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            return "⭐ 4.9 (342 đánh giá)";
        }

        if (type.contains("family")) {
            return "⭐ 4.8 (214 đánh giá)";
        }

        if (type.contains("deluxe")) {
            return "⭐ 4.7 (286 đánh giá)";
        }

        return "⭐ 4.5 (163 đánh giá)";
    }

    private String getOfferText(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            return "Ưu đãi áp dụng: Giảm 15% cho thành viên Gold";
        }

        if (type.contains("family")) {
            return "Ưu đãi áp dụng: Combo gia đình giảm 10%";
        }

        if (type.contains("deluxe")) {
            return "Ưu đãi áp dụng: Giảm 10% khi đặt cuối tuần";
        }

        return "Ưu đãi áp dụng: Tích điểm thành viên sau khi đặt phòng";
    }

    private String getPhotoCountText(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            return "18 Photos";
        }

        if (type.contains("family")) {
            return "16 Photos";
        }

        if (type.contains("deluxe")) {
            return "14 Photos";
        }

        return "12 Photos";
    }

    private void bindAmenities(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            setAmenity(imgAmenity1, tvAmenity1, R.drawable.ic_bed_outline, "King Bed");
            setAmenity(imgAmenity2, tvAmenity2, R.drawable.ic_bathtub_outline, "Bathtub");
            setAmenity(imgAmenity3, tvAmenity3, R.drawable.ic_spa_outline, "Spa");
            setAmenity(imgAmenity4, tvAmenity4, R.drawable.ic_restaurant_outline, "Breakfast");
        } else if (type.contains("family")) {
            setAmenity(imgAmenity1, tvAmenity1, R.drawable.ic_bed_outline, "2 Beds");
            setAmenity(imgAmenity2, tvAmenity2, R.drawable.ic_restaurant_outline, "Breakfast");
            setAmenity(imgAmenity3, tvAmenity3, R.drawable.ic_pool_outline, "Pool");
            setAmenity(imgAmenity4, tvAmenity4, R.drawable.ic_ac_outline, "A/C");
        } else if (type.contains("deluxe")) {
            setAmenity(imgAmenity1, tvAmenity1, R.drawable.ic_bed_outline, "Queen Bed");
            setAmenity(imgAmenity2, tvAmenity2, R.drawable.ic_restaurant_outline, "Dinner");
            setAmenity(imgAmenity3, tvAmenity3, R.drawable.ic_pool_outline, "Pool");
            setAmenity(imgAmenity4, tvAmenity4, R.drawable.ic_ac_outline, "A/C");
        } else {
            setAmenity(imgAmenity1, tvAmenity1, R.drawable.ic_bed_outline, "Bed");
            setAmenity(imgAmenity2, tvAmenity2, R.drawable.ic_wifi_outline, "Wi-Fi");
            setAmenity(imgAmenity3, tvAmenity3, R.drawable.ic_tv_outline, "TV");
            setAmenity(imgAmenity4, tvAmenity4, R.drawable.ic_parking_outline, "Parking");
        }
    }

    private void setAmenity(ImageView imageView, TextView textView, int iconRes, String label) {
        imageView.setImageResource(iconRes);
        textView.setText(label);
    }

    private void bindGallery(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            imgGallery2.setImageResource(R.drawable.amenity_spa);
            imgGallery3.setImageResource(R.drawable.amenity_lobby);
        } else if (type.contains("family")) {
            imgGallery2.setImageResource(R.drawable.amenity_pool);
            imgGallery3.setImageResource(R.drawable.amenity_restaurant);
        } else if (type.contains("deluxe")) {
            imgGallery2.setImageResource(R.drawable.amenity_pool);
            imgGallery3.setImageResource(R.drawable.amenity_restaurant);
        } else {
            imgGallery2.setImageResource(R.drawable.amenity_lobby);
            imgGallery3.setImageResource(R.drawable.branch_city);
        }
    }

    private void bindPolicy(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            tvPolicy.setText(
                    "• Check-in từ 14:00\n" +
                            "• Check-out trước 12:00\n" +
                            "• Hỗ trợ đổi lịch trước 24 giờ\n" +
                            "• Có thể yêu cầu hỗ trợ check-in nhanh\n" +
                            "• Vui lòng mang giấy tờ tùy thân khi nhận phòng"
            );
        } else if (type.contains("family")) {
            tvPolicy.setText(
                    "• Check-in từ 14:00\n" +
                            "• Check-out trước 12:00\n" +
                            "• Phù hợp gia đình có trẻ em\n" +
                            "• Hỗ trợ đổi lịch trước 24 giờ\n" +
                            "• Vui lòng xác nhận số lượng khách khi nhận phòng"
            );
        } else {
            tvPolicy.setText(
                    "• Check-in từ 14:00\n" +
                            "• Check-out trước 12:00\n" +
                            "• Hỗ trợ đổi lịch trước 24 giờ\n" +
                            "• Vui lòng mang giấy tờ tùy thân khi nhận phòng"
            );
        }
    }

    private void bindHostInfo(Room room) {
        String type = room.getType().toLowerCase();

        if (type.contains("suite")) {
            tvHostName.setText("ABC Premium Support");
            tvHostMeta.setText("⭐ 4.9 (1.8K review)");
            imgHostAvatar.setImageResource(R.drawable.avatar_female);
        } else if (type.contains("family")) {
            tvHostName.setText("Family Care Team");
            tvHostMeta.setText("⭐ 4.8 (1.2K review)");
            imgHostAvatar.setImageResource(R.drawable.avatar_female);
        } else if (type.contains("deluxe")) {
            tvHostName.setText("ABC Hotel Team");
            tvHostMeta.setText("⭐ 4.7 (980 review)");
            imgHostAvatar.setImageResource(R.drawable.avatar_male);
        } else {
            tvHostName.setText("Hotel Support");
            tvHostMeta.setText("⭐ 4.6 (620 review)");
            imgHostAvatar.setImageResource(R.drawable.avatar_male);
        }
    }
}