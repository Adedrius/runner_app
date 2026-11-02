package runner_app;

//Importing all necessary packages.
import java.time.Duration;
import java.time.LocalDate;

public class TempoRun extends Run {
	
		// This variable represent the pace the user wants to hold.
		private final int tempoPaceInSeconds;

		/**
		 * Getter method for target tempo pace.
		 * @return
		 */
		public int getTempoPaceInSeconds() {
			return tempoPaceInSeconds;
		}

		/**
		 * Tempo Run constructor that extends the Run abstract class.
		 * @param runId
		 * @param distance
		 * @param duration
		 * @param averageHeartRate
		 * @param date
		 * @param tempoPaceInSeconds
		 */
		public TempoRun(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date, int tempoPaceInSeconds) {
			super(runId, distance, duration, averageHeartRate, date);

			// If the pace is less than or equal to zero, throw an exception.
			if (tempoPaceInSeconds <= 0) {
				throw new IllegalArgumentException("The Tempo Run pace must be a positive integer representing the pace in seconds.");
			}
			this.tempoPaceInSeconds = tempoPaceInSeconds;
		}

		@Override
		/*
		 * Getter method that overrides Run abstract class and returns specific run type.
		 */
		public String getRunType() {
			return "Tempo Run";
		}

		@Override
		/**
		 * Method that returns the summary of a run type for the user.
		 */
		public String getRunSummary() {
			String summary = 
					 "Run Type: " + getRunType() + "\n\n" +
			
					 "Total Distance: " + getDistance() + " km" + "\n" +
					 "Total Duration: " + getFormattedDuration()  + "\n" + 
					 "Average Pace: " + getFormattedPace() + " min/km" + "\n\n" + 
					 
					 "TARGET PACE: " + getFormattedTempoPace() + " min/km" + "\n" +
					 comparePace() + "\n";
	
			if (getAverageHeartRate() != null) {
				summary += "\n" + "Average HR: "+ getAverageHeartRate() + " BPM" + "\n" + zoneSummary();
			}
			
			return summary;
		}
		
		/**
		 * Formats the target tempo pace and returns it.
		 * @return
		 */
		private String getFormattedTempoPace() {
		    long hours = tempoPaceInSeconds / 3600;
		    long remainingMinutes = (tempoPaceInSeconds % 3600) / 60;
		    long remainingSeconds = tempoPaceInSeconds % 60;

		    
		    return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds);
		}
		
		/**
		 * Method that compares the actual average pace with the target tempo pace and gives feedback based on it.
		 * @return
		 */
		private String comparePace() {
			long roundedPace = Math.round(calculatePaceInSeconds());
			
			if (roundedPace < tempoPaceInSeconds) {
				return "You were faster than your target pace by " + paceDifference() + " min/km! Well done...keep pushing!";
			} else if (roundedPace > tempoPaceInSeconds) { 
				return "You were slower than your target pace by " + paceDifference() + " min/km. You got it next time!";
			} else {
				return "You met your target pace. Good work!";
			}
		}
		
		/**
		 * Method that calculates the difference between actual and target pace and returns it as a difference in time.
		 * @return
		 */
		private String paceDifference() {
			long roundedPace = Math.round(calculatePaceInSeconds());
			long paceDifference = Math.abs(roundedPace - tempoPaceInSeconds);
			
		    long hours = paceDifference / 3600;
			long paceInMinutes = (paceDifference % 3600) / 60;
			long remainingSeconds = (paceDifference) % 60;
			
		    return String.format("%02d:%02d:%02d", hours, paceInMinutes, remainingSeconds);
		}

		@Override
		/**
		 * method that returns the user information about their heart rate zone.
		 */
		public String zoneSummary() {
			if (getAverageHeartRate() >= 80 && getAverageHeartRate() <= 130) {
				return "Your average heart rate was " + getAverageHeartRate() + " BPM throughout your tempo run. You have a lot of room to increase your intensity. Go at a tougher pace next time!";
			} else if (getAverageHeartRate() > 130 && getAverageHeartRate() <= 151) {
				return "Your average heart rate was " + getAverageHeartRate() + " BPM throughout your tempo run. This falls within the estimated Zone 2 range. You can definitely speed up past your aerobic base.";
			} else if (getAverageHeartRate() > 151 && getAverageHeartRate() <= 165) {
				return "You averaged the estimated Zone 3 heart rate at " + getAverageHeartRate() + " BPM throughout your tempo run. Great execution! If you want a challenge see if there's energy in the tank to slightly speed up on your next run!";
			} else if (getAverageHeartRate() > 165 && getAverageHeartRate() <= 180) {
				return "You were at an estimated Zone 4 average heart rate at " + getAverageHeartRate() + "BPM throughout your tempo run. Your cardio fitness will be sure to go up after such an intense session.";
			} else {
				return "Your average heart was a whopping " + getAverageHeartRate() + " BPM. I would recommend slowing down so you can maintain a longer tempo run at a pace that is still challenging!";
			}
		}
		
		
}
