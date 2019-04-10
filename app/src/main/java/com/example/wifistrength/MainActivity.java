package com.example.wifistrength;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    int ll=0;
    static final String FILE_NAME = "example.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void getwifiinfo(View view){
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        textView4=(TextView)findViewById(R.id.textView4);
        String x="";
        List<ScanResult> list= wifiManager.getScanResults();
        /*list of all net*/
        StringBuffer sb=new StringBuffer("");
        for(ScanResult sr:list) {
            sb.append("Name: "+sr.SSID+" Strength: "+sr.frequency + "\n");
        }
        textView2.setText(new String(sb));
        if(wifiManager.isWifiEnabled()){

                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
                    x = x + "Name: " + wifiInfo.getSSID() + "\nStrength: " + wifiInfo.getRssi() + "\n" + wifiInfo.getLinkSpeed() + "\n";
                    textView.setText(x);

                    //Toast.makeText(this, x + "",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please connect wifi ", Toast.LENGTH_SHORT).show();
                }

        }else{
            wifiManager.setWifiEnabled(true);
        }

    }

    public void save(View v){
        String text=textView4.getText().toString();
        FileOutputStream fos=null;
        File filepath=getExternalFilesDir("sangram");
        File file = new File(filepath, "sangram.txt");
        try {
            fos = new FileOutputStream(file);
            fos.write(text.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos=openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Data is saved in file "+getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void display(View view) {
        File file = new File(Environment.getExternalStorageDirectory(),"sangram.txt");

        Uri selectedUri = Uri.parse(file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "text/csv");

        if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
        {
            startActivity(intent);
        }
    }
    public void load(View v){
        FileInputStream fis=null;
        try {
            fis= openFileInput(FILE_NAME);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;
            while((text = br.readLine())!= null){
                sb.append(text).append("\n");
            }
            textView3.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void start(View v){
        ll=0;
        TextView textView4;
        textView4=(TextView)findViewById(R.id.textView4);
        textView4.setText("");
            st();
    }
    public  void st(){
        Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addd();
                    if(ll!=10) {
                        ll++;st();
                    }

                }
            }, 1000);
    }
    public void addd(){
        TextView textView4;
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        textView4=(TextView)findViewById(R.id.textView4);
        String x=textView4.getText().toString();
        if(wifiManager.isWifiEnabled()){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                x = x + "Name: " + wifiInfo.getSSID() + "Strength: " + wifiInfo.getRssi() +" T: "+currentDateandTime+"\n";
                textView4.setText(x);
                //Toast.makeText(this, x + "",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please connect wifi ", Toast.LENGTH_SHORT).show();
            }

        }else{
            wifiManager.setWifiEnabled(true);
        }
    }
}
