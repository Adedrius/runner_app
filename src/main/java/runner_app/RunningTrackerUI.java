package runner_app;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class RunningTrackerUI {

    private final Scanner scanner;

    public RunningTrackerUI(Scanner scanner) {
        this.scanner = scanner;
    }

    private void displayRun(Run run) {
        System.out.println();
        System.out.println("Run ID: " + run.getRunId());
        System.out.println();
        System.out.println("Date: " + run.getDate());
        System.out.println();
        System.out.println(run.getRunSummary());
        System.out.println();
    }
    
    
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
    
    public void showRunSaved(Run run) {

        System.out.println("\nRun successfully recorded!");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        displayRun(run);
        
    }
    
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
    
    
    public void showSearchResults(Run run) {

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        displayRun(run);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        
    }
    
    public String promptSearchRunId() {

        System.out.println();
        System.out.println("----- SEARCH BY RUN ID -----");
        System.out.println("(\"B\" to go back to Search Run Menu)");
        System.out.println();

        System.out.print("Enter Run ID: ");

        return scanner.nextLine().trim();
        
    }
    
    

    
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
    
	
	public String promptDeleteRunId() {

	    System.out.println();
	    System.out.println("----- DELETE A RUN -----");

	    System.out.print("Enter Run ID (\"B\" to return to delete menu): ");

	    return scanner.nextLine().trim();
	    
	}
	
	
	
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
	
	public String promptDeleteRunIdByDate() {

	    System.out.print("Enter the Run ID to delete (\"B\" to cancel): ");

	    return scanner.nextLine().trim();
	}
	
	
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
	

    public void showMessage(String message) {
        System.out.println(message);
    }
}
