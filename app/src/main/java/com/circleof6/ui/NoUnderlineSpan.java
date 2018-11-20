package com.circleof6.ui;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by N-Pex on 2018-11-20.
 */
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setUnderlineText(false);
    }
}