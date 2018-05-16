package xyz.the1124.packagescanner.model;

import java.util.Calendar;
import java.util.Date;

public class Package {

    private Recipient recipient;
    private String sender;
    private String trackingNumber;
    private String carrier;
    private Receiver packageReciever;
    private Date created;
    private boolean isDelivered;

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber.toUpperCase();
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Receiver getPackageReciever() {
        return packageReciever;
    }

    public void setPackageReciever(Receiver packageReciever) {
        this.packageReciever = packageReciever;
    }

    public boolean isDelivered() {
        if(!packageReciever.isSigned())
        {
            isDelivered = false;
        }
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public void setIsDelivered()
    {
        isDelivered = true;
    }

    public boolean isMonthOld()
    {
        Date monthAgo = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return created.before(monthAgo);
    }

    @Override
    public String toString() {
        return "Package{" +
                "recipient=" + recipient +
                ", sender='" + sender + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", carrier='" + carrier + '\'' +
                ", packageReciever=" + packageReciever +
                ", created=" + created +
                ", isDelivered=" + isDelivered +
                '}';
    }

    public static void main(String[] args) {
        Package package1 = new Package();
        package1.setTrackingNumber("76435hdrdhy89");
        System.out.println(package1.getTrackingNumber());
    }
}
