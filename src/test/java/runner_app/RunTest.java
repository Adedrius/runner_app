package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import org.junit.jupiter.api.Test;

class RunTest {
	
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    
    private EasyRun run(int id, double km, Duration duration, Integer hr, LocalDate date) {
        return new EasyRun(id, km, duration, hr, date, 3);
    }
    
    
    @Test void gettersAndPaceAreCorrect() {
    	
        EasyRun r = run(2, 5, Duration.ofMinutes(25), 140, DATE);
        
        assertEquals(2, r.getRunId()); assertEquals(5, r.getDistance());
        
        assertEquals(Duration.ofMinutes(25), r.getDuration()); assertEquals(140, r.getAverageHeartRate());
        assertEquals(DATE, r.getDate());
        assertEquals(300, r.calculatePaceInSeconds());
        assertEquals("00:05:00", r.getFormattedPace());
        assertEquals("00:25:00", r.getFormattedDuration());
        
    }
    
    @Test 
    void rejectsInvalidId() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(0, 5, Duration.ofMinutes(25), null, DATE));
        assertThrows(IllegalArgumentException.class, () -> run(-1, 5, Duration.ofMinutes(25), null, DATE));
        
    }
    
    
    @Test 
    void rejectsInvalidDistance() {
    	
        for (double distance : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> run(1, distance, Duration.ofMinutes(25), null, DATE));
        
    }
    
    
    @Test 
    void rejectsInvalidDuration() {
    	
        for (Duration duration : new Duration[]{null, Duration.ZERO, Duration.ofSeconds(-1)})
            assertThrows(IllegalArgumentException.class, () -> run(1, 5, duration, null, DATE));
        
    }
    
    
    @Test 
    void validatesHeartRateBoundsAndOptionalValue() {
    	
        assertNull(run(1, 5, Duration.ofMinutes(25), null, DATE).getAverageHeartRate());
        assertEquals(80, run(1, 5, Duration.ofMinutes(25), 80, DATE).getAverageHeartRate());
        assertEquals(230, run(1, 5, Duration.ofMinutes(25), 230, DATE).getAverageHeartRate());
        
        for (int hr : new int[]{79, 231})
            assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), hr, DATE));
        
    }
    
    
    @Test 
    void rejectsNullOrFutureDate() {
    	
        assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), null, null));
        assertThrows(IllegalArgumentException.class, () -> run(1, 5, Duration.ofMinutes(25), null, LocalDate.now().plusDays(1)));
        
    }
}
