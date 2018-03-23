package com.semantria.example;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Useful utility methods used in examples
 */
public class Utils {

    private static Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Reads contents of {@code filename} and returns a list of lines.
     */
    public static List<String> readTextFile(String filename) {

        File file = new File(filename);
        if (!file.exists()) {
            log.error("Can't find data file: {}", filename);
            return Collections.emptyList();
        }

        List<String> data = new ArrayList<String>();
        try {
            FileInputStream stream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(stream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                if (Strings.isNullOrEmpty(strLine)) {
                    continue;
                }
                data.add(strLine);
            }
            in.close();
        } catch (Exception e) {
            log.error("Error reading data: {}", e.getMessage());
            return null;
        }

        return data;
    }

    public static void sleep(float seconds) {
        try {
            Thread.sleep((int) (seconds * 1000));
        } catch (InterruptedException e) {
            // Ignore -- expected exception
        }
    }



}
