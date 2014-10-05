package com.example.comicim_20.scene;

import com.example.comicim_20.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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

public class SceneView extends View{
	
	//private static Context context;
	private Bitmap bitmap;
	private Canvas canvas;
	private ImageView imageView;
	
	public SceneView(Context context, AttributeSet attrs, String text)
	{
		super(context, attrs);
		this.imageView = newImage (context, text);
	}
	public ImageView newImage(Context context, String text)
	{
		Drawable[] layers = new Drawable[2];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dude, options).copy(Bitmap.Config.ARGB_4444, true);
		layers[0] = context.getResources().getDrawable(R.drawable.dude);
		
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTextSize(14);
		paint.setAntiAlias(true);
		
		this.canvas = new Canvas(bitmap);
		
		String[] textArr = text.split(" ");
		String string = new String();
		int offset = 0;
		string = textArr[0];
		canvas.drawText(string, 0, canvas.getHeight()/4 + offset, paint);
		
		for(int i = 1; i < textArr.length; i++){
			string = string + " " + textArr[i];
			Log.d("EdmanDebug", string);
			if(string.length() > 25){
				string = new String();
				string = string + textArr[i];
				offset += 10;
			}
			canvas.drawText(string, 0, canvas.getHeight()/4 + offset, paint);
		}
		layers[1] = new BitmapDrawable(context.getResources(), bitmap);
		
		LayerDrawable ld = new LayerDrawable(layers);
		ImageView i = new ImageView(context);
		i.setImageDrawable(ld);
		return i;
	}

}
