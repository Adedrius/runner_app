package runner_app;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;


@ResourceLock("runs.csv")
class CsvArchiveTest {
	
    private final Path csv = Path.of("runs.csv").toAbsolutePath();
    private byte[] original;
    private boolean existed;
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);

    
    @BeforeEach void backup() throws IOException {
    	
        existed = Files.exists(csv);
        
        if (existed) 
        	original = Files.readAllBytes(csv);
        
        Files.deleteIfExists(csv);
        
    }
    
    @AfterEach void restore() throws IOException {
    	
        if (existed) 
        	Files.write(csv, original);
        
        else Files.deleteIfExists(csv);
        
    }
    
    @Test void savesAndReloadsAllFourRunTypes() {
    	
        CsvArchive archive = new CsvArchive();
        
        archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3));
        archive.addRun(new TempoRun(2, 8, Duration.ofMinutes(40), 160, DATE, 300));
        archive.addRun(new LongRun(3, 15, Duration.ofMinutes(90), 145, DATE, 15));
        archive.addRun(new IntervalRun(4, 170, DATE, 5, Duration.ofMinutes(3), Duration.ofMinutes(2), .8, .2));
        
        CsvArchive reloaded = new CsvArchive();
        
        assertEquals(4, reloaded.getAllRecordedRuns().size());
        assertInstanceOf(EasyRun.class, reloaded.getRun(1).orElseThrow());
        assertInstanceOf(TempoRun.class, reloaded.getRun(2).orElseThrow());
        assertInstanceOf(LongRun.class, reloaded.getRun(3).orElseThrow());
        assertInstanceOf(IntervalRun.class, reloaded.getRun(4).orElseThrow());
        assertEquals(5.0, reloaded.getRun(4).orElseThrow().getDistance(), 0.000001);
    }
    
    
    @Test
    void rejectsNullAndDuplicateIds() {
    	
        CsvArchive archive = new CsvArchive();
        
        assertThrows(IllegalArgumentException.class, () -> archive.addRun(null));
        archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3));
        
        assertThrows(IllegalArgumentException.class, () -> archive.addRun(new EasyRun(1, 6, Duration.ofMinutes(35), null, DATE, 4)));
        assertEquals(1, archive.getAllRecordedRuns().size());
        
    }
    
    
    @Test
    void deletesAndPersistsDeletion() {
    	
        CsvArchive archive = new CsvArchive();
        
        assertFalse(archive.deleteRun(99));
        archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3));
        
        assertTrue(archive.deleteRun(1));
        assertTrue(archive.getRun(1).isEmpty());
        assertTrue(new CsvArchive().getAllRecordedRuns().isEmpty());
        
    }
    

    
}
