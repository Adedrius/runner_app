package runner_app;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvArchive implements RunArchive {

	private final Map<Integer, Run> runCollection = new HashMap<>();
	private final String filePath = "runs.csv";
	
	@Override
	public void addRun(Run run) {
		if (run == null) {
			throw new IllegalArgumentException("Run cannot be null.");
		}
		
		if (runCollection.containsKey(run.getRunId())) {
			throw new IllegalArgumentException("A run already recorded has the same run ID.");
		}
		
		runCollection.put(run.getRunId(), run);
		
		saveRunToCsv();
	}

	@Override
	public List<Run> getAllRecordedRuns() {
		return new ArrayList<>(runCollection.values());
	}

	@Override
	public Optional<Run> getRun(int runId) {	
		return Optional.ofNullable(runCollection.get(runId));
	}

	@Override
	public boolean deleteRun(int runId) {
		boolean deleteSucess = runCollection.remove(runId) != null;
		
		if (deleteSucess) {
			saveRunToCsv();
		}
		
		return deleteSucess;
	}
	
	private void saveRunToCsv() {
		try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
			for (Run run : runCollection.values()) {
				fileWriter.write(String.valueOf(run.getRunId()) + "," + run.getRunType() + "," + run.getDistance() + "," + run.getDuration().toSeconds() + "," + run.getAverageHeartRate() + "," + run.getDate());
				fileWriter.newLine();
			}
		} catch (IOException e) {
			System.out.println("There was an error in saving the run: " + e.getMessage());
		}
	}
	
	private String convertRunToCsv(Run run) {
		String simpleRun = run.getRunId() + "," + run.getRunType() + "," + run.getDistance() + "," + run.getDuration().toSeconds() + "," + (run.getAverageHeartRate() == null ? "" : run.getAverageHeartRate()) + "," + run.getDate();
		
		if (run instanceof EasyRun easyRun) {
			return simpleRun + "," + easyRun.getEffort() + ",,,,,,,";
		} else if (run instanceof TempoRun tempoRun) {
			return simpleRun + ",," + tempoRun.getTempoPaceInSeconds() + ",,,,,,";
		} else if (run instanceof LongRun longRun) {
			return simpleRun + ",,," + longRun.getTargetDistance() + ",,,,,";
		} else if (run instanceof IntervalRun intervalRun) {
			return simpleRun + ",,,," + intervalRun.getNumIntervals() + "," + intervalRun.getIntervalDuration().toSeconds() + "," + intervalRun.getRecoveryDuration().toSeconds() + "," + intervalRun.getIntervalDistance() + "," + intervalRun.getRecoveryDistance();
		}
		
		throw new IllegalArgumentException("This run type is unsupported.");
		
		
	}

}
