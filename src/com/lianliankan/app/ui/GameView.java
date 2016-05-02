package com.lianliankan.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.lianliankan.app.R;
import com.lianliankan.app.logic.GameLogic;
import com.lianliankan.app.utils.LogUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View{

	private GameLogic gameLogic = null;
	
	private Bitmap elementImg = null;
	
	private Bitmap mBitmap = null;
	
	private Canvas mCanvas = null;
	
	private Paint mPaint = null;
	
	private static int MAP_ROW = 8;
	
	private static int MAP_COL = 10;
	
	private static int MAP_WIDTH = 480;
	
	private static int MAP_HEIGHT = 384;
	
	private static int ELEMENT_NUM = 13;
	
	private static int ELEMENT_WIDTH = 48;
	
	private static int ELEMENT_HEIGHT = 48;
	
	private List<com.lianliankan.app.model.Point> selected = null;
	
	private List<com.lianliankan.app.model.Point> connPath = null;
	
	private Context context = null;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		gameLogic = new GameLogic(MAP_ROW, MAP_COL, ELEMENT_NUM);
		elementImg = ((BitmapDrawable) getResources().getDrawable(R.drawable.elements)).getBitmap();
		mBitmap = Bitmap.createBitmap(MAP_WIDTH, MAP_HEIGHT, Config.ARGB_8888);
		mCanvas = new Canvas();
		mCanvas.setBitmap(mBitmap);
		mPaint = new Paint();
		gameLogic.init();
		this.context = context;
		selected = new ArrayList<com.lianliankan.app.model.Point>();
		connPath = new ArrayList<com.lianliankan.app.model.Point>();
	}
	
	public Rect getSrcRect(final int type) {
		Rect srcRect = new Rect();
		srcRect.left = (type - 1) * ELEMENT_WIDTH;
		srcRect.top = 0;
		srcRect.right = srcRect.left +ELEMENT_WIDTH;
		srcRect.bottom = ELEMENT_HEIGHT;
		return srcRect;
	}

	public Rect getDstRect(final int row, final int col) {
		Rect dstRect = new Rect();
		dstRect.left = col * ELEMENT_WIDTH;
		dstRect.top = row * ELEMENT_HEIGHT;
		dstRect.right = dstRect.left + ELEMENT_WIDTH;
		dstRect.bottom = dstRect.top + ELEMENT_HEIGHT;
		return dstRect;
	}
	
	private void drawMap() {
		Rect srcRect = null;
		Rect dstRect = null;
		mBitmap.eraseColor(Color.TRANSPARENT);
		for (int i = 0; i < MAP_ROW; i++) {
			for (int j = 0; j < MAP_COL; j++) {
				int elementType = gameLogic.getElementType(i, j);
				//LogUtils.d("MainActivity", String.valueOf(elementType));
				if (elementType != 0) {
					srcRect = getSrcRect(elementType);
					dstRect = getDstRect(i, j);
				}else {
					srcRect = new Rect(0, 0, 0, 0);
					dstRect = new Rect(0, 0, 0, 0);
				}
				mCanvas.drawBitmap(elementImg, srcRect, dstRect, mPaint);
				//LogUtils.d("MainActivity", "success");
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//LogUtils.d("MainActivity", "ondraw");
		if (connPath != null && connPath.size() > 0) {
			drawPath(canvas);
			connPath.clear();
			return;
		}
		drawMap();
		
		if (selected.size() == 1) {
			enlargeElement();
		}
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		//LogUtils.d("MainActivity", "drawBitmap");
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		com.lianliankan.app.model.Point location = new com.lianliankan.app.model.Point();
		int row = (int) (event.getY() / ELEMENT_HEIGHT);
		int col = (int) (event.getX() / ELEMENT_WIDTH);
		location.setRow(row);
		location.setCol(col);
		
		if (gameLogic.getElementType(row, col) != 0) {
			selected.add(location);
			
			if (selected.size() == 2) {
				connPath = gameLogic.kill(selected.get(0), selected.get(1));
				
				if (connPath != null && connPath.size() > 0) {
					selected.clear();
					LogUtils.d("MainActivity", "满足消子条件");
					
					if (gameLogic.getLeftNum() == 0) {
						gameWin();
					}
				}else {
					selected.remove(0);
					LogUtils.d("MainActivity", "不满足消子条件");
				}
			}
			
			postInvalidate();
		}
		

		return super.onTouchEvent(event);
	}
	
	private void enlargeElement() {
		Paint paint = new Paint();
		int row = selected.get(0).getRow();
		int col = selected.get(0).getCol();
		
		Rect srcRect = getSrcRect(gameLogic.getElementType(row, col));
		Rect dstRect = getDstRect(row, col);
		
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		mCanvas.save();
		mCanvas.clipRect(dstRect);
		mCanvas.drawPaint(paint);
		mCanvas.restore();
		
		dstRect.left  -= ELEMENT_WIDTH / 5;
		dstRect.top -= ELEMENT_WIDTH / 5;
		dstRect.right += ELEMENT_WIDTH / 5;
		dstRect.bottom += ELEMENT_WIDTH / 5;
		
		mCanvas.drawBitmap(elementImg, srcRect, dstRect, mPaint);
		
	}
	
	private void drawPath(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		
		for (int i = 0; i < connPath.size() - 1; i++) {
			com.lianliankan.app.model.Point pointOne = connPath.get(i);
			com.lianliankan.app.model.Point pointTwo = connPath.get(i + 1);
			
			float lineStartX = pointOne.getCol() * ELEMENT_WIDTH + ELEMENT_WIDTH / 2;
			float lineStartY = pointOne.getRow() * ELEMENT_HEIGHT + ELEMENT_HEIGHT / 2;
			float lineStopX = pointTwo.getCol() * ELEMENT_WIDTH + ELEMENT_WIDTH / 2;
			float lineStopY = pointTwo.getRow() * ELEMENT_HEIGHT + ELEMENT_HEIGHT / 2;
			
			canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, paint);
		}
		
		postInvalidateDelayed(500);
	}
	
	private void gameWin() {
		new AlertDialog.Builder(context).setTitle("恭喜您获胜！").setMessage("是否再来一局？").setPositiveButton
		("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				GameView.this.gameLogic.init();
				GameView.this.postInvalidate();
			}
		}).setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GameView.this.context, MainActivity.class);
				GameView.this.context.startActivity(intent);
			}
		}).show();
	}
	
}
