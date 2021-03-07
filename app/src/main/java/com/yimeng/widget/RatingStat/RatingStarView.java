package com.yimeng.widget.RatingStat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yimeng.haidou.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/7 0007 下午 03:19.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class RatingStarView extends View implements View.OnClickListener {
    private static final String TAG = "RatingStarView";
    private static final int DEFAULT_STAR_HEIGHT = 32;
    private float cornerRadius = 4.0F;
    private int starForegroundColor = -1226165;
    private int strokeColor = -1226165;
    private int starBackgroundColor = -1;
    private CornerPathEffect pathEffect;
    private ArrayList<StarModel> starList;
    private float rating;
    private int starNum = 5;
    private int starCount;
    private float starWidth;
    private float starHeight;
    private float starMargin = 8.0F;
    private float strokeWidth = 2.0F;
    private boolean drawStrokeForFullStar;
    private boolean drawStrokeForHalfStar = true;
    private boolean drawStrokeForEmptyStar = true;
    private boolean enableSelectRating = false;
    private boolean onlyHalfStar = true;
    private float starThicknessFactor = 0.5F;
    private float dividerX;
    private float clickedX;
    private float clickedY;
    private Paint paint;
    private OnClickListener mOuterOnClickListener;

    public RatingStarView(Context context) {
        super(context);
        this.init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.loadAttributes(attrs, defStyle);
        this.paint = new Paint();
        this.paint.setFlags(1);
        this.paint.setStrokeWidth(this.strokeWidth);
        this.pathEffect = new CornerPathEffect(this.cornerRadius);
        super.setOnClickListener(this);
    }

    private void loadAttributes(AttributeSet attrs, int defStyle) {
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.RatingStarView, defStyle, 0);
        this.strokeColor = a.getColor(R.styleable.RatingStarView_strokeColor, this.strokeColor);
        this.starForegroundColor = a.getColor(R.styleable.RatingStarView_starForegroundColor, this.starForegroundColor);
        this.starBackgroundColor = a.getColor(R.styleable.RatingStarView_starBackgroundColor, this.starBackgroundColor);
        this.cornerRadius = a.getDimension(R.styleable.RatingStarView_rsv_cornerRadius, this.cornerRadius);
        this.starMargin = a.getDimension(R.styleable.RatingStarView_starMargin, this.starMargin);
        this.strokeWidth = a.getDimension(R.styleable.RatingStarView_rsv_strokeWidth, this.strokeWidth);
        this.starThicknessFactor = a.getFloat(R.styleable.RatingStarView_starThickness, this.starThicknessFactor);
        this.rating = a.getFloat(R.styleable.RatingStarView_rating, this.rating);
        this.starNum = a.getInteger(R.styleable.RatingStarView_starNum, this.starNum);
        this.drawStrokeForEmptyStar = a.getBoolean(R.styleable.RatingStarView_drawStrokeForEmptyStar, true);
        this.drawStrokeForFullStar = a.getBoolean(R.styleable.RatingStarView_drawStrokeForFullStar, false);
        this.drawStrokeForHalfStar = a.getBoolean(R.styleable.RatingStarView_drawStrokeForHalfStar, true);
        this.enableSelectRating = a.getBoolean(R.styleable.RatingStarView_enableSelectRating, false);
        this.onlyHalfStar = a.getBoolean(R.styleable.RatingStarView_onlyHalfStar, true);
        a.recycle();
    }

    public RatingStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0);
    }

    public RatingStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs, defStyle);
    }

    private void setStarBackgroundColor(int color) {
        this.starBackgroundColor = color;
        this.invalidate();
    }

    public void setStarThickness(float thicknessFactor) {
        Iterator var2 = this.starList.iterator();

        while (var2.hasNext()) {
            StarModel star = (StarModel) var2.next();
            star.setThickness(thicknessFactor);
        }

        this.invalidate();
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        this.invalidate();
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        if (rating != this.rating) {
            this.rating = rating;
            this.invalidate();
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        this.invalidate();
    }

    public void setStarMargin(int margin) {
        this.starMargin = (float) margin;
        this.calcStars();
        this.invalidate();
    }

    private void calcStars() {
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int contentWidth = this.getWidth() - paddingLeft - paddingRight;
        int contentHeight = this.getHeight() - paddingTop - paddingBottom;
        int left = paddingLeft;
        int top = paddingTop;
        int starHeight = contentHeight;
        if (contentHeight > contentWidth) {
            starHeight = contentWidth;
        }

        if (starHeight > 0) {
            float startWidth = StarModel.getStarWidth((float) starHeight);
            int starCount = (int) (((float) contentWidth + this.starMargin) / (startWidth + this.starMargin));
            if (starCount > this.starNum) {
                starCount = this.starNum;
            }

            this.starHeight = (float) starHeight;
            this.starWidth = startWidth;
            Log.d("RatingStarView", "drawing starCount = " + starCount + ", contentWidth = " + contentWidth + ", startWidth = " + startWidth + ", starHeight = " + starHeight);
            this.starList = new ArrayList(starCount);

            for (int i = 0; i < starCount; ++i) {
                StarModel star = new StarModel(this.starThicknessFactor);
                this.starList.add(star);
                star.setDrawingOuterRect(left, top, starHeight);
                left = (int) ((float) left + startWidth + 0.5F + this.starMargin);
            }

            this.starCount = starCount;
            this.starWidth = startWidth;
            this.starHeight = (float) starHeight;
        }
    }

    public void setStarNum(int count) {
        if (this.starNum != count) {
            this.starNum = count;
            this.calcStars();
            this.invalidate();
        }

    }

    private void onPaddingChanged() {
        int left = this.getPaddingLeft();
        int top = this.getPaddingTop();
        Iterator var3 = this.starList.iterator();

        while (var3.hasNext()) {
            StarModel star = (StarModel) var3.next();
            star.moveStarTo((float) left, (float) top);
        }

    }

    public void setDrawStrokeForFullStar(boolean draw) {
        this.drawStrokeForFullStar = draw;
    }

    public void setDrawStrokeForEmptyStar(boolean draw) {
        this.drawStrokeForEmptyStar = draw;
    }

    private void drawFullStar(StarModel star, Canvas canvas) {
        this.drawSolidStar(star, canvas, this.starForegroundColor);
        if (this.drawStrokeForFullStar) {
            this.drawStarStroke(star, canvas);
        }

    }

    private void drawEmptyStar(StarModel star, Canvas canvas) {
        this.drawSolidStar(star, canvas, this.starBackgroundColor);
        if (this.drawStrokeForEmptyStar) {
            this.drawStarStroke(star, canvas);
        }

    }

    private void drawPartialStar(StarModel star, Canvas canvas, float percent) {
        Log.d("RatingStarView", "drawPartialStar percent = " + percent);
        if (percent <= 0.0F) {
            this.drawEmptyStar(star, canvas);
        } else if (percent >= 1.0F) {
            this.drawFullStar(star, canvas);
        } else {
            this.drawSolidStar(star, canvas, this.starBackgroundColor);
            float dividerX = star.getOuterRect().left + star.getOuterRect().width() * percent;
            this.dividerX = dividerX;
            RectF r = star.getOuterRect();
            canvas.saveLayerAlpha(r.left, r.top, r.right, r.bottom, 255, 2);
            RectF clip = new RectF(star.getOuterRect());
            clip.right = dividerX;
            canvas.clipRect(clip);
            this.drawSolidStar(star, canvas, this.starForegroundColor);
            canvas.restore();
            if (this.drawStrokeForHalfStar) {
                this.drawStarStroke(star, canvas);
            }

        }
    }

    private void drawSolidStar(StarModel star, Canvas canvas, int fillColor) {
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setColor(fillColor);
        this.paint.setPathEffect(this.pathEffect);
        VertexF prev = star.getVertex(1);
        Path path = new Path();

        for (int i = 0; i < 5; ++i) {
            path.rewind();
            path.moveTo(prev.x, prev.y);
            VertexF next = prev.next;
            path.lineTo(next.x, next.y);
            path.lineTo(next.next.x, next.next.y);
            path.lineTo(next.next.x, next.next.y);
            canvas.drawPath(path, this.paint);
            prev = next.next;
        }

        path.rewind();
        prev = star.getVertex(1);
        path.moveTo(prev.x - 1.0F, prev.y - 1.0F);
        prev = prev.next.next;
        path.lineTo(prev.x + 1.5F, prev.y - 0.5F);
        prev = prev.next.next;
        path.lineTo(prev.x + 1.5F, prev.y + 1.0F);
        prev = prev.next.next;
        path.lineTo(prev.x, prev.y + 1.0F);
        prev = prev.next.next;
        path.lineTo(prev.x - 1.0F, prev.y + 1.0F);
        this.paint.setPathEffect(null);
        canvas.drawPath(path, this.paint);
    }

    private void drawStarStroke(StarModel star, Canvas canvas) {
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(this.strokeColor);
        this.paint.setPathEffect(this.pathEffect);
        VertexF prev = star.getVertex(1);
        Path path = new Path();

        for (int i = 0; i < 5; ++i) {
            path.rewind();
            path.moveTo(prev.x, prev.y);
            VertexF next = prev.next;
            path.lineTo(next.x, next.y);
            path.lineTo(next.next.x, next.next.y);
            path.lineTo(next.next.x, next.next.y);
            canvas.drawPath(path, this.paint);
            prev = next.next;
        }

    }

    public void setOnClickListener(OnClickListener l) {
        this.mOuterOnClickListener = l;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.clickedX = event.getX();
            this.clickedY = event.getY();
        }

        return super.onTouchEvent(event);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h != oldh) {
            this.calcStars();
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.starList == null) {
            this.calcStars();
        }

        if (this.starList != null && this.starList.size() != 0) {
            for (int i = 0; i < this.starList.size(); ++i) {
                if (this.rating >= (float) (i + 1)) {
                    this.drawFullStar(this.starList.get(i), canvas);
                } else {
                    float decimal = this.rating - (float) i;
                    if (decimal > 0.0F) {
                        if (this.onlyHalfStar) {
                            decimal = 0.5F;
                        }

                        this.drawPartialStar(this.starList.get(i), canvas, decimal);
                    } else {
                        this.drawEmptyStar(this.starList.get(i), canvas);
                    }
                }
            }

        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = 32;
            if (heightMode == -2147483648) {
                height = Math.min(height, heightSize);
            }
        }

        float starHeight = (float) (height - this.getPaddingBottom() - this.getPaddingTop());
        float width;
        if (widthMode == 1073741824) {
            width = (float) widthSize;
        } else {
            width = (float) (this.getPaddingLeft() + this.getPaddingRight());
            if (this.starNum > 0 && starHeight > 0.0F) {
                width += this.starMargin * (float) (this.starNum - 1);
                width += StarModel.getStarWidth(starHeight) * (float) this.starNum;
            }

            if (widthMode == -2147483648) {
                width = Math.min((float) widthSize, width);
            }
        }

        Log.d("RatingStarView", "[onMeasure] width = " + width + ", pLeft = " + this.getPaddingLeft() + ", pRight = " + this.getPaddingRight() + ", starMargin = " + this.starMargin + ", starHeight = " + starHeight + ", starWidth = " + StarModel.getStarWidth(starHeight));
        int widthInt = (int) width;
        if ((float) widthInt < width) {
            ++widthInt;
        }

        this.setMeasuredDimension(widthInt, height);
    }

    public void onClick(View v) {

        if (this.enableSelectRating) {
            this.changeRatingByClick();
        }

        if (this.mOuterOnClickListener != null) {
            this.mOuterOnClickListener.onClick(v);
        }
    }

    private void changeRatingByClick() {
        int paddingTop = this.getPaddingTop();
        if (this.clickedY >= (float) paddingTop && this.clickedY <= (float) paddingTop + this.starHeight) {
            int paddingLeft = this.getPaddingLeft();
            float starWidth = this.starWidth;
            float starMargin = this.starMargin;
            float left = (float) paddingLeft;

            for (int i = 1; i <= this.starCount; ++i) {
                float right = left + starWidth;
                if (this.clickedX >= left && this.clickedX <= right) {
                    if (this.rating == (float) i) {
                        this.setRating(0.0F);
                    } else {
                        this.setRating((float) i);
                    }
                    break;
                }

                left += starWidth + starMargin;
            }

        }
    }

}
