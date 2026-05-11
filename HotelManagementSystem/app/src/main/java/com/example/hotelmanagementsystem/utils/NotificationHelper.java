package com.example.hotelmanagementsystem.utils;

import android.content.Context;

import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.AppNotification;
import com.example.hotelmanagementsystem.models.Booking;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class NotificationHelper {

    public static ArrayList<AppNotification> getCurrentNotifications(Context context) {
        ArrayList<AppNotification> notifications = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(context);
        DatabaseHelper db = new DatabaseHelper(context);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        // 1. Booking thành công gần nhất
        int lastBookingId = sessionManager.getLastBookingId();

        if (lastBookingId != -1) {
            Booking latestBooking = db.getBookingById(lastBookingId);

            if (latestBooking != null && "booked".equals(latestBooking.getStatus())) {
                notifications.add(new AppNotification(
                        "booking_success_" + latestBooking.getId(),
                        "Đặt phòng thành công #" + latestBooking.getId(),
                        "Phòng: " + latestBooking.getRoomName() + "\n" +
                                "Người đặt: " + latestBooking.getCustomerName() + "\n" +
                                "Check-in: " + latestBooking.getCheckIn() +
                                " • Check-out: " + latestBooking.getCheckOut() + "\n" +
                                "Tổng tiền: " + formatter.format(latestBooking.getTotalPrice()) + " VNĐ",
                        latestBooking.getId()
                ));
            }
        }

        // 2. Nhắc lịch check-in/check-out
        ArrayList<Booking> bookings = db.getBookingsByUser(sessionManager.getUserId());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar today = Calendar.getInstance();

        for (Booking booking : bookings) {
            if (!"booked".equals(booking.getStatus())) {
                continue;
            }

            try {
                Calendar checkInCal = Calendar.getInstance();
                checkInCal.setTime(sdf.parse(booking.getCheckIn()));

                Calendar checkOutCal = Calendar.getInstance();
                checkOutCal.setTime(sdf.parse(booking.getCheckOut()));

                long diffCheckIn = daysBetween(today, checkInCal);
                long diffCheckOut = daysBetween(today, checkOutCal);

                if (diffCheckIn == 3 || diffCheckIn == 1) {
                    notifications.add(new AppNotification(
                            "booking_reminder_" + booking.getId() + "_" + diffCheckIn,
                            "Nhắc lịch lưu trú",
                            "Quý khách có booking phòng " + booking.getRoomName() +
                                    " vào ngày " + booking.getCheckIn() +
                                    ". Vui lòng chuẩn bị giấy tờ tùy thân để check-in đúng giờ.",
                            booking.getId()
                    ));
                }

                if (diffCheckIn == 0) {
                    notifications.add(new AppNotification(
                            "checkin_today_" + booking.getId(),
                            "Hôm nay là ngày check-in",
                            "Booking #" + booking.getId() + " - " + booking.getRoomName() +
                                    ". Quý khách có thể check-in từ 14:00.",
                            booking.getId()
                    ));
                }

                if (diffCheckOut == 0) {
                    notifications.add(new AppNotification(
                            "checkout_today_" + booking.getId(),
                            "Hôm nay là ngày check-out",
                            "Booking #" + booking.getId() + " - " + booking.getRoomName() +
                                    ". Vui lòng check-out trước 12:00.",
                            booking.getId()
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 3. Thông báo ưu đãi giả định
        notifications.add(new AppNotification(
                "offer_gold_member",
                "Ưu đãi thành viên Gold",
                "Bạn được giảm 10% cho lần đặt phòng tiếp theo và tích điểm sau mỗi booking.",
                -1
        ));

        notifications.add(new AppNotification(
                "offer_weekend_sale",
                "Flash Sale cuối tuần",
                "Ưu đãi giảm đến 15% cho phòng Deluxe và Suite khi đặt trong tuần này.",
                -1
        ));

        return notifications;
    }

    public static int getUnreadCount(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        ArrayList<AppNotification> notifications = getCurrentNotifications(context);

        int count = 0;

        for (AppNotification notification : notifications) {
            if (!sessionManager.isNotificationSeen(notification.getKey())) {
                count++;
            }
        }

        return count;
    }

    public static void markAllCurrentNotificationsAsSeen(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        ArrayList<AppNotification> notifications = getCurrentNotifications(context);

        Set<String> keys = new HashSet<>();

        for (AppNotification notification : notifications) {
            keys.add(notification.getKey());
        }

        sessionManager.markNotificationsAsSeen(keys);
    }

    private static long daysBetween(Calendar today, Calendar target) {
        Calendar todayOnly = Calendar.getInstance();
        todayOnly.set(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        todayOnly.set(Calendar.MILLISECOND, 0);

        Calendar targetOnly = Calendar.getInstance();
        targetOnly.set(
                target.get(Calendar.YEAR),
                target.get(Calendar.MONTH),
                target.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        targetOnly.set(Calendar.MILLISECOND, 0);

        long diff = targetOnly.getTimeInMillis() - todayOnly.getTimeInMillis();
        return diff / (24 * 60 * 60 * 1000);
    }
}