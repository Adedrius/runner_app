package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

class TempoRunTest {
	
	
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    
    
    private TempoRun run(int actualSecondsPerKm, int target, Integer hr) {
        return new TempoRun(1, 5, Duration.ofSeconds(actualSecondsPerKm * 5L), hr, DATE, target);
    }
    
    @Test 
    void comparesFasterSlowerAndEqualTargetPaces() {
    	
        assertTrue(run(290, 300, null).getRunSummary().contains("faster than your target pace"));
        assertTrue(run(310, 300, null).getRunSummary().contains("slower than your target pace"));
        assertTrue(run(300, 300, null).getRunSummary().contains("met your target pace"));
        
    }
    
    @Test 
    void summaryIncludesFormattedDurationAndPace() {
    	
        String summary = run(300, 300, null).getRunSummary();
        assertTrue(summary.contains("00:25:00"));
        assertTrue(summary.contains("00:05:00"));
        assertFalse(summary.contains("Average HR:"));
        assertEquals("Tempo Run", run(300, 300, null).getRunType());
        
    }
    
    @Test 
    void summaryIncludesEveryHeartRateZone() {
    	
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 180, 181, 230}) {
            String summary = run(300, 300, hr).getRunSummary();
            assertTrue(summary.contains("Average HR: " + hr + " BPM"));
            assertFalse(run(300, 300, hr).zoneSummary().isEmpty());
        }
        
    }
    
    
    @Test 
    void paceDifferenceFormatsCorrectly() {
        assertTrue(run(290, 300, null).getRunSummary().contains("00:00:10"));
        assertTrue(run(310, 300, null).getRunSummary().contains("00:00:10"));
    }
    
    @Test 
    void rejectsInvalidTargetPace() {
        assertThrows(IllegalArgumentException.class, () -> run(300, 0, null));
        assertThrows(IllegalArgumentException.class, () -> run(300, -1, null));
    }
    
    @Test 
    void exposesTargetPace() { assertEquals(300, run(300, 300, null).getTempoPaceInSeconds()); }
}
