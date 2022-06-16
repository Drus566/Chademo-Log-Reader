package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    private static final int INIT_CAPCITY = 100;

    public ArrayList<String> readedLog = new ArrayList<>(INIT_CAPCITY);

    public void readLog() {
        try (FileReader fr = new FileReader("chademo.log"); BufferedReader rd = new BufferedReader(fr)) {
            String line;
            while ((line = rd.readLine()) != null) {
                readedLog.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printReadedLog() {
        for (String l : readedLog) {
            System.out.println(l);
        }
    }
}
