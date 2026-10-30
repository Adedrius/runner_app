package runner_app;

import java.util.List;
import java.util.Optional;

public interface RunArchive {
	
	void addRun(Run run);
	
	List<Run> getAllRecordedRuns();
	
	Optional<Run> getRun(int runId);
	
	boolean deleteRun(int runId);
		
	
}
