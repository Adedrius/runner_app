package runner_app;

import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class InputHandlerTest {
	
    private InputHandler handler(String input) { 
    	return new InputHandler(new Scanner(input)); 
    }

    @Test void durationAcceptsValidValues() {
    	
        assertEquals(Duration.ofHours(1).plusMinutes(30).plusSeconds(45), handler("01:30:45\n").readDuration("Duration: "));
        assertEquals(Duration.ofSeconds(1), handler("00:00:01\n").readDuration());
        
    }
    
    @Test void durationRetriesAllInvalidFormatsAndRanges() {
    	
        String input = String.join("\n", "10:30", "abc:30:00", "-1:00:00", "00:-1:00", "00:60:00", "00:00:-1", "00:00:60", "00:00:00", "00:01:02") + "\n";
        assertEquals(Duration.ofSeconds(62), handler(input).readDuration("Duration: "));
        
    }
    
    
    @Test void durationRetriesOverflow() {
    	
        assertEquals(Duration.ofMinutes(1), handler("999999999999999999999:00:00\n00:01:00\n").readDuration());
        
    }
    
    
    @Test void durationCancellation() {
    	
        assertThrows(InputCancelledException.class, () -> handler("B\n").readDuration());
        assertThrows(InputCancelledException.class, () -> handler(" b \n").readDuration("Duration: "));
        
    }
    
    
    @Test void distanceRetriesInvalidValues() {
    	
        assertEquals(5.5, handler("abc\n0\n-3\nNaN\nInfinity\n5.5\n").readDistance());
        
    }
    
    @Test void heartRateOptionalAndBoundaries() {
    	
        assertNull(handler("\n").readHeartRate());
        assertEquals(80, handler("79\nabc\n80\n").readHeartRate());
        assertEquals(230, handler("231\n230\n").readHeartRate());
        
    }
    
    @Test void runDateRetriesInvalidAndFutureDates() {
    	
        String future = LocalDate.now().plusDays(1).toString();
        assertEquals(LocalDate.of(2026, 1, 1), handler("bad\n" + future + "\n2026-01-01\n").readRunDate());
        
    }
    
    
}
