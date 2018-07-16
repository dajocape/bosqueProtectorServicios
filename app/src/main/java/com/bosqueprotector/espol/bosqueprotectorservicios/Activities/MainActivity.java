package com.bosqueprotector.espol.bosqueprotectorservicios.Activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.bosqueprotector.espol.bosqueprotectorservicios.R;
import com.bosqueprotector.espol.bosqueprotectorservicios.Services.SendingAudiosService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SLEEP_TIME;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.call;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.onService;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.setPreferencesApplications;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.alarmManager;
import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.pendingIntent;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    SendingAudiosService senderAudio;
    private boolean CONNECTION = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //INICIAR EL SERVICIO
        if(!onService) {
            createAlarm();
        }
        setContentView(com.bosqueprotector.espol.bosqueprotectorservicios.R.layout.activity_main);
    }

    private void createAlarm() {
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        setPreferencesApplications(getApplicationContext());

        pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(this, SendingAudiosService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    SLEEP_TIME, pendingIntent);
            Log.d("ALARMA", "ALARMA CREADA");
        }
        onService = true;
    }

    //REINICIAR EL SERVICIO
    public void reboot(){
        Intent intentAudioRecord = new Intent(this, SendingAudiosService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(this, SendingAudiosService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SendingAudiosService.class.getName().equals(service.service.getClassName())) {
                alarmManager.cancel(pendingIntent);
                call.cancel();
                SendingAudiosService.thread.interrupt();
                stopService(intentAudioRecord);
                break;
            }
        }
        createAlarm();
        Toast.makeText(this, "SERVICIO REINICIADO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prefs:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.reboot:
                reboot();
                break;
        }
        return true;
    }

}