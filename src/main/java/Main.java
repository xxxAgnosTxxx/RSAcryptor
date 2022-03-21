import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int exitCode = 0;

        while (exitCode != -1) {
            ConsoleDialog.start();
            try {
                int action = scanner.nextInt();
                ConsoleDialog.actions(action);
                exitCode = action;
            } catch (InputMismatchException e) {
                System.out.println("Mismatch error: please, enter a number of action.\n");
                scanner.next();
            }
        }
    }
}
