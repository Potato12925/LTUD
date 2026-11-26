package com.example.ltud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

public class KnowledgeOrbView extends View {
    private static final int NODE_COUNT = 350;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF frame = new RectF();
    private final Path clip = new Path();
    private RadialGradient outerGlow;
    private RadialGradient innerGlow;
    private final float[] nodeX = new float[NODE_COUNT];
    private final float[] nodeY = new float[NODE_COUNT];
    private final float[] nodeZ = new float[NODE_COUNT];
    private final int[] nodeColors = new int[NODE_COUNT];
    private float rotationY = 0.3f;
    private float lastTouchX;

    public KnowledgeOrbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] colors = {
                ContextCompat.getColor(context, R.color.welcome_blue),
                ContextCompat.getColor(context, R.color.welcome_cyan),
                ContextCompat.getColor(context, R.color.welcome_rose),
                ContextCompat.getColor(context, R.color.welcome_amber),
                ContextCompat.getColor(context, R.color.welcome_mint)
        };
        // A deterministic point cloud keeps the illustration stable across redraws.
        for (int index = 0; index < NODE_COUNT; index++) {
            double y = 1.0 - 2.0 * (index + 0.5) / NODE_COUNT;
            double ring = Math.sqrt(1.0 - y * y);
            double angle = index * Math.PI * (3.0 - Math.sqrt(5.0));
            nodeX[index] = (float) (ring * Math.cos(angle));
            nodeY[index] = (float) y;
            nodeZ[index] = (float) (ring * Math.sin(angle));
            nodeColors[index] = colors[index % colors.length];
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        float radius = Math.min(width, height) * 0.38f;
        frame.set(0, 0, width, height);
        clip.reset();
        float corner = getResources().getDimension(R.dimen.welcome_hero_corner);
        clip.addRoundRect(frame, corner, corner, Path.Direction.CW);
        outerGlow = new RadialGradient(width / 2f, height / 2f, radius * 1.6f,
                ContextCompat.getColor(getContext(), R.color.welcome_glow_outer),
                Color.TRANSPARENT, Shader.TileMode.CLAMP);
        innerGlow = new RadialGradient(width / 2f, height / 2f, radius * 0.4f,
                ContextCompat.getColor(getContext(), R.color.welcome_glow_inner),
                Color.TRANSPARENT, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) * 0.38f;
        canvas.save();
        canvas.clipPath(clip);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.welcome_hero_background));

        paint.setShader(outerGlow);
        canvas.drawCircle(width / 2, height / 2, radius * 1.6f, paint);
        paint.setShader(null);
        paint.setAlpha(255);
        for (int index = 0; index < NODE_COUNT; index++) {
            float rotatedX = (float) (nodeX[index] * Math.cos(rotationY)
                    + nodeZ[index] * Math.sin(rotationY));
            float rotatedZ = (float) (-nodeX[index] * Math.sin(rotationY)
                    + nodeZ[index] * Math.cos(rotationY));
            float perspective = 1.0f + rotatedZ * 0.15f;
            float x = width / 2 + rotatedX * radius * perspective;
            float y = height / 2 + nodeY[index] * radius * perspective;
            paint.setColor(nodeColors[index]);
            paint.setAlpha((int) (115 + 120 * (rotatedZ + 1) / 2));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, rotatedZ > 0 ? 2.4f : 1.3f, paint);
        }
        paint.setAlpha(255);
        paint.setShader(innerGlow);
        canvas.drawCircle(width / 2, height / 2, radius * 0.4f, paint);
        paint.setShader(null);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            lastTouchX = event.getX();
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            rotationY += (event.getX() - lastTouchX) / Math.max(getWidth(), 1) * 3;
            lastTouchX = event.getX();
            invalidate();
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            performClick();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
