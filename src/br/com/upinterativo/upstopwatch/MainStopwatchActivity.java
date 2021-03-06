package br.com.upinterativo.upstopwatch;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainStopwatchActivity extends Activity {
	protected static String IS_RUNNING = "isRunning";
	protected static String IS_PLAYING_SOUND = "isPlayingSound";
	protected static String ELAPSED_TIME = "elapsedTime";
	protected static String REAL_TIME = "realTime";
	protected static String FAKE_TIME = "fakeTime";
	protected static String URI_SONG = "uriSong";
	protected static String TOCAR_ALARME = "tocarAlarme";
	protected static String START_TIME = "startTime";
	protected static String PAUSED_TIME = "pausedTime";
	protected static String IS_PAUSED = "isPaused";
	protected static String LAST_PAUSED = "lastPaused";
	
	private GestureDetector gd;
	private LinearLayout layoutButton;
	private boolean isRunning;
	private boolean isPlayingSound;
	private Timer timer;
	private long elapsedTime;
	private long startTime;
	private long realTime;
	private long fakeTime;
	private long lastPaused;
	private long pausedTime;
	private boolean isPaused;
	private Ringtone r;
	private String uriSong;
	private boolean tocarAlarme;
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stopwatch);
        gd = new GestureDetector(this, simpleGestureDetector);
        isRunning = false;
        isPaused = false;
        lastPaused = 0;
        pausedTime = 0;
        timer = new Timer();
        elapsedTime = 0;
        startTime = 0;
        isPlayingSound = false;
        
        prepareTimer();
        prepareButtons();
        prepareSensors();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putBoolean(IS_RUNNING, isRunning);
    	outState.putBoolean(IS_PLAYING_SOUND, isPlayingSound);
    	outState.putLong(ELAPSED_TIME, elapsedTime);
    	outState.putLong(FAKE_TIME, fakeTime);
    	outState.putLong(REAL_TIME, realTime);
    	outState.putString(URI_SONG, uriSong);
    	outState.putBoolean(TOCAR_ALARME, tocarAlarme);
    	outState.putBoolean(IS_PAUSED, isPaused);
    	outState.putLong(START_TIME, startTime);
    	outState.putLong(PAUSED_TIME, pausedTime);
    	outState.putLong(LAST_PAUSED, lastPaused);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	isRunning = savedInstanceState.getBoolean(IS_RUNNING);
    	isPlayingSound = savedInstanceState.getBoolean(IS_PLAYING_SOUND);
    	elapsedTime = savedInstanceState.getLong(ELAPSED_TIME);
    	fakeTime = savedInstanceState.getLong(FAKE_TIME);
    	realTime = savedInstanceState.getLong(REAL_TIME);
    	tocarAlarme = savedInstanceState.getBoolean(TOCAR_ALARME);
    	isPaused = savedInstanceState.getBoolean(IS_PAUSED);
    	startTime = savedInstanceState.getLong(START_TIME);
    	pausedTime = savedInstanceState.getLong(PAUSED_TIME);
    	lastPaused = savedInstanceState.getLong(LAST_PAUSED);
    	uriSong = savedInstanceState.getString(URI_SONG);
    	if(uriSong != null && !uriSong.isEmpty())
    		r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(uriSong));
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	timer.cancel();
    }
    
    private void setTimeText(){
    	long milisecs = (elapsedTime % 1000);
    	if(isPlayingSound){
			elapsedTime -= milisecs;
			milisecs = 0;
		}
		long secs = (elapsedTime/1000) % 60;
		long min = (elapsedTime/(60*1000));
		TextView milis = (TextView)findViewById(R.id.text_view_mili);
		TextView seconds = (TextView)findViewById(R.id.text_view_seconds);
		TextView minutes = (TextView)findViewById(R.id.text_view_minutes);
		milis.setText( String.format("%02d", milisecs/10));
		seconds.setText(String.format("%02d" , secs));
		minutes.setText(String.format("%02d", min));
		
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.activity_main_stopwatch, menu);
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return gd.onTouchEvent(event);
    }
    
    private void prepareTimer(){
    	timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(isRunning){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(isRunning){
								Date date = new Date();
								long timeNow = date.getTime();
								if(!isPaused)
									elapsedTime = (timeNow - startTime)*fakeTime/realTime - pausedTime;
								else {
									pausedTime += (timeNow - lastPaused)*fakeTime/realTime;
									lastPaused = timeNow;
								}
								setTimeText();
								if(elapsedTime >= fakeTime){
									isRunning = false;
									if(tocarAlarme){
										r.play();
									}
									isPlayingSound = true;
								}
							}
						}
					});
				}
				
			}
		}, 0, 10);
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
    
    private void prepareButtons(){
    	layoutButton = (LinearLayout)findViewById(R.id.watchstop_circle_button);
        layoutButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME, 0);
		        realTime = settings.getLong(Settings.REAL_TIME, 20*60*1000);
		        fakeTime = settings.getLong(Settings.FAKE_TIME, 20*60*1000);
		        uriSong = settings.getString(Settings.URL_SONG, "");
		        tocarAlarme = settings.getBoolean(Settings.TOCAR_ALARME, false);
		        r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(uriSong));
		        if(!isPlayingSound){
		        	Date date = new Date();
					if(!isRunning)
					{
						layoutButton.setBackgroundResource(R.drawable.watchstop_background_green);
						startTime = date.getTime();
						pausedTime = 0;
						isRunning = true;
					} else {
						if( !isPaused ){
							layoutButton.setBackgroundResource(R.drawable.watchstop_background);
						} else {
							layoutButton.setBackgroundResource(R.drawable.watchstop_background_green);
						}
						lastPaused = date.getTime();
						isPaused = isPaused ? false : true;
					}
		        } else {
		        	if(tocarAlarme)
		        		r.stop();
		        	isPlayingSound = false;
		        	elapsedTime = 0;
		        	pausedTime = 0;
		        	setTimeText();
		        	layoutButton.setBackgroundResource(R.drawable.watchstop_background);
		        }
			}
		});
        
        layoutButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(isPaused){
					isRunning = isPaused = false;
					elapsedTime = 0;
					pausedTime = 0;
					TextView milis = (TextView)findViewById(R.id.text_view_mili);
					TextView seconds = (TextView)findViewById(R.id.text_view_seconds);
					TextView minutes = (TextView)findViewById(R.id.text_view_minutes);
					milis.setText(R.string.init_miliseconds);
					seconds.setText(R.string.init_second);
					minutes.setText(R.string.init_minute);
					return true;
				}
				return false;
			}
		});
    }
    
    private void prepareSensors(){
    	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();   

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
        	public void onShake() {
        		if(!isRunning)
        			startActivity(new Intent(getApplicationContext(), Settings.class));
        	}
        });
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mSensorManager.registerListener(mSensorListener,
          mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
          SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
    	mSensorManager.unregisterListener(mSensorListener);
    	super.onStop();
    }
}
