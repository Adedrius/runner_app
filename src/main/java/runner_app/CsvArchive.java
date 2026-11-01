package runner_app;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.time.Duration;
import java.time.LocalDate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

import java.time.format.DateTimeParseException;

public class CsvArchive implements RunArchive {

	private final Map<Integer, Run> runCollection = new HashMap<>();
	private final String filePath = "runs.csv";
	
	private boolean csvLoadError = false;
	
	public CsvArchive() {
		loadRunsFromCsv();
	}
	
	@Override
	public void addRun(Run run) {
		if (run == null) {
			throw new IllegalArgumentException("Run cannot be null.");
		}
		
		if (runCollection.containsKey(run.getRunId())) {
			throw new IllegalArgumentException("A run already recorded has the same run ID.");
		}
		
		runCollection.put(run.getRunId(), run);

		try {
		    saveRunToCsv();
		} catch (IllegalStateException e) {
		    runCollection.remove(run.getRunId());
		    throw e;
		}
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
		
	    Run removedRun = runCollection.remove(runId);

	    if (removedRun == null) {
	        return false;
	    }

	    try {
	        saveRunToCsv();
	    } catch (IllegalStateException e) {
	        runCollection.put(runId, removedRun);
	        throw e;
	    }

	    return true;
	}
	
	
	private void saveRunToCsv() {
		if (csvLoadError) {
		    throw new IllegalStateException( "Cannot save runs because there was an error when loading the data in the CSV file.");
		}
		
		try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
			for (Run run : runCollection.values()) {
				fileWriter.write(convertRunToCsv(run));
				fileWriter.newLine();
			}
		} catch (IOException e) {
			throw new IllegalStateException("Run save was unsuccessful.", e);
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
	
	
	private void loadRunsFromCsv() {
		try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
			
			String readLine;
			while ((readLine = fileReader.readLine()) != null) {
				
				try {
				
					String[] runInfo = readLine.split(",", -1);
					
					if (runInfo.length != 14) {
						throw new IllegalArgumentException("This CSV record is invalid. There should 14 allocated array cells for the run information.");
					}
					
					int runId = Integer.parseInt(runInfo[0]);
					
					if (runCollection.containsKey(runId)) {
					    throw new IllegalArgumentException("Duplicate Run ID found in CSV: " + runId);
					}
					
					String runType = runInfo[1];
					double distance = Double.parseDouble(runInfo[2]);
					Duration duration = Duration.ofSeconds(Long.parseLong(runInfo[3]));
					
					Integer averageHeartRate;
					if (runInfo[4].isEmpty()) {
						averageHeartRate = null;
					} else {
						averageHeartRate = Integer.parseInt(runInfo[4]);
					}
					
					LocalDate date = LocalDate.parse(runInfo[5]);
					
					
					
					
					if (runType.equals("Easy Run")) {
						
						int effort = Integer.parseInt(runInfo[6]);
						Run easyRun = new EasyRun(runId, distance, duration, averageHeartRate, date, effort);
						runCollection.put(runId, easyRun);
						
					} else if (runType.equals("Tempo Run")) {
						
						int tempoPaceInSeconds = Integer.parseInt(runInfo[7]);
						Run tempoRun = new TempoRun(runId, distance, duration, averageHeartRate, date, tempoPaceInSeconds);
						runCollection.put(runId, tempoRun);
						
					} else if (runType.equals("Long Run")) {
						
						double targetDistance = Double.parseDouble(runInfo[8]);
						Run longRun = new LongRun(runId, distance, duration, averageHeartRate, date, targetDistance);
						runCollection.put(runId, longRun);
						
					} else if (runType.equals("Interval Run")) {
						
						int numIntervals = Integer.parseInt(runInfo[9]);
						Duration intervalDuration = Duration.ofSeconds(Long.parseLong(runInfo[10]));
						Duration recoveryDuration = Duration.ofSeconds(Long.parseLong(runInfo[11]));
						double intervalDistance = Double.parseDouble(runInfo[12]);
						double recoveryDistance = Double.parseDouble(runInfo[13]);
						
						Run intervalRun = new IntervalRun(runId, distance, duration, averageHeartRate, date, numIntervals, intervalDuration, recoveryDuration, intervalDistance, recoveryDistance);
						runCollection.put(runId, intervalRun);
						
					} else {
						throw new IllegalArgumentException("This run type is not supported: " + runType);
					}
				} catch (IllegalArgumentException | DateTimeParseException e) {
				    csvLoadError = true;
				    System.out.println("Skipping invalid CSV record: " + e.getMessage());
				}
			}
			
			
		} catch (FileNotFoundException e) {
		    // This will be on the first launch as there are no saved files yet.

		} catch (IOException e) {
			csvLoadError = true;
		    System.out.println("There was an error loading runs: " + e.getMessage());
		}
		
		
	}


}
