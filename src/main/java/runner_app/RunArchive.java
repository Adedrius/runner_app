package runner_app;

//Importing all necessary packages.
import java.util.List;
import java.util.Optional;

/*
 * Interface that defines the methods required for storing, retrieving, and deleting recorded runs.
 */
public interface RunArchive {
	
	// Adds a new run to the archive.
	void addRun(Run run);
	
	// Returns a list containing all recorded runs.
	List<Run> getAllRecordedRuns();
	
    // Searches for a run using its unique ID. Returns an Optional in case the run does not exist.
	Optional<Run> getRun(int runId);
	
    // Deletes a run using its unique ID. Returns true if the run was deleted, otherwise false.
	boolean deleteRun(int runId);
		
	
}
