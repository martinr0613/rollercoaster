package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import rollercoaster.controller.BuildingService;
import rollercoaster.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingServiceTest {

    @Test
    void payService() {
        Restaurant r = new Restaurant(null, 3, 1, 0, 0);
        Guest g = new Guest(12, 0, 0, r);

        BuildingService.payService(r, g);

        assertEquals(9, g.getMoney());
        assertEquals(r.getLocalMoney(), 3);
    }
}