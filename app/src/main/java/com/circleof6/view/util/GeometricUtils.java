package com.circleof6.view.util;


import android.graphics.Bitmap;
import android.graphics.PointF;

public class GeometricUtils {

    public static boolean isPointInsideCircle(PointF centerCircle, double radius, PointF point) {
        double d = Math.pow((double) (point.x - centerCircle.x), 2.0) + Math.pow((double) (point.y - centerCircle.y), 2.0);
        return d < Math.pow(radius, 2.0);
    }

    public static Bitmap redimenBitmap( Bitmap bitmap, int dimens)
    {
        return Bitmap.createScaledBitmap(bitmap, dimens, dimens, false);
    }

}
