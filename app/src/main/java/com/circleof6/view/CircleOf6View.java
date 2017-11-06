package com.circleof6.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.util.ConstantsAnalytics;
import com.circleof6.view.util.ConstantsView;
import com.circleof6.view.util.DrawUtils;
import com.circleof6.view.util.GeometricUtils;
import com.circleof6.view.util.OnClickListenerCircleOf6View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircleOf6View extends View {

    public static final String TAG = CircleOf6View.class.getSimpleName();


    private int externalCircleColor, internalCircleColor, centerTextColor, detailsColor;
    private int resourceImageAddContact, resourceImageLocation, resourceImagePhone, resourceImageMessage, resourceImageInfo, resourceImageDefaultContact, resourceImage911;

    private int externalRadius, internalRadius, centerRadius, contactsRadius, locationRadius, phoneRadius, messageRadius, infoRadius, b911Radius;
    private int perimeterAskElements;

    private PointF centerGlobal;

    private List<PointF> centerCirclesContacts, centerImagesContacts;

    private PointF centerImageLocation, centerCircleLocation;
    private PointF centerImagePhone, centerCirclePhone;
    private PointF centerImageMessage, centerCircleMessage;

    private ArrayList<Point> startLinesAsk, endLinesAsk;

    private PointF centerTextAddContacts, centerTextAs, centerTextClose;

    private List<PointF> centerNamesContacts;

    private PointF centerImageInfo, centerCircleInfo;


    private PointF centerImage911, centerCircle911;

    private int heightTextAddContacts, heightTextAs;

    private Paint paintExternalCircle;
    private Paint paintInternalCircle;
    private Paint paintStrokeInternalCircle;
    private Paint paintDetailsCircle;

    private Paint paintAddContactsCenterText, paintAsCenterText, paintCloseCenterText;

    private Paint paintTriangle;

    private Paint paintsNamesContactsLeft, paintsNamesContactsRight;

    private Bitmap imageAddContact, imageLocation, imagePhone, imageMessage, imageDefaultContact, imageInfo, image911;
    private String textCloseAskCircle;
    private String[] textAddContact, textAskCircle;

    private Path pathTriangleAsCircle;
    private Path pathTriangleCancelCircle;


    private OnClickListenerCircleOf6View onClickListener;

    private ArrayList<Contact> contacts;

    private boolean showAsFriendsActions, isTextAddContacts, enableClicks, enableButton911;




    public CircleOf6View(Context context, AttributeSet attrs)
    {
        super(context, attrs);


        initVariables();
        TypedArray attributes = context.getTheme()
                                       .obtainStyledAttributes(attrs, R.styleable.CircleOf6View, 0,
                                                               0);
        try
        {
            obtainAttributes(attributes);
        }
        finally
        {
            attributes.recycle();
        }

        initPaints();
        initImages();
    }


    private void initVariables()
    {
        this.contacts = new ArrayList<>(
                Collections.<Contact> nCopies(ConstantsView.NUM_CONTACTS, null));

        isTextAddContacts = true;
        showAsFriendsActions = false;
        enableClicks = true;
        enableButton911 = false;
    }

    private void obtainAttributes(TypedArray attributes)
    {
        externalCircleColor = attributes
                .getColor(R.styleable.CircleOf6View_external_circle_color, 0);
        internalCircleColor = attributes
                .getColor(R.styleable.CircleOf6View_internal_circle_color, 0);

        centerTextColor = attributes.getColor(R.styleable.CircleOf6View_center_text_color, 0);

        detailsColor = attributes.getColor(R.styleable.CircleOf6View_details_color, 0);

        resourceImageAddContact = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_add_contact,
                        R.drawable.app_icon);

        resourceImageLocation = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_location, R.drawable.app_icon);
        resourceImagePhone = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_phone, R.drawable.app_icon);
        resourceImageMessage = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_message, R.drawable.app_icon);

        textAddContact = attributes.getString(R.styleable.CircleOf6View_text_add_contacts)
                                   .split("\n");
        textAskCircle = attributes.getString(R.styleable.CircleOf6View_text_ask_circle).split("\n");
        textCloseAskCircle = attributes.getString(R.styleable.CircleOf6View_text_close_ask_circle);

        resourceImageDefaultContact = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_default_contact,
                        R.drawable.app_icon);

        resourceImageInfo = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_info, R.drawable.app_icon);

        resourceImage911 = attributes
                .getResourceId(R.styleable.CircleOf6View_src_image_911, R.drawable.app_icon);


    }

    public void initPaints()
    {
        paintExternalCircle = DrawUtils.createPaintFill(externalCircleColor);

        paintInternalCircle = DrawUtils.createPaintFill(internalCircleColor);
        paintStrokeInternalCircle = DrawUtils
                .createPaintStroke(detailsColor, ConstantsView.STROKE_WIDTH_INTERNAL_CIRCLE);


        paintDetailsCircle = DrawUtils.createPaintFill(detailsColor);


        paintAddContactsCenterText = DrawUtils.cratePaintCenterText(getContext(), centerTextColor,
                ConstantsView.TEXT_SIZE_CENTER_ADD_CONTACTS);

        paintAsCenterText = DrawUtils.cratePaintCenterText(getContext(), detailsColor,
                ConstantsView.TEXT_SIZE_CENTER_ASK);

        paintCloseCenterText = DrawUtils.cratePaintCenterText(getContext(), centerTextColor,
                ConstantsView.TEXT_SIZE_CENTER_CLOSE);

        paintTriangle = DrawUtils.createPaintFill(centerTextColor);


        paintsNamesContactsLeft = DrawUtils.cratePaintNamesContacts(getContext(), detailsColor,
                ConstantsView.TEXT_SIZE_NAMES_CONTACS,
                Paint.Align.LEFT);
        paintsNamesContactsRight = DrawUtils.cratePaintNamesContacts(getContext(), detailsColor,
                ConstantsView.TEXT_SIZE_NAMES_CONTACS,
                Paint.Align.RIGHT);
    }

    private void initImages() {
        imageAddContact = BitmapFactory.decodeResource(getResources(), resourceImageAddContact);
        imageLocation = BitmapFactory.decodeResource(getResources(), resourceImageLocation);
        imagePhone = BitmapFactory.decodeResource(getResources(), resourceImagePhone);
        imageMessage = BitmapFactory.decodeResource(getResources(), resourceImageMessage);
        imageDefaultContact = BitmapFactory.decodeResource(getResources(),
                                                           resourceImageDefaultContact);
        imageInfo = BitmapFactory.decodeResource(getResources(), resourceImageInfo);
        image911 = BitmapFactory.decodeResource(getResources(), resourceImage911);

    }

    public void setOnClickListener(OnClickListenerCircleOf6View onClickListener) {
        this.onClickListener = onClickListener;
    }


    public void setContacts(ArrayList<Contact> contacts) {
        if (contacts.size() > 6)
            throw new IllegalArgumentException("CircleOf6View only support 6 contacts, check that the arraylist size is 6 ");
        else
        {
            this.contacts = contacts;
            invalidate();
        }
    }

    public void addContact(Contact contact, int index) {
        if (index >= 6 || index < 0)
            throw new ArrayIndexOutOfBoundsException("CircleOf6View only support 6 contacts, check that the index is between 0 and 5");
        else {

            TrackEventContact(index);
            contacts.set(index, contact);
            invalidate();
        }
    }

    private void TrackEventContact(int index) {
        if (TextUtils.isEmpty(contacts.get(index).getName())) {
            CircleOf6Application.defaultLabelTrackingEvent(getContext(),
                                                           ConstantsAnalytics.ACTION_CONTACT_ADDED);
        } else {
            CircleOf6Application.defaultLabelTrackingEvent(getContext(),
                                                           ConstantsAnalytics.ACTION_CONTACT_CHANGED);
        }
    }

    public void setIsTextAddContacts(boolean isTextAddContacts)
    {
        this.isTextAddContacts = isTextAddContacts;
    }

    public void setEnableClicks(boolean enableClicks)
    {
        this.enableClicks = enableClicks;
    }

    public void setEnableButton911(boolean enableButton911)
    {
        this.enableButton911 = enableButton911;
    }

    private void setupCenter(int viewWight, int viewHeight) {
        int viewWidthHalf = viewWight / 2;
        int viewHeightHalf = viewHeight / 2;
        centerGlobal = new PointF(viewWidthHalf, viewHeightHalf);
    }

    private void setupRadius() {
        if (centerGlobal.x > centerGlobal.y) {
            externalRadius = (int) (centerGlobal.y * ConstantsView.EXTERNAL_CIRCLE_SCALE);
            internalRadius = (int) (centerGlobal.y * ConstantsView.INTERNAL_CIRCLE_SCALE);
            centerRadius = (int) (centerGlobal.y * ConstantsView.CENTER_CIRCLE_SCALE);
            perimeterAskElements = (int) (centerGlobal.y * ConstantsView.ASK_CIRCLES_SCALE);
        } else {
            externalRadius = (int) (centerGlobal.x * ConstantsView.EXTERNAL_CIRCLE_SCALE);
            internalRadius = (int) (centerGlobal.x * ConstantsView.INTERNAL_CIRCLE_SCALE);
            centerRadius = (int) (centerGlobal.x * ConstantsView.CENTER_CIRCLE_SCALE);
            perimeterAskElements = (int) (centerGlobal.x * ConstantsView.ASK_CIRCLES_SCALE);
        }
    }

    private void setupCenterCirclesContacts() {
        centerCirclesContacts = new ArrayList<>();
        centerImagesContacts = new ArrayList<>();
        for(int i = 0; i < ConstantsView.NUM_CONTACTS; i++)
        {
            double angle = -i * 2 * Math.PI / 6;

            float xCenterCircle = (float) (internalRadius * Math.cos(angle) + centerGlobal.x);
            float yCenterCircle = (float) (internalRadius * Math.sin(angle) + centerGlobal.y);
            float xCenterImage = xCenterCircle - (imageAddContact.getWidth() / 2.0f);
            float yCenterImage = yCenterCircle - (imageAddContact.getHeight() / 2.0f);

            centerCirclesContacts.add(new PointF(xCenterCircle, yCenterCircle));
            centerImagesContacts.add(new PointF(xCenterImage, yCenterImage));
        }
    }

    private void setupCenterImages() {

        double angle;
        float xCenterCircle;
        float yCenterCircle;
        float xCenterImage;
        float yCenterImage;

        angle = Math.toRadians(150);
        xCenterCircle = (float) (perimeterAskElements * Math.cos(angle) + centerGlobal.x);
        yCenterCircle = (float) (perimeterAskElements * Math.sin(angle) + centerGlobal.y);
        xCenterImage = xCenterCircle - (imageLocation.getWidth() / 2.0f);
        yCenterImage = yCenterCircle - (imageLocation.getHeight() / 2.0f);

        centerCircleLocation = new PointF(xCenterCircle, yCenterCircle);
        centerImageLocation = new PointF(xCenterImage, yCenterImage);

        angle = Math.toRadians(90);
        xCenterCircle = (float) (perimeterAskElements * Math.cos(angle) + centerGlobal.x);
        yCenterCircle = (float) (perimeterAskElements * Math.sin(angle) + centerGlobal.y);
        xCenterImage = xCenterCircle - (imagePhone.getWidth() / 2.0f);
        yCenterImage = yCenterCircle - (imagePhone.getHeight() / 2.0f);

        centerCirclePhone = new PointF(xCenterCircle, yCenterCircle);
        centerImagePhone = new PointF(xCenterImage, yCenterImage);

        angle = Math.toRadians(30);
        xCenterCircle = (float) (perimeterAskElements * Math.cos(angle) + centerGlobal.x);
        yCenterCircle = (float) (perimeterAskElements * Math.sin(angle) + centerGlobal.y);
        xCenterImage = xCenterCircle - (imageMessage.getWidth() / 2.0f);
        yCenterImage = yCenterCircle - (imageMessage.getHeight() / 2.0f);

        centerCircleMessage = new PointF(xCenterCircle, yCenterCircle);
        centerImageMessage = new PointF(xCenterImage, yCenterImage);

        angle = Math.toRadians(270);
        xCenterCircle = (float) (perimeterAskElements * Math.cos(angle) + centerGlobal.x);
        yCenterCircle = (float) (perimeterAskElements * Math.sin(angle) + centerGlobal.y);
        xCenterImage = xCenterCircle - (imageInfo.getWidth() / 2.0f);
        yCenterImage = yCenterCircle - (imageInfo.getHeight() / 2.0f);

        centerCircleInfo = new PointF(xCenterCircle, yCenterCircle);
        centerImageInfo = new PointF(xCenterImage, yCenterImage);


        xCenterCircle = (float) (externalRadius * Math.cos(angle) + centerGlobal.x);
        yCenterCircle = (float) ((externalRadius + image911.getHeight()) * Math
                .sin(angle) + centerGlobal.y);
        xCenterImage = xCenterCircle - (image911.getWidth() / 2.0f);
        yCenterImage = yCenterCircle - (image911.getHeight() / 2.0f);

        centerCircle911 = new PointF(xCenterCircle, yCenterCircle);
        centerImage911 = new PointF(xCenterImage, yCenterImage);

    }

    private void setupRadiusCirclesWithImages()
    {
        contactsRadius = imageAddContact.getWidth();
        locationRadius = (int) (imageLocation.getWidth() * ConstantsView.RADIUS_CIRCLES_ASK_SCALE);
        phoneRadius = (int) (imagePhone.getWidth() * ConstantsView.RADIUS_CIRCLES_ASK_SCALE);
        messageRadius = (int) (imageMessage.getWidth() * ConstantsView.RADIUS_CIRCLES_ASK_SCALE);

        infoRadius = imageInfo.getWidth();
        b911Radius = image911.getWidth();


    }

    private void setupLinesAsk() {
        startLinesAsk = new ArrayList<>();
        endLinesAsk = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(150 - (60 * i));
            double catAdj = Math.cos(angle);
            double catOps = Math.sin(angle);

            int xPoint = (int) ((centerRadius - 1) * catAdj + centerGlobal.x);
            int yPoint = (int) ((centerRadius - 1) * catOps + centerGlobal.y);
            startLinesAsk.add(new Point(xPoint, yPoint));

            int radioRef = perimeterAskElements - (i == 0
                    ? locationRadius
                    : i == 1
                    ? phoneRadius
                    : messageRadius) + 1;


            xPoint = (int) (radioRef * catAdj + centerGlobal.x);
            yPoint = (int) (radioRef * catOps + centerGlobal.y);

            endLinesAsk.add(new Point(xPoint, yPoint));

        }
    }

    private void setupCenterTextAs()
    {
        Rect dimesText = new Rect();

        paintAsCenterText.getTextBounds(textAskCircle[0], 0, textAskCircle[0].length(), dimesText);

        heightTextAs = dimesText.height();

        float y = centerGlobal.y + heightTextAs / 2.0f;
        y -= dimesText.bottom;
        y -= heightTextAs * (textAskCircle.length - 1) * 0.55f;

        centerTextAs = new PointF(centerGlobal.x, y);
    }

    private void setupCenterTextAddContacts()
    {
        Rect dimesText = new Rect();

        paintAddContactsCenterText
                .getTextBounds(textAddContact[0], 0, textAddContact[0].length(), dimesText);

        heightTextAddContacts = dimesText.height();

        float y = centerGlobal.y + heightTextAddContacts / 2.0f;
        y -= dimesText.bottom;
        y -= heightTextAddContacts * (textAddContact.length - 1) * 0.55f;

        centerTextAddContacts = new PointF(centerGlobal.x, y);
    }

    private void setupCenterTextClose()
    {

        Rect dimesText = new Rect();

        paintCloseCenterText
                .getTextBounds(textCloseAskCircle, 0, textCloseAskCircle.length(), dimesText);
        float y = centerGlobal.y + dimesText.height() / 2.0f - dimesText.bottom;

        centerTextClose = new PointF(centerGlobal.x, y);
    }


    private void setupCenterAsTriangle()
    {
        pathTriangleAsCircle = new Path();
        float x, y;
        int px = DrawUtils.pxFromDp(getContext(), ConstantsView.SIZE_TRIANGLE);

        x = centerGlobal.x - px / 2;

        y = centerTextAs.y + 1.1f * heightTextAs * (textAskCircle.length - 1) + px / 3.0f;

        pathTriangleAsCircle.moveTo(x, y);

        x = x + px;
        pathTriangleAsCircle.lineTo(x, y);

        x = centerGlobal.x;
        y = y + px / 2.0f;
        pathTriangleAsCircle.lineTo(x, y);
    }


    private void setupCenterCancelTriangle()
    {
        pathTriangleCancelCircle = new Path();
        float x, y;
        int px = DrawUtils.pxFromDp(getContext(), ConstantsView.SIZE_TRIANGLE);

        x = centerGlobal.x;
        y = centerTextAs.y + 1.1f * heightTextAs * (textAskCircle.length - 1) + px / 3.0f;

        pathTriangleCancelCircle.moveTo(x, y);

        x = centerGlobal.x + px / 2.0f;
        y = y + px / 2.0f;

        pathTriangleCancelCircle.lineTo(x, y);

        x = centerGlobal.x - px / 2.0f;

        pathTriangleCancelCircle.lineTo(x, y);
    }




    private void setupCenterNamesText() {
        centerNamesContacts = new ArrayList<>();
        float x, y;
        PointF centerContact;

        double angleRadians = Math.toRadians(ConstantsView.ANGLE_POSITION_NAMES_CONTACTS);

        x = (float) (ConstantsView.POSITION_NAMES_CONTACS_SCALE * contactsRadius * Math
                .cos(angleRadians));
        y = (float) (ConstantsView.POSITION_NAMES_CONTACS_SCALE * contactsRadius * Math
                .sin(angleRadians));

        Rect dimesText = new Rect();

        paintsNamesContactsLeft
                .getTextBounds(ConstantsView.DEFAULT_TEXT, 0, ConstantsView.DEFAULT_TEXT.length(),
                               dimesText);
        float heightText = dimesText.height() - 2 * dimesText.bottom;

        centerContact = centerCirclesContacts.get(0);
        centerNamesContacts.add(new PointF(centerContact.x + x, centerContact.y - y));
        centerContact = centerCirclesContacts.get(1);
        centerNamesContacts.add(new PointF(centerContact.x + x, centerContact.y - y));
        centerContact = centerCirclesContacts.get(2);
        centerNamesContacts.add(new PointF(centerContact.x - x, centerContact.y - y));
        centerContact = centerCirclesContacts.get(3);
        centerNamesContacts.add(new PointF(centerContact.x - x, centerContact.y - y));

        centerContact = centerCirclesContacts.get(4);
        centerNamesContacts.add(new PointF(centerContact.x - x, centerContact.y + y + heightText));
        centerContact = centerCirclesContacts.get(5);
        centerNamesContacts.add(new PointF(centerContact.x + x, centerContact.y + y + heightText));

    }

    private void drawExternalCircle(Canvas canvas)
    {
        canvas.drawCircle(centerGlobal.x, centerGlobal.y, externalRadius, paintExternalCircle);
    }

    private void drawInternalCircle(Canvas canvas)
    {

        canvas.drawCircle(centerGlobal.x, centerGlobal.y, internalRadius, paintInternalCircle);
        canvas.drawCircle(centerGlobal.x, centerGlobal.y, internalRadius,
                          paintStrokeInternalCircle);
    }

    private void drawCenterElements(Canvas canvas)
    {

        if(showAsFriendsActions)
        {
            drawAskCenterCircle(canvas);
            drawCenterTextCloseAsActions(canvas);
            drawAskActions(canvas);
        }
        else
        {
            drawCenterCircle(canvas);
            drawCenterText(canvas);
        }
    }

    private void drawCenterCircle(Canvas canvas)
    {
        canvas.drawCircle(centerGlobal.x, centerGlobal.y, centerRadius, paintDetailsCircle);
    }

    private void drawCenterText(Canvas canvas)
    {

        if(isTextAddContacts)
        {
            drawCenterTextAddContact(canvas);
        }
        else
        {
            drawCenterTextAs(canvas);
        }
    }

    private void drawCenterTextAddContact(Canvas canvas)
    {

        for(int i = 0; i < textAddContact.length; i++)
        {
            canvas.drawText(textAddContact[i], centerTextAddContacts.x,
                            centerTextAddContacts.y + 1.1f * heightTextAddContacts * i,
                            paintAddContactsCenterText);

        }

    }

    private void drawCenterTextAs(Canvas canvas)
    {

        for(int i = 0; i < textAskCircle.length; i++)
        {
            canvas.drawText(textAskCircle[i], centerTextAs.x,
                            centerTextAs.y + 1.1f * heightTextAs * i, paintCloseCenterText);

        }

        canvas.drawPath(pathTriangleAsCircle, paintTriangle);

    }

    private void drawAskCenterCircle(Canvas canvas) {
        canvas.drawCircle(centerGlobal.x, centerGlobal.y, centerRadius, paintDetailsCircle);
    }

    private void drawCenterTextCloseAsActions(Canvas canvas)
    {
        drawAskCenterCircle(canvas);
        canvas.drawText(textCloseAskCircle, centerTextClose.x, centerTextClose.y,
                        paintCloseCenterText);

        canvas.drawPath(pathTriangleCancelCircle, paintTriangle);

    }

    private void drawAskActions(Canvas canvas) {
        for(int i = 0; i < ConstantsView.NUM_LINES_ASK; i++)
        {
            canvas.drawLine(startLinesAsk.get(i).x, startLinesAsk.get(i).y, endLinesAsk.get(i).x,
                            endLinesAsk.get(i).y, paintStrokeInternalCircle);
        }

        canvas.drawCircle(centerCircleLocation.x, centerCircleLocation.y, locationRadius,
                          paintDetailsCircle);
        canvas.drawBitmap(imageLocation, centerImageLocation.x, centerImageLocation.y, null);

        canvas.drawCircle(centerCirclePhone.x, centerCirclePhone.y, phoneRadius,
                          paintDetailsCircle);
        canvas.drawBitmap(imagePhone, centerImagePhone.x, centerImagePhone.y, null);

        canvas.drawCircle(centerCircleMessage.x, centerCircleMessage.y, messageRadius,
                          paintDetailsCircle);
        canvas.drawBitmap(imageMessage, centerImageMessage.x, centerImageMessage.y, null);

    }


    private void drawContacts(Canvas canvas)
    {
        drawImageContacts(canvas);
        drawNamesContacts(canvas);
    }



    private void drawImageContacts(Canvas canvas)
    {

        for (int i = 0; i < centerCirclesContacts.size(); i++) {
            PointF centerCircle = centerCirclesContacts.get(i);
            PointF centerImage = centerImagesContacts.get(i);
            Contact contact = contacts.get(i);

            try
            {
                if (contact == null || contact.getPhoneNumber().isEmpty())
                {
                    canvas.drawCircle(centerCircle.x, centerCircle.y, contactsRadius,
                                      paintDetailsCircle);
                    canvas.drawBitmap(imageAddContact, centerImage.x, centerImage.y, null);
                }
                else if(/*!(new File(contact.getPhoto()).exists()) || */contact.getPhoto().equals(ConstantsView.DEFAULT_PHOTO))
                {
                    showDefaultContactPhoto(canvas, centerCircle);
                }
                else
                {

                    Log.d(TAG, contact.getPhoto());
                    Bitmap imageContact = GeometricUtils
                            .redimenBitmap(DrawUtils.getBitmapFromPath(contact.getPhoto()),
                                           contactsRadius * 2);
                    PointF centerContactImage = new PointF(centerCircle.x - imageContact.getWidth() / 2,
                                                           centerCircle.y - imageContact
                                                                   .getHeight() / 2);

                    canvas.drawBitmap(imageContact, centerContactImage.x, centerContactImage.y, null);
                    canvas.drawCircle(centerCircle.x, centerCircle.y, contactsRadius,
                                      paintStrokeInternalCircle);

                }
            }
            catch(Exception e)
            {
                showDefaultContactPhoto(canvas, centerCircle);
            }

        }
    }

    private void showDefaultContactPhoto(Canvas canvas, PointF centerCircle)
    {
        canvas.drawCircle(centerCircle.x, centerCircle.y, contactsRadius,
                          paintDetailsCircle);
        canvas.drawBitmap(imageDefaultContact, centerCircle.x - imageDefaultContact.getWidth() / 2, centerCircle.y - imageDefaultContact.getHeight() / 2, null);
    }

    private void drawNamesContacts(Canvas canvas) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i) != null) {
                PointF centerContact = centerNamesContacts.get(i);
                Paint paintNamesContact = centerContact.x < centerGlobal.x
                        ? paintsNamesContactsRight
                        : paintsNamesContactsLeft;

                canvas.drawText(contacts.get(i).getName(), centerContact.x, centerContact.y,
                                paintNamesContact);
            }
        }
    }

    private void drawOtherButtons(Canvas canvas)
    {
        drawImageInfo(canvas);
        if(enableButton911)
        {
            drawImage911(canvas);
        }
    }

    private void drawImageInfo(Canvas canvas)
    {
        canvas.drawBitmap(imageInfo, centerImageInfo.x, centerImageInfo.y, null);
    }


    private void drawImage911(Canvas canvas)
    {
        canvas.drawBitmap(image911, centerImage911.x, centerImage911.y, null);

    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP && enableClicks && onClickListener != null)
        {
            PointF pointClicked = new PointF(event.getX(), event.getY());

            if(GeometricUtils.isPointInsideCircle(centerGlobal, centerRadius, pointClicked) && !isTextAddContacts)
            {
                showAsFriendsActions = ! showAsFriendsActions;
                invalidate();
            }
            else if(GeometricUtils.isPointInsideCircle(centerCircleLocation, locationRadius,
                                                       pointClicked) && showAsFriendsActions)
            {
                onClickListener.locationClicked();
            }

            else if(GeometricUtils.isPointInsideCircle(centerCircleMessage, messageRadius,
                                                       pointClicked) && showAsFriendsActions)
            {
                onClickListener.messageClicked();
            }

            else if(GeometricUtils.isPointInsideCircle(centerCirclePhone, phoneRadius,
                                                       pointClicked) && showAsFriendsActions)
            {
                onClickListener.phoneClicked();
            }

            else if(GeometricUtils.isPointInsideCircle(centerCircleInfo, infoRadius, pointClicked))
            {
                onClickListener.informationClicked();
            }
            else if(GeometricUtils.isPointInsideCircle(centerCircle911, b911Radius, pointClicked) && enableButton911)
            {
                onClickListener.button911Clicked();
            }
            else
            {
                for(int i = 0; i < centerCirclesContacts.size(); i++)
                {
                    PointF circleCenter = centerCirclesContacts.get(i);
                    if(GeometricUtils
                            .isPointInsideCircle(circleCenter, contactsRadius, pointClicked))
                    {
                        onClickListener.contactClicked(i);
                        break;
                    }
                }
            }

        }

        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        setupCenter(w, h);
        setupRadius();

        setupCenterCirclesContacts();

        setupCenterImages();
        setupRadiusCirclesWithImages();
        setupLinesAsk();

        setupCenterTextAddContacts();
        setupCenterTextAs();
        setupCenterAsTriangle();
        setupCenterTextClose();
        setupCenterCancelTriangle();
        setupCenterNamesText();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawExternalCircle(canvas);
        drawInternalCircle(canvas);

        drawCenterElements(canvas);

        drawContacts(canvas);

        drawOtherButtons(canvas);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleBitmaps();
    }

    public int getWidthCircleContact()
    {
        return contactsRadius * 2;
    }

    private void recycleBitmaps() {
        imageAddContact.recycle();
        imageDefaultContact.recycle();
        imageLocation.recycle();
        imagePhone.recycle();
        imageMessage.recycle();
        imageInfo.recycle();
        image911.recycle();
    }


}
