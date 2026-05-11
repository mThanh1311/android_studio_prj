package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;

public class AmenityDetailActivity extends AppCompatActivity {

    private ImageView imgAmenityHeader;
    private TextView tvAmenityTitle, tvAmenityTime, tvAmenityDesc;
    private LinearLayout layoutGalleryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenity_detail);

        imgAmenityHeader = findViewById(R.id.imgAmenityHeader);
        tvAmenityTitle = findViewById(R.id.tvAmenityTitle);
        tvAmenityTime = findViewById(R.id.tvAmenityTime);
        tvAmenityDesc = findViewById(R.id.tvAmenityDesc);
        layoutGalleryContainer = findViewById(R.id.layoutGalleryContainer);

        String amenityType = getIntent().getStringExtra("amenity_type");

        if (amenityType == null) {
            amenityType = "pool";
        }

        loadAmenityData(amenityType);
    }

    private void loadAmenityData(String type) {
        layoutGalleryContainer.removeAllViews();

        switch (type) {
            case "pool":
                imgAmenityHeader.setImageResource(R.drawable.amenity_pool);
                tvAmenityTitle.setText("Hồ bơi");
                tvAmenityTime.setText("06:00 - 22:00");
                tvAmenityDesc.setText("Không gian hồ bơi ngoài trời, phù hợp thư giãn, bơi lội và tận hưởng kỳ nghỉ trong không gian sang trọng, sạch sẽ.");

                addGalleryItem(R.drawable.amenity_pool_1,
                        "Khu vực hồ bơi rộng rãi, thiết kế hiện đại, mang lại cảm giác thư thái và cao cấp.");
                addGalleryItem(R.drawable.amenity_pool_2,
                        "Hồ bơi vô cực với không gian mở, phù hợp thư giãn và chụp ảnh cùng gia đình, bạn bè.");
                addGalleryItem(R.drawable.amenity_pool_3,
                        "Không gian hồ bơi luôn được vệ sinh sạch sẽ, đảm bảo trải nghiệm an toàn và thoải mái cho khách lưu trú.");
                break;

            case "restaurant":
                imgAmenityHeader.setImageResource(R.drawable.amenity_restaurant);
                tvAmenityTitle.setText("Nhà hàng");
                tvAmenityTime.setText("06:30 - 21:30");
                tvAmenityDesc.setText("Nhà hàng phục vụ buffet sáng miễn phí, các món Á - Âu phong phú và không gian dùng bữa sang trọng cho gia đình.");

                addGalleryItem(R.drawable.amenity_restaurant_1,
                        "Buffet sáng miễn phí với nhiều lựa chọn món ăn hấp dẫn, phục vụ từ 06:30 đến 10:00.");
                addGalleryItem(R.drawable.amenity_restaurant_2,
                        "Không gian buổi tối sang trọng, phù hợp cho gia đình, cặp đôi hoặc gặp gỡ đối tác.");
                addGalleryItem(R.drawable.amenity_restaurant_3,
                        "Thực đơn đa dạng với nhiều món Á - Âu, đồ uống và món tráng miệng được chuẩn bị chỉn chu.");
                break;

            case "spa":
                imgAmenityHeader.setImageResource(R.drawable.amenity_spa);
                tvAmenityTitle.setText("Spa");
                tvAmenityTime.setText("09:00 - 22:00");
                tvAmenityDesc.setText("Dịch vụ massage, xông hơi và chăm sóc sức khỏe giúp khách hàng thư giãn, tái tạo năng lượng sau những giờ làm việc hoặc du lịch.");

                addGalleryItem(R.drawable.amenity_spa_1,
                        "Không gian spa yên tĩnh, sang trọng, phù hợp để thư giãn và cân bằng cơ thể.");
                addGalleryItem(R.drawable.amenity_spa_2,
                        "Dịch vụ massage chuyên nghiệp kết hợp kỹ thuật chăm sóc sức khỏe hiện đại.");
                addGalleryItem(R.drawable.amenity_spa_3,
                        "Khu vực xông hơi và chăm sóc cơ thể giúp khách hàng phục hồi năng lượng hiệu quả.");
                break;

            case "lobby":
                imgAmenityHeader.setImageResource(R.drawable.amenity_lobby);
                tvAmenityTitle.setText("Sảnh khách sạn");
                tvAmenityTime.setText("Hỗ trợ 24/7");
                tvAmenityDesc.setText("Sảnh khách sạn là khu vực tiếp đón chính với phong cách sang trọng, đội ngũ nhân viên tận tụy và dịch vụ hỗ trợ khách hàng 24/7.");

                addGalleryItem(R.drawable.amenity_lobby_1,
                        "Không gian sảnh rộng rãi, tinh tế, tạo ấn tượng chuyên nghiệp ngay từ khi khách đến.");
                addGalleryItem(R.drawable.amenity_lobby_2,
                        "Nhân viên lễ tân tận tụy hỗ trợ check-in, check-out nhanh chóng và giải đáp thông tin mọi lúc.");
                addGalleryItem(R.drawable.amenity_lobby_3,
                        "Khu vực tiếp khách được bố trí sang trọng, thuận tiện cho việc chờ đợi, gặp gỡ và nghỉ ngơi ngắn.");
                break;
        }
    }

    private void addGalleryItem(int imageResId, String caption) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.item_amenity_gallery, layoutGalleryContainer, false);

        ImageView imgGallery = itemView.findViewById(R.id.imgGallery);
        TextView tvGalleryCaption = itemView.findViewById(R.id.tvGalleryCaption);

        imgGallery.setImageResource(imageResId);
        tvGalleryCaption.setText(caption);

        layoutGalleryContainer.addView(itemView);
    }
}