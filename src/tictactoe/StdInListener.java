package tictactoe;

import java.util.Scanner;

/**
 * Created by gerben on 3-12-15.
 */
public class StdInListener extends Thread{
    String line;
    Scanner scanner;

    public StdInListener() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        while(true) {
            if (scanner.hasNextLine()){
                line = scanner.nextLine();
            }
        }
    }

    public String popLine() {
        String out = line;
        line = null;
        return out;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String getNextLine() {
        while (line == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        return popLine();
    }
}