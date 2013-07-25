package br.com.upinterativo.upstopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings  extends Activity {
	
	private Button btPlusRealTime;
	private Button btLessRealTime;
	private Button btPlusFakeTime;
	private Button btLessFakeTime;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        /*Botoes de interacao com o tempo real*/
        
        btPlusRealTime = (Button)findViewById(R.id.btPlusRealTime);
        btLessRealTime = (Button)findViewById(R.id.btLessRealTime);
        
        /*Botoes de interacao com o tempo falso*/
        
        btPlusFakeTime = (Button)findViewById(R.id.btPlusFakeTime);
        btLessFakeTime = (Button)findViewById(R.id.btLessFakeTime);
        
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
    }
}
