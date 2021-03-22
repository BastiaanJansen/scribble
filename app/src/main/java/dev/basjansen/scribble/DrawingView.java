package dev.basjansen.scribble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private float strokeWidth;
    private int drawColor;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private boolean erase;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.erase = false;
        this.strokeWidth = 15;
        this.drawColor = Color.BLACK;
        setup();
    }

    public void setup() {
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(drawColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }

    public void setErase(boolean erase) {
        this.erase = erase;
        this.drawPaint = new Paint();

        setup();

        if (erase) {
            PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.WHITE, mode);

            drawPaint.setColorFilter(porterDuffColorFilter);
            drawPaint.setXfermode(new PorterDuffXfermode(mode));
            drawPaint.setColor(Color.WHITE);
        }
    }

    public void clear() {
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    public boolean getErase() {
        return erase;
    }

    public void setColor(int color) {
        this.drawColor = color;
        drawPaint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        drawPaint.setStrokeWidth(width);
    }

    public Bitmap getCanvasBitmap() {
        return canvasBitmap;
    }

    public Canvas getDrawCanvas() {
        return getDrawCanvas();
    }
}
