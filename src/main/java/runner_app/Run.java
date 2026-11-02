package runner_app;

// Importing all necessary packages.
import java.time.LocalDate;
import java.time.Duration;

public abstract class Run {
	
	// Common run attributes from all runs types.
	private final int runId;
	private final double distance;
	private final Duration duration;
	private final Integer averageHeartRate;
	private final LocalDate date;
	
	/**
	 * Constructor for the main main run abstract class.. These attributes are shared by all run classes.
	 * @param runId
	 * @param distance
	 * @param duration
	 * @param averageHeartRate
	 * @param date
	 */
	public Run(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date) {
		
		// Run ID can't be less than or equal to zero. Only positive integers can be used as Run IDs.
		if(runId <= 0) {
			throw new IllegalArgumentException("RunID must be positive.");
		}
		this.runId = runId;
		
		
		// The distance can't be less than or equal to zero and has to be finite.
		if(distance <= 0 || !Double.isFinite(distance)) {
			throw new IllegalArgumentException("Distance must be greater than 0 and finite.");
		}
		this.distance = distance;
		
		
		// The duration can't be null, negative, or zero.
		if(duration == null || duration.isNegative() || duration.isZero()) {
			throw new IllegalArgumentException("Duration must be greater than 0.");
		}
		this.duration = duration;
		
		
		// Average heart is an optional attribute, however, if entered, it must meet the constraint of 80-230 BPM.
		if (averageHeartRate != null && (averageHeartRate < 80 || averageHeartRate > 230)) {
			throw new IllegalArgumentException("Average heart is either too high or too low to be valid. Enter a valid heart rate.");
		}
		this.averageHeartRate = averageHeartRate;
		
		
		// The date can't be null and has to be either the present or the past.
		if (date == null || date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("The date has to be provided and can't be after today.");
		}
		this.date = date;
	}
	
	/**
	 * Getter for runID.
	 * @return
	 */
	public int getRunId() {
		return runId;
	}
	
	/**
	 * Getter for distance.
	 * @return
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * Getter for duration.
	 * @return
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Getter for average heart rate.
	 * @return
	 */
	public Integer getAverageHeartRate() {
		return averageHeartRate;
	}
	
	/**
	 * Getter for the date.
	 * @return
	 */
	public LocalDate getDate() {
		return date;
	}
	
	// Abstract methods that will be implemented by subclasses. They are shared by the subclasses but have slightly different implementations.
	public abstract String getRunType();
	public abstract String getRunSummary();
	public abstract String zoneSummary();
	
	/**
	 * Method that returns a formatted duration.
	 * @return
	 */
	protected String getFormattedDuration() {
		// Get each unit of time.
		long hours =  getDuration().toHours();
		long minutes =  getDuration().toMinutesPart();
		long seconds =  getDuration().toSecondsPart();
		
		// Format into a proper time at the end.
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	/**
	 * Getter method that returns a formatted pace.
	 * @return
	 */
	protected String getFormattedPace() {
		// Getting each unit of time.
	    long paceInSeconds = Math.round(calculatePaceInSeconds());

	    long hours = paceInSeconds / 3600;
	    long minutes = (paceInSeconds % 3600) / 60;
	    long seconds = paceInSeconds % 60;

		// Format into a proper pace time at the end.
	    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	protected double calculatePaceInSeconds() {
		double paceInSeconds = getDuration().toSeconds();
		double pace = paceInSeconds / getDistance();
		
		return pace;
	}
	


}
