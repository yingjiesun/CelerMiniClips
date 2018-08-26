package com.yingjiesun.celerminiclips.persistence;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
public class PersistenceHelper {
    /**
     * Downloads a file given URL to specified destination
     * @param url
     * @param destFile
     * @return
     */
    //public static boolean downloadFile(Context context, String url, String destFile) {
    public static void downloadFile(Context context, final String url, final File destFile) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                    BufferedInputStream bis = null;
                    FileOutputStream fos = null;
                    InputStream is = null;

                    try {
                        URL myUrl = new URL(url);
                        URLConnection connection = myUrl.openConnection();

                        is = connection.getInputStream();
                        bis = new BufferedInputStream(is);
                        ByteArrayOutputStream baf = new ByteArrayOutputStream(1024);

                        int n = 0;
                        while( (n = bis.read()) != -1 )
                            baf.write((byte) n);

                        // save to internal storage
                        Log.i("tag", "*** Start write file..." );
                        fos = new FileOutputStream(destFile);
                        //context.openFileOutput(destFile, context.MODE_PRIVATE);
                        fos.write(baf.toByteArray());
                        fos.close();
                        //Log.v(TAG, "File saved successfully.");
                        Log.i("tag", "*** Video file saved to internal storage successfully");

                    }
                    catch(Exception e) {
                        Log.i("tag", "*** PersistenceHelper Exception." + e);
                    }
                    finally {
                        try {
                            if ( fos != null ) fos.close();
                        } catch( IOException e ) {}
                        try {
                            if ( bis != null ) bis.close();
                        } catch( IOException e ) {}
                        try {
                            if ( is != null ) is.close();
                        } catch( IOException e ) {}
                    }

            }
        }) .start();

    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
