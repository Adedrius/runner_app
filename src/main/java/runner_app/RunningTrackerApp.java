package runner_app;

import java.util.Scanner;


public class RunningTrackerApp {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		RunArchive archive = new CsvArchive();
		
		boolean appRunning = true;
		
		while (appRunning) {
			
			System.out.println("\n");
			System.out.println("----- RUNNING TRACKER -----");
			System.out.println("1. Log a New Run");
			System.out.println("2. View Run History");
			System.out.println("3. Search for a Run");
			System.out.println("4. Delete a Run");
			System.out.println("5. View Running Statistics");
			System.out.println("6. Pace Calculator (min/km)");
			System.out.println("\n");
			System.out.println("0: Exit Application");
			
			System.out.println("\n");
			System.out.println("Welcome to Running Tracker. Enter a choice between 1-6. (Enter 0 to exit): ");
			
			
			String choice = scanner.nextLine();
			
			switch (choice) {
			case "1":
				System.out.println("Log a New Run");
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
		
		scanner.close();
	}
	
}
