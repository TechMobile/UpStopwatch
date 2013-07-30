package br.com.upinterativo.upstopwatch;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Settings  extends Activity {
	
	private Button btPlusRealTime;
	private Button btLessRealTime;
	private Button btPlusFakeTime;
	private Button btLessFakeTime;
	
	private CheckBox checkAlarm;
	
	private TextView vl_soundSelect;
	
	private GestureDetector gd;
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        /*Evento para tratativa de gestos*/
        
        gd = new GestureDetector(this, simpleGestureDetector);
        
        /*Botoes de interacao com o tempo real*/
        
        btPlusRealTime = (Button)findViewById(R.id.btPlusRealTime);
        btLessRealTime = (Button)findViewById(R.id.btLessRealTime);
        
        /*Botoes de interacao com o tempo falso*/
        
        btPlusFakeTime = (Button)findViewById(R.id.btPlusFakeTime);
        btLessFakeTime = (Button)findViewById(R.id.btLessFakeTime);
        
        /*TextView do som selecionado*/
        
        vl_soundSelect = (TextView)findViewById(R.id.soundSelect);
        
        /*Checkbox para validar se tocara alarme*/
        
        checkAlarm = (CheckBox)findViewById(R.id.chAlarm);
        
        /*Status inicial do texview de som selecionado*/
        
        vl_soundSelect.setVisibility(View.GONE);
        
        /*Definindo cor transparente aos botoes*/
        
        btPlusRealTime.setBackgroundColor(0);
        btLessRealTime.setBackgroundColor(0);
        btPlusFakeTime.setBackgroundColor(0);
        btLessFakeTime.setBackgroundColor(0);
        
        /*Evento de clique para incrementar minutos do tempo real*/
        
        btPlusRealTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText valueRealTime = (EditText)findViewById(R.id.vl_realTime);
				int value = Integer.parseInt(valueRealTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 99) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueRealTime.setText(newValue);
				}
			}
		});
        
        /*Evento de clique para decrementar minutos do tempo real*/
        
        btLessRealTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText valueRealTime = (EditText)findViewById(R.id.vl_realTime);
				int value = Integer.parseInt(valueRealTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueRealTime.setText(newValue);
				}
			}
		});
        
        /*Evento de clique para incrementar minutos do tempo falso*/
        
        btPlusFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText valueRealTime = (EditText)findViewById(R.id.vl_fakeTime);
				int value = Integer.parseInt(valueRealTime.getText().toString());
				value++;
				String newValue = "";
				
				if (value <= 99) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueRealTime.setText(newValue);
				}
			}
		});
        
        /*Evento de clique para decrementar minutos do tempo real*/
        
        btLessFakeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText valueRealTime = (EditText)findViewById(R.id.vl_fakeTime);
				int value = Integer.parseInt(valueRealTime.getText().toString());
				value--;
				String newValue = "";
				
				if (value >= 0) {
					if (value < 10) {
						newValue = "0" + value;
					}
					else {
						newValue = "" + value;
					}
					
					valueRealTime.setText(newValue);
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
	        	  File file = new File(uri.toString());
	        	  String title = "Tocar‡:\n\t" + file.getName();
	              this.vl_soundSelect.setText(title);
	              this.vl_soundSelect.setVisibility(View.VISIBLE);
	          }
	          else
	          {
	        	  this.vl_soundSelect.setText("");
	        	  this.checkAlarm.setChecked(false);
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
			int dist = (int) (e1.getX() - e2.getX());
			if(velocityX < - 5000 && dist > 200){
				finish();
			}
			
			return false;
		}
    };
}