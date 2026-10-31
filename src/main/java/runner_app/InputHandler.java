package runner_app;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }
    
    private double readDistance() {

        while (true) {
            System.out.print("Enter run distance (km): ");
            String input = scanner.nextLine();

            try {
            	
                double distance = Double.parseDouble(input);

                if (distance > 0 && Double.isFinite(distance)) {
                    return distance;
                }
                System.out.println("The distance must be a positive number.");

            } catch (NumberFormatException e) {
                System.out.println("That input is invalid. Please enter a positive number (km).");
            }
        }
    }
	
    
	private Duration readDuration() {

	    while (true) {
	    	
	        System.out.print("Enter duration (mm:ss): ");
	        String input = scanner.nextLine();

	        try {
	        	
	            String[] timeParts = input.split(":");

	            if (timeParts.length != 2) {
	                System.out.println("That format is invalid. Use mm:ss format.");
	                continue;
	            }
	            
	       
	            
	            int minutes = Integer.parseInt(timeParts[0]);
	            int seconds = Integer.parseInt(timeParts[1]);
	            
	            if (minutes < 0 || seconds < 0 || seconds > 59) {
	                System.out.println("That duration is invalid. Please try again.");
	                continue;
	            }
	            long totalSeconds = minutes * 60L + seconds;
	            

	            if (totalSeconds == 0) {
	                System.out.println("The duration must be greater than zero.");
	                continue;
	            }
	            

	            return Duration.ofSeconds(totalSeconds);

	        } catch (NumberFormatException e) {
	            System.out.println("That input is invalid. Enter numbers in mm:ss format.");
	        }
	    }
	}
	

	private Integer readHeartRate() {

		while (true) {
			System.out.print("Enter your average heart rate (80-230 BPM, or press Enter to skip): ");
			String input = scanner.nextLine().trim();

			
			if (input.isEmpty()) {
			    return null;
			}
			

			try {
				
				int heartRate = Integer.parseInt(input);

				if (heartRate >= 80 && heartRate <= 230) {
					return heartRate;
				}
				System.out.println("Your heart rate must be between 80 and 230 BPM to be registered as a valid run.");

				
			} catch (NumberFormatException e) {
				System.out.println("That input is invalid. Please enter a whole number between 80 and 230.");
			}
		}
	}
	
	
	private LocalDate readRunDate() {

	    while (true) {
	        System.out.print("Enter the run date (yyyy-MM-dd): ");
	        String input = scanner.nextLine().trim();

	        try {
	        	
	            LocalDate date = LocalDate.parse(input);

	            if (date.isAfter(LocalDate.now())) {
	                System.out.println("The date of the run cannot be in the future.");
	                continue;
	            }

	            return date;

	        } catch (DateTimeParseException e) {
	            System.out.println("That date is invalid. Please use yyyy-MM-dd format.");
	        }
	    }
	}
	
	
	private int readEffort() {

	    while (true) {
	        System.out.print("Enter perceived effort (1-10): ");
	        String input = scanner.nextLine();

	        try {
	        	
	            int effort = Integer.parseInt(input);

	            if (effort >= 1 && effort <= 10) {
	                return effort;
	            }
	            System.out.println("Effort must be between 1 and 10.");

	        } catch (NumberFormatException e) {
	            System.out.println("That input is invalid. Enter a whole number.");
	        }
	    }
	}
	

	private int readTempoPace() {

		while (true) {
			System.out.print("Enter your target tempo pace per km (mm:ss): ");
			String input = scanner.nextLine().trim();

			try {
				
				String[] timeParts = input.split(":");

				if (timeParts.length != 2) {
					System.out.println("That format is invalid. Use mm:ss.");
					continue;
				}

				
				int minutes = Integer.parseInt(timeParts[0]);
				int seconds = Integer.parseInt(timeParts[1]);

				if (minutes < 0 || seconds < 0 || seconds > 59) {
					System.out.println("That pace is invalid. Please try again.");
					continue;
				}
				
				
				int tempoPaceInSeconds = minutes * 60 + seconds;

				if (tempoPaceInSeconds <= 0) {
					System.out.println("The target tempo run pace must be greater than zero.");
					continue;
				}
				return tempoPaceInSeconds;

			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Enter pace in mm:ss format.");
			}
		}
	}

	
	private double readTargetDistance() {

	    while (true) {
	        System.out.print("Enter your target distance (km): ");
	        String input = scanner.nextLine().trim();

	        try {
	        	
	            double targetDistance = Double.parseDouble(input);

	            
	            if (targetDistance > 0 && Double.isFinite(targetDistance)) {
	                return targetDistance;
	            }
	            System.out.println("Target distance must be a positive, finite number.");

	        } catch (NumberFormatException e) {
	            System.out.println("That input is invalid. Please enter a positive number (km).");
	        }
	    }
	}
	
	
	private int readNumIntervals() {

	    while (true) {
	        System.out.print("Enter number of intervals: ");
	        String input = scanner.nextLine().trim();

	        try {
	        	
	            int numIntervals = Integer.parseInt(input);

	            
	            if (numIntervals > 0) {
	                return numIntervals;
	            }
	            System.out.println("The number of intervals must be greater than zero to be a valid interval workout. If it truly is zero, consider recording using a different type of run.");

	        } catch (NumberFormatException e) {
	            System.out.println("That input is invalid Enter a positive whole number.");
	        }
	    }
	}
	
	private double readIntervalDistance() {

	    while (true) {
	        System.out.print("Enter work distance per interval (km): ");
	        String input = scanner.nextLine().trim();

	        try {
	            double intervalDistance = Double.parseDouble(input);

	            if (intervalDistance > 0 && Double.isFinite(intervalDistance)) {
	                return intervalDistance;
	            }

	            System.out.println("Work distance must be greater than zero.");

	        } catch (NumberFormatException e) {
	            System.out.println("Invalid input. Enter a positive number.");
	        }
	    }
	}

	private double readRecoveryDistance() {

	    while (true) {
	        System.out.print("Enter recovery distance per interval (km): ");
	        String input = scanner.nextLine().trim();

	        try {
	            double recoveryDistance = Double.parseDouble(input);

	            if (recoveryDistance >= 0 && Double.isFinite(recoveryDistance)) {
	                return recoveryDistance;
	            }

	            System.out.println("Recovery distance cannot be negative.");

	        } catch (NumberFormatException e) {
	            System.out.println("Invalid input. Enter a non-negative number.");
	        }
	    }
	}
}
