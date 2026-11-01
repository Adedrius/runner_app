package runner_app;

import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class RunningTrackerApp {
	
    private final Scanner scanner;
    private final RunArchive archive;
    private final RunService runService;
    
    private final InputHandler inputHandler;

    public RunningTrackerApp(Scanner scanner, RunArchive archive) {
        this.scanner = scanner;
        this.archive = archive;
        this.inputHandler = new InputHandler(scanner);
        this.runService = new RunService(archive);
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

        double distance = inputHandler.readDistance();
        Duration duration = inputHandler.readDuration();
        Integer heartRate = inputHandler.readHeartRate();
        LocalDate date = inputHandler.readRunDate();
        
        
        
        int runId = runService.generateRunId();

        Run run;

        switch (runChoice) {
            case "1":
                int effort = inputHandler.readEffort();
                run = new EasyRun(runId, distance, duration, heartRate, date, effort);
                break;
                
            case "2":
                int tempoPaceInSeconds = inputHandler.readTempoPace();
                run = new TempoRun(runId, distance, duration, heartRate, date, tempoPaceInSeconds);
                break;
            
            case "3":
                double targetDistance = inputHandler.readTargetDistance();
                run = new LongRun(runId, distance, duration, heartRate, date, targetDistance);
                break;
            case "4":
            	while(true) {
            		
            		int numIntervals = inputHandler.readNumIntervals();

            		System.out.println("Enter the work duration per interval:");
            		Duration intervalDuration = inputHandler.readDuration();

            		System.out.println("Enter the recovery duration per interval:");
            		Duration recoveryDuration = inputHandler.readDuration();

            		double intervalDistance = inputHandler.readIntervalDistance();
            		double recoveryDistance = inputHandler.readRecoveryDistance();

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

        runService.saveRun(run);

        System.out.println("\nRun successfully recorded!");
        System.out.println("Run ID: " + run.getRunId());
        System.out.println(run.getRunSummary());
        
    }  
    
    
    private void getRunHistory() {
    	System.out.println();
    	System.out.println("----- RUN HISTORY -----");
    	
    	List<Run> allRuns = runService.getAllRecordedRuns();
    	
    	if (allRuns.isEmpty()) {
    		System.out.println("You have no recorded runs at the moment. You can log a run from the main menu.");
    		return;
    	}
    	
    	allRuns.sort(Comparator.comparing(Run::getDate).thenComparing(Run::getRunId).reversed());
    		
    	
    	for (Run run: allRuns) {
    		System.out.println("-------------------------------------------------------------------------------------------------------");
    		System.out.println();
    		System.out.println("Date: " + run.getDate());
    		System.out.println();
    		System.out.println("Run ID: " + run.getRunId());
    		System.out.println();
    		System.out.println(run.getRunSummary());
    		System.out.println("-------------------------------------------------------------------------------------------------------");
    	}
    }
    
    
    private void searchRun() {

        System.out.println();
        System.out.println("----- SEARCH FOR A RUN -----");
        System.out.println("1. Search by Run ID");
        System.out.println("2. Search by Date");
        System.out.println("0. Return to Main Menu");

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                searchRunById();
                break;

            case "2":
                searchRunByDate();
                break;

            case "0":
                System.out.println("Returning to Main Menu.");
                break;

            default:
                System.out.println("That is an invalid search option.");
        }
        
    }
    
    
    private void searchRunById() {

        System.out.println();
        System.out.println("----- SEARCH BY RUN ID -----");

        System.out.print("Enter Run ID (0 to return to Search Run Menu): ");
        
        String input = scanner.nextLine().trim();

        try {
            int runId = Integer.parseInt(input);

            if (runId == 0) {
                System.out.println("Returning to Main Menu.");
                return;
            }

            if (runId < 0) {
                System.out.println("Run ID must be a positive whole number.");
                return;
            }

            
            Optional<Run> searchResult = runService.searchRun(runId);

            if (searchResult.isPresent()) {
                Run run = searchResult.get();

                System.out.println("Run was found!");
        		System.out.println("-------------------------------------------------------------------------------------------------------");
                System.out.println("Date: " + run.getDate()); 
                System.out.println();
                System.out.println("Run ID: " + run.getRunId());
                System.out.println();
                System.out.println(run.getRunSummary());

                
            } else {
                System.out.println("There was No run found with the ID " + runId + ". Try again or try searching by date in the search type menu.");
            }

        } catch (NumberFormatException e) {
            System.out.println("This input is invalid. Please enter a whole number.");
        }
    }

    
    private void searchRunByDate() {

        System.out.println();
        System.out.println("----- SEARCH BY DATE -----");

        LocalDate date = inputHandler.readRunDate();

        List<Run> matchingRuns = runService.searchRunByDate(date);

        if (matchingRuns.isEmpty()) {
            System.out.println("There were no runs were found on " + date);
            return;
        }

        matchingRuns.sort(Comparator.comparing(Run::getRunId).reversed());

        System.out.println("Found " + matchingRuns.size() + " run(s) on " + date);

        for (Run run : matchingRuns) {
        	
            System.out.println("Run was found!");
    		System.out.println("-------------------------------------------------------------------------------------------------------");
            System.out.println("Date: " + run.getDate()); 
            System.out.println();
            System.out.println("Run ID: " + run.getRunId());
            System.out.println();
            System.out.println(run.getRunSummary());
            
        }
    }
		
    
    private void deleteRun() {

        System.out.println();
        System.out.println("----- DELETE A RUN -----");
        System.out.println("1. Delete by Run ID");
        System.out.println("2. Delete by Date");
        System.out.println("0. Return to Main Menu");

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                deleteRunById();
                break;

            case "2":
                break;

            case "0":
                System.out.println("Returning to Main Menu.");
                break;

            default:
                System.out.println("Invalid deletion option.");
        }
    }
	
    private void deleteRunById() {

        System.out.println();
        System.out.println("----- DELETE A RUN -----");

        System.out.print("Enter Run ID (0 to return to Main Menu): ");
        String input = scanner.nextLine().trim();

        try {
            int runId = Integer.parseInt(input);

            if (runId == 0) {
                System.out.println("Returning to Main Menu.");
                return;
            }

            if (runId < 0) {
                System.out.println("The run ID must be a positive integer.");
                return;
            }

            Optional<Run> result = runService.searchRun(runId);

            if (result.isEmpty()) {
                System.out.println("There was no run found with the ID " + runId);
                return;
            }

            Run run = result.get();

            System.out.println("Run was found!");
    		System.out.println("-------------------------------------------------------------------------------------------------------");
            System.out.println("Date: " + run.getDate()); 
            System.out.println();
            System.out.println("Run ID: " + run.getRunId());
            System.out.println();
            System.out.println(run.getRunSummary());

            
            System.out.print("Are you sure you want to delete this run? (Y/N): ");
            
            String deleteConfirmation = scanner.nextLine().trim();

            
            if (deleteConfirmation.equalsIgnoreCase("Y")) {
            	
                boolean isDeleted = runService.deleteRun(runId);

                if (isDeleted) {
                    System.out.println("Run " + runId + " was successfully deleted.");
                } else {
                    System.out.println("Unable to delete the run.");
                }

            } else {
                System.out.println("Deletion process was cancelled.");
            }
            

        } catch (NumberFormatException e) {
            System.out.println("That input is invalid. Please enter an integer.");
        }
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
			System.out.println("\n");
			System.out.println("0: Exit Application");
			
			System.out.println();
			System.out.println("Welcome to Running Tracker. Enter a choice between 1-4. (Enter 0 to exit): ");
			
			
			String choice = scanner.nextLine();
			
		switch (choice) {
			case "1":
				newRun();
				break;
				
			case "2":
				getRunHistory();
				break;
				
			case "3":
				searchRun();
				break;
				
			case "4":
				deleteRun();
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
