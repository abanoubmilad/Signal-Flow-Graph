package com.example.sfg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

public class Drawer extends View {
	Paint paint;
	Path path;

	private boolean dragged = false;
	private float displayWidth;
	private float displayHeight;
	private static float MIN_ZOOM = 1f;
	private static float MAX_ZOOM = 5f;
	private float scaleFactor = 1.f;
	private ScaleGestureDetector detector;
	private static int NONE = 0;
	private static int DRAG = 1;
	private static int ZOOM = 2;
	private int mode;
	private float startX = 0f;
	private float startY = 0f;
	private float translateX = 0f;
	private float translateY = 0f;
	private float previousTranslateX = 0f;
	private float previousTranslateY = 0f;

	public Drawer(Context context) {
		super(context);
		detector = new ScaleGestureDetector(getContext(), new ScaleListener());
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			startX = event.getX() - previousTranslateX;
			startY = event.getY() - previousTranslateY;
			break;

		case MotionEvent.ACTION_MOVE:
			translateX = event.getX() - startX;
			translateY = event.getY() - startY;
			double distance = Math
					.sqrt(Math.pow(
							event.getX() - (startX + previousTranslateX), 2)
							+ Math.pow(event.getY()
									- (startY + previousTranslateY), 2));

			if (distance > 0) {
				dragged = true;
			}

			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			break;

		case MotionEvent.ACTION_UP:
			mode = NONE;
			dragged = false;
			previousTranslateX = translateX;
			previousTranslateY = translateY;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mode = DRAG;
			previousTranslateX = translateX;
			previousTranslateY = translateY;
			break;
		}

