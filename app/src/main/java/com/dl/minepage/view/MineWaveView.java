package com.dl.minepage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dl on 2018/3/16.
 */

public class MineWaveView extends View {

    private int alpha = 80;//透明度
    private int delayTime = 20;//更新时间

    private Path mAboveWavePath;//上面水波的路径
    private Path mBelowWavePath;//下面水波的路径
    private Paint mAboveWavePaint;//上面水波的画笔
    private Paint mBelowWavePaint;//上面水波的画笔
    private PaintFlagsDrawFilter mDrawFilter;//画布抗锯齿
    private OnWaveAnimListener listener;//动画接口回调

    private float φ;//初相
    private float a1 = 10;//振幅
    private float a2 = 15;//振幅
    private float k = 10;//偏距

    public MineWaveView(Context context) {
        this(context, null);
    }

    public MineWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public MineWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化路径、画笔
     */
    private void init() {
        mAboveWavePath = new Path();
        mBelowWavePath = new Path();

        mAboveWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAboveWavePaint.setAntiAlias(true);
        mAboveWavePaint.setStyle(Paint.Style.FILL);
        mAboveWavePaint.setColor(Color.WHITE);

        mBelowWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBelowWavePaint.setAntiAlias(true);
        mBelowWavePaint.setStyle(Paint.Style.FILL);
        mBelowWavePaint.setColor(Color.WHITE);
        mBelowWavePaint.setAlpha(alpha);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        mAboveWavePath.reset();
        mBelowWavePath.reset();
        //绘制水波曲线
        drawWave(canvas);
        //更新ui通过修改初相位的值来实现移动效果
        postInvalidateDelayed(delayTime);
    }

    /**
     * 正弦曲线可表示为y=Asin(ωx+φ)+k
     * A——振幅，当物体作轨迹符合正弦曲线的直线往复运动时，其值为行程的1/2。
     * (ωx+φ)——相位，反映变量y所处的状态。
     * φ——初相，x=0时的相位；反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
     * k——偏距，反映在坐标系上则为图像的上移或下移。
     * ω——角速度， 控制正弦周期(单位弧度内震动的次数)。
     *
     * @param canvas
     */
    private void drawWave(Canvas canvas) {
        //初相位
        φ -= 0.1f;
        float y, y2;
        double ω = 2 * Math.PI / getWidth();
        mAboveWavePath.moveTo(getLeft(), getBottom());
        mBelowWavePath.moveTo(getLeft(), getBottom());
        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (a1 * Math.cos(ω * x + φ) + k);
            y2 = (float) (a2 * Math.sin(ω * x + φ));
            mAboveWavePath.lineTo(x, y);
            mBelowWavePath.lineTo(x, y2);
            if (listener != null) {
                int x2 = (int) Math.abs((ω * x + φ * 30));
                listener.onWaveAnimLoading(x2, y, y2);
            }
        }
        mAboveWavePath.lineTo(getRight(), getBottom());
        mBelowWavePath.lineTo(getRight(), getBottom());
        canvas.drawPath(mAboveWavePath, mAboveWavePaint);
        canvas.drawPath(mBelowWavePath, mBelowWavePaint);
    }

    /**
     * 设置水波动画接口回调
     *
     * @param l
     */
    public void setOnWaveAnimListener(OnWaveAnimListener l) {
        this.listener = l;
    }

    /**
     * 水波动画接口
     */
    public interface OnWaveAnimListener {
        void onWaveAnimLoading(float x, float aboveY, float belowY);
    }
}
