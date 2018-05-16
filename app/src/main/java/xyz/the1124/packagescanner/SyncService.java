package xyz.the1124.packagescanner;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import xyz.the1124.packagescanner.Socket.Client;
import xyz.the1124.packagescanner.model.Package;
import xyz.the1124.packagescanner.model.PackageDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class SyncService extends Service {

    private String TAG = "Scanner";
    private PackageDatabase packageDatabase = new PackageDatabase();
    private Client syncClient;

    public void createClient(String ipAddress) throws IOException, ClassNotFoundException {
        syncClient = new Client(ipAddress, 1024);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                try {
                    String ipAddress = intent.getStringExtra("ipAddress");
                    boolean synced = sync(ipAddress);
                    if(!synced)
                    {
                        for (int i = 0; i < 5 || !synced; i++) {
                            synced = sync(ipAddress);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
        return Service.START_NOT_STICKY;
    }

    public boolean sync(String ipAddress) throws IOException, ClassNotFoundException {
        createClient(ipAddress);
        syncClient.connect();

        syncClient.send(packageDatabase.getManuallyAddedPackagesList());
        ArrayList<Package> returnedManuallyAddedPackages = (ArrayList<Package>) syncClient.receive();
        packageDatabase.deleteFromManuallyAddedPackagesDatabase(returnedManuallyAddedPackages);
        packageDatabase.deleteMonthOldAddedPackages();

        syncClient.send(packageDatabase.getSyncedWithServerList());
        ArrayList<Package> returnedSyncedWithServerPackages = (ArrayList<Package>) syncClient.receive();
        //packageDatabase.

        ArrayList<Package> packagesToSyncList = packageDatabase.getPackagesToSyncList();
        syncClient.send(packagesToSyncList);
        int ack = syncClient.receiveAcknowledgement();
        if (ack == packagesToSyncList.size())
        {
            syncClient.sendAcknowledgement(packagesToSyncList.size());
        }

        ArrayList<Package> packagesRecieved = (ArrayList<Package>) syncClient.receive();
        syncClient.sendAcknowledgement(packagesRecieved.size());
        int syncedAck = syncClient.receiveAcknowledgement();
        if(syncedAck == packagesRecieved.size()) {
            TreeMap<String, Package> packageTreeMap = new TreeMap<>();
            for (int i = 0; i < packagesRecieved.size(); i++) {
                packageTreeMap.put(packagesRecieved.get(i).getTrackingNumber(), packagesRecieved.get(i));
                packageDatabase.addToSyncedWithServer(packageTreeMap);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
