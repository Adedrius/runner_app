package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the TempoRun class to verify target pace comparisons,
 * run summary formatting, heart rate zone feedback, and pace validation.
 */
class TempoRunTest {
	
	
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    
    /**
     * Creates a TempoRun object with the specified pace and heart rate.
     *
     * @param actualSecondsPerKm the actual pace in seconds per kilometer
     * @param target the target pace in seconds per kilometer
     * @param hr the average heart rate, or null if not provided
     * @return a new TempoRun object
     */
    private TempoRun run(int actualSecondsPerKm, int target, Integer hr) {
        return new TempoRun(1, 5, Duration.ofSeconds(actualSecondsPerKm * 5L), hr, DATE, target);
    }
    
    /**
     * Verifies that the run summary correctly identifies whether
     * the actual pace is faster, slower, or equal to the target pace.
     */
    @Test 
    void 
    comparesFasterSlowerAndEqualTargetPaces() {
    	
        assertTrue(run(290, 300, null).getRunSummary().contains("faster than your target pace"));
        assertTrue(run(310, 300, null).getRunSummary().contains("slower than your target pace"));
        assertTrue(run(300, 300, null).getRunSummary().contains("met your target pace"));
        
    }
    
    /**
     * Verifies that the run summary includes correctly formatted
     * duration and pace values, along with the correct run type.
     */
    @Test 
    void
    summaryIncludesFormattedDurationAndPace() {
    	
        String summary = run(300, 300, null).getRunSummary();
        assertTrue(summary.contains("00:25:00"));
        assertTrue(summary.contains("00:05:00"));
        assertFalse(summary.contains("Average HR:"));
        assertEquals("Tempo Run", run(300, 300, null).getRunType());
        
    }
    
    /**
     * Verifies that different heart rate values appear in the run
     * summary and produce non-empty heart rate zone feedback.
     */
    @Test 
    void 
    summaryIncludesEveryHeartRateZone() {
    	
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 180, 181, 230}) {
            String summary = run(300, 300, hr).getRunSummary();
            assertTrue(summary.contains("Average HR: " + hr + " BPM"));
            assertFalse(run(300, 300, hr).zoneSummary().isEmpty());
        }
        
    }
    
    
    /**
     * Verifies that the difference between actual and target paces
     * is correctly formatted in the run summary.
     */
    @Test 
    void 
    paceDifferenceFormatsCorrectly() {
        assertTrue(run(290, 300, null).getRunSummary().contains("00:00:10"));
        assertTrue(run(310, 300, null).getRunSummary().contains("00:00:10"));
    }
    
    
    /**
     * Verifies that zero and negative target paces
     * throw an IllegalArgumentException.
     */
    @Test 
    void 
    rejectsInvalidTargetPace() {
        assertThrows(IllegalArgumentException.class, () -> run(300, 0, null));
        assertThrows(IllegalArgumentException.class, () -> run(300, -1, null));
    }
    
    /**
     * Verifies that the target tempo pace is correctly
     * returned in seconds per kilometer.
     */
    @Test 
    void 
    exposesTargetPace() { 
    	assertEquals(300, run(300, 300, null).getTempoPaceInSeconds()); 
    }
}
