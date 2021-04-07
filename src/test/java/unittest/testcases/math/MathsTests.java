package unittest.testcases.math;

import io.github.lucasstarsz.fastj.math.Maths;
import io.github.lucasstarsz.fastj.math.Point;
import io.github.lucasstarsz.fastj.math.Pointf;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MathsTests {

    @Test
    public void checkGenerateRandoms_ensureWithinExpectedRange() {
        double minimumRandomRange = 3.5d;
        double maximumRandomRange = 7.5d;

        String assertFailMessage = "Generated random value should be within expected range.";
        for (int i = 0; i < 255; i++) {
            double generatedRandom = Maths.random(minimumRandomRange, maximumRandomRange);
            assertTrue(assertFailMessage, generatedRandom >= minimumRandomRange && generatedRandom <= maximumRandomRange);
        }
    }

    @Test
    public void checkGenerateRandoms_ensureMatchAtLeastOneEdge() {
        double leftEdge = 3.5d;
        double rightEdge = 7.5d;

        String assertFailMessage = "Generated random value should match at least one edge.";
        for (int i = 0; i < 255; i++) {
            double generatedRandom = Maths.randomAtEdge(leftEdge, rightEdge);
            assertTrue(assertFailMessage, generatedRandom == leftEdge || generatedRandom == rightEdge);
        }
    }

    @Test
    public void checkSnapDoubleValueToEdge_whenExpectedIsRightEdge() {
        double leftEdge = 5.0d;
        double rightEdge_expected = 6.0d;
        double valueToSnap = 5.6d;

        double actualEdge = Maths.snap(valueToSnap, leftEdge, rightEdge_expected);
        assertEquals("Actual edge should match the expected edge (right edge).", rightEdge_expected, actualEdge);
    }

    @Test
    public void checkSnapDoubleValueToEdge_whenExpectedIsLeftEdge() {
        double leftEdge_expected = 5.0d;
        double rightEdge = 6.0d;
        double valueToSnap = 5.4d;

        double actualEdge = Maths.snap(valueToSnap, leftEdge_expected, rightEdge);
        assertEquals("Actual edge should match the expected edge (left edge).", leftEdge_expected, actualEdge);
    }

    @Test
    public void checkSnapDoubleValueToEdge_whenEdgesAreEquidistant() {
        double leftEdge = 5.0d;
        double rightEdge_expected = 6.0d;
        double valueToSnap = 5.5d;

        double actualEdge = Maths.snap(valueToSnap, leftEdge, rightEdge_expected);
        assertEquals("Actual edge should match the right edge.", rightEdge_expected, actualEdge);
    }

    @Test
    public void checkMagnitudeWithDoubleValues() {
        assertEquals("The magnitude should be equal to 0.0.", 0d, Maths.magnitude(0, 0));
        assertEquals("The magnitude should be equal to 1.0.", 1d, Maths.magnitude(0, 1));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(1, 1));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(-1, -1));
        assertEquals("The magnitude should be equal to 5.", Math.sqrt(25), Maths.magnitude(3, 4));
    }

    @Test
    public void checkMagnitudeWithPointfObjects() {
        assertEquals("The magnitude should be equal to 0.0.", 0d, Maths.magnitude(new Pointf()));
        assertEquals("The magnitude should be equal to 1.0.", 1d, Maths.magnitude(new Pointf(0, 1)));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(new Pointf(1)));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(new Pointf(-1)));
        assertEquals("The magnitude should be equal to 5.", Math.sqrt(25), Maths.magnitude(new Pointf(3, 4)));
    }

    @Test
    public void checkMagnitudeWithPointObjects() {
        assertEquals("The magnitude should be equal to 0.0.", 0d, Maths.magnitude(new Point()));
        assertEquals("The magnitude should be equal to 1.0.", 1d, Maths.magnitude(new Point(0, 1)));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(new Point(1)));
        assertEquals("The magnitude should be equal to the square root of 2.0.", Math.sqrt(2), Maths.magnitude(new Point(-1)));
        assertEquals("The magnitude should be equal to 5.", Math.sqrt(25), Maths.magnitude(new Point(3, 4)));
    }
}
