package classact.com.xprize.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;

/**
 * Created by hyunchanjeong on 2017/01/25.
 */

public class FetchResource {

    private static final String LOCATION = "/Android/media/";
    private static final String VIDEOS = "raw";
    private static final String SOUNDS = "/sounds/";
    private static final String IMAGES = "/images/";

    private static final String JPG = ".jpg";
    private static final String PNG = ".png";
    private static final String MP3 = ".mp3";
    private static final String MP4 = ".mp4";
    private static final String WAV = ".wav";

    public static String video(Context context, String name) {
        String path = null;

        try {
            // Get MP4 path
            String packageName = context.getPackageName();
            int resourceId = context.getResources().getIdentifier(name, "raw", packageName);
            path = "android.resource://" + packageName + "/" + resourceId;

        } catch (Exception ex) {
            System.err.println("FetchResource.video > Exception: " + ex.getMessage());
            path = null;
        }

        return path;
    }

    public static String videoPath(Context context, String name) {
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
        String path;

        try {
            // Get MP3/wav path
            String packageName = context.getPackageName();
            int resourceId = context.getResources().getIdentifier(name, "raw", packageName);
            path = "android.resource://" + packageName + "/" + resourceId;

        } catch (Exception ex) {
            System.err.println("FetchResource.video > Exception: " + ex.getMessage());
            path = null;
        }

        return path;
    }

    public static int imageId(Context context, String value) {
        int imageId = 0;
        try {
            if (value.equals(Code.BLANK_IMAGE)) {
                return 0;
            } else {
                imageId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
                if (imageId == 0) {
                    throw new Exception("FetchResource.imageId: invalid resource name");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content), "image", value);
        }
        return imageId;
    }

    public static int imageId(Context context, JSONObject jsonObject, String name) {
        int imageId = 0;
        String value = "";
        try {
            value = jsonObject.getString(name);
            if (value.equals(Code.BLANK_IMAGE)) {
                return 0;
            } else {
                imageId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
                if (imageId == 0) {
                    throw new ResourceException("FetchResource.imageId: invalid resource name");
                }
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content),
                    "JSON from object", name).show();
        } catch (ResourceException rex) {
            rex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content),
                    "image", value).show();
        }
        return imageId;
    }

    public static int imageId(Context context, JSONArray jsonArray, int index, String name) {
        int imageId = 0;
        String value = "";
        try {
            value = jsonArray.getJSONObject(index).getString(name);
            if (value.equals(Code.BLANK_IMAGE)) {
                return 0;
            } else {
                imageId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
                if (imageId == 0) {
                    throw new ResourceException("FetchResource.imageId: invalid resource name");
                }
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content),
                    "JSON from array.object(" + index + ")", name).show();
        } catch (ArrayIndexOutOfBoundsException aex) {
            aex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content),
                    "array index", name + " (" + index + ")").show();
        } catch (ResourceException rex) {
            rex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content),
                    "image", value).show();
        }
        return imageId;
    }

    public static String imagePath(Context context, String name) {
        String path = null;
        try {
            String packageName = context.getPackageName();
            int resourceId = context.getResources().getIdentifier(name, "drawable", packageName);
            path = "android.resource://" + packageName + "/" + resourceId;

        } catch (Exception ex) {
            ex.printStackTrace();
            Globals.bugBar(((Activity) context).findViewById(android.R.id.content), "image", name).show();
        }
        return path;
    }

    public static String soundPath(Context context, String name) {
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

    public static Uri imageURI(Context context, String name) {
        Uri uri = null;
        FileInputStream fis = null;
        FileDescriptor fd;

        try {
            try {
                // Get PNG path
                String head = Environment.getExternalStorageDirectory() + LOCATION + context.getPackageName() + IMAGES;
                String path = head + name + PNG;

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

                uri = Uri.parse(path);

            } catch (Exception ex) {
                System.err.println("FetchResource.imageURI > Exception: " + ex.getMessage());
                uri = null;

            } finally {
                // Close fis
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException ioex) {
            System.err.println("FetchResource.imageURI > IOException: " + ioex.getMessage());
            uri = null;
        }

        return uri;
    }

    public static Drawable drawable(Context context, String name) {
        Drawable d;

        try {
            d = Drawable.createFromPath(image(context, name));

        } catch (Exception ex) {
            System.err.println("FetchResource.drawable > Exception: " + ex.getMessage());
            d = null;
        }

        return d;
    }

    public static ScaleDrawable scaleDrawable(Context context, String name, float scaleWidth, float scaleHeight) {
        ScaleDrawable sd;

        try {
            Drawable d = Drawable.createFromPath(image(context, name));
            d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * 0.5), (int) (d.getIntrinsicHeight() * 0.5));
            sd = new ScaleDrawable(d, Gravity.CENTER, scaleWidth, scaleHeight);

        } catch (Exception ex) {
            System.err.println("FetchResource.drawable > Exception: " + ex.getMessage());
            sd = null;
        }

        return sd;
    }

    private static class ResourceException extends Exception {
        public ResourceException() {
            super();
        }

        public ResourceException(String message) {
            super(message);
        }
    }
}
