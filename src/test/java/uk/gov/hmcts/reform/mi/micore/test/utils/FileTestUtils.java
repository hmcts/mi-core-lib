package uk.gov.hmcts.reform.mi.micore.test.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public final class FileTestUtils {

    public static String getDataFromFile(String filePath) throws IOException {
        return FileUtils.readFileToString(file(filePath), "UTF-8");

    }

    private static File file(String filePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new File(classLoader.getResource(filePath).getFile());
    }

    private FileTestUtils() {
        //Utility constructor
    }

}
