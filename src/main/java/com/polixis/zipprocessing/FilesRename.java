package com.polixis.zipprocessing;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FilesRename {


    public static int filesUnzipCount;

    public static void rename(String target) throws IOException {

        File targetFilePath = new File(target);
        File[] targetFilePath_array = targetFilePath.listFiles();

        filesUnzipCount = Objects.requireNonNull(targetFilePath_array).length;
        int i = 0;
        for (File file:targetFilePath_array) {
            if (!file.getName().equals("schema-all.sql") &
                    !file.getName().equals("data.zip")) {
                File newFileName = new File( target +"\\"+ file.getName());
                if(newFileName.exists()) {
                    if(!newFileName.renameTo(new File(target + "\\" + i + ".csv"))) {
                        throw new IOException("Failed to rename "+file+" to "+newFileName);
                    }
                }
                i+=1;
            }
        }
    }

    public static int getFilesUnzipCount() {
        return filesUnzipCount;
    }

    public static void setFilesUnzipCount(int filesUnzipCount) {
        FilesRename.filesUnzipCount = filesUnzipCount;
    }
}
