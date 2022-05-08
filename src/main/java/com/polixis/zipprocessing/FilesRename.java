package com.polixis.zipprocessing;

import java.io.File;
import java.io.IOException;

public class FilesRename {

    public static int filesUnzipCount;

    public static void rename(String target) throws IOException {

        File targetFilePath = new File(target);
        File[] targetFilePath_array = targetFilePath.listFiles();

        filesUnzipCount = targetFilePath_array.length;
        int i = 0;
        for (File file:targetFilePath_array) {
            if (!file.getName().equals("schema-all.sql") &
                    !file.getName().equals("data.zip")) {
                File newFileName = new File( target +"\\"+ file.getName());
                newFileName.renameTo(new File(target + "\\" + i + ".csv"));
                System.out.println(targetFilePath_array[i].getName());
                i+=1;
            }
        }
    }

    public static int getFilesUnzipCount() {

        return filesUnzipCount;
    }
}
