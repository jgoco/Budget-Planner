package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// NOTICE:
// Structure of this persistence function is made with the guidance of, and therefore based around,
// the "Teller" application from CPSC 210, which can be found and downloaded at:
// https://github.students.cs.ubc.ca/CPSC210/TellerApp

// Represents a writer that can write data to file
public class Writer {
    private PrintWriter printWriter;

    // EFFECTS: constructs a writer that writes data to file
    public Writer(File file) throws FileNotFoundException, UnsupportedEncodingException {
        printWriter = new PrintWriter(file, "UTF-8");
    }

    // MODIFIES: this
    // EFFECTS:  writes data to file
    public void write(SaveableData data) {
        data.save(printWriter);
    }

    // MODIFIES: this
    // EFFECTS:  closes print writer
    //           NOTE: MUST call this method after writing data
    public void close() {
        printWriter.close();
    }

}
