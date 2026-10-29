package runner_app;

import java.time.LocalDate;
import java.time.Duration;

public abstract class Run {
	
	private final int runId;
	private final double distance;
	private final Duration duration;
	private final Integer averageHeartRate;
	private final LocalDate date;
	
	public Run(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date) {
		if(runId <= 0) {
			throw new IllegalArgumentException("RunID must be positive.");
		}
		this.runId = runId;
		
		
		
		if(distance <= 0 || !Double.isFinite(distance)) {
			throw new IllegalArgumentException("Distance must be greater than 0 and finite.");
		}
		this.distance = distance;
		
		
		
		if(duration == null || duration.isNegative() || duration.isZero()) {
			throw new IllegalArgumentException("Duration must be greater than 0.");
		}
		this.duration = duration;
		
		
		
		if (averageHeartRate != null && (averageHeartRate < 80 || averageHeartRate > 230)) {
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
	
	public Duration getDuration() {
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
	public abstract String zoneSummary();
	
	protected String getFormattedDuration() {
		long hours =  getDuration().toHours();
		long minutes =  getDuration().toMinutesPart();
		long seconds =  getDuration().toSecondsPart();
		
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	protected String getFormattedPace() {
		long paceInSeconds = Math.round(calculatePaceInSeconds());
		
		
		long paceInMinutes = paceInSeconds / 60;
		long remainingSeconds = paceInSeconds % 60;
		
		
		return String.format("%d:%02d", paceInMinutes, remainingSeconds);
	}
	
	protected double calculatePaceInSeconds() {
		double paceInSeconds = getDuration().toSeconds();
		double pace = paceInSeconds / getDistance();
		
		return pace;
	}
	


}
