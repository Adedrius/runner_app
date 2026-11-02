package runner_app;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;


/**
 * Tests the CsvArchive class to verify CSV file persistence,
 * run management, input validation, and error handling.
 * Ensures recorded runs are saved, loaded, and deleted correctly.
 */

@ResourceLock("runs.csv")
class CsvArchiveTest {
	
    private final Path csv = Path.of("runs.csv").toAbsolutePath();
    private byte[] original;
    private boolean existed;
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);

    
    /**
     * Backs up the existing CSV file before each test
     * and removes it to ensure an isolated test environment.
     *
     * @throws IOException if a file operation fails
     */
    @BeforeEach 
    void backup() throws IOException {
    	
        existed = Files.exists(csv);
        
        if (existed) 
        	original = Files.readAllBytes(csv);
        
        Files.deleteIfExists(csv);
        
    }
    
    /**
     * Restores the original CSV file after each test
     * or removes the test file if no original existed.
     *
     * @throws IOException if a file operation fails
     */
    @AfterEach 
    void restore() throws IOException {
    	
        if (existed) 
        	Files.write(csv, original);
        
        else Files.deleteIfExists(csv);
        
    }
    
    /**
     * Verifies that all four run types are saved to the CSV file
     * and correctly reconstructed when the archive is reloaded.
     */
    @Test 
    void savesAndReloadsAllFourRunTypes() {
    	
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
    
    
    /**
     * Verifies that all four run types are saved to the CSV file
     * and correctly reconstructed when the archive is reloaded.
     */
    @Test
    void rejectsNullAndDuplicateIds() {
    	
        CsvArchive archive = new CsvArchive();
        
        assertThrows(IllegalArgumentException.class, () -> archive.addRun(null));
        archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3));
        
        assertThrows(IllegalArgumentException.class, () -> archive.addRun(new EasyRun(1, 6, Duration.ofMinutes(35), null, DATE, 4)));
        assertEquals(1, archive.getAllRecordedRuns().size());
        
    }
    
    /**
     * Verifies that runs can be deleted and that deletions
     * persist after the CSV archive is reloaded.
     */
    @Test
    void deletesAndPersistsDeletion() {
    	
        CsvArchive archive = new CsvArchive();
        
        assertFalse(archive.deleteRun(99));
        archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3));
        
        assertTrue(archive.deleteRun(1));
        assertTrue(archive.getRun(1).isEmpty());
        assertTrue(new CsvArchive().getAllRecordedRuns().isEmpty());
        
    }
    
    /**
     * Verifies that invalid CSV data prevents new runs from being saved
     * and protects the original file from being overwritten.
     *
     * @throws IOException if a file operation fails
     */
    @Test
    void invalidCsvPreventsOverwritingOriginal() throws IOException {
    	
        String invalid = "not,a,valid,csv\n";
        
        Files.writeString(csv, invalid);
        CsvArchive archive = new CsvArchive();
        
        assertTrue(archive.getAllRecordedRuns().isEmpty());
        assertThrows(IllegalStateException.class, () -> archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3)));
        assertEquals(invalid, Files.readString(csv));
        
    }
    
    /**
     * Verifies that duplicate run IDs in the CSV file are detected
     * and prevent further modifications to the archive.
     *
     * @throws IOException if a file operation fails
     */
    @Test
    void duplicateRunIdsInCsv() throws IOException {
    	
        String record = "1,Easy Run,5.0,1800,,2026-01-01,3,,,,,,,\n";

        Files.writeString(csv, record + record);

        CsvArchive archive = new CsvArchive();

        assertEquals(1, archive.getAllRecordedRuns().size());
        assertThrows(IllegalStateException.class,() -> archive.addRun(new EasyRun(2, 5, Duration.ofMinutes(30), null, DATE, 3)));
        
    }

    /**
     * Verifies that unsupported run types in the CSV file are rejected
     * and prevent new runs from being saved.
     *
     * @throws IOException if a file operation fails
     */
    @Test
    void unsupportedRunTypeInCsv() throws IOException {
    	
        String record = "1,Unknown Run,5.0,1800,,2026-01-01,3,,,,,,,\n";

        Files.writeString(csv, record);

        CsvArchive archive = new CsvArchive();

        assertTrue(archive.getAllRecordedRuns().isEmpty());
        assertThrows(IllegalStateException.class,() -> archive.addRun(new EasyRun(2, 5, Duration.ofMinutes(30), null, DATE, 3)));
    }
    
    /**
     * Verifies that the archive handles an IOException during CSV loading
     * and prevents saving runs when loading fails.
     *
     * @throws IOException if a file operation fails
     */
    @Test
	void handlesIOExceptionWhenLoadingCsv() throws IOException {
    	
	    Files.createDirectory(csv);
	
	    try {
	        CsvArchive archive = new CsvArchive();
	
	        assertTrue(archive.getAllRecordedRuns().isEmpty());
	
	        assertThrows(IllegalStateException.class,() -> archive.addRun(new EasyRun(1, 5, Duration.ofMinutes(30), null, DATE, 3)));
	        
	    } finally {
	        Files.deleteIfExists(csv);
	    }
	}
    
}
