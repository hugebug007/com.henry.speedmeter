package com.lzg.speedmeter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lzg.speedmeter.R;

/**
 * test neetspeed
 * 
 * @author lzg
 * 
 */
public class TestNeetSpeedView extends View {

	private final int ZORE = 0;
	private final int FIRST_STALL = 256;
	private final int SECOND_STALL = 512;
	private final int THIRD_STALL = 1028;
	private final int FOURTH_STALL = 2048;
	private final int FIFTH_STALL = 5120;
	private final int SIXTH_STALL = 10240;
	private final int SEVENTH_STALL = 20480;
	private final int EIGHTH_STALL = 51200;
	private final int NINTH_STALL = 102400;
	private final int TENTH_STALL = 204800;
	private final int STEP_DEGREE = 30;// 每个大格子的角度
	private final int ALL_STEPS = 30;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 旋转的度数
	private double mDegrees = 0;
	private Matrix mMatrix = new Matrix();
	private Double neetSpeed;
	Bitmap auto_pointer;// 指针
	Bitmap auto_dashdoard;// 刻度面板
	Resources res = getResources();
	private double stepValue;// 步进度数
	private double dstDegrees;// 目标度数
	private boolean getDst = false;// 到达目标值的信号
	private Context context;
	private float rectWihPx = 0;
	private float viewWithPx;
	private float actionBarHeight;
	private float statuHeight;
	private float screenWithPx;
	private float dashdoardWithHalf;
	/** The width of the view */
	private int width;
	/** The height of the view */
	private int height;
	/** The circle's center X coordinate of Matrix */
	private float cx;
	/** The circle's center Y coordinate of Matrix */
	private float cy;
	/** The left bound for the circle RectF */
	private float left;
	/** The right bound for the circle RectF */
	private float right;
	/** The top bound for the circle RectF */
	private float top;
	/** The bottom bound for the circle RectF */
	private float bottom;

	/**
	 * The RectF of dashRectF
	 */
	private RectF dashRectF = new RectF();
	/**
	 * The RectF of pointRectF
	 */
	private RectF pointRectF = new RectF();

	public float getActionBarHeight() {
		return actionBarHeight;
	}

	public void setActionBarHeight(float actionBarHeight) {
		this.actionBarHeight = actionBarHeight;
	}

