package classact.com.clever_little_monkey.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by hcdjeong on 2017/09/16.
 */

@Singleton
public class Fetch {

    @Inject Context context;
    private AssetManager assetManager;

    @Inject
    public Fetch() {
        // blank constructor
    }

    public String raw(String name) {
        String path;

        try {
            String packageName = context.getPackageName();
            int resourceId = context.getResources().getIdentifier(name, "raw", packageName);
            path = "android.resource://" + packageName + "/" + resourceId;

        } catch (Exception ex) {
            Toast.makeText(context, "Error getting raw: " + name, Toast.LENGTH_LONG).show();
            path = null;
        }

        return path;
    }

    public int rawId(String name) {
        int resourceId = 0;

        try {
            String packageName = context.getPackageName();
            resourceId = context.getResources().getIdentifier(name, "raw", packageName);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error getting raw Id: " + name, Toast.LENGTH_LONG).show();
        }

        return resourceId;
    }

    public String imagePath(String name) {
        String path = null;
        try {
            String packageName = context.getPackageName();
            int resourceId = context.getResources().getIdentifier(name, "drawable", packageName);
            path = "android.resource://" + packageName + "/" + resourceId;

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error getting drawable: " + name, Toast.LENGTH_LONG).show();
        }
        return path;
    }

    public int imageId(String name) {
        int resourceId = 0;
        try {
            String packageName = context.getPackageName();
            resourceId = context.getResources().getIdentifier(name, "drawable", packageName);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error getting drawable Id: " + name, Toast.LENGTH_LONG).show();
        }
        return resourceId;
    }

    public Typeface font() {
        if (assetManager == null) {
            assetManager = context.getAssets();
        }
        return Typeface.createFromAsset(assetManager, "fonts/edu_aid_gr1_solid.ttf");
    }


    /**
     * Below 3 methods courtesy:
     * Courtesy: https://github.com/iTech-Developer/TimedTextTest/blob/master/src/com/example/media/timedtexttest/MainActivity.java
     */

    public String rawFile(String resName) {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            int resId = Integer.parseInt(Uri.parse(raw(resName)).getPath().replace("/", ""));
            String fileName = context.getResources().getResourceEntryName(resId);
            File subtitleFile = context.getFileStreamPath(fileName);
            /*
            if (subtitleFile.exists()) {
                Log.d("tet", "" + resId + " Already exists");
                return subtitleFile.getAbsolutePath();
            }
            Log.d("test", "Subtitle does not exists, copy it from res/raw");
            */

            // Copy the file from the res/raw folder to your app folder on the
            // device

            inputStream = context.getResources().openRawResource(resId);
            outputStream = new FileOutputStream(subtitleFile, false);
            copyFile(inputStream, outputStream);
            return subtitleFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStreams(inputStream, outputStream);
        }
        return "";
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    // A handy method I use to close all the streams
    private void closeStreams(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable stream : closeables) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}