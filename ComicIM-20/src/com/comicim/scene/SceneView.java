package com.comicim.scene;

import java.util.List;

import com.comicim.Message;
import com.example.comicim_20.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
//import android.graphics.drawable.LayerDrawable;
//import android.text.Layout;
//import android.view.View;
import android.widget.ImageView;
//import android.widget.LinearLayout;

public class SceneView extends View {
	public Paint textPaint;
	public List<Message> messages;
	public Drawable dudeDrawable;
	
	public SceneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setColor(Color.BLACK);
		textPaint.setAntiAlias(true);
		
		dudeDrawable = context.getResources().getDrawable(R.drawable.dude);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	   // Try for a width based on our minimum
	   int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
	   int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

	   int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
	   int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

	   setMeasuredDimension(w, h);
	}
	
	public void renderDudes(Canvas canvas) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		int dudeHeight = height / 2;
		int dudeWidth = dudeHeight * dudeDrawable.getIntrinsicWidth() / dudeDrawable.getIntrinsicHeight();
		
		canvas.save();
		canvas.translate(10, height / 2 - 10);
		dudeDrawable.setBounds(0, 0, dudeWidth, dudeHeight);
		dudeDrawable.draw(canvas);
		canvas.restore();
		
		canvas.save();
		canvas.translate(width - 10, height / 2 - 10);
		canvas.scale(-1, 1);
		dudeDrawable.setBounds(0, 0, dudeWidth, dudeHeight);
		dudeDrawable.draw(canvas);
		canvas.restore();
	}
	
	public void renderBoundingBox(Canvas canvas) {
		textPaint.setStyle(Style.STROKE);
		RectF rect = new RectF(5, 5, canvas.getWidth() - 5, canvas.getHeight() - 5);
		canvas.drawRoundRect(rect, 3, 3, textPaint);
		textPaint.setStyle(Style.FILL_AND_STROKE);
	}
	
	public void renderMessages(Canvas canvas) {
		for (int i = 0; i < messages.size(); i++) {
			canvas.drawText(messages.get(i).text, 10, 20 + 20 * i, textPaint);
		}
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		if (messages == null) return;
		Log.i("SceneView", canvas.getWidth() + " " + canvas.getHeight());
		
		renderBoundingBox(canvas);
		renderDudes(canvas);
		renderMessages(canvas);
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
