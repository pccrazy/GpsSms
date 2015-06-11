package qais.com.gpssms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;


public class Status extends AppCompatActivity {


    TextView lock,gsm,gps,alarm;
    Button activatealarm,getlocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        lock=(TextView)findViewById(R.id.status_loc);
        gsm=(TextView)findViewById(R.id.status_gsm);
        gps=(TextView)findViewById(R.id.status_gps);
        alarm=(TextView)findViewById(R.id.status_alarm);
        activatealarm=(Button)findViewById(R.id.status_activatealaram);
        activatealarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendSMS("94200300","GSP_A1");

            }
        });
        getlocation=(Button)findViewById(R.id.status_getlocation);
        getlocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 sendSMS("94200300","locate");
             }
         });



        Smsread();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void Smsread()

    {
        SmsRadar.initializeSmsRadarService(Status.this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                Toast.makeText(getApplication(), sms.getMsg(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSmsReceived(Sms sms) {

                Log.d("data", "sms" + sms.getMsg());

                    if(sms.getMsg().equals("GSP_L1")) {
                        lock.setText("ON");
                    }
                    else if(sms.getMsg().equals("GSP_L0")) {
                        lock.setText("OFF");
                    }
                    else if(sms.getMsg().equals("GSP_GS1")) {
                        gsm.setText("ON");
                    }
                    else if(sms.getMsg().equals("GSP_GS0")) {
                        gsm.setText("OFF");
                    }
                    else if(sms.getMsg().equals("GSP_GP1")) {
                        gps.setText("ON");
                    }
                    else if(sms.getMsg().equals("GSP_GP0")) {
                        gps.setText("OFF");
                    }
                    else if(sms.getMsg().equals("GSP_A1")) {
                        alarm.setText("ON");
                    }
                    else if(sms.getMsg().equals("GSP_A0")) {
                        alarm.setText("OFF");
                    }

                }

        });
    }
    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