	public TestNeetSpeedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.context = context;
		mPaint.setAntiAlias(true);
		initDrawable();
	}

	public TestNeetSpeedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.context = context;
		mPaint.setAntiAlias(true);
		initDrawable();
	}

	public TestNeetSpeedView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.context = context;
		// statuHeight = ScreenUtils.getStatusHeight(context);
		// screenWithPx = (float) ((ScreenUtils.getScreenWidth(context)));
		// BitmapDrawable bmpDraw2 = (BitmapDrawable) res
		// .getDrawable(R.drawable.labe_bak);
		// BitmapDrawable bmpDraw = (BitmapDrawable) res
		// .getDrawable(R.drawable.point_bak);
		// auto_dashdoard = bmpDraw2.getBitmap();
		// auto_pointer = bmpDraw.getBitmap();
		mPaint.setAntiAlias(true);
		initDrawable();
	}

	private void initDrawable() {
		auto_dashdoard = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.labe_bak);
		auto_pointer = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.point_bak);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.argb(0, 0, 0, 0));

		drawDashdoard(canvas);
		drawPoint(canvas);
		super.onDraw(canvas);

	}

	/**
	 * 绘制仪表盘
	 * 
	 * @param canvas
	 */
	private void drawDashdoard(Canvas canvas) {
		/*
		 * 保证刻度图剧中
		 */
		// mMatrix.setRotate((float) 0, cx, cy);
		// canvas.setMatrix(mMatrix);
		canvas.drawBitmap(auto_dashdoard, null, dashRectF, mPaint);
	}

	/**
	 * 绘制指针
	 * 
	 * @param canvas
	 */
	private void drawPoint(Canvas canvas) {
		// final RotateAnimation animation =new
		// RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
		// 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		View parentView = (View) getParent();
		// int left = parentView.getLeft();
		// int top = parentView.getTop();
		// mMatrix.setRotate((float) mDegrees, cx, cy + 300);
		// canvas.setMatrix(mMatrix);
		canvas.rotate((float) mDegrees, cx, cy);
		canvas.drawBitmap(auto_pointer, null, pointRectF, mPaint);
		/* 解除画布的锁定 */
		// canvas.restore();
		if (!getDst)
			// 获取每次步进的度数
			stepValue = (dstDegrees - mDegrees) / ALL_STEPS;

		// 正时针旋转
		if (stepValue > ZORE && !getDst) {
			if (mDegrees < dstDegrees) {
				mDegrees = mDegrees + 4 * stepValue;
				getDst = false;
			}
			if (mDegrees >= dstDegrees - 0.1) {
				mDegrees = dstDegrees;
				getDst = true;
				stepValue = 0;
			}
		}
		// 逆时针旋转
		if (stepValue < ZORE && !getDst) {
			if (mDegrees > dstDegrees) {
				mDegrees = mDegrees + 4 * stepValue;
				getDst = false;
			}
			if (mDegrees <= dstDegrees + 0.1) {
				mDegrees = dstDegrees;
				getDst = true;
				stepValue = 0;

			}
		}

		// 到达目标值之后回零
		if (getDst) {
			if (mDegrees > 0) {
				mDegrees -= 1;
			}
			if (mDegrees <= 0) {
				mDegrees = 0;
				dstDegrees = 0;
				getDst = false;
			}
		}
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth(); // Get View Width
		height = getHeight();// Get View Height
		int size = (width > height) ? height : width; // Choose the smaller
		dashdoardWithHalf = size / 2 * 100 / 100;
		cx = width / 2;
		cy = height / 2;
		left = cx - dashdoardWithHalf;
		right = cx + dashdoardWithHalf;
		top = cy - dashdoardWithHalf;
		bottom = cy + dashdoardWithHalf;
		dashRectF.set(left, top, right, bottom);
		pointRectF.set(left, top, right, bottom);

	}

	/**
	 * 设置网速
	 * 
	 * @param neetSpeed
	 */
	public void setNeetSpeed(Double neetSpeed) {
		this.getDst = false;
		this.neetSpeed = neetSpeed;
		this.dstDegrees = getDstDegreesByNeetSeep();

	}

	/**
	 * 根据网速获取旋转角度
	 * 
	 * @return
	 */
	private double getDstDegreesByNeetSeep() {
		Double dstDegree = 0d;
		if (neetSpeed >= ZORE && neetSpeed < FIRST_STALL) {
			dstDegree = neetSpeed * STEP_DEGREE / FIRST_STALL;
		} else if (neetSpeed >= FIRST_STALL && neetSpeed < SECOND_STALL) {
			dstDegree = STEP_DEGREE + (neetSpeed - FIRST_STALL) * STEP_DEGREE
					/ (SECOND_STALL - FIRST_STALL);
		} else if (neetSpeed >= SECOND_STALL && neetSpeed < THIRD_STALL) {
			dstDegree = 2 * STEP_DEGREE + (neetSpeed - SECOND_STALL)
					* STEP_DEGREE / (THIRD_STALL - SECOND_STALL);
		} else if (neetSpeed >= THIRD_STALL && neetSpeed < FOURTH_STALL) {
			dstDegree = 3 * STEP_DEGREE + (neetSpeed - THIRD_STALL)
					* STEP_DEGREE / (FOURTH_STALL - THIRD_STALL);
		} else if (neetSpeed >= FOURTH_STALL && neetSpeed < FIFTH_STALL) {
			dstDegree = 4 * STEP_DEGREE + (neetSpeed - FOURTH_STALL)
					* STEP_DEGREE / (FIFTH_STALL - FOURTH_STALL);
		} else if (neetSpeed >= FIFTH_STALL && neetSpeed < SIXTH_STALL) {
			dstDegree = 5 * STEP_DEGREE + (neetSpeed - FIFTH_STALL)
					* STEP_DEGREE / (SIXTH_STALL - FIFTH_STALL);
		} else if (neetSpeed >= SIXTH_STALL && neetSpeed < SEVENTH_STALL) {
			dstDegree = 6 * STEP_DEGREE + (neetSpeed - SIXTH_STALL)
					* STEP_DEGREE / (SEVENTH_STALL - SIXTH_STALL);
		} else if (neetSpeed >= SEVENTH_STALL && neetSpeed < EIGHTH_STALL) {
			dstDegree = 7 * STEP_DEGREE + (neetSpeed - SEVENTH_STALL)
					* STEP_DEGREE / (EIGHTH_STALL - SEVENTH_STALL);
		} else if (neetSpeed >= EIGHTH_STALL && neetSpeed < NINTH_STALL) {
			dstDegree = 8 * STEP_DEGREE + (neetSpeed - EIGHTH_STALL)
					* STEP_DEGREE / (NINTH_STALL - EIGHTH_STALL);
		} else if (neetSpeed >= NINTH_STALL && neetSpeed <= TENTH_STALL) {
			dstDegree = 9 * STEP_DEGREE + (neetSpeed - NINTH_STALL)
					* STEP_DEGREE / (TENTH_STALL - NINTH_STALL);
		} else {
			dstDegree = 10d * STEP_DEGREE;
		}
		return dstDegree;
	}
}
