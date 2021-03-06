package org.jala.efeeder.api.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.opencsv.CSVReader;

public class ReadFileUtil {

	public static Map<String, List<String>> readCSVFile(FileItem fileItem) {

		Map<String, List<String>> parameters = new HashMap<>();

		try (CSVReader reader = new CSVReader(new InputStreamReader(fileItem.getInputStream(), "UTF-8"));) {

			String[] nextLine;
			int count=0;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine[0].startsWith("#") || nextLine[0].trim().length() == 0) {
					continue;
				}
				parameters.put(String.valueOf(count), Arrays.asList(nextLine));
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parameters;
	}
}
