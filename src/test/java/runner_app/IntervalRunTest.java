package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the IntervalRun class to verify distance and duration
 * calculations, input validation, and heart rate zone feedback.
 */
class IntervalRunTest {
	
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1); 
    
    /**
     * Creates an IntervalRun object with the specified workout details.
     *
     * @param count the number of intervals
     * @param work the duration of each work interval
     * @param rest the duration of each recovery interval
     * @param workKm the distance of each work interval
     * @param restKm the distance of each recovery interval
     * @param hr the average heart rate, or null if not provided
     * @return a new IntervalRun object
     */
    private IntervalRun run(int count, Duration work, Duration rest, double workKm, double restKm, Integer hr) {
        return new IntervalRun(1, hr, DATE, count, work, rest, workKm, restKm);
    }
    
    /**
     * Verifies that total distance and duration are calculated correctly
     * and that interval workout details appear in the run summary.
     */
    @Test 
    void calculatesTotalDistanceAndDuration() {
    	
        IntervalRun r = run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), 0.8, 0.2, null);
        
        assertEquals(5.0, r.getDistance(), 0.000001);
        assertEquals(Duration.ofMinutes(25), r.getDuration());
        assertEquals(5, r.getNumIntervals());
        assertEquals(Duration.ofMinutes(3), r.getIntervalDuration());
        assertEquals(Duration.ofMinutes(2), r.getRecoveryDuration());
        assertEquals(0.8, r.getIntervalDistance()); assertEquals(0.2, r.getRecoveryDistance());
        assertEquals("Interval Run", r.getRunType());
        assertTrue(r.getRunSummary().contains("00:03:00"));
        assertTrue(r.getRunSummary().contains("00:02:00"));
        
    }
    
    /**
     * Verifies that invalid interval counts and distances are rejected,
     * while a recovery distance of zero is accepted.
     */
    @Test 
    void rejectsInvalidCountsAndDistances() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(0, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, .2, null));
        
        for (double distance : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), distance, .2, null));
        
        for (double distance : new double[]{-1, Double.NaN, Double.POSITIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, distance, null));
        
        assertEquals(4.0, run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, 0, null).getDistance(), 0.000001);
        
    }
    
    /**
     * Verifies that zero and negative work or recovery durations
     * throw an IllegalArgumentException.
     */
    @Test 
    void rejectsZeroAndNegativeDurations() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ZERO, Duration.ofMinutes(2), .8, .2, null));
        assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ofMinutes(3), Duration.ZERO, .8, .2, null));
        assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ofSeconds(-1), Duration.ofMinutes(2), .8, .2, null));
        assertThrows(IllegalArgumentException.class, () -> run(5, Duration.ofMinutes(3), Duration.ofSeconds(-1), .8, .2, null));
        
    }
    
    /**
     * Verifies that heart rate information is optional and that valid
     * heart rate values produce appropriate summary information.
     */
    @Test 
    void heartRateZonesAndOptionalSummary() {
    	
        assertFalse(run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, .2, null).getRunSummary().contains("Average HR:"));
        
        
        for (int hr : new int[]{80, 130, 131, 151, 152, 165, 166, 180, 181, 230}) {
        	
            IntervalRun r = run(5, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, .2, hr);
            assertTrue(r.getRunSummary().contains("Average HR: " + hr + " BPM"));
            assertFalse(r.zoneSummary().isEmpty());
            
        }
    }
}
