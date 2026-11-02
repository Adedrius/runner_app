package runner_app;

import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

/**
 * Tests the InputHandler class to verify input validation,
 * error handling, re-prompting, and user cancellation
 * across the different running tracker inputs.
 */
class InputHandlerTest {
	
	/**
	 * Creates an InputHandler with simulated user input.
	 *
	 * @param input the simulated console input
	 * @return a new InputHandler object
	 */
    private InputHandler handler(String input) { 
    	return new InputHandler(new Scanner(input)); 
    }

    /**
     * Verifies that valid durations are correctly converted
     * into Duration objects.
     */
    @Test 
    void durationAcceptsValidValues() {
    	
        assertEquals(Duration.ofHours(1).plusMinutes(30).plusSeconds(45), handler("01:30:45\n").readDuration("Duration: "));
        assertEquals(Duration.ofSeconds(1), handler("00:00:01\n").readDuration());
        
    }
    
    /**
     * Verifies that invalid duration formats and values are rejected
     * until a valid duration is entered.
     */
    @Test 
    void durationRetriesAllInvalidFormatsAndRanges() {
    	
        String input = String.join("\n", "10:30", "abc:30:00", "-1:00:00", "00:-1:00", "00:60:00", "00:00:-1", "00:00:60", "00:00:00", "00:01:02") + "\n";
        assertEquals(Duration.ofSeconds(62), handler(input).readDuration("Duration: "));
        
    }
    
    
    /**
     * Verifies that excessively large duration inputs are rejected
     * and the user is prompted to enter a valid duration.
     */
    @Test 
    void durationRetriesOverflow() {
    	
        assertEquals(Duration.ofMinutes(1), handler("999999999999999999999:00:00\n00:01:00\n").readDuration());
        
    }
    
    
    /**
     * Verifies that entering "B" cancels duration input
     * and throws an InputCancelledException.
     */
    @Test 
    void durationCancellation() {
    	
        assertThrows(InputCancelledException.class, () -> handler("B\n").readDuration());
        assertThrows(InputCancelledException.class, () -> handler(" b \n").readDuration("Duration: "));
        
    }
    
    
    /**
     * Verifies that invalid distances are rejected until
     * a positive, finite distance is entered.
     */
    @Test 
    void distanceRetriesInvalidValues() {
    	
        assertEquals(5.5, handler("abc\n0\n-3\nNaN\nInfinity\n5.5\n").readDistance());
        
    }
    
    /**
     * Verifies that heart rate input can be skipped and
     * accepts values within the valid range of 80-230 BPM.
     */
    @Test 
    void heartRateOptionalAndBoundaries() {
    	
        assertNull(handler("\n").readHeartRate());
        assertEquals(80, handler("79\nabc\n80\n").readHeartRate());
        assertEquals(230, handler("231\n230\n").readHeartRate());
        
    }
    
    
    /**
     * Verifies that invalid and future dates are rejected
     * until a valid run date is entered.
     */
    @Test 
    void runDateRetriesInvalidAndFutureDates() {
    	
        String future = LocalDate.now().plusDays(1).toString();
        assertEquals(LocalDate.of(2026, 1, 1), handler("bad\n" + future + "\n2026-01-01\n").readRunDate());
        
    }
    
    /**
     * Verifies that invalid effort levels are rejected
     * and values between 1 and 10 are accepted.
     */
    @Test 
    void effortRetriesInvalidAndAcceptsBounds() {
    	
        assertEquals(1, handler("bad\n0\n11\n1\n").readEffort());
        assertEquals(10, handler("10\n").readEffort());
        
    }
    
    /**
     * Verifies that invalid tempo pace inputs are rejected
     * and valid pace values are converted into seconds.
     */
    @Test
    void tempoPaceRetriesInvalidAndAcceptsSeconds() {
    	
        assertEquals(300, handler("bad\n00:60:00\n00:00:00\n00:05:00\n").readTempoPace());
        
    }
    
    /**
     * Verifies that target, interval, and recovery distances
     * follow their respective validation requirements.
     */
    @Test 
    void targetAndIntervalDistancesValidate() {
    	
        assertEquals(10.0, handler("0\nNaN\n10\n").readTargetDistance());
        assertEquals(0.8, handler("-1\nabc\n0.8\n").readIntervalDistance());
        assertEquals(0.0, handler("-1\n0\n").readRecoveryDistance());
        
    }
    
    /**
     * Verifies that invalid interval counts are rejected
     * until a positive whole number is entered.
     */
    @Test 
    void numberOfIntervalsValidates() {
    	
        assertEquals(3, handler("abc\n0\n-1\n3\n").readNumIntervals());
        
    }
    
    /**
     * Verifies that entering "B" cancels distance input
     * and throws an InputCancelledException.
     */
    @Test 
    void otherInputsCanBeCancelled() {
    	
        assertThrows(InputCancelledException.class, () -> handler("B\n").readDistance());
        
    }
    
}
