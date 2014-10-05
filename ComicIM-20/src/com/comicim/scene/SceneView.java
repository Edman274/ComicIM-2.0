package com.comicim.scene;

import com.comicim.Message;
import com.example.comicim_20.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
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
	public Message message;
	public Drawable dudeDrawable;
	
	public SceneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		textPaint = new Paint();
		textPaint.setTextSize(40);
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
	
	public void renderDudes(Canvas canvas, Rect rect) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		int dudeWidth = (height / 2) * dudeDrawable.getIntrinsicWidth() / dudeDrawable.getIntrinsicHeight();
		
		dudeDrawable.setBounds(0, height * 1 / 2, dudeWidth, height);
		dudeDrawable.draw(canvas);
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		Rect canvasRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		if (message != null) {
			renderDudes(canvas, canvasRect);
			canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), textPaint);
			canvas.drawText(message.text, 10, 10, textPaint);
		}
		Log.i("SceneView", canvas.getWidth() + " " + canvas.getHeight());
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
