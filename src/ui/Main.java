package ui;

import java.io.IOException;

/**
 * Please run the main method in order to initiate the program. Remember this program only
 * supports .csv files, and only calls in for String, bool, date, int and double data types
 * (case sensitive).
 */
public class Main {
    public static void main(String[] args) {
        try {
            UI ui = new UI();
        } catch (IOException e) {
            e.fillInStackTrace();
        }

    }

}
