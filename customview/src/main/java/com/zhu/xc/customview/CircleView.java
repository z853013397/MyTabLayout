package com.zhu.xc.customview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CircleView extends View {
	//
	// /**
	// * 正确的颜色
	// */
	// public static final String RIGHT_COLOR = "#36E21B";
	//
	// /**
	// * 错误的颜色
	// */
	// public static final String ERROR_COLOR = "#E21B1B";
	//
	// /**
	// * 半对 上半圆的颜色
	// */
	// public static final String TOP_COLOR = "#E21B1B";
	// /**
	// * 半对 下半圆的颜色
	// */
	// public static final String BOTTOM_COLOR = "#36E21B";
	//
	//
	// public static final int RIGHT = 2;
	//
	// public static final int ERROR = 0;
	//
	// public static final int HALF = 1;
	//
	// int state;

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context) {
		this(context, null);
	}

	/**
	 * 用来画圆和半圆的paint
	 */
	private Paint mPaint;

	/**
	 * 用来画字的paint
	 */
	private Paint textPaint;
	/**
	 * 用来计算baseline 和中心点的距离
	 */
	private float textoffset;

	// /**
	// * 圆的颜色
	// */
	// private int cirlceColor = Color.GREEN;
	//
	//
	// /**
	// * 上半圆的颜色
	// */
	// private int topArcColor = Color.RED;
	//
	// /**
	// * 下半圆的颜色
	// */
	// private int bottomArcColor = Color.BLUE;

	/**
	 * 矩形4个点
	 */
	private RectF f;

	/**
	 * 样式
	 */
	// public enum Style {
	// RIGHT, WRONG, HALF, WRONG_COMMENT, RIGHT_COMMENT, HALF_COMMENT
	// }

	// /**
	// * 默认是圆
	// */
	// private Style mStyle = Style.RIGHT;
	//
	// public Style getStyle() {
	// return mStyle;
	// }

	public static final int WRONG = 0;
	public static final int RIGHT = 1;
	public static final int HALF = 2;
	public static final int WRONG_COMMENT = 3;
	public static final int RIGHT_COMMENT = 4;
	public static final int HALF_COMMENT = 5;

	// public void setStyle(Style mStyle) {
	// this.mStyle = mStyle;
	// invalidate();
	// }

	private int mState = RIGHT;

	/**
	 * 初始化数据
	 */
	private void init() {
		// setLayerType(LAYER_TYPE_SOFTWARE, null);// 这个是模糊渲染

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		// mPaint.setColor(cirlceColor);

		// right = BitmapFactory.decodeResource(getResources(),
		// R.drawable.bg_right);

		f = new RectF();
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(40);
		textPaint.setTextAlign(Align.CENTER);
		
		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/text_font_english.ttf");
		textPaint.setTypeface(typeface);

		textoffset = (textPaint.descent() + textPaint.ascent()) / 2;
		// mPaint.setMaskFilter(new BlurMaskFilter(20, Blur.SOLID));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desireWidth = dp2Px(36);//处理下设置wrap-content时默认的宽度
		int desireHeight = dp2Px(36);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;
		switch (widthMode) {
		case MeasureSpec.EXACTLY:
			width = widthSize;
			break;
		case MeasureSpec.AT_MOST:// wrap_content
			width = Math.min(widthSize, desireWidth);
			break;
		default:
			width = desireWidth;
			break;
		}

		switch (heightMode) {
		case MeasureSpec.EXACTLY:
			height = heightSize;
			break;
		case MeasureSpec.AT_MOST:// wrap_content
			height = Math.min(desireHeight, heightSize);
			break;
		default:
			height = desireHeight;
			break;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bitmap = null;
		switch (mState) {
		case WRONG:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_wrong);
			break;
		case RIGHT:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_right);
			break;
		case HALF:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_half);

			break;
		case WRONG_COMMENT:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_wrong_comment);
			break;

		case RIGHT_COMMENT:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_right_comment);
			break;
		default:
//			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_helf_comment);
			break;
		}

		canvas.drawBitmap(bitmap, 0, 0, mPaint);//根据判断来画图片

		canvas.drawText(number, getCircleX(), getCircleY() - textoffset, textPaint);//在中心点画数字

	}
	/**
	 * @return 得到中心点的x坐标
	 */
	@SuppressLint("NewApi")
	private int getCircleX() {
		return getMeasuredWidth() / 2;
	}

	/**
	 * @return 得到中心点的y坐标
	 */
	@SuppressLint("NewApi")
	private int getCircleY() {
		return getMeasuredHeight() / 2;
	}

	private String number = "";

	/**
	 * 设置中心的数字
	 * @param number 
	 */
	public void setText(String number) {
		this.number = number;
	}

	/** dp转成px
	 * @param dp
	 * @return
	 */
	private int dp2Px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

	}
	
	/**
	 * 设置状态
	 * @param state
	 */
	public void setState(int state) {
		mState = state;
		invalidate();
	}
	

}
