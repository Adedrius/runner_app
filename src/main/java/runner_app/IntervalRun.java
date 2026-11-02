package runner_app;

//Importing all necessary packages.
import java.time.Duration;
import java.time.LocalDate;

public class IntervalRun extends Run {
	
	// These variables represents values specific to an interval run
	private final int numIntervals;
	private final Duration intervalDuration;
	private final Duration recoveryDuration;
	private final double intervalDistance;
	private final double recoveryDistance;
	
	/*
	 * Getter for the number of intervals
	 */
	public int getNumIntervals() {
		return numIntervals;
	}

	/*
	 * Getter for the duration.
	 */
	public Duration getIntervalDuration() {
		return intervalDuration;
	}

	/**
	 * Getter for the duration of recovery.
	 * @return
	 */
	public Duration getRecoveryDuration() {
		return recoveryDuration;
	}

	/**
	 * Get for the interval distance.
	 * @return
	 */
	public double getIntervalDistance() {
		return intervalDistance;
	}

	/**
	 * Getter for the recovery distance.
	 * @return
	 */
	public double getRecoveryDistance() {
		return recoveryDistance;
	}

	/**
	 * Constructor for interval runs that validates the values the user gives before assigning them.
	 * @param runId
	 * @param averageHeartRate
	 * @param date
	 * @param numIntervals
	 * @param intervalDuration
	 * @param recoveryDuration
	 * @param intervalDistance
	 * @param recoveryDistance
	 */
	public IntervalRun(int runId, Integer averageHeartRate, LocalDate date, int numIntervals, Duration intervalDuration, Duration recoveryDuration, double intervalDistance, double recoveryDistance) {
	    super(runId, (intervalDistance + recoveryDistance) * numIntervals, intervalDuration.plus(recoveryDuration).multipliedBy(numIntervals), averageHeartRate, date);
		
		
		if (numIntervals <= 0) {
			throw new IllegalArgumentException("The number of intervals should be a positive integer.");
		}
		
		if(intervalDuration == null || intervalDuration.isNegative() || intervalDuration.isZero()) {
			throw new IllegalArgumentException("Interval Duration can't be null and must be greater than 0.");
		}
		
		if(recoveryDuration == null || recoveryDuration.isNegative() || recoveryDuration.isZero()) {
			throw new IllegalArgumentException("Recovery Duration can't be null and must be greater than 0.");
		}
		
		
		if(intervalDistance <= 0 || !Double.isFinite(intervalDistance)) {
			throw new IllegalArgumentException("The interval distance must be greater than 0 and finite.");
		}
		
		if(recoveryDistance < 0 || !Double.isFinite(recoveryDistance)) {
			throw new IllegalArgumentException("The recovery distance must be greater than 0 and finite.");
		}
		
	
		this.numIntervals = numIntervals;
		this.intervalDuration = intervalDuration;
		this.recoveryDuration = recoveryDuration;
		this.intervalDistance = intervalDistance;
		this.recoveryDistance = recoveryDistance;
		
	}

	
	@Override
	/*
	 * Getter method that overrides Run abstract class and returns specific run type.
	 */
	public String getRunType() {
		return "Interval Run";
	}

	
	@Override
	/**
	 * Method that returns the summary of a run type for the user.
	 */
	public String getRunSummary() {
		String summary = 
				 "Run Type: " + getRunType() + "\n\n" +
		
				 "INTERVAL WORK: " + getFormattedIntervalDuration() + ", " + intervalDistance + "km" + " x " + numIntervals + "\n" +
				 "RECOVERY WORK: " + getFormattedRecoveryDuration() + ", " + recoveryDistance + "km"  + " x " + numIntervals + "\n\n" +

				 
				 "Total Distance: " + getDistance() + " km" + "\n" +
				 "Total Duration: " + getFormattedDuration()  + "\n\n" + 
				 
				 "Average Pace: " + getFormattedPace() + " min/km" + "\n" ;

		if (getAverageHeartRate() != null) {
		    summary += "\n" + "Average HR: "+ getAverageHeartRate() + " BPM" +  "\n" + zoneSummary();
		}
		
		return summary;
	}
	
	
	/**
	 * Formats th e interval duration and returns it.
	 * @return
	 */
	private String getFormattedIntervalDuration() {
		long hours =  intervalDuration.toHours();
		long minutes =  intervalDuration.toMinutesPart();
		long seconds =  intervalDuration.toSecondsPart();
		
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	
	/**
	 * formats the recovery durations and returns it.
	 * @return
	 */
	private String getFormattedRecoveryDuration() {
		long hours =  recoveryDuration.toHours();
		long minutes =  recoveryDuration.toMinutesPart();
		long seconds =  recoveryDuration.toSecondsPart();
		
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	@Override
	/**
	 * method that returns the user information about their heart rate zone.
	 */
	public String zoneSummary() {
		if (getAverageHeartRate() > 130 && getAverageHeartRate() <= 151) {
			return "Your average heart rate was " + getAverageHeartRate() + " BPM throughout your intervals. This falls within the estimated Zone 2 range. The recovery breaks offset your high intensity intervals very well.";
		} else if (getAverageHeartRate() > 151 && getAverageHeartRate() <= 165) {
			return "You averaged the estimated Zone 3 heart rate at " + getAverageHeartRate() + " BPM throughout your intervals. This is normal for a interval speed workout.";
		} else if (getAverageHeartRate() > 165 && getAverageHeartRate() <= 180) {
			return "You were at an estimated Zone 4 average heart rate at " + getAverageHeartRate() + " BPM throughout your intervals. Take long rest periods if you need to so you don't overwork youself.";
		} else if (getAverageHeartRate() > 180 && getAverageHeartRate() <= 230) {
			return "Your average heart rate was a whopping " + getAverageHeartRate() + " BPM. Hopefully your heart adjusts next time to the high intesity work. Don't be afraid to increase your rest or lower your interval pace slightly.";
		} else {
			return "You could've definitely increased the intensity with this this interval workout. Your heart rate was only an average of " + getAverageHeartRate() + " BPM. Try reducing your rest break, increasing the interval speed, or maintaining the speed for a longer distance for your next workout.";
		}
	}
	

}
