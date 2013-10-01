package com.github.rosjava.android.openni_test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.openni.DeviceInfo;
import org.openni.OpenNI;

import java.util.List;

public class OpenniTest extends Activity
{
    private BroadcastReceiver usbDevicePermissionReceiver;
    public static final String USB_PERMISSION = "org.ros.android.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(USB_PERMISSION), 0);

        usbDevicePermissionReceiver =
                new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if(USB_PERMISSION.equals(intent.getAction())) {
                            initializeOpenNI();
                        }
                    }
                };

        registerReceiver(usbDevicePermissionReceiver, new IntentFilter(USB_PERMISSION));
        onUsbDeviceAttached(getIntent());
    }

    private void initializeOpenNI() {
        try {
            OpenNI.initialize();
            OpenNI.addDeviceConnectedListener(new OpenNI.DeviceConnectedListener() {
                public void onDeviceConnected(final DeviceInfo arg0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "got a " + arg0.getVendor() + "connected", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Toast.makeText(getApplicationContext(), "OpenNI initialized", Toast.LENGTH_SHORT).show();
        } catch(Throwable t) {
            Log.e("opennitest", "Exception initializing OpenNI: " + t);
            Toast.makeText(getApplicationContext(), "OpenNI initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onUsbDeviceAttached(intent);
    }

    private void onUsbDeviceAttached(Intent intent) {
        Toast.makeText(getApplicationContext(), "New Intent", Toast.LENGTH_SHORT).show();
        if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            Toast.makeText(getApplicationContext(), "It's USB", Toast.LENGTH_SHORT).show();
            UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Log.d("opennitest", "Accessing device: " + usbDevice.getDeviceName());
            initializeOpenNI();
        } else {
            Toast.makeText(getApplicationContext(), "It's " + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        List<DeviceInfo> devices = OpenNI.enumerateDevices();
        for(DeviceInfo device: devices) {
            Toast.makeText(getApplicationContext(), "device:" + device, Toast.LENGTH_SHORT).show();
        }
        if(devices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "no devices", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
