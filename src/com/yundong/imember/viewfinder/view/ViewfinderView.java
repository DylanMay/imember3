/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yundong.imember.viewfinder.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.yundong.imember.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 100L;
  private static final int OPAQUE = 0xFF;

  private final Paint paint;
  private Bitmap resultBitmap;
  private final int maskColor;
  private final int resultColor;
  private int frameColor;
  private final int laserColor;
  private final int resultPointColor;
  private int scannerAlpha;
  private Collection<ResultPoint> possibleResultPoints;
  private Collection<ResultPoint> lastPossibleResultPoints;
  
  private Resources resources;
  private int width_line;
  private int height_line;
  
  //补齐两个构造函数，否则出现Binary XML file line #56: Error inflating class <unknown>
	public ViewfinderView(Context context) {
		super(context);
		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		// 外框颜色
		// frameColor = resources.getColor(R.color.viewfinder_frame);
		// frameColor = resources.getColor(R.drawable.red);
		frameColor = Color.rgb(65, 189, 216);

		// 闪烁线的颜色
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}
	//补齐两个构造函数，否则出现Binary XML file line #56: Error inflating class <unknown>
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		// 外框颜色
		// frameColor = resources.getColor(R.color.viewfinder_frame);
		// frameColor = resources.getColor(R.drawable.red);
		frameColor = Color.rgb(65, 189, 216);

		// 闪烁线的颜色
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

  @Override
  public void onDraw(Canvas canvas) {
//    Rect frame = CameraManager.get().getFramingRect();
//    if (frame == null) {
//      return;
//    }
//    int width = canvas.getWidth();
//    int height = canvas.getHeight();
//    width_line = (frame.right - frame.left)/3;
//    height_line = (frame.bottom - frame.top)/3;
//
//    paint.reset();
//    // Draw the exterior (i.e. outside the framing rect) darkened
//    paint.setColor(resultBitmap != null ? resultColor : maskColor);
//    canvas.drawRect(0, 0, width, frame.top, paint);
//    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
//    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
//    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
//
//    if (resultBitmap != null) {
//      // Draw the opaque result bitmap over the scanning rectangle
//      paint.setAlpha(OPAQUE);
//      canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
//    } else {
//
//    	// Draw a two pixel solid black border inside the framing rect
//    	
//      // Draw a red "laser scanner" line through the middle to show decoding is active
//      paint.setColor(laserColor);
//      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + frame.top;
//      //画闪烁线段
////      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
//
//      Collection<ResultPoint> currentPossible = possibleResultPoints;
//      Collection<ResultPoint> currentLast = lastPossibleResultPoints;
//      if (currentPossible.isEmpty()) {
//        lastPossibleResultPoints = null;
//      } else {
//        possibleResultPoints = new HashSet<ResultPoint>(5);
//        lastPossibleResultPoints = currentPossible;
//        paint.setAlpha(OPAQUE);
//        paint.setColor(resultPointColor);
//        for (ResultPoint point : currentPossible) {
////          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
//        }
//      }
//      if (currentLast != null) {
//        paint.setAlpha(OPAQUE / 2);
//        paint.setColor(resultPointColor);
//        for (ResultPoint point : currentLast) {
////          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
//        }
//      }
//      
//      switch(StaticData.SCAN_TYPE){
//      case StaticData.SCAN_COUNT_SUCCESS:
//    	  frameColor = Color.rgb(65, 189, 216);
//    	  break;
//      case StaticData.SCAN_EXCHANGE_SUCCESS:
//    	  frameColor = Color.rgb(255, 102, 102);
//    	  break;
//      }
//   // Draw a two pixel solid black border inside the framing rect
//      paint.setColor(frameColor);
//      paint.setStyle(Style.STROKE);
//      paint.setStrokeWidth(5);
////      canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
////      canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
////      canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
////      canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
//      
//      canvas.drawLine(frame.left, frame.top, frame.left + width_line, frame.top, paint);
//      canvas.drawLine(frame.right - width_line, frame.top, frame.right, frame.top, paint);
//      canvas.drawLine(frame.left, frame.bottom, frame.left + width_line, frame.bottom, paint);
//      canvas.drawLine(frame.right - width_line, frame.bottom, frame.right, frame.bottom, paint);
//      
//      canvas.drawLine(frame.left, frame.top, frame.left, frame.top + height_line, paint);
//      canvas.drawLine(frame.right, frame.top, frame.right, frame.top + height_line, paint);
//      canvas.drawLine(frame.left, frame.bottom - height_line, frame.left, frame.bottom, paint);
//      canvas.drawLine(frame.right, frame.bottom - height_line, frame.right, frame.bottom, paint);
//
//      // Request another update at the animation interval, but only repaint the laser line,
//      // not the entire viewfinder mask.
//      postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
//    }
  }

  public void drawViewfinder() {
    resultBitmap = null;
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   *
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    possibleResultPoints.add(point);
  }

}
