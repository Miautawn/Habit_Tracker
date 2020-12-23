package vu.mif.habit_tracker.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.lang.Math;

import vu.mif.habit_tracker.R;

public class CircularProgressBar extends View{

    private final float MAX_VALUE = 100f;
    private final float DEFAULT_START_ANGLE = 270f;

    // Properties
    private ValueAnimator progressAnimator;

    // View
    private Drawable image;
    private final Drawable mCustomImage;
    private final RectF rectF = new RectF();
    private final Rect rectMain = new Rect();
    private final RectF rectFImg = new RectF();
    private final Rect rectImg = new Rect();
    private final Paint foregroundPaint = new Paint();

    // Attributes
    private float progress = 60f;
    private float progressMax = 100f;
    private float progressBarWidth;
    private int progressBarColor;
    private float startAngle = DEFAULT_START_ANGLE;

    // Setters
    public void setImage(Drawable drawable){
        if (drawable == null) {
            image = null;
        } else {
            image = drawable;
        }
    }
    public void setProgress(float value) {
        progress = (Math.min(value, progressMax));
        invalidate();
    }
    public void setProgressMax(float value) {
        progressMax = ((value >= 0) ? value : MAX_VALUE);
    }
    public void setProgressBarWidth(float value) {
        progressBarWidth = dpToPx(value);
        foregroundPaint.setStrokeWidth(progressBarWidth);
        requestLayout();
        invalidate();
    }
    public void setProgressBarColor(int value){
        progressBarColor = value;
        manageColor();
        invalidate();
    }
    public void setStartAngle(float value) {
        float angle = value + DEFAULT_START_ANGLE;
        while (angle > 360) {
            angle -= 360;
        }
        if (angle < 0) {
            startAngle = 0f;
        } else if(angle > 360) {
            startAngle = 360f;
        } else {
            startAngle = angle;
        }
        invalidate();
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeCap(Paint.Cap.ROUND);

        mCustomImage = ResourcesCompat.getDrawable(getResources(), R.drawable.habit_info_bg, null);

        setProgressBarColor(Color.parseColor("#d82e76"));

        setProgressBarWidth(getResources().getDimension(R.dimen.default_stroke_width));

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Load attributes
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularProgressBar,
                0,0);

        // Image
        setImage(attributes.getDrawable(R.styleable.CircularProgressBar_progressBar_image));

        // Progress value
        setProgress(attributes.getFloat(
                R.styleable.CircularProgressBar_progressBar_progress, progress));
        setProgressMax(attributes.getFloat(
                R.styleable.CircularProgressBar_progressBar_progress_max, progressMax));

        // Stroke width
        setProgressBarWidth(dpToPx(attributes.getDimension(
                R.styleable.CircularProgressBar_progressBar_width, progressBarWidth)));

        // Color
        setProgressBarColor(attributes.getColor(
                R.styleable.CircularProgressBar_progressBar_color, progressBarColor));

        // Angle
        setStartAngle(attributes.getFloat(
                R.styleable.CircularProgressBar_progressBar_startAngle, 0f));

        attributes.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (progressAnimator != null) progressAnimator.cancel();
    }

    // Draw event
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        manageColor();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCustomImage.setBounds(rectMain);
        mCustomImage.draw(canvas);

        if (image != null) {
            image.setBounds(rectImg);
            image.draw(canvas);
        }

        float realProgress = progress * MAX_VALUE / progressMax;
        float angle = 360 * realProgress / 100;

        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    public void manageColor() {
        foregroundPaint.setColor(progressBarColor);
    }

    // Measure event
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = min * 0.18f;
        float insideImage = min * 0.36f;
        rectF.set(highStroke, highStroke,
                min - highStroke, min - highStroke);
        rectMain.set(0, 0, min, min);
        rectFImg.set(insideImage, insideImage, min-insideImage, min-insideImage);
        rectFImg.round(rectImg);
    }

    // Progress with animation
    public void setProgressWithAnimation(float progress, long duration){
        if (progressAnimator != null) progressAnimator.cancel();
        progressAnimator = ValueAnimator.ofFloat(this.progress, progress);
        progressAnimator.setDuration(duration);
        progressAnimator.addUpdateListener(animator -> setProgress((float)animator.getAnimatedValue()));
        progressAnimator.start();
    }

    // Utils
    private float dpToPx(float value) {
        return value * Resources.getSystem().getDisplayMetrics().density;
    }
}
