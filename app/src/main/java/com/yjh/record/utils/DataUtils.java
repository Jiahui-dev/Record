package com.yjh.record.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by youjiahui on 2026/8/2
 */
public class DataUtils {

    /**
     * 计算从购买日期到现在的天数（日期格式：2024.6.1）
     */
    public static long getDaysFromPurchaseDate(String purchaseDate) {
        if (purchaseDate == null || purchaseDate.isEmpty()) {
            return 0;
        }

        try {
            String[] parts = purchaseDate.split("\\.");
            if (parts.length != 3) {
                return 0;
            }

            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, day, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);

            Date purchase = cal.getTime();
            Date now = new Date();

            long diff = now.getTime() - purchase.getTime();
            return diff / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double calculateDailyCost(String purchaseDate, double price) {
        if (purchaseDate == null || purchaseDate.isEmpty() || price <= 0) {
            return 0;
        }

        long days = getDaysFromPurchaseDate(purchaseDate);
        if (days <= 0) {
            return 0;
        }

        return price / days;
    }

    public static String calculateDailyCostFormatted(String purchaseDate, double price) {
        double result = calculateDailyCost(purchaseDate, price);
        return String.format("%.2f", result);
    }

    public static String formatNumber(double value) {
        // 判断是否为整数（没有小数位）
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            return String.format("%.2f", value);
        }
    }

}
