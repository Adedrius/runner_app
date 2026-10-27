package runner_app;

import java.time.LocalDate;

public abstract class Run {
	
	private final int runId;
	private final double distance;
	private final double duration;
	private final Integer averageHeartRate;
	private final LocalDate date;
	
	public Run(int runId, double distance, double duration, Integer averageHeartRate, LocalDate date) {
		if(runId <= 0) {
			throw new IllegalArgumentException("RunID must be positive.");
		}
		this.runId = runId;
		
		
		
		if(distance <= 0 || !Double.isFinite(distance)) {
			throw new IllegalArgumentException("Distance must be greater than 0 and finite.");
		}
		this.distance = distance;
		
		
		
		if(duration <= 0 || !Double.isFinite(duration)) {
			throw new IllegalArgumentException("Duration must be greater than 0 and finite.");
		}
		this.duration = duration;
		
		
		
		if (averageHeartRate < 80 || averageHeartRate > 230) {
			throw new IllegalArgumentException("Average heart is either too high or too low to be valid. Enter a valid heart rate.");
		}
		this.averageHeartRate = averageHeartRate;
		
		
		
		if (date == null || date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("The date has to be provided and can't be after today.");
		}
		this.date = date;
	}
	
	
	public int getRunId() {
		return runId;
	}
	public double getDistance() {
		return distance;
	}
	public double getDuration() {
		return duration;
	}
	public Integer getAverageHeartRate() {
		return averageHeartRate;
	}
	public LocalDate getDate() {
		return date;
	}
	
	public abstract String getRunType();
	public abstract String getRunSummary();
	
	
	

}
