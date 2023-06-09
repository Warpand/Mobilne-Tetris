package com.example.tetris.android;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tetris.bluetooth.BluetoothError;

import java.util.ArrayList;

public abstract class AbstractBluetoothActivity extends AppCompatActivity {
    protected BluetoothAdapter bluetoothAdapter;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean areGranted = true;
                for(boolean isGranted : permissions.values())
                    areGranted &= isGranted;
                if(!areGranted)
                    toastForError(BluetoothError.PERMISSION_DENIED);
            }
    );

    private final ActivityResultLauncher<Intent> requestBluetoothLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if(result.getResultCode() != RESULT_OK)
                toastForError(BluetoothError.BLUETOOTH_OFF);
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothAdapter = getSystemService(BluetoothManager.class).getAdapter();
    }

    protected void checkPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_SCAN);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_ADVERTISE);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if(!permissionsList.isEmpty()) {
            String[] requestedPermissions = new String[permissionsList.size()];
            for(int i = 0; i < permissionsList.size(); i++)
                requestedPermissions[i] = permissionsList.get(i);
            requestPermissionLauncher.launch(requestedPermissions);
        }
    }

    protected void turnBluetoothOn() {
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestBluetoothLauncher.launch(enableBtIntent);
        }
    }

    protected void turnEverythingOn() {
        checkPermissions();
        turnBluetoothOn();
    }

    protected void toastForError(BluetoothError error) {
        String text = "ERROR";
        switch (error) {
            case BLUETOOTH_OFF:
                text = "Turn the Bluetooth on in order to use this function.";
                break;
            case PERMISSION_DENIED:
                text = "Necessary permissions denied.";
                break;
            case USER_DENIED_OPERATION:
                text = "Accept the prompted operation in order to use this function.";
                break;
        }
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
