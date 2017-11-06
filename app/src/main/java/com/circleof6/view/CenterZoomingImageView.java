package com.circleof6.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by michael on 8/28/14.
 */
public class CenterZoomingImageView extends ImageView
{
    private Matrix mImgMatrix;

    public CenterZoomingImageView(Context context)
    {
        super(context);
        mImgMatrix = new Matrix();
    }

    public CenterZoomingImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mImgMatrix = new Matrix();
    }

    public CenterZoomingImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mImgMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        // FIXME: these should not be hard-coded!
        int vertBuffer = 0;
        final int HORIZ_BUFFER = 0;

        vertBuffer = getVertBuffer();

        int trueViewWidth = this.getWidth();
        int trueViewHeight = this.getHeight();

        int virtualViewWidth = trueViewWidth - HORIZ_BUFFER;
        int virtualViewHeight = trueViewHeight - vertBuffer;

        int imageWidth = getDrawable().getIntrinsicWidth();
        int imageHeight = getDrawable().getIntrinsicHeight();

        if(1.0f * virtualViewHeight / virtualViewWidth > 1.0f * imageHeight / imageWidth)
        {
            float widthUsedOfImage = 1.0f * virtualViewWidth * imageHeight / virtualViewHeight;

            mImgMatrix.setRectToRect(new RectF(0.5f * (imageWidth - widthUsedOfImage), 0,
                                               0.5f * (imageWidth + widthUsedOfImage), imageHeight),
                                     new RectF(0, 0.5f * (trueViewHeight - virtualViewHeight),
                                               virtualViewWidth,
                                               0.5f * (trueViewHeight + virtualViewHeight)),
                                     Matrix.ScaleToFit.FILL);
        }
        else
        {
            float heightUsedOfImage = 1.0f * virtualViewHeight * imageWidth / virtualViewWidth;

            mImgMatrix.setRectToRect(
                    new RectF(0, 0.5f * (imageHeight - heightUsedOfImage), imageWidth,
                              0.5f * (imageHeight + heightUsedOfImage)),
                    new RectF(0, 0.5f * (trueViewHeight - virtualViewHeight), virtualViewWidth,
                              0.5f * (trueViewHeight + virtualViewHeight)), Matrix.ScaleToFit.FILL);
        }

        setImageMatrix(mImgMatrix);
    }

    private int getVertBuffer()
    {
        int vertBuffer;
        int densityDpi = getContext().getResources().getDisplayMetrics().densityDpi;
        Log.d(CenterZoomingImageView.class.getSimpleName(), "density: " + densityDpi);

        switch(densityDpi)
        {
            case DisplayMetrics.DENSITY_560:
                vertBuffer = 50;
                break;
            default:
                vertBuffer = 280;
                break;
        }

        return vertBuffer;
    }
}
