package runner_app;

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
    
}
