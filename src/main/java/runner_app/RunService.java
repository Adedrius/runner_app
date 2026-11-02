package runner_app;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Handles the business logic for managing recorded runs.
 * Uses the RunArchive interface to save, retrieve, search,
 * and delete runs.
 */
public class RunService {

    private final RunArchive archive;

    /**
     * Initializes the RunService with a run archive.
     *
     * @param archive the archive used to manage recorded runs
     */
    public RunService(RunArchive archive) {
        this.archive = archive;
    }

    /**
     * Saves a new run to the archive.
     *
     * @param run the run to save
     */
    public void saveRun(Run run) {
        archive.addRun(run);
    }
    
    /**
     * Generates a new run ID based on the highest existing ID.
     *
     * @return the next available run ID
     */
    public int generateRunId() {
        return archive.getAllRecordedRuns().stream().mapToInt(Run::getRunId).max().orElse(0) + 1;
    }
    
    /**
     * Retrieves all recorded runs from the archive.
     *
     * @return a list of all recorded runs
     */
    public List<Run> getAllRecordedRuns() {
        return archive.getAllRecordedRuns();
    }
    
    /**
     * Searches for a recorded run using its unique ID.
     *
     * @param runId the ID of the run to search for
     * @return an Optional containing the run if found
     */
    public Optional<Run> searchRun(int runId) {
        return archive.getRun(runId);
    }
    
    /**
     * Retrieves all runs recorded on a specified date.
     *
     * @param date the date to search for
     * @return a list of runs matching the specified date
     */
    public List<Run> searchRunByDate(LocalDate date) {
        return archive.getAllRecordedRuns().stream().filter(run -> run.getDate().equals(date)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Deletes a recorded run using its unique ID.
     *
     * @param runId the ID of the run to delete
     * @return true if the run was deleted, otherwise false
     */
    public boolean deleteRun(int runId) {
        return archive.deleteRun(runId);
    }
}
