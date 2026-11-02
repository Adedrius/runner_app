package runner_app;

//Importing all necessary packages.
import java.time.Duration;
import java.time.LocalDate;

public class LongRun extends Run {
	
	// This variable represents the target distance the user wanted to reach for their run.
	private final double targetDistance;
	
	/*
	 * Getter method to retrieve the target distance.
	 */
	public double getTargetDistance() {
		return targetDistance;
	}

	/**
	 * Constructor initialize a Long Run object.
	 * @param runId
	 * @param distance
	 * @param duration
	 * @param averageHeartRate
	 * @param date
	 * @param targetDistance
	 */
	public LongRun(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date, double targetDistance) {
		super(runId, distance, duration, averageHeartRate, date);
		
		if(targetDistance <= 0 || !Double.isFinite(targetDistance)) {
			throw new IllegalArgumentException("Distance must be greater than 0 and finite.");
		}
		this.targetDistance = targetDistance;
		
	}
	

	@Override
	/*
	 * Getter method that overrides Run abstract class and returns specific run type.
	 */
	public String getRunType() {
		return "Long Run";
	}

	/**
	 * Method that returns the summary of a run type for the user.
	 */
	@Override
	public String getRunSummary() {
		String summary = 
				 "Run Type: " + getRunType() + "\n\n" +
		
				 "Total Distance: " + getDistance() + " km" + "\n" +
				 "Total Duration: " + getFormattedDuration()  + "\n" + 
				 "Average Pace: " + getFormattedPace() + " min/km" + "\n\n" + 
				 
				 "TARGET DISTANCE: " + targetDistance + " km" + "\n" +
				 compareDistance() + "\n";

		if (getAverageHeartRate() != null) {
		    summary += "\n" + "Average HR: "+ getAverageHeartRate() + " BPM" +  "\n" + zoneSummary();
		}
		
		return summary;
	}
	
	
	/**
	 * Method that compares the actual distance with the target distance and gives feedback based on it.
	 * @return
	 */
	private String compareDistance() {
		if (getDistance() < targetDistance) {
			return "You had " + distanceDifference() + " km left to run to hit your goal. Push to the end next time!";
		} else if (getDistance() > targetDistance) { 
			return "Wow, you exceeded your goal distance by " + distanceDifference() + " km! You are outstanding!";
		} else {
			return "You've achieved your goal distance for this run. Amazing work!";
		}
	}
	
	/*
	 * Helper method that helps calculate the difference between target and actual distance. 
	 */
	private String distanceDifference() {
		double distanceDifference = Math.abs(getDistance() - targetDistance);
		
		
		return String.format("%.2f", distanceDifference);
	}
	
	@Override
	/**
	 * method that returns the user information about their heart rate zone.
	 */
	public String zoneSummary() {
		if (getAverageHeartRate() >= 80 && getAverageHeartRate() <= 130) {
			return "Wow! Your average heart rate was " + getAverageHeartRate() + " BPM throughout the entire long run. This falls within the estimated Zone 1 range. Your cardio fitness is high!";
		} else if (getAverageHeartRate() > 130 && getAverageHeartRate() <= 151) {
			return "Wow! Your average heart rate was " + getAverageHeartRate() + " BPM throughout the entire long run. This falls within the estimated Zone 2 range. Keep maintaining that aerobic base!";
		} else if (getAverageHeartRate() > 151 && getAverageHeartRate() <= 165) {
			return "You averaged the estimated Zone 3 heart rate at " + getAverageHeartRate() + " BPM throughout the entire long run. I would sign up for a race if I were you!";
		} else if (getAverageHeartRate() > 165 && getAverageHeartRate() <= 180) {
			return "Your adrenaline must've been high! You were at an estimated Zone 4 average heart rate at " + getAverageHeartRate() + " BPM throughout the entire long run. You must've been racing!";
		} else {
			return "Your average heart rate was a whopping " + getAverageHeartRate() + " BPM. I would recommend slowing down your pace to avoid heart rate spikes at a longer duration of running.";
		}
	}
	
	
}
