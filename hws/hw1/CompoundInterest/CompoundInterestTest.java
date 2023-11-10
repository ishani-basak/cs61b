import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2022));
        assertEquals(1, CompoundInterest.numYears(2023));
        assertEquals(100, CompoundInterest.numYears(2122));

    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).

        // The variable below can be used when you write your tests.
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2024), tolerance);
        assertEquals(7.3695, CompoundInterest.futureValue(12, -15, 2025), tolerance);
        assertEquals(38.9061369, CompoundInterest.futureValue(15, 10, 2032), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496,
                CompoundInterest.futureValueReal(10, 12, 2024, 3), tolerance);
        assertEquals(6.52006195,
                CompoundInterest.futureValueReal(12, -15, 2025, 4), tolerance);
        assertEquals(23.2945413,
                CompoundInterest.futureValueReal(15, 10, 2032, 5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2024, 10), tolerance);
        assertEquals(4993.375, CompoundInterest.totalSavings(1000, 2025, 15), tolerance);
        assertEquals(2482.48, CompoundInterest.totalSavings(250, 2027, 20), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15571.895,
                CompoundInterest.totalSavingsReal(5000, 2024, 10, 3), tolerance);
        assertEquals(4417.818624,
                CompoundInterest.totalSavingsReal(1000, 2025, 15, 4), tolerance);
        assertEquals(1920.895702,
                CompoundInterest.totalSavingsReal(250, 2027, 20, 5), tolerance);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
