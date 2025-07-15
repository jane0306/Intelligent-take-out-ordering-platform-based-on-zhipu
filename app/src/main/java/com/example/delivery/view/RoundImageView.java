package com.example.delivery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.delivery.R;

public class RoundImageView extends AppCompatImageView {

    private float radius = 100;
    private boolean isCircular;
    private Paint paint;
    private RectF rectF;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyleable(context, attrs);
        init();
    }

    private void initStyleable(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        radius = a.getDimension(R.styleable.RoundImageView_riv_radius, 0);
        isCircular = a.getBoolean(R.styleable.RoundImageView_riv_is_circular, false);
        a.recycle();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        isCircular = false;
        invalidate();
    }

    public void setCircular(boolean circular) {
        isCircular = circular;
        if (isCircular) {
            radius = 0; // Ensure cornerRadius is 0 for circular image
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            super.onDraw(canvas);
            return;
        }

        Bitmap bitmap = getBitmapFromDrawable();
        if (bitmap == null) {
            super.onDraw(canvas);
            return;
        }

        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        rectF.set(0, 0, getWidth(), getHeight());

        if (isCircular) {
            canvas.drawOval(rectF, paint);
        } else {
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }

    private Bitmap getBitmapFromDrawable() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
