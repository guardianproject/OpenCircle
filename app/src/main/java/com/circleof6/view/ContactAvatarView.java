package com.circleof6.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.view.util.ConstantsView;
import com.circleof6.view.util.DrawUtils;


/**
 * Created by N-Pex on 2018-10-30.
 */
public class ContactAvatarView extends android.support.v7.widget.AppCompatImageView {

    private Contact contact;

    private float borderWidthPercent = 5.0f;
    private float borderWidth;
    private Paint paintBorder;
    private Path clipPath;

    private LinearGradient shaderNormal;
    private LinearGradient shaderUnread;

    public ContactAvatarView(Context context) {
        super(context);
        init(null);
    }

    public ContactAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ContactAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        clipPath = new android.graphics.Path();
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        if (this.contact == null || TextUtils.isEmpty(this.contact.getPhoneNumber())) {
            // Show the add!
            setImageResource(R.drawable.ic_add);
        } else if (contact.getPhoto() == null || contact.getPhoto().equals(ConstantsView.DEFAULT_PHOTO)) {
            // Show default image
            setImageResource(R.drawable.contact_icon);
        } else {
            setImageBitmap(DrawUtils.getBitmapFromPath(contact.getPhoto()));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float size = Math.min(w, h) - 1;

        borderWidth = (size * borderWidthPercent) / 100.0f;
        setPadding((int)borderWidth-1, (int)borderWidth - 1, (int)borderWidth - 1, (int)borderWidth - 1);

        clipPath.reset();
        clipPath.addCircle((float)w/2.0f, (float)h/2.0f, (size - borderWidth) / 2.0f, Path.Direction.CW);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(Color.WHITE);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderWidth);

        shaderNormal = buildShader(Color.WHITE, Color.BLUE, w, h);
        shaderUnread = buildShader(0xffff0000, 0xff800000, w, h);
    }

    private LinearGradient buildShader(int colStart, int colEnd, int width, int height) {
        return new LinearGradient(0,0,width, height,new int[] { colStart, colEnd, colStart}, new float[] { 0, 0.2f, 1}, Shader.TileMode.REPEAT);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
        canvas.restore();

        if (this.contact != null && !TextUtils.isEmpty(contact.getPhoneNumber())) {
            StatusUpdate status = CircleOf6Application.getInstance().getContactStatus(this.contact);
            if (status != null) {
                if (!status.isSeen()) {
                    paintBorder.setShader(shaderUnread);
                } else {
                    paintBorder.setShader(shaderNormal);
                }
                canvas.drawPath(clipPath, paintBorder);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, getWidth(), getHeight());
        }

    }
}
