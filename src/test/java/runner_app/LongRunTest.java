package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

class LongRunTest {
	
    private LongRun run(double actual, double target, Integer hr) {
        return new LongRun(1, actual, Duration.ofMinutes(90), hr, LocalDate.of(2026, 1, 1), target);
    }
    
    
    @Test 
    void goalComparisons() {
    	
        assertTrue(run(10, 15, null).getRunSummary().contains("left to run"));
        assertTrue(run(20, 15, null).getRunSummary().contains("exceeded your goal"));
        assertTrue(run(15, 15, null).getRunSummary().contains("achieved your goal"));
        assertEquals(15, run(15, 15, null).getTargetDistance());
        
    }
    
    
    @Test 
    void invalidTargetDistances() {
    	
        for (double target : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(10, target, null));
        
    }
    
    
    @Test 
    void heartRateZonesAndOptionalSummary() {
    	
        assertFalse(run(15, 15, null).getRunSummary().contains("Average HR:"));
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 180, 181, 230}) {
            assertTrue(run(15, 15, hr).getRunSummary().contains("Average HR: " + hr + " BPM"));
            assertFalse(run(15, 15, hr).zoneSummary().isEmpty());
        }
        
    }
}
