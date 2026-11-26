package com.example.ltud;

import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class KnowledgeOrbView extends View {
    private static final int NODE_COUNT = 1200;
    private static final int CONNECTED_NODE_COUNT = 450;
    private static final int CONNECTION_LIMIT = 750;
    private static final float SPHERE_RADIUS = 105f;
    private static final float CONNECTION_DISTANCE = 30f;
    private static final long ROTATION_DURATION_MS = 36000L;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF frame = new RectF();
    private final Path clip = new Path();
    private final float[] nodeX = new float[NODE_COUNT];
    private final float[] nodeY = new float[NODE_COUNT];
    private final float[] nodeZ = new float[NODE_COUNT];
    private final int[] nodeColors = new int[NODE_COUNT];
    private final float[] projectedX = new float[NODE_COUNT];
    private final float[] projectedY = new float[NODE_COUNT];
    private final float[] projectedZ = new float[NODE_COUNT];
    private final float[] pointRadius = new float[NODE_COUNT];
    private final int[] connectionFirst = new int[CONNECTION_LIMIT];
    private final int[] connectionSecond = new int[CONNECTION_LIMIT];
    private final int[] connectionColors = new int[CONNECTION_LIMIT];
    private final int[] palette;
    private final ValueAnimator rotationAnimator;

    private RadialGradient outerGlow;
    private RadialGradient innerGlow;
    private float rotationY;
    private float rotationX = 0.14f;
    private float baseRotationY;
    private float lastTouchX;
    private float lastTouchY;
    private boolean dragging;
    private int connectionCount;
    private float corePulse = 1f;

    public KnowledgeOrbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        palette = new int[]{
                ContextCompat.getColor(context, R.color.welcome_blue),
                ContextCompat.getColor(context, R.color.welcome_cyan),
                ContextCompat.getColor(context, R.color.welcome_rose),
                ContextCompat.getColor(context, R.color.welcome_amber),
                ContextCompat.getColor(context, R.color.welcome_mint),
                ContextCompat.getColor(context, R.color.welcome_glow_inner)
        };
        generateSphere();
        generateConnections();

        rotationAnimator = ValueAnimator.ofFloat(0f, (float) (Math.PI * 2));
        rotationAnimator.setDuration(ROTATION_DURATION_MS);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addUpdateListener(animator -> {
            float phase = (float) animator.getAnimatedValue();
            corePulse = 1f + (float) Math.sin(phase * 14f) * 0.06f;
            if (!dragging) {
                rotationY = baseRotationY + phase;
            }
            invalidate();
        });
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void generateSphere() {
        Random random = new Random(114L);
        for (int index = 0; index < NODE_COUNT; index++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);
            float nodeRadius = SPHERE_RADIUS
                    * (0.35f + 0.65f * (float) Math.sqrt(random.nextDouble()));
            nodeX[index] = nodeRadius * (float) (Math.sin(phi) * Math.cos(theta));
            nodeY[index] = nodeRadius * (float) (Math.sin(phi) * Math.sin(theta));
            nodeZ[index] = nodeRadius * (float) Math.cos(phi);
            nodeColors[index] = palette[random.nextInt(palette.length)];
        }
    }

    private void generateConnections() {
        Random random = new Random(115L);
        for (int first = 0; first < CONNECTED_NODE_COUNT && connectionCount < CONNECTION_LIMIT; first++) {
            for (int second = first + 1;
                 second < CONNECTED_NODE_COUNT && connectionCount < CONNECTION_LIMIT; second++) {
                float dx = nodeX[first] - nodeX[second];
                float dy = nodeY[first] - nodeY[second];
                float dz = nodeZ[first] - nodeZ[second];
                float distanceSquared = dx * dx + dy * dy + dz * dz;
                if (distanceSquared < CONNECTION_DISTANCE * CONNECTION_DISTANCE
                        && random.nextFloat() > 0.38f) {
                    connectionFirst[connectionCount] = first;
                    connectionSecond[connectionCount] = second;
                    connectionColors[connectionCount] = nodeColors[first];
                    connectionCount++;
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        float radius = Math.min(width, height) * 0.76f;
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rotationAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        rotationAnimator.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) * 0.76f;
        projectNodes(width, height, radius);

        canvas.save();
        canvas.clipPath(clip);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.welcome_hero_background));
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(outerGlow);
        paint.setAlpha(255);
        canvas.drawCircle(width / 2, height / 2, radius * 1.6f, paint);
        paint.setShader(null);

        drawConnections(canvas);
        drawParticles(canvas);

        paint.setAlpha(255);
        paint.setShader(innerGlow);
        canvas.drawCircle(width / 2, height / 2, radius * 0.4f * corePulse, paint);
        paint.setShader(null);
        canvas.restore();
    }

    private void projectNodes(float width, float height, float radius) {
        float sinY = (float) Math.sin(rotationY);
        float cosY = (float) Math.cos(rotationY);
        float sinX = (float) Math.sin(rotationX);
        float cosX = (float) Math.cos(rotationX);
        for (int index = 0; index < NODE_COUNT; index++) {
            float rotatedX = nodeX[index] * cosY + nodeZ[index] * sinY;
            float rotatedZ = -nodeX[index] * sinY + nodeZ[index] * cosY;
            float rotatedY = nodeY[index] * cosX - rotatedZ * sinX;
            rotatedZ = nodeY[index] * sinX + rotatedZ * cosX;
            float perspective = 1f + rotatedZ / (SPHERE_RADIUS * 12f);
            projectedX[index] = width / 2f + rotatedX / SPHERE_RADIUS * radius * perspective;
            projectedY[index] = height / 2f + rotatedY / SPHERE_RADIUS * radius * perspective;
            projectedZ[index] = rotatedZ / SPHERE_RADIUS;
            pointRadius[index] = (0.9f + (projectedZ[index] + 1f) * 0.4f)
                    * getResources().getDisplayMetrics().density;
        }
    }

    private void drawConnections(Canvas canvas) {
        paint.setShader(null);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (int index = 0; index < connectionCount; index++) {
            int first = connectionFirst[index];
            int second = connectionSecond[index];
            int depthAlpha = (int) (12 + 65 * Math.max(0f, (projectedZ[first] + projectedZ[second] + 2f) / 4f));
            paint.setColor(connectionColors[index]);
            paint.setAlpha(depthAlpha);
            canvas.drawLine(projectedX[first], projectedY[first], projectedX[second],
                    projectedY[second], paint);
        }
        paint.setAlpha(255);
    }

    private void drawParticles(Canvas canvas) {
        for (int index = 0; index < NODE_COUNT; index++) {
            paint.setShader(null);
            paint.setColor(nodeColors[index]);
            int depthAlpha = (int) (55 + 200 * (projectedZ[index] + 1f) / 2f);
            if (index % 3 == 0) {
                paint.setAlpha(depthAlpha / 5);
                canvas.drawCircle(projectedX[index], projectedY[index], pointRadius[index] * 2.2f, paint);
            }
            paint.setAlpha(depthAlpha);
            canvas.drawCircle(projectedX[index], projectedY[index], pointRadius[index], paint);
        }
        paint.setAlpha(255);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                dragging = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                float density = getResources().getDisplayMetrics().density;
                rotationY += (event.getX() - lastTouchX) * 0.008f / density;
                rotationX += (event.getY() - lastTouchY) * 0.008f / density;
                rotationX = Math.max(-1.3f, Math.min(1.3f, rotationX));
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragging = false;
                baseRotationY = rotationY;
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    performClick();
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
