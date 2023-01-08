package persistence;

import java.io.PrintWriter;

// NOTICE:
// Structure of this persistence function is made with the guidance of, and therefore based around,
// the "Teller" application from CPSC 210, which can be found and downloaded at:
// https://github.students.cs.ubc.ca/CPSC210/TellerApp

// Represents data that can be saved to file
public interface SaveableData {

    // MODIFIES: printWriter
    // EFFECTS:  writes the data to printWriter
    void save(PrintWriter printwriter);

}
