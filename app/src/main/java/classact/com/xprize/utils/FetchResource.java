package classact.com.xprize.utils;

import android.content.Context;
import android.os.Environment;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by hyunchanjeong on 2017/01/25.
 */

public class FetchResource {

    private static final String LOCATION = "/Android/media/";
    private static final String VIDEOS = "/videos/";
    private static final String SOUNDS = "/sounds/";
    private static final String IMAGES = "/images/";

    private static final String JPG = ".jpg";
    private static final String PNG = ".png";
    private static final String MP3 = ".mp3";
    private static final String MP4 = ".mp4";
    private static final String WAV = ".wav";

    public static String video(Context context, String name) {
        String path = null;
        FileInputStream fis = null;
        FileDescriptor fd;

        try {
            try {
                // Get MP4 path
                String head = Environment.getExternalStorageDirectory() + LOCATION + context.getPackageName() + VIDEOS;
                path = head + name + MP4;

                // Initialize new fis
                fis = new FileInputStream(path);

                // Get file descriptor
                fd = fis.getFD();

                // Check if file descriptor is valid
                if (!fd.valid()) {
                    throw new Exception("File Descriptor is invalid");
                }

            } catch (Exception ex) {
                System.err.println("FetchResource.video > Exception: " + ex.getMessage());
                path = null;

            } finally {
                // Close fis
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException ioex) {
            System.err.println("FetchResource.video > IOException: " + ioex.getMessage());
            path = null;
        }

        return path;
    }

    public static String sound(Context context, String name) {
        String path = null;
        FileInputStream fis = null;
        FileDescriptor fd;

        try {
            try {
                // Get MP3 Path
                String head = Environment.getExternalStorageDirectory() + LOCATION + context.getPackageName() + SOUNDS;
                path = head + name + MP3;

                // Initialize new fis
                fis = new FileInputStream(path);

                // Get file descriptor
                fd = fis.getFD();

                // // Validate file descriptor
                if (!fd.valid()) {

                    // Close existing fis
                    fis.close();

                    // Try Get WAV Path
                    path = head + name + WAV;

                    // Open new fis
                    fis = new FileInputStream(path);

                    // Get file descriptor
                    fd = fis.getFD();

                    // Validate file descriptor
                    if (!fd.valid()) {
                        throw new Exception("File Descriptor is invalid");
                    }
                }

            } catch (Exception ex) {
                System.err.println("FetchResource.sound > Exception: " + ex.getMessage());
                path = null;

            } finally {
                // Close fis
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException ioex) {
            System.err.println("FetchResource.sound > IOException: " + ioex.getMessage());
            path = null;
        }

        return path;
    }

    public static String image(Context context, String name) {
        String path = null;
        FileInputStream fis = null;
        FileDescriptor fd;

        try {
            try {
                // Get PNG path
                String head = Environment.getExternalStorageDirectory() + LOCATION + context.getPackageName() + IMAGES;
                path = head + name + PNG;

                // Initialize new fis
                fis = new FileInputStream(path);

                // Get file descriptor
                fd = fis.getFD();

                // // Validate file descriptor
                if (!fd.valid()) {

                    // Close existing fis
                    fis.close();

                    // Get JPG path instead
                    path = head + name + JPG;

                    // Open new fis
                    fis = new FileInputStream(path);

                    // Get file descriptor
                    fd = fis.getFD();

                    // Validate file descriptor
                    if (!fd.valid()) {
                        throw new Exception("File Descriptor is invalid");
                    }
                }

            } catch (Exception ex) {
                System.err.println("FetchResource.image > Exception: " + ex.getMessage());
                path = null;

            } finally {
                // Close fis
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException ioex) {
            System.err.println("FetchResource.image > IOException: " + ioex.getMessage());
            path = null;
        }

        return path;
    }
}
