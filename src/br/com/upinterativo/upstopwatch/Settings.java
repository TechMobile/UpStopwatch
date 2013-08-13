package br.com.upinterativo.upstopwatch;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Settings  extends Activity {
	
	private Button btPlusMinRealTime;
	private Button btLessMinRealTime;
	private Button btPlusMinFakeTime;
	private Button btLessMinFakeTime;
	private Button btPlusSecRealTime;
	private Button btLessSecRealTime;
	private Button btPlusSecFakeTime;
	private Button btLessSecFakeTime;
	
	private CheckBox checkAlarm;
	
	private TextView vl_soundSelect;
	
	private GestureDetector gd;
	
	public static long realMinutes;
	public static long fakeMinutes;
	public static long realSeconds;
	public static long fakeSeconds;
	
	public static boolean tocarAlarme;
	
	public static String urlSong;
	public static String nameSong;
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	
	public static final String PREFS_NAME = "DataSettings";
	public static final String REAL_MINUTES = "realMinutes";
	public static final String FAKE_MINUTES = "fakeMinutes";
	public static final String REAL_SECONDS = "realSeconds";
	public static final String FAKE_SECONDS = "fakeSeconds";
	public static final String REAL_TIME = "realTime";
	public static final String FAKE_TIME = "fakeTime";
	public static final String TOCAR_ALARME = "tocarAlarme";
	public static final String URL_SONG = "urlSong";
	public static final String NAME_SONG = "nameSong";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        /*Dados armazenados*/
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        realMinutes = settings.getLong(REAL_MINUTES, 1);
        fakeMinutes = settings.getLong(FAKE_MINUTES, 20);
        realSeconds = settings.getLong(REAL_SECONDS, 0);
        fakeSeconds = settings.getLong(FAKE_SECONDS, 0);
        
        urlSong = settings.getString(URL_SONG, "");
        nameSong = settings.getString(NAME_SONG, "");
        tocarAlarme = settings.getBoolean(TOCAR_ALARME, false);
        
        //Prepara sensor de movimento
        prepareSensors();
        
        /*Evento para tratativa de gestos*/
        
        gd = new GestureDetector(this, simpleGestureDetector);
        
        /*Botoes de longeracao com o tempo real*/
        
         btPlusMinRealTime = (Button)findViewById(R.id.btPlusMinRealTime);
         btLessMinRealTime = (Button)findViewById(R.id.btLessMinRealTime);
         btPlusSecRealTime = (Button)findViewById(R.id.btPlusSecRealTime);
         btLessSecRealTime = (Button)findViewById(R.id.btLessSecRealTime);
         
        
        /*Botoes de longeracao com o tempo falso*/
        
        btPlusMinFakeTime = (Button)findViewById(R.id.btPlusMinFakeTime);
        btLessMinFakeTime = (Button)findViewById(R.id.btLessMinFakeTime);
        btPlusSecFakeTime = (Button)findViewById(R.id.btPlusSecFakeTime);
        btLessSecFakeTime = (Button)findViewById(R.id.btLessSecFakeTime);
        
        /*TextView do som selecionado*/
        
        vl_soundSelect = (TextView)findViewById(R.id.soundSelect);
        
        /*Checkbox para validar se tocara alarme*/
        
        checkAlarm = (CheckBox)findViewById(R.id.chAlarm);
        
        /*Status inicial dos valores de tempo*/        
        
        if (realMinutes > 0) {
        	TextView valueMinRealTime = (TextView)findViewById(R.id.vl_realMinTime);
        	if (realMinutes > 9)
        		valueMinRealTime.setText(String.valueOf(realMinutes));
        	else {
        		String value = "0" + realMinutes;
        		valueMinRealTime.setText(value);
        	}
        }
        
        if (fakeMinutes > 0) {
        	TextView valueMinFakeTime = (TextView)findViewById(R.id.vl_fakeMinTime);
        	if (fakeMinutes > 9)
    			valueMinFakeTime.setText(String.valueOf(fakeMinutes));
        	else {
        		String value = "0" + fakeMinutes;
        		valueMinFakeTime.setText(value);
        	}
        }
        
        if (realSeconds > 0) {
        	TextView valueSecRealTime = (TextView)findViewById(R.id.vl_realSecTime);
        	if (realSeconds > 9) {
        		valueSecRealTime.setText(String.valueOf(realSeconds));
        	}
        	else {
        		String value = "0" + realSeconds;
        		valueSecRealTime.setText(value);
        	}
        }
        
        if (fakeSeconds > 0) {
        	TextView valueSecFakeTime = (TextView)findViewById(R.id.vl_fakeSecTime);
        	if (fakeSeconds > 9) {
        		valueSecFakeTime.setText(String.valueOf(fakeSeconds));
        	}
        	else {
        		String value = "0" + fakeSeconds;
        		valueSecFakeTime.setText(value);
        	}
        }
        
        /*Status inicial do checkbox para emitir alarme*/
        
        checkAlarm.setChecked(tocarAlarme);
        
        /*Status inicial do texview de som selecionado*/
        
        if (urlSong.isEmpty())
        	vl_soundSelect.setVisibility(View.GONE);
        else
        	vl_soundSelect.setText(nameSong);
        
        /*Definindo cor transparente aos botoes*/
        
        btPlusMinRealTime.setBackgroundColor(0);
        btLessMinRealTime.setBackgroundColor(0);
        btPlusMinFakeTime.setBackgroundColor(0);
        btLessMinFakeTime.setBackgroundColor(0);

        btPlusSecFakeTime.setBackgroundColor(0);
        btPlusSecRealTime.setBackgroundColor(0);
        btLessSecFakeTime.setBackgroundColor(0);
        btLessSecRealTime.setBackgroundColor(0);

        btPlusSecRealTime.setBackgroundColor(0);
        btLessSecRealTime.setBackgroundColor(0);
        btPlusSecFakeTime.setBackgroundColor(0);
        btLessSecFakeTime.setBackgroundColor(0);
        
        /*Evento de clique para incrementar minutos do tempo real*/
        
        btPlusMinRealTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView valueMinRealTime = (TextView)findViewById(R.id.vl_realMinTime);
				long value = Long.parseLong(valueMinRealTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 99) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueMinRealTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(REAL_MINUTES, value);
					
					/*DEFINE TEMPO REAL*/
					long secondsReal = settings.getLong(REAL_SECONDS, 0);
					editor.putLong(REAL_TIME, (value * 60000) + (secondsReal * 1000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para incrementar segundos do tempo real*/
        
        btPlusSecRealTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView valueSecRealTime = (TextView)findViewById(R.id.vl_realSecTime);
				long value = Long.parseLong(valueSecRealTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 60) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueSecRealTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(REAL_SECONDS, value);
					
					/*DEFINE TEMPO REAL*/
					long minutesReal = settings.getLong(REAL_MINUTES, 0);
					editor.putLong(REAL_TIME, (value * 1000) + (minutesReal * 60000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para decrementar minutos do tempo real*/
        
        btLessMinRealTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView valueMinRealTime = (TextView)findViewById(R.id.vl_realMinTime);
				long value = Long.parseLong(valueMinRealTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueMinRealTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(REAL_MINUTES, value);
					
					/*DEFINE TEMPO REAL*/
					long secondsReal = settings.getLong(REAL_SECONDS, 0);
					editor.putLong(REAL_TIME, (value * 60000) + (secondsReal * 1000));
					
					editor.commit();
				}
			}
		});
        
    	/*Evento de clique para decrementar segundos do tempo real*/
        
        btLessSecRealTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView valueSecRealTime = (TextView)findViewById(R.id.vl_realSecTime);
				long value = Long.parseLong(valueSecRealTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueSecRealTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(REAL_SECONDS, value);
					
					/*DEFINE TEMPO REAL*/
					long minutesReal = settings.getLong(REAL_MINUTES, 0);
					editor.putLong(REAL_TIME, (value * 1000) + (minutesReal * 60000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para incrementar minutos do tempo falso*/
        
        btPlusMinFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TextView valueMinFakeTime = (TextView)findViewById(R.id.vl_fakeMinTime);
				long value = Long.parseLong(valueMinFakeTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 99) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueMinFakeTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(FAKE_MINUTES, value);
					
					/*DEFINE TEMPO FALSO*/
					long secondsFake = settings.getLong(FAKE_SECONDS, 0);
					editor.putLong(FAKE_TIME, (value * 60000) + (secondsFake * 1000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para incrementar segundos do tempo falso*/
        
        btPlusSecFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TextView valueSecFakeTime = (TextView)findViewById(R.id.vl_fakeSecTime);
				long value = Long.parseLong(valueSecFakeTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 60) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueSecFakeTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(FAKE_SECONDS, value);
					
					/*DEFINE TEMPO FALSO*/
					long minutesFake = settings.getLong(FAKE_MINUTES, 0);
					editor.putLong(FAKE_TIME, (value * 1000) + (minutesFake * 60000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para decrementar minutos do tempo falso*/
        
        btLessMinFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView valueMinFakeTime = (TextView)findViewById(R.id.vl_fakeMinTime);
				long value = Long.parseLong(valueMinFakeTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueMinFakeTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(FAKE_MINUTES, value);
					
					/*DEFINE TEMPO FALSO*/
					long secondsFake = settings.getLong(FAKE_SECONDS, 0);
					editor.putLong(FAKE_TIME, (value * 60000) + (secondsFake * 1000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de clique para decrementar segundos do tempo falso*/
        
        btLessSecFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView valueSecFakeTime = (TextView)findViewById(R.id.vl_fakeSecTime);
				long value = Long.parseLong(valueSecFakeTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueSecFakeTime.setText(newValue);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong(FAKE_SECONDS, value);
					
					/*DEFINE TEMPO FALSO*/
					long minutesFake = settings.getLong(FAKE_MINUTES, 0);
					editor.putLong(FAKE_TIME, (value * 1000) + (minutesFake * 60000));
					
					editor.commit();
				}
			}
		});
        
        /*Evento de checkbox para validacao de alarme*/
        
        checkAlarm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean checked = ((CheckBox) v).isChecked();
				
				if (checked) {
					getSound();
				}
				else {
					vl_soundSelect.setVisibility(View.GONE);
					
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(NAME_SONG, "");
					editor.putString(URL_SONG, "");
					editor.putBoolean(TOCAR_ALARME, false);
					
					editor.commit();
				}
			}
		});
    }
	
	private void getSound() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
		this.startActivityForResult(intent, 5);
	}
	
	@Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
	 {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5)
	     {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

	          if (uri != null)
	          {
	        	  Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
	        	  String ringToneName = ringtone.getTitle(this);
	              this.vl_soundSelect.setText(ringToneName);
	              this.vl_soundSelect.setVisibility(View.VISIBLE);
	             
	              SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	              SharedPreferences.Editor editor = settings.edit();
	              editor.putString(NAME_SONG, ringToneName);
	              editor.putString(URL_SONG, uri.toString());
	              editor.putBoolean(TOCAR_ALARME, true);
					
	              editor.commit();
	          }
	          else
	          {
	        	  this.vl_soundSelect.setText("");
	        	  this.checkAlarm.setChecked(false);
	        	  
	        	  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        	  SharedPreferences.Editor editor = settings.edit();
	        	  editor.putString(NAME_SONG, "");
	        	  editor.putString(URL_SONG, "");
	        	  editor.putBoolean(TOCAR_ALARME, false);
				
	        	  editor.commit();
	          }
	      }            
	  }
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	return gd.onTouchEvent(event);
    }

    SimpleOnGestureListener simpleGestureDetector = new SimpleOnGestureListener() {
    	
    	/*Swype para troca de tela (Abre a tela principal)*/
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
			long dist = (long) (e1.getX() - e2.getX());
			if(velocityX < - 5000 && dist > 200) {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				realMinutes = settings.getLong(REAL_MINUTES, 0);
				fakeMinutes = settings.getLong(FAKE_MINUTES, 0);
				
				finish();
			}
			
			return false;
		}
    };
    
    private void prepareSensors(){
    	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();   

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
        	public void onShake() {
        		finish();
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