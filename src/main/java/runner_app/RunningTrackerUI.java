package runner_app;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

/**
 * Handles the console-based user interface for the Running Tracker application.
 * Displays menus, run information, and messages while collecting user selections.
 */
public class RunningTrackerUI {

    private final Scanner scanner;

    /**
     * Initializes the user interface with a Scanner.
     *
     * @param scanner the Scanner used to read user input
     */
    public RunningTrackerUI(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the details and summary of a recorded run.
     *
     * @param run the run to display
     */
    private void displayRun(Run run) {
        System.out.println();
        System.out.println("Run ID: " + run.getRunId());
        System.out.println();
        System.out.println("Date: " + run.getDate());
        System.out.println();
        System.out.println(run.getRunSummary());
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    
    /**
     * Displays the main menu and reads the user's selection.
     *
     * @return the user's menu choice
     */
    public String showMainMenu() {

        System.out.println();
        System.out.println("----- RUNNING TRACKER -----");
        System.out.println("1. Log a New Run");
        System.out.println("2. View Run History");
        System.out.println("3. Search for a Run");
        System.out.println("4. Delete a Run");
        System.out.println();
        System.out.println("0: Exit Application");
        System.out.println();

        
        System.out.print(
            "Welcome to Running Tracker. Enter a choice between 1-4. (Enter 0 to exit): "
        );

        
        return scanner.nextLine();
        
    }
    
    /**
     * Displays the available run types and reads the user's selection.
     *
     * @return the selected run type option
     */
    public String showRunTypeMenu() {

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

        return scanner.nextLine();
        
    }
    
    /**
     * Displays a confirmation message and the newly saved run.
     *
     * @param run the run that was saved
     */
    public void showRunSaved(Run run) {

        System.out.println("\nRun successfully recorded!");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        displayRun(run);
        
    }
    
    /**
     * Displays all recorded runs or a message if no runs exist.
     *
     * @param allRuns the list of recorded runs
     */
    public void showRunHistory(List<Run> allRuns) {

        System.out.println();
        System.out.println();
        System.out.println("----- RUN HISTORY -----");

        if (allRuns.isEmpty()) {
            System.out.println("You have no recorded runs at the moment. You can log a run from the main menu.");
            return;
        }

        for (Run run : allRuns) {

            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            displayRun(run);
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        
    }
    
    /**
     * Displays the search menu and reads the user's selection.
     *
     * @return the selected search option
     */
    public String showSearchMenu() {

        System.out.println();
        System.out.println("-------- SEARCH FOR A RUN --------");
        System.out.println("1. Search by Run ID");
        System.out.println("2. Search by Date");
        System.out.println();
        System.out.println("(\"B\" to go back to Main Menu)");
        System.out.println("-----------------------------------");
        System.out.println();

        System.out.print("Enter your choice: ");

        return scanner.nextLine();
        
    }
    
    
    
    /**
     * Displays the details of a run found during a search.
     *
     * @param run the matching run
     */
    public void showSearchResults(Run run) {


        displayRun(run);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        
    }
    
    /**
     * Prompts the user to enter a run ID for searching.
     *
     * @return the entered run ID as a String
     */
    public String promptSearchRunId() {

        System.out.println();
        System.out.println("----- SEARCH BY RUN ID -----");
        System.out.println("(\"B\" to go back to Search Run Menu)");
        System.out.println();

        System.out.print("Enter Run ID: ");

        return scanner.nextLine().trim();
        
    }
    
    

    /**
     * Displays the deletion menu and reads the user's selection.
     *
     * @return the selected deletion option
     */
	public String showDeleteMenu() {
	
	    System.out.println();
	    System.out.println("---------- DELETE A RUN ----------");
	    System.out.println("1. Delete by Run ID");
	    System.out.println("2. Delete by Date");
	    System.out.println();
	    System.out.println("(\"B\" to go back to Main Menu)");
	    System.out.println("-----------------------------------");
	    System.out.println();
	
	    System.out.print("Enter your choice: ");
	
	    return scanner.nextLine();
	    
	}
    
	
	/**
	 * Prompts the user to enter a run ID for deletion.
	 *
	 * @return the entered run ID as a String
	 */
	public String promptDeleteRunId() {

	    System.out.println();
	    System.out.println("----- DELETE A RUN -----");

	    System.out.print("Enter Run ID (\"B\" to return to delete menu): ");

	    return scanner.nextLine().trim();
	    
	}
	
	
	/**
	 * Displays all recorded runs matching a specified date.
	 *
	 * @param matchingRuns the list of runs recorded on the date
	 * @param date the selected run date
	 */
	public void showDeleteRunsByDate(List<Run> matchingRuns, LocalDate date) {

	    System.out.println("Found " + matchingRuns.size() + " run(s) on " + date);

	    for (Run run : matchingRuns) {
	        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        System.out.println();
	        System.out.println("Run ID: " + run.getRunId());
	        System.out.println(run.getRunSummary());
	        System.out.println();
	    }

	    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	
	}
	
	/**
	 * Prompts the user to select a run ID from the date search results.
	 *
	 * @return the entered run ID as a String
	 */
	public String promptDeleteRunIdByDate() {

	    System.out.print("Enter the Run ID to delete (\"B\" to cancel): ");

	    return scanner.nextLine().trim();
	}
	
	/**
	 * Requests user confirmation before deleting a run.
	 *
	 * @param runId the ID of the run to delete
	 * @return true if confirmed, false if cancelled
	 */
	public boolean confirmDeleteRun(int runId) { 
		
	    while (true) {
	        System.out.print("Are you sure you want to delete Run " + runId + "? (Y/N): ");

	        String answer = scanner.nextLine().trim();

	        if (answer.equalsIgnoreCase("Y")) {
	            return true;
	        }

	        if (answer.equalsIgnoreCase("N")) {
	            return false;
	        }

	        showMessage("That's an invalid choice. Please enter Y or N.");
	    }
	    
	}
	
	/**
	 * Displays a message in the console.
	 *
	 * @param message the message to display
	 */
    public void showMessage(String message) {
        System.out.println(message);
    }
}
