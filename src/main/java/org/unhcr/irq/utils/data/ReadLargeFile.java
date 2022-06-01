/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unhcr.irq.utils.data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MATAYO
 */
public class ReadLargeFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        readFile("C:\\Users\\MATAYO\\OneDrive - UNHCR\\Documents\\dumps\\Dump20210514.sql", "C:\\Users\\MATAYO\\OneDrive - UNHCR\\Documents\\dumps\\Dump20210514_.sql", 35162, 48083);
    }

    private static void readFile(String filetxt, String outtxt, int startPoint, int endPoint) {
        FileWriter fw = null;

        try {
            Files.deleteIfExists(Paths.get(outtxt)); //surround it in try catch block
            fw = new FileWriter(outtxt);
            List<String> c = Files.lines(Paths.get(filetxt)).toList();
            Logger.getLogger(ReadLargeFile.class.getName()).log(Level.INFO, "File loaded successfully");
           // Stream<String> lines = Files.lines(Paths.get(filetxt));
            for (int j = startPoint; j <= endPoint; j++) {
                String line = c.get(j);
                Logger.getLogger(ReadLargeFile.class.getName()).log(Level.INFO, "Line {0}:{1}", new Object[]{j, line});
                fw.write(line);
                fw.write("\n");
            }

        } catch (IOException ex) {
            Logger.getLogger(ReadLargeFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ReadLargeFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
