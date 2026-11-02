package runner_app;

import java.time.LocalDate;
import java.time.Duration;


public class EasyRun extends Run {
	
	// This variable represents run effort from 1-10 entered by the user.
	private final int effort;

	/**
	 * Getter method to retrieve effort value.
	 * @return
	 */
	public int getEffort() {
		return effort;
	}

	/**
	 * Easy Run constructor that extends the Run abstract class.
	 * @param runId
	 * @param distance
	 * @param duration
	 * @param averageHeartRate
	 * @param date
	 * @param effort
	 */
	public EasyRun(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date, int effort) {
		super(runId, distance, duration, averageHeartRate, date);
		
		// If the overall effort isn't in the range 1-10, throw an exception to handle.
		if (effort < 1 || effort > 10) {
			throw new IllegalArgumentException("Effort must be an integer between 1 and 10.");
		}
		this.effort = effort;
	}

	@Override
	/**
	 * Getter method that returns the run type.
	 */
	public String getRunType() {
		
		return "Easy Run";
	}

	@Override
	/**
	 * Method that returns the summary of a run type for the user.
	 */
	public String getRunSummary() {
		String summary = "Run Type: " + getRunType() + "\n\n" + 
	
						 "Total Distance: " + getDistance() + " km" + "\n" +
						 "Total Duration: " + getFormattedDuration()  + "\n" + 
						 "Average Pace: " + getFormattedPace() + " min/km" + "\n\n" + 
						 
						 "PERCEIVED EFFORT: " + effort + "/10"+ "\n";
		
		// Only provide a summary for heart if the user provides a heart rate value.
		if (getAverageHeartRate() != null) {
			summary += "\n" + "Average HR: "+ getAverageHeartRate() + " BPM" + "\n" + zoneSummary();
		}
		
		return summary;
	}

	
	@Override
	/**
	 * method that returns the user information about their heart rate zone.
	 */
	public String zoneSummary() {
		
		if (getAverageHeartRate() >= 80 && getAverageHeartRate() <= 130) {
			return "Wow! Your average heart rate was " + getAverageHeartRate() + " BPM. You kept a very light pace throughout the easy run. This falls within the estimated Zone 1 range. Your cardio fitness is high!";
		} else if (getAverageHeartRate() > 130 && getAverageHeartRate() <= 151) {
			return "Wow! Your average heart rate was " + getAverageHeartRate() + " BPM throughout the easy run. This falls within the estimated Zone 2 range. Keep maintaining that aerobic base!";
		} else if (getAverageHeartRate() > 151 && getAverageHeartRate() <= 165) {
			return "You averaged the estimated Zone 3 heart rate at " + getAverageHeartRate() + " BPM throughout the easy run. This is on the higher end for heart rate for an easy run and should be monitored.";
		} else {
			return "Your average heart was a whopping " + getAverageHeartRate() + " BPM. While not every run is perfect, your heart rate shouldn't typically be this high for an easy run.";
		}
		
	}
	
	
	
}
