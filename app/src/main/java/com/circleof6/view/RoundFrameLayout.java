package com.circleof6.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.circleof6.R;


/**
 * Created by N-Pex on 2018-10-30.
 */
public class RoundFrameLayout extends FrameLayout {

    private float borderWidthPercent = -1f;
    private float borderWidth;
    private int borderColor;
    private boolean insetBorder; // True if we should pad the content with border width

    private Paint paintBorder;
    private Path clipPath;


    public RoundFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        clipPath = new Path();
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);
            if (a != null) {
                TypedValue borderWidthValue = new TypedValue();
                if (a.getValue(R.styleable.RoundFrameLayout_frameBorderWidth, borderWidthValue)) {
                    if (borderWidthValue.type == TypedValue.TYPE_DIMENSION) {
                        borderWidth = a.getDimensionPixelSize(R.styleable.RoundFrameLayout_frameBorderWidth, 0);
                    } else if (borderWidthValue.type == TypedValue.TYPE_FLOAT) {
                        // Fraction
                        borderWidthPercent = borderWidthValue.getFloat();
                    }
                }
                borderColor = a.getColor(R.styleable.RoundFrameLayout_frameBorderColor, Color.BLACK);
                insetBorder = a.getBoolean(R.styleable.RoundFrameLayout_frameInsetBorder, true);
                a.recycle();
            }
        }

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(borderColor);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderWidth);
        setWillNotDraw(false);
        setClipChildren(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int w = right - left;
        int h = bottom - top;
        float size = Math.min(w, h) - 1;

        if (borderWidthPercent >= 0) {
            borderWidth = (size * borderWidthPercent) / 100.0f;
        }

        clipPath.reset();
        clipPath.addCircle((float) w / 2.0f, (float) h / 2.0f, (size - borderWidth) / 2.0f, Path.Direction.CW);
        paintBorder.setStrokeWidth(borderWidth);

        int padding = insetBorder ? (int)borderWidth : 0;
        super.onLayout(changed, left + padding, top + padding, right - padding, bottom - padding);
    }

    public void setBorderColor(int color) {
        borderColor = color;
        paintBorder.setColor(color);
        paintBorder.setShader(null);
    }

    public void setBorderShader(Shader shader) {
        paintBorder.setColor(Color.BLACK);
        paintBorder.setShader(shader);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clipPath);
        super.draw(canvas);
        canvas.restore();

        // Draw border
        if (borderWidth > 0) {
            canvas.drawPath(clipPath, paintBorder);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.getMeasuredWidth();
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
    }
}
