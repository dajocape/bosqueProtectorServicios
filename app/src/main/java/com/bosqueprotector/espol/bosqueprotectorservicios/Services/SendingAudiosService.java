package com.bosqueprotector.espol.bosqueprotectorservicios.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.FolderIterator;
import com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bosqueprotector.espol.bosqueprotectorservicios.Utils.Identifiers.SENDING_AUDIO_TIME;

/**
 * Created by joset on 16/01/2018.
 */

public class SendingAudiosService extends Service {

    /*
    * Tags
     */
    //private  final String url ="http://200.126.1.156/gzip/UploadFile";
    //private  final String url ="http://10.0.2.2:3000/upload";
    private  final String url ="http://192.168.183.234:3000/upload";
    private int counter = 0;
    private static final String TAG = SendingAudiosService.class.getSimpleName();
    OkHttpClient okHttpClient = new OkHttpClient();
    @Nullable
    //
    /**
     Binder given to clients, to bind the service to the
     */
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SendingAudiosService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SendingAudiosService.this;
        }
    }

    @Nullable
    @Override
    /*
     * return my binder class
     */
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSendingAudios();
            }
        }).start();
        return START_STICKY;
    }



    /**
     * Aqui estan implementados los metodos del cliente, aquellas cosas
     * Implemented client's methods
     */

    public void printImConnected(){
        Log.i(TAG,"esto es un print de estoy conectado" );
    }

    public void startSendingAudios(){
        Log.i(TAG, "esta es carpeta: " + Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio");
        //iteratingFolders( new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio"));
        //FolderIterator folderIterator = new FolderIterator();
        //folderIterator.iteratingFolders(TAG,url, new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio/"),okHttpClient);
        //Declare the timer

        Timer t = new Timer();
        //Set the schedule function and rate
                t.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        Log.i(TAG, "enter in the timer\n ");
                        FolderIterator folderIterator = new FolderIterator();
                        folderIterator.iteratingFolders(TAG,url, new File(Environment.getExternalStorageDirectory().getPath()+ "/rfcx/audio/"),okHttpClient);

                    }

                },
        //Set how long before to start calling the TimerTask (in milliseconds)
        200,
        //Set the amount of time between each execution (in milliseconds)
                        SENDING_AUDIO_TIME*1000);



    }








}
