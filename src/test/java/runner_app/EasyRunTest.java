package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

class EasyRunTest {
	
    private EasyRun run(Integer hr, int effort) {
        return new EasyRun(1, 5, Duration.ofMinutes(30), hr, LocalDate.of(2026, 1, 1), effort);
    }
    
    @Test 
    void effortAndSummary() {
    	
        assertEquals(3, run(null, 3).getEffort());
        assertEquals("Easy Run", run(null, 3).getRunType());
        assertTrue(run(null, 3).getRunSummary().contains("PERCEIVED EFFORT: 3/10"));
        assertFalse(run(null, 3).getRunSummary().contains("Average HR:"));
        
    }
    
    @Test 
    void effortBounds() {
    	
        assertEquals(1, run(null, 1).getEffort()); assertEquals(10, run(null, 10).getEffort());
        assertThrows(IllegalArgumentException.class, () -> run(null, 0));
        assertThrows(IllegalArgumentException.class, () -> run(null, 11));
        
    }
    
    @Test 
    void everyHeartRateZoneAppearsInSummary() {
    	
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 230}) {
            assertTrue(run(hr, 3).getRunSummary().contains("Average HR: " + hr + " BPM"));
            assertFalse(run(hr, 3).zoneSummary().isEmpty());
        }
        
    }
}
