package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;

public class ModelTest {
    @AfterEach
    void resetDB() {
        try {
            System.out.println("Resetting DB After Test...");
            DB.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
