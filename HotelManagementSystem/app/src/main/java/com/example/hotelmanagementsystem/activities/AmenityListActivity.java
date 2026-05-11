package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.ImageSliderAdapter;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;

import java.util.Arrays;
import java.util.List;

public class AmenityListActivity extends AppCompatActivity {

    private TextView btnBackAmenity;

    private ViewPager2 vpPool, vpRestaurant, vpSpa, vpLobby;

    private View[] dotsPool, dotsRestaurant, dotsSpa, dotsLobby;

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());

    private Runnable poolRunnable, restaurantRunnable, spaRunnable, lobbyRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenity_list);

        initViews();
        setupEvents();
        setupSliders();
        BottomNavHelper.setup(this, "home");
    }

    private void initViews() {
        btnBackAmenity = findViewById(R.id.btnBackAmenity);

        vpPool = findViewById(R.id.vpPool);
        vpRestaurant = findViewById(R.id.vpRestaurant);
        vpSpa = findViewById(R.id.vpSpa);
        vpLobby = findViewById(R.id.vpLobby);

        dotsPool = new View[]{
                findViewById(R.id.dotPool1),
                findViewById(R.id.dotPool2),
                findViewById(R.id.dotPool3),
                findViewById(R.id.dotPool4)
        };

        dotsRestaurant = new View[]{
                findViewById(R.id.dotRestaurant1),
                findViewById(R.id.dotRestaurant2),
                findViewById(R.id.dotRestaurant3),
                findViewById(R.id.dotRestaurant4)
        };

        dotsSpa = new View[]{
                findViewById(R.id.dotSpa1),
                findViewById(R.id.dotSpa2),
                findViewById(R.id.dotSpa3),
                findViewById(R.id.dotSpa4)
        };

        dotsLobby = new View[]{
                findViewById(R.id.dotLobby1),
                findViewById(R.id.dotLobby2),
                findViewById(R.id.dotLobby3),
                findViewById(R.id.dotLobby4)
        };
    }

    private void setupEvents() {
        btnBackAmenity.setOnClickListener(v -> finish());
    }

    private void setupSliders() {
        List<Integer> poolImages = Arrays.asList(
                R.drawable.amenity_pool,
                R.drawable.amenity_pool_1,
                R.drawable.amenity_pool_2,
                R.drawable.amenity_pool_3
        );

        List<Integer> restaurantImages = Arrays.asList(
                R.drawable.amenity_restaurant,
                R.drawable.amenity_restaurant_1,
                R.drawable.amenity_restaurant_2,
                R.drawable.amenity_restaurant_3
        );

        List<Integer> spaImages = Arrays.asList(
                R.drawable.amenity_spa,
                R.drawable.amenity_spa_1,
                R.drawable.amenity_spa_2,
                R.drawable.amenity_spa_3
        );

        List<Integer> lobbyImages = Arrays.asList(
                R.drawable.amenity_lobby,
                R.drawable.amenity_lobby_1,
                R.drawable.amenity_lobby_2,
                R.drawable.amenity_lobby_3
        );

        setupSingleSlider(vpPool, poolImages, dotsPool, "pool");
        setupSingleSlider(vpRestaurant, restaurantImages, dotsRestaurant, "restaurant");
        setupSingleSlider(vpSpa, spaImages, dotsSpa, "spa");
        setupSingleSlider(vpLobby, lobbyImages, dotsLobby, "lobby");
    }

    private void setupSingleSlider(ViewPager2 viewPager, List<Integer> images, View[] dots, String type) {
        ImageSliderAdapter adapter = new ImageSliderAdapter(images);
        viewPager.setAdapter(adapter);

        updateIndicators(dots, 0);

        Runnable autoRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getAdapter() == null) return;

                int next = (viewPager.getCurrentItem() + 1) % images.size();
                viewPager.setCurrentItem(next, true);
                sliderHandler.postDelayed(this, 3000);
            }
        };

        if ("pool".equals(type)) {
            poolRunnable = autoRunnable;
        } else if ("restaurant".equals(type)) {
            restaurantRunnable = autoRunnable;
        } else if ("spa".equals(type)) {
            spaRunnable = autoRunnable;
        } else if ("lobby".equals(type)) {
            lobbyRunnable = autoRunnable;
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(dots, position);
                restartSlider(type);
            }
        });
    }

    private void updateIndicators(View[] dots, int activePosition) {
        for (int i = 0; i < dots.length; i++) {
            View dot = dots[i];
            ViewGroup.LayoutParams params = dot.getLayoutParams();

            if (i == activePosition) {
                params.width = dpToPx(18);
                params.height = dpToPx(6);
                dot.setBackgroundResource(R.drawable.bg_indicator_active);
            } else {
                params.width = dpToPx(6);
                params.height = dpToPx(6);
                dot.setBackgroundResource(R.drawable.bg_indicator_inactive);
            }

            dot.setLayoutParams(params);
        }
    }

    private void restartSlider(String type) {
        if ("pool".equals(type)) {
            sliderHandler.removeCallbacks(poolRunnable);
            sliderHandler.postDelayed(poolRunnable, 3000);
        } else if ("restaurant".equals(type)) {
            sliderHandler.removeCallbacks(restaurantRunnable);
            sliderHandler.postDelayed(restaurantRunnable, 3000);
        } else if ("spa".equals(type)) {
            sliderHandler.removeCallbacks(spaRunnable);
            sliderHandler.postDelayed(spaRunnable, 3000);
        } else if ("lobby".equals(type)) {
            sliderHandler.removeCallbacks(lobbyRunnable);
            sliderHandler.postDelayed(lobbyRunnable, 3000);
        }
    }

    private void startAllAutoSlide() {
        if (poolRunnable != null) sliderHandler.postDelayed(poolRunnable, 3000);
        if (restaurantRunnable != null) sliderHandler.postDelayed(restaurantRunnable, 3000);
        if (spaRunnable != null) sliderHandler.postDelayed(spaRunnable, 3000);
        if (lobbyRunnable != null) sliderHandler.postDelayed(lobbyRunnable, 3000);
    }

    private void stopAllAutoSlide() {
        if (poolRunnable != null) sliderHandler.removeCallbacks(poolRunnable);
        if (restaurantRunnable != null) sliderHandler.removeCallbacks(restaurantRunnable);
        if (spaRunnable != null) sliderHandler.removeCallbacks(spaRunnable);
        if (lobbyRunnable != null) sliderHandler.removeCallbacks(lobbyRunnable);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAllAutoSlide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAllAutoSlide();
    }
}