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
        int ROTATION_RELATIVE = 0;
        int ROTATION_ABSOLUTE = 1;
    }

    public interface generatorType {
        int RANDOM = 0;
        int SEQUENCE_RANDOM = 1;
    }

    public interface soundEffects {
        int ON = 0;
        int OFF = 1;
    }

    public interface speedType {
        int CONSTANT = 0;
        int ADJUSTING = 1;
        int CONSTANT_HARD = 2;
    }

    private static void fillWithDefault(FileOutputStream oS) throws IOException {
        oS.write(tiltDetectorType.ROTATION_RELATIVE);
        oS.write(generatorType.RANDOM);
        oS.write(soundEffects.ON);
        oS.write(speedType.CONSTANT);
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
    protected int generator;
    protected int sound;
    protected int speed;

    public Settings(Context context) {
        try (FileInputStream iS = createIfNotExist(context)) {
            tiltDetector = iS.read();
            generator = iS.read();
            sound = iS.read();
            speed = iS.read();

        } catch (Exception e) {
            Log.d("SETTINGS", "Unknown error while loading settings");
        }
    }

    public int getTiltDetectorType() {
        return tiltDetector;
    }

    public int getGeneratorType() {
        return generator;
    }

    public int getSoundEffects() {
        return sound;
    }

    public int getSpeed() {
        return speed;
    }

    public static class SettingsWriter extends Settings {
        public SettingsWriter(Context context) {
            super(context);
        }

        public void setTiltDetectorType(int type) {
            tiltDetector = type;
        }

        public void setGeneratorType(int type) {
            generator = type;
        }

        public void setSoundEffects(int on_off) {
            sound = on_off;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public void save(Context context) {
            try(OutputStream oS = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                oS.write(tiltDetector);
                oS.write(generator);
                oS.write(sound);
                oS.write(speed);
            }
            catch (IOException e) {
                Log.d("SETTINGS", "Unknown error while writing settings");
            }
        }
    }
}
