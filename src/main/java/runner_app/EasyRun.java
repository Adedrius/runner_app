package runner_app;

import java.time.LocalDate;
import java.time.Duration;

public class EasyRun extends Run {
	
	private final int effort;

	public EasyRun(int runId, double distance, Duration duration, Integer averageHeartRate, LocalDate date, int effort) {
		super(runId, distance, duration, averageHeartRate, date);
		
		if (effort < 1 || effort > 10) {
			throw new IllegalArgumentException("Effort must be an integer between 1 and 10.");
		}
		this.effort = effort;
	}

	@Override
	public String getRunType() {
		
		return "Easy Run";
	}

	@Override
	public String getRunSummary() {
		String summary = "Total Distance: " + getDistance() + " km" + "\n" +
						 "Total Duration: " + getFormattedDuration()  + "\n" + 
						 "Average Pace: " + getFormattedPace() + " min/km" + "\n" + 
						 "Perceived Effort: " + effort;
		
		return summary;
	}
	
	
	public static void main(String[] args) {
		Run run = new EasyRun(1,20,Duration.ofHours(1).plusMinutes(15).plusSeconds(4),125,LocalDate.now(), 5);
		
		System.out.println(run.getRunSummary());
	}
	
	
	
	
	
	
}
