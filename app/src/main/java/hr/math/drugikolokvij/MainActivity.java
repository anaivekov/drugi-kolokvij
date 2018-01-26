package hr.math.drugikolokvij;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    private int MY_PERM=1;

    public String downloadedFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},MY_PERM);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},MY_PERM);
        }

        // ovo sam dodala samo da provjerim pita li me za permission i pitao me prvi put, a za pristup internetu nikad
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},MY_PERM);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},MY_PERM);
        }

        DBAdapter db = new DBAdapter(this);
        //---add a contact---
        db.open();
        long id = db.insertSite("www.jutarnji.hr", "vijesti");
        id = db.insertSite("www.mojposao.hr", "posao");
        id = db.insertSite("www.facebook.com", "drustvena mreza");
        id = db.insertSite("www.sportskenovosti.hr", "sport");
        id = db.insertSite("www.studentski.hr", "informativno");

        id = db.insertOwner(1);
        id = db.insertOwner(2);
        id = db.insertOwner(3);
        id = db.insertOwner(4);
        id = db.insertOwner(5);
        db.close();
    }

    //sends an SMS message to another device
    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }


    public void downloadWantedText(View view){
        EditText mEdit = (EditText) findViewById(R.id.download_address);
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try{
            in = OpenHttpConnection(mEdit.getText().toString());
        } catch (IOException e1){
            Toast.makeText(this, e1.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            e1.printStackTrace();
            return;
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        char[] inputBuffer = new char[BUFFER_SIZE];
        try{
            while((charRead = isr.read(inputBuffer)) > 0)
            {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                downloadedFile += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        sendSMS("5556", "datoteka1.txt je preuzeta");
    }

    public void showData(View view) {

        try {
            Intent k = new Intent(this, Main2Activity.class);
            startActivity(k);
        } catch (Exception e) {
        }

    }

    public void deleteSomeSite(View view) {
        try {
            Intent k = new Intent(this, Main3Activity.class);
            startActivity(k);
        } catch (Exception e) {
        }
    }

    public void deleteSomeOwner(View view) {
        try {
            Intent k = new Intent(this, Main4Activity.class);
            startActivity(k);
        } catch (Exception e) {
        }
    }

    public void displayTheThirdSite(View view) {
        try {
            Intent k = new Intent(this, Main5Activity.class);
            startActivity(k);
        } catch (Exception e) {
        }
    }

}
