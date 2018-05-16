package xyz.the1124.packagescanner.model;

import android.content.Context;
import xyz.the1124.packagescanner.ObjectIO;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class PackageDatabase {

    private TreeMap<String, Package> manuallyAddedPackages;
    private TreeMap<String, Package> syncedWithServer;
    private TreeMap<String, Package> packagesToSync;

    private Context context;

    public PackageDatabase(Context context)
    {
        this.context = context;
        importFromStorage();
    }

    public PackageDatabase(){
        importFromStorage();
    }

    public void addToManualAddTreeMap(Package packageAdd)
    {
        importFromStorage();
        manuallyAddedPackages.put(packageAdd.getTrackingNumber(), packageAdd);
    }

    public void addToSyncedWithServer(TreeMap<String, Package> packages)
    {
        importFromStorage();
        syncedWithServer.putAll(packages);
    }

    public void deliverPackages(ArrayList<Package> packages)
    {
        importFromStorage();
        for (int i = 0; i < packages.size(); i++) {
            if(syncedWithServer.containsKey(packages.get(i).getTrackingNumber()))
            {
                syncedWithServer.remove(packages.get(i).getTrackingNumber());
                packagesToSync.put(packages.get(i).getTrackingNumber(), packages.get(i));
            }
            else if(manuallyAddedPackages.containsKey(packages.get(i).getTrackingNumber()))
            {
                manuallyAddedPackages.remove(packages.get(i).getTrackingNumber());
                packagesToSync.put(packages.get(i).getTrackingNumber(), packages.get(i));
            }
        }
    }

    public void deleteFromPackagesToSyncDatabase(ArrayList<Package> packages)
    {
        importFromStorage();
        for (int i = 0; i < packages.size(); i++) {
            packagesToSync.remove(packages.get(i).getTrackingNumber());
        }
    }

    public void deleteFromManuallyAddedPackagesDatabase(ArrayList<Package> packages)
    {
        importFromStorage();
        for (int i = 0; i < packages.size(); i++) {
            if(manuallyAddedPackages.containsKey(packages.get(i).getTrackingNumber())) {
                manuallyAddedPackages.remove(packages.get(i).getTrackingNumber());
            }
        }
    }

    public void deleteMonthOldAddedPackages()
    {
        importFromStorage();
        for (int i = 0; i < manuallyAddedPackages.size(); i++) {
            if(manuallyAddedPackages.get(i).isMonthOld())
            {
                manuallyAddedPackages.remove(manuallyAddedPackages.get(i).getTrackingNumber());
            }
        }
    }

    public ArrayList<Package> getDeliveredManuallyAddedPackages()
    {
        importFromStorage();
        ArrayList<Package> deliveredPackages = new ArrayList<>();
        for (int i = 0; i < manuallyAddedPackages.size(); i++) {
            if(manuallyAddedPackages.get(i).isDelivered())
            {
                deliveredPackages.add(manuallyAddedPackages.get(i));
            }
        }
        return deliveredPackages;
    }

    public void importFromStorage()
    {
        File syncedPackagesFile = new File(context.getFilesDir(), "syncedPackages");
        ObjectIO syncedIO = new ObjectIO(syncedPackagesFile);
        syncedWithServer = (TreeMap<String, Package>) syncedIO.readObject();

        File manuallyAddedPackagesFile = new File(context.getFilesDir(), "manuallyAddedPackages");
        ObjectIO manuallyAddedIO = new ObjectIO(manuallyAddedPackagesFile);
        manuallyAddedPackages = (TreeMap<String, Package>) manuallyAddedIO.readObject();

        File packagesToSyncFile = new File(context.getFilesDir(), "packagesToSync");
        ObjectIO toSyncIO = new ObjectIO(packagesToSyncFile);
        packagesToSync = (TreeMap<String, Package>) toSyncIO.readObject();
    }

    public void updateStorage()
    {
        File syncedPackagesFile = new File(context.getFilesDir(), "syncedPackages");
        ObjectIO syncedIO = new ObjectIO(syncedPackagesFile);
        syncedIO.writeObject(syncedWithServer);

        File manuallyAddedPackagesFile = new File(context.getFilesDir(), "manuallyAddedPackages");
        ObjectIO manuallyAddedIO = new ObjectIO(manuallyAddedPackagesFile);
        manuallyAddedIO.writeObject(manuallyAddedPackages);

        File packagesToSyncFile = new File(context.getFilesDir(), "packagesToSync");
        ObjectIO toSyncIO = new ObjectIO(packagesToSyncFile);
        toSyncIO.writeObject(packagesToSync);
    }

    public TreeMap<String, Package> searchByReciepient(String reciepientName)
    {
        ArrayList<Package> packages = getAllPackages();
        TreeMap<String, Package> packagesByReciepient = new TreeMap<>();
        for (int i = 0; i < packages.size(); i++) {
            String reciepient = packages.get(i).getRecipient().getRecipientName();
            if(reciepient.toLowerCase().contains(reciepientName.toLowerCase()))
            {
                packagesByReciepient.put(reciepient, packages.get(i));
            }
        }
        return packagesByReciepient;
    }

    public TreeMap<String, Package> searchByReciever(String recieverName)
    {
        ArrayList<Package> packages = getAllPackages();
        TreeMap<String, Package> packagesByReciever = new TreeMap<>();
        for (int i = 0; i < packages.size(); i++) {
            String reciever = packages.get(i).getPackageReciever().getReceiverName();
            if(reciever.toLowerCase().contains(recieverName.toLowerCase()))
            {
                packagesByReciever.put(reciever, packages.get(i));
            }
        }
        return packagesByReciever;
    }

    public TreeMap<String, Package> searchByTrackingNumber(String trackingNumber) {
        ArrayList<Package> packages = getAllPackages();
        TreeMap<String, Package> packagesByTrackingNumber = new TreeMap<>();
        for (int i = 0; i < packages.size(); i++) {
            String tracking = packages.get(i).getTrackingNumber();
            if(tracking.toUpperCase().contains(trackingNumber))
            {
                packagesByTrackingNumber.put(tracking, packages.get(i));
            }
        }

        return packagesByTrackingNumber;
    }

    public ArrayList<Package> getAllPackages()
    {
        importFromStorage();
        ArrayList<Package> packages = new ArrayList<>(manuallyAddedPackages.values());
        packages.addAll(syncedWithServer.values());
        packages.addAll(packagesToSync.values());
        return packages;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public ArrayList<Package> getManuallyAddedPackagesList() {
        importFromStorage();
        return new ArrayList<>(manuallyAddedPackages.values());
    }

    public ArrayList<Package> getSyncedWithServerList() {
        importFromStorage();
        return new ArrayList<>(syncedWithServer.values());
    }

    public ArrayList<Package> getPackagesToSyncList() {
        importFromStorage();
        return new ArrayList<>(packagesToSync.values());
    }

    public void setPackagesToSync(TreeMap<String, Package> packagesToSync) {
        this.packagesToSync = packagesToSync;
    }
}
