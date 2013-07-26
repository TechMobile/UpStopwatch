package br.com.upinterativo.upstopwatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MainStopwatchActivity extends Activity {

	private GestureDetector gd;
	private LinearLayout layoutButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stopwatch);
        startActivity(new Intent(getApplicationContext(), Settings.class));
        gd = new GestureDetector(this, simpleGestureDetector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_stopwatch, menu);
        layoutButton = (LinearLayout)findViewById(R.id.watchstop_circle_button);
        layoutButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				layoutButton.setBackgroundResource(R.drawable.watchstop_background_green);
				
			}
		});
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	return gd.onTouchEvent(event);
    }

    SimpleOnGestureListener simpleGestureDetector = new SimpleOnGestureListener(){
    	
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int dist = (int) (e1.getX() - e2.getX());
			if(velocityX < - 5000 && dist > 200){
				startActivity(new Intent(getApplicationContext(), Settings.class));
			}
			return false;
		}
    };
}
