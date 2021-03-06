package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public abstract class OldSolidDirectoryList extends SolidIndexList {


    private final ArrayList<String> list = new ArrayList<>(10);




    public OldSolidDirectoryList(Context c, String k) {
        super(Storage.preset(c), k);
        initList(list);
    }

    public abstract void initList(ArrayList<String> list);
    
    @Override
    public int length() {
        return list.size();
    }

    @Override
    public String getValueAsString(int i) {
        return list.get(i);
    }
    
    
    @Override
    public String toString() {
        return getValueAsString();
    }

    public File toFile() {
        return new File(getValueAsString());
    }
    


    public static void fillDirectoryList(ArrayList<String> list, String[] pf) {
        for (String aPf : pf) fillList(list, aPf);
    }


    public static void  fillList(ArrayList<String> list, String pf) {
        addFileToList(list, Environment.getExternalStorageDirectory(), pf);
        addFileToList(list, Environment.getDataDirectory(), pf);
        addPathToList(list, "/mnt", pf);
    }


    public static void addPathToList(ArrayList<String> l, String p, String pf) {
        File files[] = new File(p).listFiles();
        if (files != null) {
            for (File file : files) addFileToList(l, file, pf);
        }
    }


    public static void addFileToList(ArrayList<String> l, File f, String pf) {
        if (f.exists()) l.add(f.getAbsolutePath() + "/" + pf);
    }
    
    public static void addFileToList(ArrayList<String> l, File f) {
        if (f.exists()) l.add(f.getAbsolutePath());
    }
}
