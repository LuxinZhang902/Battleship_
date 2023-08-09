package edu.duke.ece651.teamX.server;
import battleship.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void test_GetMessage() {
      App a = new App();
      assertEquals("Hello from the server for teamX", a.getMessage());
    }
}

