package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the LongRun class to verify distance goal comparisons,
 * target distance validation, and heart rate zone feedback.
 */
class LongRunTest {
	
	/**
	 * Creates a LongRun object with the specified running details.
	 *
	 * @param actual the actual distance completed
	 * @param target the target distance
	 * @param hr the average heart rate, or null if not provided
	 * @return a new LongRun object
	 */
    private LongRun run(double actual, double target, Integer hr) {
        return new LongRun(1, actual, Duration.ofMinutes(90), hr, LocalDate.of(2026, 1, 1), target);
    }
    
    /**
     * Verifies that the run summary correctly identifies whether
     * the runner fell short of, exceeded, or achieved their distance goal.
     */
    @Test 
    void goalComparisons() {
    	
        assertTrue(run(10, 15, null).getRunSummary().contains("left to run"));
        assertTrue(run(20, 15, null).getRunSummary().contains("exceeded your goal"));
        assertTrue(run(15, 15, null).getRunSummary().contains("achieved your goal"));
        assertEquals(15, run(15, 15, null).getTargetDistance());
        
    }
    
    /**
     * Verifies that invalid target distances are rejected
     * and throw an IllegalArgumentException.
     */
    @Test 
    void invalidTargetDistances() {
    	
        for (double target : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(10, target, null));
        
    }
    
    /**
     * Verifies that heart rate information is optional and that
     * valid heart rate values produce non-empty zone feedback.
     */
    @Test 
    void heartRateZonesAndOptionalSummary() {
    	
        assertFalse(run(15, 15, null).getRunSummary().contains("Average HR:"));
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 180, 181, 230}) {
            assertTrue(run(15, 15, hr).getRunSummary().contains("Average HR: " + hr + " BPM"));
            assertFalse(run(15, 15, hr).zoneSummary().isEmpty());
        }
        
    }
}
