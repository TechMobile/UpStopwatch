package br.com.upinterativo.upstopwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainStopwatchActivity extends Activity {

	private GestureDetector gd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stopwatch);
        
        gd = new GestureDetector(this, simpleGestureDetector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_stopwatch, menu);
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	return gd.onTouchEvent(event);
    }

    SimpleOnGestureListener simpleGestureDetector = new SimpleOnGestureListener(){
    	
//    	public boolean onDown(MotionEvent e) {
//    		return true;
//    	}
    	
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(velocityX < - 20){
				Toast.makeText(getApplicationContext(), "Swiiiiiiiiipe", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), Settings.class));
			}
			return false;
		}
    };
}
