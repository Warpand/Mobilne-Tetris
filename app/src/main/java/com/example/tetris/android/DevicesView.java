package com.example.tetris.android;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tetris.R;
import com.example.tetris.bluetooth.BluetoothConnect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DevicesView extends RecyclerView.Adapter<DevicesView.DeviceRep> {
    static class DeviceRep extends RecyclerView.ViewHolder {
        private final TextView deviceNameView;
        private final TextView deviceMacView;
        private final Button joinButton;

        DeviceRep(View itemView) {
            super(itemView);
            deviceNameView = itemView.findViewById(R.id.bluetooth_device_name);
            deviceMacView = itemView.findViewById(R.id.bluetooth_device_mac);
            joinButton = itemView.findViewById(R.id.bluetooth_device_join);
        }
    }

    private final ArrayList<BluetoothDevice> content = new ArrayList<>();
    private final HashSet<BluetoothDevice> devices = new HashSet<>();

    @NonNull
    @Override
    public DeviceRep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceRep(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.device_rep, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRep holder, int position) {
        BluetoothDevice device = content.get(position);
        try {
            holder.deviceNameView.setText(device.getName());
            holder.deviceMacView.setText(device.getAddress());
        }
        catch (SecurityException e) {
            Log.e("BLUETOOTH", "SECURITY PERMISSIONS WERE DENIED", e);
        }
        holder.joinButton.setOnClickListener(view -> {
            BluetoothConnect connector = new BluetoothConnect(
                    device,
                    view.getContext(),
                    new Intent(view.getContext(), MultiplayerJoinActivity.class)
            );
            connector.start();
        });
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public void addNewDevice(BluetoothDevice device) {
        if(!devices.contains(device)) {
            devices.add(device);
            content.add(device);
            super.notifyItemInserted(content.size() - 1);
        }
    }

    public void addMultipleDevices(Collection<BluetoothDevice> multipleDevices) {
        int itemCount = 0;
        int rangeStart = content.size();
        for(BluetoothDevice d : multipleDevices) {
            if(!devices.contains(d)) {
                itemCount++;
                devices.add(d);
                content.add(d);
            }
        }
        if(itemCount > 0)
            super.notifyItemRangeInserted(rangeStart, itemCount);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        devices.clear();
        content.clear();
        super.notifyDataSetChanged();
    }
}
