package src.src.main.java;

/**
 * Simulate arrival and departure of cars for variable number of parking lots.
 * Print results of each parking lot containing
 *     - total cars allowed in parking lot,
 *     - total cars refused parking,
 *     - total number of hours car parked,
 *     - total amount paid by cars,
 *     - average hours a car was parked.
 *
 *  @author   - Trapti Tiwari
 */

public class Main {
    static final int[] PKG_SIZE = {50, 100, 150, 200, 250}; // Different parking size.
    static final int TOTAL_DAYS = 31;
    static final int HOURS_SIMULATION = TOTAL_DAYS * 24;

    /**
     * main method which simulate parking lot process for total number of 31 days.
     * Parking lot only accept cars between 6.00 AM to 8.59 PM .
     * Cars can depart any time when their parking hours get exhausted.
     * Simulate process of each parking lot one by one and maintain their record separately.
     *
     * @param args given from command line, not required.
     */
    public static void main(String[] args) {
        final int simulationSize = PKG_SIZE.length;
        int[] totPaid = new int[simulationSize];      // Total paid by cars for the simulation.
        int[] totIn = new int[simulationSize];        // Total number of cars accepted to enter the parking.
        int[] totRefused = new int[simulationSize];   // Total number of cars not accepted to enter the parking.
        int[] totHours = new int[simulationSize];     // Total number of hours that all cars parked.

        // Simulate parking process.
        for (int ipk = 0; ipk < simulationSize; ipk++) {
            Parking parkingLot = new Parking();
            int size = PKG_SIZE[ipk];
            int[] parking = new int[size];
            int[] time = new int[size];

            for (int hours = 0; hours < HOURS_SIMULATION; hours++) {
                int iHour = hours % 24;
                parkingLot.simulateHour (ipk, iHour, parking, time, totPaid, totIn, totRefused, totHours);
            }
        }

        // Print simulation results.
        printSimulationResult(simulationSize, totPaid, totIn, totHours, totRefused);
    }

    /**
     * Print obtained data from simulation of different parking lots.
     * @param simulationSize represents total number of parking lot simulated.
     * @param totPaid        represents total amount paid by cars for each parking lot.
     * @param totIn          represents total cars allowed in each parking lot.
     * @param totHours       represents total hours cars were parked in a parking lot.
     * @param totRefused     represents total cars refused parking for individual parking lot.
     */
    private static void printSimulationResult(int simulationSize, int[] totPaid, int[] totIn, int[] totHours, int[] totRefused) {
        System.out.println();
        System.out.println("Result of simulation.");
        System.out.printf("Duration for each parking size: %d hours (31 Days and 0 hours)", HOURS_SIMULATION);
        System.out.println();
        System.out.print("\t\t (1)\t\t (2)\t\t (3)\t\t (4)\t\t (5)\t\t (6)");
        System.out.println();


        for (int simulation = 0; simulation < simulationSize; simulation++) {
            System.out.printf(String.format("%,12d%,12d%,12d%,12d%,12d%,12.2f", PKG_SIZE[simulation],totPaid[simulation], totIn[simulation], totRefused[simulation], totHours[simulation],(totHours[simulation] * 1.0) / totIn[simulation]));
            System.out.println();
        }
        System.out.println();
        System.out.print("""
            (1) : parking size (number of slots)
            (2) : total ($) paid during simulation.
            (3) : number of cars accepted to park during simulation.
            (4) : number of cars refused to park during simulation.
            (5) : total number of hours for all cars which parked.
            (6) : average number of hours that a car parked.
        """);
    }
}