package net.ivanvega.mibroadcastreceiverytelefonia;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MiReceiverTelefonia;
import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    MyBroadcastReceiver myBroadcastReceiver =
            new MyBroadcastReceiver();
    private static final String TAG = "Llamar";
    MiReceiverTelefonia miReceiverTelefonia = new MiReceiverTelefonia();

    Button btnS, btnConf;
    TextView lbl;
    EditText txtTel, txtMessage;
    static String tel = "", msm = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnS = findViewById(R.id.btnSend);
        lbl = findViewById(R.id.lbl);
        txtTel = findViewById(R.id.txtPhone);
        txtMessage = findViewById(R.id.txtTexto);
        btnConf = findViewById(R.id.btnConfi);

        btnS.setOnClickListener(view -> {
            Intent broadcast = new Intent();
            //net.ivanvega.mibroadcastreceiverytelefonia.MIBROACAST

            broadcast.setAction(getString(R.string.action_broadcast));
            broadcast.putExtra("key1", "parametro de la difusion");
            sendBroadcast(broadcast);
        });

        btnConf.setOnClickListener(l -> {
            Toast.makeText(getApplicationContext(), "Se configuró correctamente", Toast.LENGTH_LONG).show();
            tel = txtTel.getText().toString();
            msm = txtMessage.getText().toString();
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(getString(R.string.action_broadcast));
        this.registerReceiver(myBroadcastReceiver, filter);


        //Telephony.Sms .Intents.SMS_RECEIVED_ACTION

        IntentFilter intentFilterTel = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        getApplicationContext().registerReceiver(miReceiverTelefonia,
                intentFilterTel
        );

    }

    private void enviarSMS(String tel, String msj) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(tel, null, msj,
                null, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcastReceiver);
    }

    public void btnSMS_onclick(View v) {
        enviarSMS(txtTel.getText().toString(), txtMessage.getText().toString());
    }

    public void sendMsm(String number) {
        Log.d(TAG, "sendSMSAnswer: " + tel);
        Log.d(TAG, "sendSMSAnswer: " + number);
        Log.d(TAG, "sendSMSAnswer: " + msm);
        if (number.equals("+52" + tel)) {
            enviarSMS(number, msm);
        }
    }

}