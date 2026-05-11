package com.example.hotelmanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.models.WelcomeSlide;

import java.util.ArrayList;

public class WelcomeSliderAdapter extends RecyclerView.Adapter<WelcomeSliderAdapter.SlideViewHolder> {

    private ArrayList<WelcomeSlide> slideList;

    public WelcomeSliderAdapter(ArrayList<WelcomeSlide> slideList) {
        this.slideList = slideList;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_welcome_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        WelcomeSlide slide = slideList.get(position);

        holder.imgSlide.setImageResource(slide.getImageResId());
        holder.tvSlideTitle.setText(slide.getTitle());
        holder.tvSlideDescription.setText(slide.getDescription());
    }

    @Override
    public int getItemCount() {
        return slideList.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlide;
        TextView tvSlideTitle, tvSlideDescription;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSlide = itemView.findViewById(R.id.imgSlide);
            tvSlideTitle = itemView.findViewById(R.id.tvSlideTitle);
            tvSlideDescription = itemView.findViewById(R.id.tvSlideDescription);
        }
    }
}