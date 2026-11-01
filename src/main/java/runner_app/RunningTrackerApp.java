package runner_app;

import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class RunningTrackerApp {
	
    private final Scanner scanner;
    private final RunService runService;
    
    private final InputHandler inputHandler;

    public RunningTrackerApp(Scanner scanner, RunArchive archive) {
        this.scanner = scanner;
        this.inputHandler = new InputHandler(scanner);
        this.runService = new RunService(archive);
    }
	
    
    
    private void newRun() {
    	
    	while (true) {
	        System.out.println();
	        System.out.println("-------- SELECT A RUN TYPE --------");
	        System.out.println("1. Easy Run");
	        System.out.println("2. Tempo Run");
	        System.out.println("3. Long Run");
	        System.out.println("4. Interval Run");
	        System.out.println();
	        System.out.println("(\"B\" to go back to Main Menu)");
	        System.out.println("-----------------------------------");
	
	        System.out.println();
	        System.out.print("Enter your choice: ");
	        String runChoice = scanner.nextLine();
	
	        switch (runChoice) {
	            case "1":
	            	System.out.println();
	                System.out.println("Easy Run selected.");
	                break;
	
	            case "2":
	            	System.out.println();
	                System.out.println("Tempo Run selected.");
	                break;
	
	            case "3":
	            	System.out.println();
	                System.out.println("Long Run selected.");
	                break;
	
	            case "4":
	            	System.out.println();
	                System.out.println("Interval Run selected.");
	                break;
	
	            case "B":
	            case "b":
	                System.out.println();
	                System.out.println("Returning to Main Menu.");
	                return;
	
	            default:
	            	System.out.println();
	                System.out.println("Invalid run type.");
	                continue;
	        }
	    	System.out.println("(\"B\" to go back)");
	        
	        double distance;
	        Duration duration;
	        Integer heartRate;
	        LocalDate date;
	        
	        Run run;
	        
	        try {
			    distance = inputHandler.readDistance();
			    duration = inputHandler.readDuration();
			    heartRate = inputHandler.readHeartRate();
			    date = inputHandler.readRunDate();
		        
		        
		        
		        int runId = runService.generateRunId();
		
		
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
		        
	        } catch (InputCancelledException e) {
	            System.out.println("Run entry cancelled. Returning to the Select Run Type Menu.");
	            continue;
	        } catch (IllegalStateException e) {
	            System.out.println("There was an error in trying to save the run: " + e.getMessage());
	            return;
	        }
	        
	        System.out.println("\nRun successfully recorded!");
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        System.out.println();
	        System.out.println("Run ID: " + run.getRunId());
	        System.out.println();
	        System.out.println(run.getRunSummary());
	        System.out.println();
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        System.out.println();
	        
	        return;
    	}
        
    }  
    
    
    private void getRunHistory() {
    	System.out.println();
    	System.out.println();
    	System.out.println("----- RUN HISTORY -----");
    	
    	List<Run> allRuns = runService.getAllRecordedRuns();
    	
    	if (allRuns.isEmpty()) {
    		System.out.println("You have no recorded runs at the moment. You can log a run from the main menu.");
    		return;
    	}
    	
    	allRuns.sort(Comparator.comparing(Run::getDate).thenComparing(Run::getRunId).reversed());
    		
    	
    	for (Run run: allRuns) {
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    		System.out.println();
    		System.out.println("Date: " + run.getDate());
    		System.out.println();
    		System.out.println("Run ID: " + run.getRunId());
    		System.out.println();
    		System.out.println(run.getRunSummary());
    		System.out.println();
    	}
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
    }
    
    
    private void searchRun() {

	    while (true) {
	        System.out.println();
	        System.out.println("-------- SEARCH FOR A RUN --------");
	        System.out.println("1. Search by Run ID");
	        System.out.println("2. Search by Date");
	        System.out.println();
	        System.out.println("(\"B\" to go back to Main Menu)");
	        System.out.println("-----------------------------------");
	        System.out.println();
	
	        System.out.print("Enter your choice: ");
	        String choice = scanner.nextLine();
	
	        switch (choice) {
	            case "1":
	                searchRunById();
	                break;
	
	            case "2":
	                searchRunByDate();
	                break;
	
	            case "B":
	            case "b":
	                System.out.println("Returning to Main Menu.");
	                return;
	
	            default:
	                System.out.println("That is an invalid search option.");
	        }
	        
	    }
    }
    
    
    private void searchRunById() {

        System.out.println();
        System.out.println("----- SEARCH BY RUN ID -----");
        
        System.out.println("(\"B\" to go back to Search Run Menu)");
        System.out.println();
        System.out.print("Enter Run ID: ");
        
        String input = scanner.nextLine().trim();

        
        if (input.equalsIgnoreCase("B")) {
            System.out.println();
            System.out.println("Returning to Search Run Menu.");
            System.out.println();
            return;
        }

        
        try {
            int runId = Integer.parseInt(input);

            if (runId <= 0) {
                System.out.println("Run ID must be a positive whole number.");
                return;
            }

            
            Optional<Run> searchResult = runService.searchRun(runId);

            if (searchResult.isPresent()) {
                Run run = searchResult.get();

                System.out.println();
                System.out.println("Run was found!");
        		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        		System.out.println();
                System.out.println("Date: " + run.getDate()); 
                System.out.println();
                System.out.println("Run ID: " + run.getRunId());
                System.out.println();
                System.out.println(run.getRunSummary());
                System.out.println();
        		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        		System.out.println();

                
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

        LocalDate date;

        try {
            date = inputHandler.readRunDate();
        } catch (InputCancelledException e) {
        	System.out.println();
            System.out.println("Returning to Search Menu.");
            System.out.println();
            return;
        }

        List<Run> matchingRuns = runService.searchRunByDate(date);

        if (matchingRuns.isEmpty()) {
            System.out.println("There were no runs were found on " + date);
            return;
        }

        matchingRuns.sort(Comparator.comparing(Run::getRunId).reversed());

        System.out.println();
        System.out.println("Found " + matchingRuns.size() + " run(s) on " + date + ":");

        for (Run run : matchingRuns) {
        	
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
    		System.out.println("Date: " + run.getDate()); 
            System.out.println();
            System.out.println("Run ID: " + run.getRunId());
            System.out.println();
            System.out.println(run.getRunSummary());
            System.out.println();
            
        }
        
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
    }
		
    
    
    
    
    
    private void deleteRun() {

	    while (true) {	
	        System.out.println();
	        System.out.println("---------- DELETE A RUN ----------");
	        
	        System.out.println("1. Delete by Run ID");
	        System.out.println("2. Delete by Date");
	        System.out.println();
	        System.out.println("(\"B\" to go back to Main Menu)");
	        System.out.println("-----------------------------------");
	        System.out.println();
	
	        System.out.print("Enter your choice: ");
	        String choice = scanner.nextLine();
	
	        switch (choice) {
	            case "1":
	                deleteRunById();
	                break;
	
	            case "2":
	                deleteRunByDate();
	                break;
	
	            case "B":
	            case "b":
	                System.out.println("Returning to Main Menu.");
	                return;
	
	            default:
	                System.out.println("Invalid deletion option.");
	        }
	    }
    }
	
    private void deleteRunById() {

        System.out.println();
        System.out.println("----- DELETE A RUN -----");

        System.out.print("Enter Run ID (\"B\" to return to delete menu): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("B")) {
            System.out.println("Returning to Delete Run Menu.");
            return;
        }

        try {
            int runId = Integer.parseInt(input);

            if (runId <= 0) {
                System.out.println("The run ID must be a positive integer.");
                return;
            }

            Optional<Run> result = runService.searchRun(runId);

            if (result.isEmpty()) {
                System.out.println("There was no run found with the ID " + runId);
                return;
            }

            Run run = result.get();

            System.out.println();
            System.out.println("Run was found!");
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    		System.out.println();
            System.out.println("Date: " + run.getDate()); 
            System.out.println();
            System.out.println("Run ID: " + run.getRunId());
            System.out.println();
            System.out.println(run.getRunSummary());
            System.out.println();
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    		System.out.println();
            
            System.out.print("Are you sure you want to delete this run? (Y/N): ");
            
            String deleteConfirmation = scanner.nextLine().trim();

            
            if (deleteConfirmation.equalsIgnoreCase("Y")) {
            	
            	try {
	                boolean isDeleted = runService.deleteRun(runId);
	
	                if (isDeleted) {
	                	System.out.println();
	                    System.out.println("Run " + runId + " was successfully deleted.");
	                } else {
	                    System.out.println("There was an error in deleting the run.");
	                }
            	} catch (IllegalStateException e) {
            	    System.out.println("There was an error in deleting the run: " + e.getMessage());
            	}

            } else {
            	System.out.println();
                System.out.println("The deletion process was cancelled.");
            }
            

        } catch (NumberFormatException e) {
            System.out.println("That input is invalid. Please enter an integer.");
        }
    }
    
    
    private void deleteRunByDate() {

        System.out.println();
        System.out.println("----- DELETE BY DATE -----");

        LocalDate date;

        try {
            date = inputHandler.readRunDate();
        } catch (InputCancelledException e) {
            System.out.println("Returning to Delete Menu.");
            return;
        }
        
        List<Run> matchingRuns = runService.searchRunByDate(date);

        if (matchingRuns.isEmpty()) {
            System.out.println("No runs found on " + date);
            return;
        }

        
        System.out.println("Found " + matchingRuns.size() + " run(s) on " + date);

        
        for (Run run : matchingRuns) {
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    		System.out.println();
            System.out.println("Run ID: " + run.getRunId());
            System.out.println(run.getRunSummary());
            System.out.println();
        }
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        

        System.out.print("Enter the Run ID to delete (\"B\" to cancel): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("B")) {
            System.out.println("Returning to Delete Run Menu.");
            return;
        }
        
        try {
            int runId = Integer.parseInt(input);

            Run selectedRun = null;

            for (Run run : matchingRuns) {
            	
                if (run.getRunId() == runId) {
                    selectedRun = run;
                    break;
                }
                
            }

            
            if (selectedRun == null) {
                System.out.println("That Run ID does not belong to the selected date.");
                return;
            }

            System.out.print("Are you sure you want to delete Run " + runId + "? (Y/N): ");

            String deleteConfirmation = scanner.nextLine().trim();

            if (deleteConfirmation.equalsIgnoreCase("Y")) {
            	
            	try {
	                boolean isDeleted = runService.deleteRun(runId);
	
	                if (isDeleted) {
	                    System.out.println("Run " + runId + " was successfully deleted!");
	                    
	                } else {
	                    System.out.println("There was an error in deleting the run.");
	                }
                } catch (IllegalStateException e) {
                    System.out.println("There was an error in deleting the run: " + e.getMessage());
                }

            } else {
                System.out.println("Deletion process was cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("That input is invalid. Please enter a whole number.");
            
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
			System.out.print("Welcome to Running Tracker. Enter a choice between 1-4. (Enter 0 to exit): ");
			
			
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
