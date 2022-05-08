
import com.polixis.zipprocessing.FilesRename;
import com.polixis.zipprocessing.Unzipper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FilesRenameTest {
    @Test
    public void renameCheckTest() throws IOException {
        Path source = Paths.get("src/test/resources/data.zip");
        Path target = Paths.get("src/test/resources/");

        Unzipper.unzipFolderZip4j(source, target);
//        System.out.println("Files were unzipped");

        FilesRename.rename(target.toString());

        List<String> fileNamesExpected = new LinkedList<>();
        for (int i = 0; i < FilesRename.getFilesUnzipCount()-2; i++) {
            fileNamesExpected.add(i+".csv");
        }

        List<String> fileNamesActual = new LinkedList<>();


        File myfolder = new File(target.toString());
        File[] file_array = myfolder.listFiles();
        for (int i = 0; i < file_array.length; i++) {
            if (file_array[i].isFile() && !file_array[i].getName().equals("schema-all.sql") &&
                    !file_array[i].getName().equals("data.zip")) {
                fileNamesActual.add(file_array[i].getName());
            }
        }

        Assertions.assertEquals(fileNamesExpected.size(), fileNamesActual.size());

        for (String fileName : fileNamesExpected) {
            Assertions.assertTrue(fileNamesActual.contains(fileName));
        }
    }
}