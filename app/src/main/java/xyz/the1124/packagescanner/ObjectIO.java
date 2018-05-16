package xyz.the1124.packagescanner;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Created by Myles on 3/29/17.
 */
public class ObjectIO {

    private File objectFile;

    public ObjectIO(File objectFile) {
        this.objectFile = objectFile;
    }

    public ObjectIO() {
    }

    public File getObjectFile() {
        return objectFile;
    }

    public void setObjectFile(File objectFile) {
        this.objectFile = objectFile;
    }

    public void writeObject(Object object) {
        OutputStream file = null;
        try {

            file = new FileOutputStream(getObjectFile());
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(object);
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject() {
        //use buffering
        Object object = new Object();
        try {
            InputStream file = new FileInputStream(getObjectFile());
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(getObjectFile())));

            //deserialize the List
            try {
                object = input.readObject();
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    public File getApplicationDataFolder() {
        File documentsFolder = new File(System.getProperty("user.home") + File.separator + "Documents");
        File posDataFolder = null;
        documentsFolder.mkdirs();
        if (documentsFolder.isDirectory()) {
            posDataFolder = new File(documentsFolder.getAbsolutePath() + File.separator + "Email Data");
            if(!posDataFolder.isDirectory())
            {
                posDataFolder.mkdirs();
                installDocs("credentials.crd");
            }
        }
        return posDataFolder;

    }

    public void installDocs(String fileName) {
        try {
            InputStream input = this.getClass().getResourceAsStream("/internals/" + fileName);
            Files.copy(input, Paths.get(getApplicationDataFolder().toString() + File.separator + fileName));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
