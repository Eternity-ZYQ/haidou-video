package com.yimeng.widget.RatingStat;

import android.graphics.RectF;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/7 0007 下午 03:19.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class StarModel {
    private static final String TAG = "StarModel";
    public static final float DEFAULT_THICKNESS = 0.5F;
    public static final float MIN_THICKNESS = 0.3F;
    public static final float MAX_THICKNESS = 0.9F;
    public static final float DEFAULT_SCALE_FACTOR = 0.9511F;
    private float currentScaleFactor;
    private RectF outerRect;
    private float currentThicknessFactor;
    private static final float[] starVertexes = new float[]{-0.9511F, 0.309F, 0.0F, 1.0F, 0.9511F, 0.309F, 0.5878F, -0.809F, -0.5878F, -0.809F};
    private static final float aspectRatio;
    private VertexF firstVertex;
    private VertexF[] vertexes;

    public StarModel(float thicknessFactor) {
        this.currentScaleFactor = 0.9511F;
        this.outerRect = new RectF();
        this.currentThicknessFactor = 0.5F;
        this.reset(thicknessFactor);
    }

    public StarModel() {
        this(0.5F);
    }

    private void reset(float thickness) {
        this.currentScaleFactor = 0.9511F;
        this.initAllVertexesToStandard();
        this.updateOuterRect();
        this.setThicknessOnStandardCoordinate(thickness);
        this.adjustCoordinate();
    }

    public void setDrawingOuterRect(int left, int top, int height) {
        float resizeFactor = (float)height / aspectRatio;
        this.offsetStar(-this.outerRect.left, -this.outerRect.top);
        this.changeScaleFactor(resizeFactor);
        this.offsetStar((float)left, (float)top);
        this.updateOuterRect();
    }

    public void moveStarTo(float left, float top) {
        float offsetX = left - this.outerRect.left;
        float offsetY = left - this.outerRect.top;
        this.offsetStar(offsetX, offsetY);
        this.updateOuterRect();
    }

    private void initAllVertexesToStandard() {
        if (this.firstVertex == null) {
            this.firstVertex = new VertexF(starVertexes[0], starVertexes[1]);
        } else {
            this.firstVertex.x = starVertexes[0];
            this.firstVertex.y = starVertexes[1];
        }

        if (this.vertexes == null) {
            this.vertexes = new VertexF[10];
            this.vertexes[0] = this.firstVertex;

            for(int i = 1; i < 10; ++i) {
                this.vertexes[i] = new VertexF();
                this.vertexes[i - 1].next = this.vertexes[i];
            }

            this.vertexes[9].next = this.vertexes[0];
        }

        VertexF current = this.firstVertex;

        for(int i = 0; i < 5; ++i) {
            current.x = starVertexes[i * 2];
            current.y = starVertexes[i * 2 + 1];
            current = current.next.next;
        }

        VertexF prevOuter = this.firstVertex;

        for(int i = 0; i < 5; ++i) {
            VertexF innerV = prevOuter.next;
            innerV.x = (prevOuter.x + innerV.next.x) / 2.0F;
            innerV.y = (prevOuter.y + innerV.next.y) / 2.0F;
            prevOuter = innerV.next;
        }

    }

    public VertexF getVertex(int index) {
        return this.vertexes[index];
    }

    public RectF getOuterRect() {
        return new RectF(this.outerRect);
    }

    private void updateOuterRect() {
        this.outerRect.top = this.vertexes[2].y;
        this.outerRect.right = this.vertexes[4].x;
        this.outerRect.bottom = this.vertexes[8].y;
        this.outerRect.left = this.vertexes[0].x;
    }

    private void offsetStar(float left, float top) {
        for(int i = 0; i < this.vertexes.length; ++i) {
            this.vertexes[i].x += left;
            this.vertexes[i].y += top;
        }

    }

    private void changeScaleFactor(float newFactor) {
        float scale = newFactor / this.currentScaleFactor;
        if (scale != 1.0F) {
            for(int i = 0; i < this.vertexes.length; ++i) {
                this.vertexes[i].x *= scale;
                this.vertexes[i].y *= scale;
            }

            this.currentScaleFactor = newFactor;
        }
    }

    public void setThickness(float factor) {
        if (this.currentThicknessFactor != factor) {
            float oldScale = this.currentScaleFactor;
            float left = this.outerRect.left;
            float top = this.outerRect.top;
            this.reset(factor);
            this.changeScaleFactor(oldScale);
            this.moveStarTo(left, top);
        }
    }

    private void setThicknessOnStandardCoordinate(float thicknessFactor) {
        if (thicknessFactor < 0.3F) {
            thicknessFactor = 0.3F;
        } else if (thicknessFactor > 0.9F) {
            thicknessFactor = 0.9F;
        }

        for(int i = 1; i < this.vertexes.length; i += 2) {
            this.vertexes[i].x *= thicknessFactor;
            this.vertexes[i].y *= thicknessFactor;
        }

        this.currentThicknessFactor = thicknessFactor;
    }

    private void adjustCoordinate() {
        float offsetX = -this.outerRect.left;
        float offsetY = this.outerRect.top;

        for(int i = 0; i < this.vertexes.length; ++i) {
            this.vertexes[i].y = -this.vertexes[i].y + offsetY;
            this.vertexes[i].x += offsetX;
            this.vertexes[i].x /= 2.0F;
            this.vertexes[i].y /= 2.0F;
        }

        this.updateOuterRect();
    }

    public static float getOuterRectAspectRatio() {
        return aspectRatio;
    }

    public static float getStarWidth(float starHeight) {
        return starHeight / getOuterRectAspectRatio();
    }

    static {
        aspectRatio = (starVertexes[3] - starVertexes[7]) / (starVertexes[4] - starVertexes[0]);
    }
}
