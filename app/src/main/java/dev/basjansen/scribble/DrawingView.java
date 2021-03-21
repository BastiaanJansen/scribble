package dev.basjansen.scribble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private OnDrawListener onDrawListener;

    public DrawingView(Context context, OnDrawListener onDrawListener) {
        super(context);

        this.onDrawListener = onDrawListener;

        drawPath = new Path();
        drawPaint = new Paint();

        paintColor = Color.BLACK;

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        canvasPaint.setColor(paintColor);
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.lineTo(touchX, touchY);
                onDrawListener.onDraw(new OnDrawEvent(touchX, touchY, paintColor));
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

    public void setColor(int color) {
        paintColor = color;
        drawPaint.setColor(color);
    }

    public void update(OnDrawEvent event) {
        drawPath.moveTo(event.getX(), event.getY());
        drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        drawCanvas.drawPath(drawPath, drawPaint);
    }
}
