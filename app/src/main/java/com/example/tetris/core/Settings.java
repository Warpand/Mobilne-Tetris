package com.example.tetris.core;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Settings {
    protected static final String filename = "settings";

    public interface tiltDetectorType {
        // this things should be one byte long;
        int ROTATION_VECTOR = 0;
        int GYROSCOPE = 1;
    }

    private static void fillWithDefault(FileOutputStream oS) throws IOException {
        oS.write(tiltDetectorType.ROTATION_VECTOR);
    }
    private static FileInputStream createIfNotExist(Context context) throws IOException {
         try {
             FileInputStream iS = context.openFileInput(filename);
             Log.d("SETTINGS", "settings loaded");
             return iS;
         } catch (FileNotFoundException e) {
             Log.d("SETTINGS", "Creating settings file");
             FileOutputStream oS = context.openFileOutput(filename, Context.MODE_PRIVATE);
             fillWithDefault(oS);
             oS.close();
             return context.openFileInput(filename);
         }
    }

    protected int tiltDetector;

    public Settings(Context context) {
        try (FileInputStream iS = createIfNotExist(context)) {
            tiltDetector = iS.read();
        } catch (Exception e) {
            Log.d("SETTINGS", "Unknown error while loading settings");
        }
    }

    public int getTiltDetectorType() {
        return tiltDetector;
    }

    public static class SettingsWriter extends Settings {
        public SettingsWriter(Context context) {
            super(context);
        }

        public void setTiltDetectorType(int type) {
            tiltDetector = type;
        }

        public void save(Context context) {
            try(OutputStream oS = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                oS.write(tiltDetector);
            }
            catch (IOException e) {
                Log.d("SETTINGS", "Unknown error while writing settings");
            }
        }
    }
}
