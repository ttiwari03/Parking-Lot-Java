package src.src.main.java;

/**
 *  Simulating process of parking by allowing and departing cars.
 */
class Parking {

    static final int HOURLY_COST = 3;
    static final int DAILY_COST = 20;
    static final int HOUR_MAX = 72;
    static final long MODULUS = 2147483647;
    static final long SEED = 314159;
    static int rndNb = (int) SEED;

    /**
     * Return a random number.
     * @param rnd       represents arbitrary integer number.
     * @return integer  represents random number between ZERO (Included) and MODULUS (Excluded).
     */
    protected static int getRandom(int rnd) {
        long multiplier = 16807;
        return (int) ((multiplier * rnd) % MODULUS);
    }

    /**
     * Find value in a given range of 0 (Included) and numOfParts (Excluded) based on generated random number.
     * .
     * @param numOfParts    represents range in which value in needed (excluding last value).
     * @return randomNum    represents a random number in given range.
     *
     * Example - If value is needed between 0 and 2 i.e. 0 or 1 or 2, numOfParts should be 3.
     */
    private static int valueInRange(int numOfParts) {
        int randomNum = 0;
        int range = 0;
        while (true) {
            range += (int) (MODULUS / numOfParts);
            if (rndNb < range) {
                break;
            }
            randomNum++;
        }
        return randomNum;
    }

    /**
     * Simulate working of given parking lot in particular given hour.
     * Store data obtained from simulation for given hour.
     * Parking lot accept cars by rules
     *       - between 6.00 AM to 5.59 PM with equal probability of 5 to 9 cars per hour.
     *       - between 6.00 PM to 8.59 PM with equal probability of 1 to 2 cars per hour.
     * Cars can depart when they exhaust their parking hours.
     *
     * @param ipk       number representing which parking lot is being simulated. (Starts from 0)
     * @param iHour     number representing particular hour for a day between 0 and 23 (both included).
     * @param parking   array representing individual cars parked by their parking hours in particular parking lot.
     * @param time      array representing remaining time for individual cars parked in particular parking lot.
     * @param totPaid   store total amount paid by cars departed from a particular parking lot. Indexed by parking lot being simulated.
     * @param totIn     store total cars allowed in a particular parking lot. Indexed by parking lot being simulated.
     * @param totRefused store total cars refused in a particular parking lot. Indexed by parking lot being simulated.
     * @param totHours  store summation of parking hours of all cars for a particular parking lot. Indexed by parking lot being simulated.
     */
    protected void simulateHour(int ipk, int iHour, int[] parking, int[] time, int[] totPaid, int[] totIn, int[] totRefused, int[] totHours) {
        // Depart cars who exhausted their parking time for given hour.
        departCars(ipk, parking, time, totHours, totPaid);

        // For 6.00 AM to 8.59 PM accept cars to be parked.
        if (iHour >= 6 && iHour <= 20) {
            rndNb = getRandom(rndNb);
            int numOfCars;
            // If given hour is between 6.00 AM and 5.59 PM, 5 to 9 cars can arrive per hour.
            if (iHour < 18) {
                numOfCars =  5 + valueInRange(5);
            } else {
                // If given hour is between 6.00 PM ad 8.59 PM, 1 to 2 cars can arrive per hour.
                numOfCars =  1 + valueInRange(2);
            }

            // Accept cars arrived for parking depending on space available in parking lot.
            for (int num = 0; num < numOfCars; num++) {
                if (!parkCars(ipk, parking, time, totIn, totRefused)) {
                    break;
                }
            }
        }
    }

    /**
     * Simulate process of car departing
     *          - reduce parking hours by one hour for all parked car.
     *          - if a car's parking hours are exhausted, calculate parking cost and depart the car, making the space available.
     *
     * @param ipk       represent number of parking lot.
     * @param parking   array representing all parked cars in given parking lot by their parking hours. (0 means space available)
     * @param time      array representing remaining time for each parked car. (0 means car departed)
     * @param totHours  store total parking hours (of all cars) for a parking lot, calculated when cars depart.
     * @param totPaid   store total amount paid by all cars in a parking lot, calculated when cars depart.
     */
    private static void departCars(int ipk, int[] parking, int[] time, int[] totHours, int[] totPaid) {

        for (int ind = 0; ind < time.length; ind++) {
            // Reduce parking time by one hour, if remaining time is more than 1 hour.
            if (time[ind] > 1) {
                time[ind] -= 1;
            }

            // If remaining time is only 1 hour calculate parking cost and depart car, making their space available.
            if (time[ind] == 1) {
                totHours[ipk] += parking[ind];
                totPaid[ipk] += calculateCost(parking[ind]);
                time[ind] = 0;
                parking[ind] = 0;
            }
        }
    }

    /**
     * Calculate cost of parking a car for given number of hours.
     * Maximum cost for a day (24 hours) is 20 (constant DAILY_COST).
     * Per hour cost is 3 (constant HOURLY_COST).
     *
     * @param hours     representing number of parking hours.
     * @return cost     representing calculated cost of parking.
     */
    private static int calculateCost(int hours) {
        int cost = 0;

        while (hours > 24) {
            cost += DAILY_COST;
            hours -= 24;
        }

        if (hours > 6) {
            cost += DAILY_COST;
        } else {
            cost += (hours * HOURLY_COST);
        }
        return cost;
    }

    /**
     * Simulate process of parking a car in given parking lot.
     * If space available in parking lot,
     *          - Calculate random parking hours for a car. (Between 1 and 72(constant HOUR_MAX) )
     *          - Park the car.
     *          - Increase number of cars allowed in a parking lot.
     * Else increase number of refused cars in parking lot.
     *
     * @param ipk           represent number of given parking lot.
     * @param parking       array represents individual parked car in parking lot.
     * @param time          array represents remaining time of all car parked.
     * @param totIn         represents total cars allowed in given parking lot.
     * @param totRefused    represents total cars refused in given parking lot.
     * @return              a boolean value representing true if car is parked and false if no space was available.
     */
    private static boolean parkCars(int ipk, int[] parking, int[] time, int[] totIn, int[] totRefused) {


        // Check if space in available in parking lot.
        boolean isSpaceAvailable = false;
        for (int index = 0; index < parking.length; index++) {
            if (parking[index] == 0) {
                // Generate a random number for number of hours car need to be parked. Between 1 and 72.
                rndNb = getRandom(rndNb);
                int parkingHour = valueInRange(HOUR_MAX);

                // Park car at available space and set time for generated random number.
                parking[index] = parkingHour;
                time[index] = parkingHour;

                // Increase accepted cars in given simulation.
                totIn[ipk]++;
                isSpaceAvailable = true;
                break;
            }
        }

        // If space not available increase refused cars in particular simulation.
        if (!isSpaceAvailable) {
            totRefused[ipk]++;
            return false;
        }

        // If car is parked return true.
        return true;
    }
}
