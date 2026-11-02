package runner_app;

import java.util.Scanner;
import java.time.Duration;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

/**
 * Main controller for the Running Tracker application.
 * Coordinates user input, menu navigation, and run management
 * using RunService, InputHandler, and RunningTrackerUI.
 */
public class RunningTrackerApp {
	
    private final RunService runService;
    private final RunningTrackerUI ui;
    private final InputHandler inputHandler;
    

    /**
     * Initializes the application with its required components.
     *
     * @param scanner the Scanner used for user input
     * @param archive the archive used to store and retrieve runs
     */
    public RunningTrackerApp(Scanner scanner, RunArchive archive) {
        this.inputHandler = new InputHandler(scanner);
        this.runService = new RunService(archive);
        this.ui = new RunningTrackerUI(scanner);
    }
	
    
    /**
     * Handles the creation of a new run based on the selected run type.
     * Collects user input, creates the appropriate Run object,
     * and saves it using RunService.
     */
    private void newRun() {
    	
    	while (true) {
    		
	
    		String runChoice = ui.showRunTypeMenu();
    		
	        switch (runChoice) {
	            case "1":
	            	ui.showMessage("");
	            	ui.showMessage("Easy Run selected.");
	                break;
	
	            case "2":
	            	ui.showMessage("");
	            	ui.showMessage("Tempo Run selected.");
	                break;
	
	            case "3":
	            	ui.showMessage("");
	            	ui.showMessage("Long Run selected.");
	                break;
	
	            case "4":
	            	ui.showMessage("");
	            	ui.showMessage("Interval Run selected.");
	                break;
	
	            case "B":
	            case "b":
	            	ui.showMessage("");
	                ui.showMessage("Returning to Main Menu.");
	                return;
	
	            default:
	            	ui.showMessage("");
	            	ui.showMessage("Invalid run type.");
	                continue;
	        }
	        ui.showMessage("(\"B\" to go back)");
	        
	        double distance;
	        Duration duration;
	        Integer heartRate;
	        LocalDate date;
	        
	        Run run;
	        
	        try {
	        	
	        	if (!runChoice.equals("4")) {
	        	    distance = inputHandler.readDistance();
	        	    duration = inputHandler.readDuration();    
	        	} else {
	        	    distance = 0;
	        	    duration = Duration.ZERO;
	        	}

	        	
	        	if (!runChoice.equals("4")) {
	        	    heartRate = inputHandler.readHeartRate();
	        	    date = inputHandler.readRunDate();        	    
	        	} else {
	        	    heartRate = null;
	        	    date = null;	        	    
	        	}
		        
		        
		        
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
		            	
		            		
		            	int numIntervals = inputHandler.readNumIntervals();
		            	
		
		            	Duration intervalDuration = inputHandler.readDuration("Enter the work duration per interval (hh:mm:ss): ");

		            	Duration recoveryDuration = inputHandler.readDuration("Enter the recovery duration per interval (hh:mm:ss): ");
		            	
		            	
		            	double intervalDistance = inputHandler.readIntervalDistance();
		            	double recoveryDistance = inputHandler.readRecoveryDistance();
		            		
		            	heartRate = inputHandler.readHeartRate();
		            	date = inputHandler.readRunDate();
		            		
		            	run = new IntervalRun(runId, heartRate, date, numIntervals, intervalDuration, recoveryDuration, intervalDistance, recoveryDistance);
		                
		            	break;
		                

		            default:
		            	ui.showMessage("This run type doesn't exist.");
		            	return;
		        }
		
		        runService.saveRun(run);
		        
	        } catch (InputCancelledException e) {
	        	ui.showMessage("Run entry cancelled. Returning to the Select Run Type Menu.");
	            continue;
	            
	        } catch (IllegalArgumentException e) {
	        	ui.showMessage("That run data is invalid: " + e.getMessage() + "\nPlease try logging your run again.");
	            continue;
	            
	        } catch (IllegalStateException e) {
	        	ui.showMessage("There was an error in trying to save the run: " + e.getMessage());
	            return;
	        }
	        
	        ui.showRunSaved(run);
	        
	        return;
    	}
        
    }  
    
    
    /**
     * Retrieves all recorded runs, sorts them by date and run ID
     * in descending order, and displays the run history.
     */
    private void getRunHistory() {
        List<Run> allRuns = runService.getAllRecordedRuns();
        allRuns.sort(Comparator.comparing(Run::getDate).thenComparing(Run::getRunId).reversed());
        
        ui.showRunHistory(allRuns);
    }
    
    
    /**
     * Displays the search menu and allows users to search
     * for recorded runs by ID or date.
     */
    private void searchRun() {

        while (true) {

            String choice = ui.showSearchMenu();

            switch (choice) {

                case "1":
                    searchRunById();
                    break;

                case "2":
                    searchRunByDate();
                    break;

                case "B":
                case "b":
                    ui.showMessage("Returning to Main Menu.");
                    return;

                default:
                    ui.showMessage("That is an invalid search option.");
            }
        }
    }
    
    
    /**
     * Searches for a recorded run using its unique run ID.
     * Validates the input and displays the run if found.
     */
    private void searchRunById() {

    	while (true) {
	    	String input = ui.promptSearchRunId();
	
	        
	        if (input.equalsIgnoreCase("B")) {
	        	ui.showMessage("");
	        	ui.showMessage("Returning to Search Run Menu.");
	            ui.showMessage("");
	            return;
	        }
	
	        
	        try {
	            int runId = Integer.parseInt(input);
	
	            if (runId <= 0) {
	                ui.showMessage("Run ID must be a positive whole number.");
	                continue;
	            }
	
	            
	            Optional<Run> searchResult = runService.searchRun(runId);
	
	            if (searchResult.isPresent()) {
	            	
	                Run run = searchResult.get();
	
	                ui.showMessage("\nRun was found!");
	                ui.showMessage("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	                ui.showSearchResults(run);
	                
	            } else {
	                ui.showMessage("There was No run found with the ID " + runId + ". Try again or try searching by date in the search type menu.");
	            }
	            
	            return;
	            
	        } catch (NumberFormatException e) {
	        	ui.showMessage("This input is invalid. Please enter a whole number.");
	        }
    	}
    }

    
    /**
     * Searches for all recorded runs on a specified date.
     * Displays matching runs in descending order by run ID.
     */
    private void searchRunByDate() {

    	ui.showMessage("");
    	ui.showMessage("----- SEARCH BY DATE -----");

        LocalDate date;

        try {
            date = inputHandler.readRunDate();
        } catch (InputCancelledException e) {
        	ui.showMessage("");
        	ui.showMessage("Returning to Search Menu.");
        	ui.showMessage("");
            return;
        }

        List<Run> matchingRuns = runService.searchRunByDate(date);

        if (matchingRuns.isEmpty()) {
        	ui.showMessage("");
        	ui.showMessage("There were no runs were found on " + date + ".");
            return;
        }

        matchingRuns.sort(Comparator.comparing(Run::getRunId).reversed());

        ui.showMessage("Found " + matchingRuns.size() + " run(s) on " + date + ":");
        ui.showMessage("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        for (Run run : matchingRuns) {
        	ui.showSearchResults(run);     
        }
        

    }
		
    
    
    /**
     * Displays the deletion menu and allows users to
     * delete a recorded run by ID or date.
     */
    private void deleteRun() {

        while (true) {

            String choice = ui.showDeleteMenu();

            switch (choice) {

                case "1":
                    deleteRunById();
                    break;

                case "2":
                    deleteRunByDate();
                    break;

                case "B":
                case "b":
                    ui.showMessage("Returning to Main Menu.");
                    return;

                default:
                    ui.showMessage("Invalid deletion option.");
            }
        }
        
    }
    
    
    /**
     * Searches for a run by its ID and requests confirmation
     * before permanently deleting the selected run.
     */
    private void deleteRunById() {
    	
    	while (true) {

	        String input = ui.promptDeleteRunId();
	
	        if (input.equalsIgnoreCase("B")) {
	            ui.showMessage("Returning to Delete Run Menu.");
	            return;
	        }
	
	        
	        try {
	            int runId = Integer.parseInt(input);
	
	            if (runId <= 0) {
	                ui.showMessage("The run ID must be a positive integer.");
	                continue;
	            }
	
	            
	            Optional<Run> result = runService.searchRun(runId);
	
	            
	            if (result.isEmpty()) {
	            	ui.showMessage("");
	                ui.showMessage("There was no run found with the ID " + runId + ".");
	                return;
	            }
	
	            Run run = result.get();
	
	            ui.showMessage("\nRun was found!");
	            ui.showMessage("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	
	            ui.showSearchResults(run);
	
	
	            if (ui.confirmDeleteRun(runId)) {
	
	                try {
	                    boolean isDeleted = runService.deleteRun(runId);
	
	                    if (isDeleted) {
	                        ui.showMessage("\nRun " + runId + " was successfully deleted.");
	                    } else {
	                        ui.showMessage("There was an error in deleting the run.");
	                    }
	
	                } catch (IllegalStateException e) {
	                    ui.showMessage("There was an error in deleting the run: " + e.getMessage());
	                }
	
	            } else {
	                ui.showMessage("\nThe deletion process was cancelled.");
	            }
	            
	            return;
	            
	        } catch (NumberFormatException e) {
	            ui.showMessage("That input is invalid. Please enter an integer.");
	        }
    	}
        
    }
    
    
    /**
     * Retrieves runs recorded on a specified date and allows
     * the user to select and delete one after confirmation.
     */
    private void deleteRunByDate() {

        ui.showMessage("\n----- DELETE BY DATE -----");

        LocalDate date;

        try {        	
            date = inputHandler.readRunDate();
        } catch (InputCancelledException e) {
            ui.showMessage("Returning to Delete Menu.");
            return;
        }

        List<Run> matchingRuns = runService.searchRunByDate(date);

        if (matchingRuns.isEmpty()) {
        	ui.showMessage("");
            ui.showMessage("No runs found on " + date + ".");
            return;
        }

        ui.showDeleteRunsByDate(matchingRuns, date);
        
        while (true) {

	        String input = ui.promptDeleteRunIdByDate();
	
	        if (input.equalsIgnoreCase("B")) {
	            ui.showMessage("Returning to Delete Run Menu.");
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
	                ui.showMessage("That Run ID does not belong to the selected date.");
	                continue;
	            }
	
	            
	            if (ui.confirmDeleteRun(runId)) {
	
	                try {
	                    boolean isDeleted = runService.deleteRun(runId);
	
	                    if (isDeleted) {
	                    	ui.showMessage("");
	                        ui.showMessage("Run " + runId + " was successfully deleted!");
	                    } else {
	                    	ui.showMessage("");
	                        ui.showMessage("There was an error in deleting the run.");
	                    }               
	                } catch (IllegalStateException e) {
	                	ui.showMessage("");
	                    ui.showMessage("There was an error in deleting the run: " + e.getMessage());
	                }
	
	            } else {
	            	ui.showMessage("");
	                ui.showMessage("Deletion process was cancelled.");
	            }
	            
	            return;
	
	        } catch (NumberFormatException e) {
	            ui.showMessage("That input is invalid. Please enter a whole number.");
	        }
        }
    }
    
	
    /**
     * Starts the application and displays the main menu.
     * Processes user selections until the user chooses to exit.
     */
	public void start() {
		
		boolean appRunning = true;
		
		while (appRunning) {
			
			String choice = ui.showMainMenu();
			
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
				ui.showMessage("Goodbye!");
				break;

			default:
				ui.showMessage("");
				ui.showMessage("That is an invalid choice. Review the list of choices, and please try again.");
			}
		}
		
	}
	
	
	/**
	 * Entry point of the Running Tracker application.
	 * Initializes the required components, starts the application,
	 * and closes the Scanner when the application exits.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		RunArchive archive = new CsvArchive();
	    RunningTrackerApp app = new RunningTrackerApp(scanner, archive);

	    app.start();

	    scanner.close();
	    
	}
	
}
