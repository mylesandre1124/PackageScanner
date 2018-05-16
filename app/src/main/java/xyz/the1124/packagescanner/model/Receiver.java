package xyz.the1124.packagescanner.model;

import android.media.Image;

public class Receiver {

    private String receiverName;
    private Image receiverSignature;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Image getReceiverSignature() {
        return receiverSignature;
    }

    public void setReceiverSignature(Image receiverSignature) {
        this.receiverSignature = receiverSignature;
    }

    public boolean isSigned()
    {
        return receiverSignature != null;
    }


}
