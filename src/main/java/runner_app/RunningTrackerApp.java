package runner_app;

import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class RunningTrackerApp {
	
    private final Scanner scanner;
    private final RunArchive archive;

    public RunningTrackerApp(Scanner scanner, RunArchive archive) {
        this.scanner = scanner;
        this.archive = archive;
    }
	
    private void newRun() {

        System.out.println();
        System.out.println("----- SELECT RUN TYPE -----");
        System.out.println("1. Easy Run");
        System.out.println("2. Tempo Run");
        System.out.println("3. Long Run");
        System.out.println("4. Interval Run");
        System.out.println("0. Return to Main Menu");

        System.out.print("Enter your choice: ");
        String runChoice = scanner.nextLine();

        switch (runChoice) {
            case "1":
                System.out.println("Easy Run selected.");
                break;

            case "2":
                System.out.println("Tempo Run selected.");
                break;

            case "3":
                System.out.println("Long Run selected.");
                break;

            case "4":
                System.out.println("Interval Run selected.");
                break;

            case "0":
                System.out.println("Returning to Main Menu.");
                return;

            default:
                System.out.println("Invalid run type.");
                return;
        }

        double distance = readDistance();
        Duration duration = readDuration();
        Integer heartRate = readHeartRate();
        LocalDate date = readRunDate();
        
        
        
        int runId = generateRunId();

        Run run;

        switch (runChoice) {
            case "1":
                int effort = readEffort();
                run = new EasyRun(runId, distance, duration, heartRate, date, effort);
                break;
                
            case "2":
                int tempoPaceInSeconds = readTempoPace();
                run = new TempoRun(runId, distance, duration, heartRate, date, tempoPaceInSeconds);
                break;
            
            case "3":
                double targetDistance = readTargetDistance();
                run = new LongRun(runId, distance, duration, heartRate, date, targetDistance);
                break;
            case "4":
            	while(true) {
            		
            		int numIntervals = readNumIntervals();

            		System.out.println("Enter the work duration per interval:");
            		Duration intervalDuration = readDuration();

            		System.out.println("Enter the recovery duration per interval:");
            		Duration recoveryDuration = readDuration();

            		double intervalDistance = readIntervalDistance();
            		double recoveryDistance = readRecoveryDistance();

            		Duration totalIntervalDuration = intervalDuration.plus(recoveryDuration).multipliedBy(numIntervals);

            		double totalIntervalDistance = (intervalDistance + recoveryDistance) * numIntervals;

            		if (!totalIntervalDuration.equals(duration)) {
            			System.out.println("This input is invalid. The duration of the interval work and recovery together must equal the total run duration.");
            			continue;
            		}

            		if (Math.abs(totalIntervalDistance - distance) > 0.0001) {
            			System.out.println("This input is invalid. The distance of the interval work and recovery together must equal the total run distance.");
            			continue;
            		}

            		run = new IntervalRun(runId, distance, duration, heartRate, date, numIntervals, intervalDuration, recoveryDuration, intervalDistance, recoveryDistance);
                
            		break;
                
            	}
            	
            	break;

            	default:
            		System.out.println("This run type is not implemented yet.");
            		return;
        	}

        archive.addRun(run);

        System.out.println("\nRun successfully recorded!");
        System.out.println("Run ID: " + run.getRunId());
        System.out.println(run.getRunSummary());
        
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
			System.out.print("Enter your target tempo pace (mm:ss per km): ");
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
	
	
	
	private int generateRunId() {
	    return archive.getAllRecordedRuns().stream().mapToInt(Run::getRunId).max().orElse(0) + 1;
	}
	
	
	public void start() {
		boolean appRunning = true;
		
		while (appRunning) {
			
			System.out.println();
			System.out.println("----- RUNNING TRACKER -----");
			System.out.println("1. Log a New Run");
			System.out.println("2. View Run History");
			System.out.println("3. Search for a Run");
			System.out.println("4. Delete a Run");
			System.out.println("5. View Running Statistics");
			System.out.println("6. Pace Calculator (min/km)");
			System.out.println("\n");
			System.out.println("0: Exit Application");
			
			System.out.println();
			System.out.println("Welcome to Running Tracker. Enter a choice between 1-6. (Enter 0 to exit): ");
			
			
			String choice = scanner.nextLine();
			
		switch (choice) {
			case "1":
				newRun();
				break;
				
			case "2":
				System.out.println("View Run History");
				break;
				
			case "3":
				System.out.println("Search for a Run");
				break;
				
			case "4":
				System.out.println("Delete a Run");
				break;
				
			case "5":
				System.out.println("View Running Statistics");
				break;
				
			case "6":
				System.out.println("Pace Calculator (min/km)");
				break;
				
			case "0":
				appRunning = false;
				System.out.println("Goodbye!");
				break;

			default:
				System.out.println("That is an invalid choice. Review the list of choices, and please try again.");
			}
		}
		
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		RunArchive archive = new CsvArchive();
		
	    RunningTrackerApp app = new RunningTrackerApp(scanner, archive);

	    app.start();

	    scanner.close();
	}
	
}