		detector.onTouchEvent(event);
		if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
			invalidate();
		}

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		canvas.scale(scaleFactor, scaleFactor);
		if ((translateX * -1) < 0) {
			translateX = 0;
		} else if ((translateX * -1) > (scaleFactor - 1) * displayWidth) {
			translateX = (1 - scaleFactor) * displayWidth;
		}

		if (translateY * -1 < 0) {
			translateY = 0;
		} else if ((translateY * -1) > (scaleFactor - 1) * displayHeight) {
			translateY = (1 - scaleFactor) * displayHeight;
		}
		canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
		startDrawing(canvas);

		canvas.restore();
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();
			scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
			return true;
		}
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		path = new Path();
	}

	private void startDrawing(Canvas canvas) {

		int numOfNodes = Data.numOfNodes;
		double[][] gains = Data.segmentsGains;
		float xDisplace = displayWidth / (numOfNodes + 1);
		float yCenter = (displayHeight - 120) / 2;
		float nodeRadius = 25;

		float tanBaseUp = (int) (yCenter - nodeRadius);
		float tanBaseDown = (int) (yCenter + nodeRadius);
		float selfLoopC1 = yCenter - 4 * nodeRadius;
		float selfLoopC2 = yCenter + 4 * nodeRadius;
		Path path = new Path();

		if (numOfNodes > 0) {
			paint.setColor(Color.CYAN);
			canvas.drawCircle(xDisplace - nodeRadius, yCenter - nodeRadius,
					nodeRadius, paint);
			canvas.drawCircle(xDisplace * (numOfNodes) - nodeRadius, yCenter
					- nodeRadius, nodeRadius, paint);

			paint.setColor(Color.BLACK);
			canvas.drawText("R(s)", xDisplace - nodeRadius, yCenter - 2
					* nodeRadius, paint);
			if (numOfNodes > 1)
				canvas.drawText("C(s)", xDisplace * (numOfNodes) - nodeRadius,
						yCenter - 2 * nodeRadius, paint);

		}
		paint.setColor(Color.GRAY);
		for (int i = 1; i < numOfNodes - 1; i++)
			canvas.drawCircle(xDisplace * (i + 1) - nodeRadius, yCenter
					- nodeRadius, nodeRadius, paint);

		paint.setColor(Color.BLACK);
		for (int i = 0; i < numOfNodes; i++)
			canvas.drawText("" + (i + 1),
					xDisplace * (i + 1) - nodeRadius + 18, yCenter + 8, paint);
		float x;
		for (int sNode = 0; sNode < numOfNodes; sNode++) {
			for (int eNode = 0; eNode < numOfNodes; eNode++) {
				if (gains[sNode][eNode] != 0) {
					// drawing self loops
					if (sNode == eNode) {
						// drawing arc
						paint.setColor(Color.MAGENTA);
						path = new Path();
						path.moveTo(xDisplace * (sNode + 1), tanBaseUp);
						x = xDisplace * (sNode + 1) - 3 * nodeRadius;
						path.cubicTo(x, selfLoopC1, x, selfLoopC2, xDisplace
								* (sNode + 1), tanBaseDown);
						canvas.drawPath(path,paint);

						x = x + nodeRadius - 5;
						// drawing arrow
						path = new Path();
						path.moveTo(x + 12, yCenter - 10);
						path.lineTo(x, yCenter + 12);
						path.lineTo(x - 12, yCenter - 10);
						paint.setStyle(Paint.Style.FILL);
						canvas.drawPath(path,paint);
						paint.setStyle(Paint.Style.STROKE);
						// drawing gain text
						paint.setColor(Color.BLACK);
						canvas.drawText(gains[sNode][eNode] + "", xDisplace
								* (sNode + 1) - nodeRadius, yCenter - 2
								* nodeRadius, paint);

					} else if (eNode - sNode == 1) {
						// drawing arc
						paint.setColor(Color.MAGENTA);
						canvas.drawLine((sNode + 1) * xDisplace + nodeRadius,
								yCenter, (eNode + 1) * xDisplace - nodeRadius,
								yCenter, paint);
						// drawing arrow
						x = (eNode + sNode + 2) * xDisplace / 2;
						path = new Path();
						path.moveTo(x, yCenter - 12);
						path.lineTo(x, yCenter + 12);
						path.lineTo(x + 24, yCenter);
						paint.setStyle(Paint.Style.FILL);
						canvas.drawPath(path,paint);
						paint.setStyle(Paint.Style.STROKE);		
						// drawing gain text
						paint.setColor(Color.BLACK);
						canvas.drawText(gains[sNode][eNode] + "", x,
								yCenter - 20, paint);

						// feedback
					} else if (sNode > eNode) {
						// drawing arc
						paint.setColor(Color.GREEN);
						path = new Path();
						path.moveTo(xDisplace * (sNode + 1), tanBaseDown);
						x = xDisplace * (eNode + sNode + 2) / 2;
						path.quadTo(x, yCenter + (sNode - eNode) * xDisplace
								/ 2, xDisplace * (eNode + 1), tanBaseDown);
						canvas.drawPath(path,paint);

						// drawing arrow
						path = new Path();
						path.moveTo(x - 12, yCenter + (sNode - eNode)
								* xDisplace / 4 + 12);
						path.lineTo(x + 12, yCenter + (sNode - eNode)
								* xDisplace / 4);
						path.lineTo(x + 12, yCenter + (sNode - eNode)
								* xDisplace / 4 + 24);
						paint.setStyle(Paint.Style.FILL);
						canvas.drawPath(path,paint);
						paint.setStyle(Paint.Style.STROKE);
						// drawing gain text
						paint.setColor(Color.BLACK);
						canvas.drawText(gains[sNode][eNode] + "", x - 12, yCenter
								+ (sNode - eNode) * xDisplace / 4 - 6,paint);

					} else {
						// drawing arc
						paint.setColor(Color.BLUE);
						path = new Path();
						path.moveTo(xDisplace * (sNode + 1), tanBaseUp);
						x = xDisplace * (eNode + sNode + 2) / 2;
						path.quadTo(x, yCenter - (eNode - sNode) * xDisplace
								/ 2, xDisplace * (eNode + 1), tanBaseUp);
						canvas.drawPath(path,paint);

						// drawing arrow
						path = new Path();
						path.moveTo(x + 12, yCenter - (eNode - sNode)
								* xDisplace / 4 - 12);
						path.lineTo(x - 12, yCenter - (eNode - sNode)
								* xDisplace / 4);
						path.lineTo(x - 12, yCenter - (eNode - sNode)
								* xDisplace / 4 - 24);
						paint.setStyle(Paint.Style.FILL);
						canvas.drawPath(path,paint);
						paint.setStyle(Paint.Style.STROKE);
						// drawing gain text
						paint.setColor(Color.BLACK);
						canvas.drawText(gains[sNode][eNode] + "", x - 12,
								yCenter - (eNode - sNode) * xDisplace / 4 + 24,
								paint);
					}
				}
			}

		}
	}
}