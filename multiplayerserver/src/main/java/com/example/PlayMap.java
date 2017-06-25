package com.example;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ThreadLocalRandom;


public class PlayMap {
    File mapFile;

    public PlayMap(){
        File file = new File("maps");
        System.out.println(file.getAbsolutePath());
        File[] arr = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".tmx"))
                    return true;
                return false;
            }
        });

        for(File fileArr : arr){
            System.out.println(fileArr);
        }
        mapFile = arr[ThreadLocalRandom.current().nextInt(0, arr.length)];
    }
}
