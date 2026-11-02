package runner_app;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Handles user input and validation for the Running Tracker application.
 * Re-prompts users for invalid inputs and allows cancellation by entering "B".
 */
public class InputHandler {
    private final Scanner scanner;

    /**
     * Initializes the InputHandler with a Scanner.
     *
     * @param scanner the Scanner used to read user input
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads user input and checks whether the user wants to cancel.
     *
     * @return the trimmed user input
     * @throws InputCancelledException if the user enters "B"
     */
    private String readInput() {

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("B")) {
            throw new InputCancelledException();
        }

        return input;
    }
    
    /**
     * Reads and validates the run distance.
     *
     * @return a positive distance in kilometres
     */
    public double readDistance() {
    	
        while (true) {
        	System.out.println();
            System.out.print("Enter run distance (km): ");
            
            String input = readInput();

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
	
    
    /**
     * Reads and validates a duration in hh:mm:ss format.
     *
     * @param prompt the message displayed to the user
     * @return a valid Duration greater than zero
     */
    public Duration readDuration(String prompt) {
    	
	    while (true) {
	    	System.out.println();
	    	System.out.print(prompt);
	        
	        String input = readInput();

	        try {
	        	
	            String[] timeParts = input.split(":", -1);
	            
	            if (timeParts.length != 3) {
	                System.out.println("That format is invalid. Use hh:mm:ss format.");
	                continue;
	            }
	            
	       
	            long hours = Long.parseLong(timeParts[0]);
	            int minutes = Integer.parseInt(timeParts[1]);
	            int seconds = Integer.parseInt(timeParts[2]);
	            
	            if (hours < 0 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
	                System.out.println("That duration is invalid. Please try again.");
	                continue;
	            }
	            
	            Duration runDuration = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);

	            if (runDuration.isZero()) {
	                System.out.println("Th duration must be greater than zero.");
	                continue;
	            }

	            return runDuration;

	        } catch (NumberFormatException | ArithmeticException e) {
	            System.out.println("That input is invalid. Enter numbers in hh:mm:ss format.");
	        }
	    }
	}
    
    /**
     * Reads a duration using the default prompt.
     *
     * @return a valid Duration
     */
    public Duration readDuration() {
        return readDuration("Enter duration (hh:mm:ss): ");
    }
	

    /**
     * Reads and validates the user's average heart rate.
     *
     * @return a heart rate between 80 and 230 BPM, or null if skipped
     */
    public Integer readHeartRate() {
    	
		while (true) {
			System.out.println();
			System.out.print("Enter your average heart rate (80-230 BPM, or press Enter to skip): ");
			
			String input = readInput();

			
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
	
    
    /**
     * Reads and validates the run date.
     *
     * @return a valid date that is not in the future
     */
    public LocalDate readRunDate() {
    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter the run date (yyyy-MM-dd): ");
	        
	        String input = readInput();
	        

	        try {
	        	
	            LocalDate date = LocalDate.parse(input);

	            if (date.isAfter(LocalDate.now())) {
	                System.out.println("The date of the run cannot be in the future.");
	                continue;
	            }

	            return date;

	        } catch (DateTimeParseException e) {
	            System.out.println("That date is invalid. Please use the yyyy-MM-dd format.");
	        }
	    }
	}
	
	
    /**
     * Reads and validates the perceived effort of an easy run.
     *
     * @return an effort level between 1 and 10
     */
    public int readEffort() {
    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter perceived effort (1-10): ");
	        
	        String input = readInput();

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
	

    /**
     * Reads and validates the target tempo pace.
     *
     * @return the target pace in seconds per kilometre
     */
    public int readTempoPace() {
    	
		while (true) {
			System.out.println();
			System.out.print("Enter your target tempo pace per km (hh:mm:ss): ");
			
			String input = readInput();

			try {
				
				String[] timeParts = input.split(":", -1);

				
				if (timeParts.length != 3) {
					System.out.println("That format is invalid. Use hh:mm:ss.");
					continue;
				}
				

	            long hours = Long.parseLong(timeParts[0]);
				int minutes = Integer.parseInt(timeParts[1]);
				int seconds = Integer.parseInt(timeParts[2]);
				

				if (hours < 0 || minutes < 0 || minutes > 59|| seconds < 0 || seconds > 59) {
					System.out.println("That pace is invalid. Please try again.");
					continue;
				}
				
				
	            long tempoPaceInSeconds = Math.addExact(Math.multiplyExact(hours, 3600L),minutes * 60L + seconds);

	            
				if (tempoPaceInSeconds <= 0 || tempoPaceInSeconds > Integer.MAX_VALUE) {
					System.out.println("The target tempo run pace must be greater than zero.");
					continue;
				}
				
				return (int) tempoPaceInSeconds;

			} catch (NumberFormatException | ArithmeticException  e) {
				System.out.println("Invalid input. Enter pace in hh:mm:ss format.");
			}
		}
	}

	
    /**
     * Reads and validates the target distance for a long run.
     *
     * @return a positive target distance in kilometres
     */
    public double readTargetDistance() {
    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter your target distance (km): ");
	        
	        String input = readInput();

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
	
	
    /**
     * Reads and validates the number of intervals.
     *
     * @return a positive number of intervals
     */
    public int readNumIntervals() {
    	
    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter number of intervals: ");
	        
	        String input = readInput();

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
	
    
    /**
     * Reads and validates the work distance for each interval.
     *
     * @return a positive interval distance in kilometres
     */
    public double readIntervalDistance() {

    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter work distance per interval (km): ");
	        
	        String input = readInput();

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

    
    /**
     * Reads and validates the recovery distance for each interval.
     *
     * @return a non-negative recovery distance in kilometres
     */
    public double readRecoveryDistance() {
    	
	    while (true) {
	    	System.out.println();
	        System.out.print("Enter recovery distance per interval (km): ");
	        
	        String input = readInput();

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
