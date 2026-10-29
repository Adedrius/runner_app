package runner_app;

import java.time.Duration;
import java.time.LocalDate;

public class TempoRun extends Run {
	
		private final int tempoPaceInSeconds;

		public TempoRun(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date, int tempoPaceInSeconds) {
			super(runId, distance, duration, averageHeartRate, date);

			if (tempoPaceInSeconds <= 0) {
				throw new IllegalArgumentException("The Tempo Run pace must be a positive integer representing the pace in seconds.");
			}
			this.tempoPaceInSeconds = tempoPaceInSeconds;
		}

		@Override
		public String getRunType() {
			return "Tempo Run";
		}

		@Override
		public String getRunSummary() {
			String summary = "Total Distance: " + getDistance() + " km" + "\n" +
					 "Total Duration: " + getFormattedDuration()  + "\n" + 
					 "Average Pace: " + getFormattedPace() + " min/km" + "\n" + 
					 "Target Pace: " + getFormattedTempoPace() + " min/km" + "\n" +
					 comparePace();
	
			if (getAverageHeartRate() != null) {
				summary += "\n" + "Average HR: "+ getAverageHeartRate() + " BPM" + "\n" + zoneSummary();
			}
			
			return summary;
		}
		
		private String getFormattedTempoPace() {
			long paceInMinutes = tempoPaceInSeconds / 60;
			long remainingSeconds = tempoPaceInSeconds % 60;
			
			return String.format("%d:%02d", paceInMinutes, remainingSeconds);
		}
		
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
		
		private String paceDifference() {
			long roundedPace = Math.round(calculatePaceInSeconds());
			long paceDifference = Math.abs(roundedPace - tempoPaceInSeconds);
			
			long paceInMinutes = paceDifference / 60;
			long remainingSeconds = paceDifference % 60;
			
			return String.format("%d:%02d", paceInMinutes, remainingSeconds); 
		}

		@Override
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
