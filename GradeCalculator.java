import java.util.Scanner;

public class GradeCalculator {

    public static String calculateGrade(double average) {
        if (average >= 90) return "A+ (Outstanding)";
        else if (average >= 80) return "A  (Excellent)";
        else if (average >= 70) return "B  (Very Good)";
        else if (average >= 60) return "C  (Good)";
        else if (average >= 50) return "D  (Average)";
        else if (average >= 40) return "E  (Pass)";
        else                    return "F  (Fail)";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("       STUDENT GRADE CALCULATOR          ");
        System.out.println("=========================================");

        System.out.print("Enter student name       : ");
        String name = scanner.nextLine();

        System.out.print("Enter number of subjects : ");
        int numSubjects = scanner.nextInt();

        if (numSubjects <= 0) {
            System.out.println("Number of subjects must be greater than 0.");
            scanner.close();
            return;
        }

        String[] subjects = new String[numSubjects];
        double[] marks    = new double[numSubjects];
        double   total    = 0;

        System.out.println("\nEnter marks (out of 100) for each subject:");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < numSubjects; i++) {
            System.out.print("Subject " + (i + 1) + " name  : ");
            subjects[i] = scanner.next();
            scanner.nextLine();          // consume leftover newline

            System.out.print("Marks obtained : ");
            double mark = scanner.nextDouble();
            scanner.nextLine();

            // Validate mark range
            while (mark < 0 || mark > 100) {
                System.out.println("  [!] Invalid! Please enter a value between 0 and 100.");
                System.out.print("Marks obtained : ");
                mark = scanner.nextDouble();
                scanner.nextLine();
            }

            marks[i] = mark;
            total   += mark;
        }

        double average = total / numSubjects;
        String grade   = calculateGrade(average);

        // ── Results ──────────────────────────────────────────────
        System.out.println("\n=========================================");
        System.out.println("             RESULT CARD                 ");
        System.out.println("=========================================");
        System.out.printf("Student Name       : %s%n", name);
        System.out.println("-----------------------------------------");
        System.out.printf("%-20s %10s%n", "Subject", "Marks");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < numSubjects; i++) {
            System.out.printf("%-20s %10.1f%n", subjects[i], marks[i]);
        }

        System.out.println("-----------------------------------------");
        System.out.printf("%-20s %10.1f%n",   "Total Marks",       total);
        System.out.printf("%-20s %9.2f%%%n",  "Average Percentage", average);
        System.out.printf("%-20s %10s%n",     "Grade",              grade);
        System.out.println("=========================================");

        // Pass / Fail note
        boolean passed = average >= 40;
        System.out.println(passed
            ? "  Result: PASS - Congratulations, " + name + "!"
            : "  Result: FAIL - Better luck next time, " + name + ".");
        System.out.println("=========================================");

        scanner.close();
    }
}