package com.polixis;

import com.polixis.batchprocessing.PersonItemProcessor;
import com.polixis.zipprocessing.FilesRename;
import com.polixis.zipprocessing.Unzipper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class BatchProcessingApplication {

	public static void main(String[] args) throws Exception {

		final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

		Path sourcePathDataZip = Paths.get("src/main/resources/data.zip");
		Path targetPathUnzip = Paths.get("src/main/resources/");

		try {
			Unzipper.unzipFolderZip4j(sourcePathDataZip, targetPathUnzip);
			log.info("Files were unzipped");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try{
			FilesRename.rename(targetPathUnzip.toString());
			log.info("Files were renamed");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));
	}
}
