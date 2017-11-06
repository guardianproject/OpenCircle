package com.circleof6.view.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import com.circleof6.view.CircleOf6View;

public class DrawUtils
{

    public static Paint createPaintFill (int color)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;

    }

    public static Paint createPaintStroke(int color, int strokeWidth){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;
    }

    public static Paint cratePaintCenterText(Context context, int color, int textSize)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(color);
        paint.setTextSize(pxFromDp(context, textSize));
        paint.setTextAlign(Paint.Align.CENTER);

        return paint;
    }

    public static Paint cratePaintNamesContacts(Context context, int color, int textSize, Paint.Align align)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(color);
        paint.setTextSize(pxFromDp(context, textSize));
        paint.setTextAlign(align);

        return paint;
    }


    public static int pxFromDp(Context context, int dp)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }

    public static Bitmap getBitmapFromPath (String path){
        return BitmapFactory.decodeFile(path);
    }


    public static Bitmap getCroppedBitmap(Bitmap bitmap)
    {
        int fudgeFactor = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap
                .createBitmap(width - fudgeFactor, height - fudgeFactor, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width - fudgeFactor, height - fudgeFactor);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(width / 2 - fudgeFactor, height / 2 - fudgeFactor,
                          width / 2 - fudgeFactor, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getCroppedAndMaskedBitmap(CircleOf6View circleOf6View, Bitmap bitmap)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        int size = circleOf6View.getWidthCircleContact();
        final Paint antiAliasPaint = new Paint();
        antiAliasPaint.setAntiAlias(true);
        antiAliasPaint.setFlags(Paint.DITHER_FLAG);
        antiAliasPaint.setFilterBitmap(true);

        bitmap = Bitmap
                .createScaledBitmap(bitmap, size, size, true); //makes bitmap square-size of mask
        bitmap = DrawUtils.getCroppedBitmap(bitmap); // makes bitmap circular

        return bitmap;

    }

}
