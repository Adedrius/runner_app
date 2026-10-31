package runner_app;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RunService {

    private final RunArchive archive;

    public RunService(RunArchive archive) {
        this.archive = archive;
    }

    public void saveRun(Run run) {
        archive.addRun(run);
    }
    
    public int generateRunId() {
        return archive.getAllRecordedRuns().stream().mapToInt(Run::getRunId).max().orElse(0) + 1;
    }
    
    public List<Run> getAllRecordedRuns() {
        return archive.getAllRecordedRuns();
    }
    
    public Optional<Run> searchRun(int runId) {
        return archive.getRun(runId);
    }
    
    public List<Run> searchRunByDate(LocalDate date) {
        return archive.getAllRecordedRuns().stream().filter(run -> run.getDate().equals(date)).toList();
    }
}
