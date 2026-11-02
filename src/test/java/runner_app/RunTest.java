package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the Run class to verify run attributes, pace calculations,
 * input validation, and heart rate boundaries.
 */
class RunTest {
	
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    
    /**
     * Creates an EasyRun object to test the shared Run functionality.
     *
     * @param id the unique run identifier
     * @param km the distance in kilometers
     * @param duration the total run duration
     * @param hr the average heart rate, or null if not provided
     * @param date the date of the run
     * @return a new EasyRun object
     */
    private EasyRun run(int id, double km, Duration duration, Integer hr, LocalDate date) {
        return new EasyRun(id, km, duration, hr, date, 3);
    }
    
    
    /**
     * Verifies that run attributes are retrieved correctly
     * and pace and duration calculations are properly formatted.
     */
    @Test 
    void gettersAndPaceAreCorrect() {
    	
        EasyRun r = run(2, 5, Duration.ofMinutes(25), 140, DATE);
        
        assertEquals(2, r.getRunId()); assertEquals(5, r.getDistance());
        
        assertEquals(Duration.ofMinutes(25), r.getDuration()); assertEquals(140, r.getAverageHeartRate());
        assertEquals(DATE, r.getDate());
        assertEquals(300, r.calculatePaceInSeconds());
        assertEquals("00:05:00", r.getFormattedPace());
        assertEquals("00:25:00", r.getFormattedDuration());
        
    }
    
    
    /**
     * Verifies that zero and negative run IDs
     * throw an IllegalArgumentException.
     */
    @Test 
    void rejectsInvalidId() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(0, 5, Duration.ofMinutes(25), null, DATE));
        assertThrows(IllegalArgumentException.class, () -> run(-1, 5, Duration.ofMinutes(25), null, DATE));
        
    }
    
    
    /**
     * Verifies that zero, negative, and non-finite distances
     * are rejected.
     */
    @Test 
    void rejectsInvalidDistance() {
    	
        for (double distance : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(1, distance, Duration.ofMinutes(25), null, DATE));
        
    }
    
    
    /**
     * Verifies that null, zero, and negative durations
     * throw an IllegalArgumentException.
     */
    @Test 
    void rejectsInvalidDuration() {
    	
        for (Duration duration : new Duration[]{null, Duration.ZERO, Duration.ofSeconds(-1)})
            assertThrows(IllegalArgumentException.class, () -> run(1, 5, duration, null, DATE));
        
    }
    
    
    /**
     * Verifies that heart rate is optional, accepts values
     * between 80 and 230 BPM, and rejects values outside this range.
     */
    @Test 
    void validatesHeartRateBoundsAndOptionalValue() {
    	
        assertNull(run(1, 5, Duration.ofMinutes(25), null, DATE).getAverageHeartRate());
        assertEquals(80, run(1, 5, Duration.ofMinutes(25), 80, DATE).getAverageHeartRate());
        assertEquals(230, run(1, 5, Duration.ofMinutes(25), 230, DATE).getAverageHeartRate());
        
        for (int hr : new int[]{79, 231})
            assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), hr, DATE));
        
    }
    
    
    /**
     * Verifies that null and future run dates
     * throw an IllegalArgumentException.
     */
    @Test 
    void rejectsNullOrFutureDate() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), null, null));
        assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), null, LocalDate.now().plusDays(1)));
        
    }
}
