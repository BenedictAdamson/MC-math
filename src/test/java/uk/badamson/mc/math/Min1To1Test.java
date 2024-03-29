package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018,22.
 *
 * This file is part of MC-math.
 *
 * MC-math is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MC-math is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MC-math.  If not, see <https://www.gnu.org/licenses/>.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;
import uk.badamson.mc.math.Min1To1.Bracket;

/**
 * <p>
 * Unit tests for the class {@link Min1To1}.
 * </p>
 */
@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public class Min1To1Test {

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class BracketTest {

        public static void assertInvariants(final Min1To1.Bracket bracket) {
            ObjectVerifier.assertInvariants(bracket);// inherited

            final Function1To1Value left = bracket.getLeft();
            final Function1To1Value inner = bracket.getInner();
            final Function1To1Value right = bracket.getRight();
            final double width = bracket.getWidth();

            assertNotNull(left, "Not null, left");// guard
            assertNotNull(inner, "Not null, inner");// guard
            assertNotNull(right, "Not null, right");// guard

            Function1To1ValueTest.assertInvariants(left);
            Function1To1ValueTest.assertInvariants(inner);
            Function1To1ValueTest.assertInvariants(right);

            final double innerX = inner.x();
            final double innerY = inner.f();
            assertTrue(left.x() < innerX,
                    "The inner point " + inner + " is to the right of the leftmost point " + left);
            assertTrue(innerY < left.f(), "The inner point " + inner + " is below the leftmost point " + left + ".");
            assertTrue(innerX < right.x(), "The rightmost point is to the right of the inner point.");
            assertTrue(innerY < right.f(), "The rightmost point is above the inner point.");

            assertEquals(inner.f(), bracket.getMin(), Double.MIN_NORMAL,
                    "The smallest function value of the points constituting this bracket is the y value of the inner point of the bracket");
            assertTrue(0.0 < width, "The width of a bracket <" + width + "> is always positive.");
            assertEquals(right.x() - left.x(), width, Double.MIN_NORMAL, "width");
        }

        public static void assertInvariants(final Min1To1.Bracket bracket1, final Min1To1.Bracket bracket2) {
            ObjectVerifier.assertInvariants(bracket1, bracket2);// inherited
        }

        private static Min1To1.Bracket constructor(final Function1To1Value left, final Function1To1Value inner,
                                                   final Function1To1Value right) {
            final Min1To1.Bracket bracket = new Min1To1.Bracket(left, inner, right);
            assertInvariants(bracket);
            assertSame(left, bracket.getLeft(), "left");
            assertSame(inner, bracket.getInner(), "inner");
            assertSame(right, bracket.getRight(), "right");
            return bracket;
        }

        @Test
        public void constructor_a() {
            constructor(POINT_1, POINT_2, POINT_5);
        }

        @Test
        public void constructor_b() {
            constructor(POINT_2, POINT_3, POINT_4);
        }
    }// class

    private static final Function1To1Value POINT_1 = new Function1To1Value(1, 8);
    private static final Function1To1Value POINT_2 = new Function1To1Value(2, 7);
    private static final Function1To1Value POINT_3 = new Function1To1Value(4, 3);
    private static final Function1To1Value POINT_4 = new Function1To1Value(5, 4);
    private static final Function1To1Value POINT_5 = new Function1To1Value(6, 9);

    private static final Function1To1 SQUARED = x -> x * x;

    private static final Function1To1 POWER_4 = x -> {
        final double x2 = x * x;
        return x2 * x2;
    };

    private static final Function1To1 ORDER_3 = x -> {
        final double x2 = x * x;
        return x + x2 - x * x2;
    };

    private static final Function1To1 NOT_SMOOTH = x -> {
        double f = x * x;
        if (-1 < x) {
            f += x + 1;
        }
        return f;
    };

    private static final Function1To1 COS = Math::cos;

    private static final Function1To1WithGradient SQUARED_WITH_GRADIENT = x -> new Function1To1WithGradientValue(x, x * x, 2.0 * x);

    private static final Function1To1WithGradient POWER_4_WITH_GRADIENT = x -> {
        final double x2 = x * x;
        return new Function1To1WithGradientValue(x, x2 * x2, 4.0 * x2 * x);
    };

    private static void assertConsistent(final Min1To1.Bracket bracket, final Function1To1 f) {
        assertConsistent("Left point of bracket", bracket.getLeft(), f);
        assertConsistent("Inner point of bracket", bracket.getInner(), f);
        assertConsistent("Right point of bracket", bracket.getRight(), f);
    }

    private static void assertConsistent(final String message, final Function1To1Value p, final Function1To1 f) {
        assertEquals(f.value(p.x()), p.f(), Double.MIN_NORMAL,
                message + " <" + p + "> is consistent with function <" + f + ">");
    }

    private static void assertConsistent(final String message, final Function1To1WithGradientValue p,
            final Function1To1WithGradient f) {
        final Function1To1WithGradientValue fp = f.value(p.x());
        assertEquals(fp.f(), p.f(), Double.MIN_NORMAL,
                message + " <" + p + "> is consistent with function <" + f + ">, codomain");
        assertEquals(fp.dfdx(), p.dfdx(), Double.MIN_NORMAL,
                message + " <" + p + "> is consistent with function <" + f + ">, gradient");
    }

    private static Min1To1.Bracket findBracket(final Function1To1 f, final double x1, final double x2)
            throws PoorlyConditionedFunctionException {
        final Min1To1.Bracket bracket = Min1To1.findBracket(f, x1, x2);

        assertNotNull(bracket, "Not null, bracket");// guard
        BracketTest.assertInvariants(bracket);
        assertConsistent(bracket, f);

        return bracket;
    }

    private static Function1To1Value findBrent(final Function1To1 f, final Min1To1.Bracket bracket, final double tolerance) {
        final Function1To1Value min = Min1To1.findBrent(f, bracket, tolerance);

        assertNotNull(min, "The method always returns a bracket");// guard
        BracketTest.assertInvariants(bracket);
        assertConsistent("Minimum", min, f);

        assertTrue(min.f() <= bracket.getInner().f(), "The minimum value of the returned bracket <" + min
                + "> is not larger than the minimum value of the given bracket <" + bracket + ">");

        return min;
    }

    private static Function1To1WithGradientValue findBrent(final Function1To1WithGradient f, final Bracket bracket,
                                                           final double tolerance) {
        final Function1To1WithGradientValue min = Min1To1.findBrent(f, bracket, tolerance);

        assertNotNull(min, "The method always returns a bracket");// guard
        BracketTest.assertInvariants(bracket);
        assertConsistent("Minimum", min, f);

        assertTrue(min.f() <= bracket.getInner().f(), "The minimum value of the returned bracket <" + min
                + "> is not larger than the minimum value of the given bracket <" + bracket + ">");
        return min;
    }

    private static void findBrent_power4(final double x1, final double x2, final double x3,
                                         final double tolerance) {
        assert x1 < x2;
        assert x2 < x3;
        assert x1 < 0.0;
        assert 0.0 < x3;
        final Min1To1.Bracket bracket = new Bracket(new Function1To1Value(x1, POWER_4.value(x1)),
                new Function1To1Value(x2, POWER_4.value(x2)), new Function1To1Value(x3, POWER_4.value(x3)));

        findBrent(SQUARED, bracket, tolerance);
    }

    private static void findBrent_squared(final double x1, final double x2, final double x3,
                                          final double tolerance) {
        assert x1 < x2;
        assert x2 < x3;
        assert x1 < 0.0;
        assert 0.0 < x3;
        final Min1To1.Bracket bracket = new Bracket(new Function1To1Value(x1, SQUARED.value(x1)),
                new Function1To1Value(x2, SQUARED.value(x2)), new Function1To1Value(x3, SQUARED.value(x3)));

        findBrent(SQUARED, bracket, tolerance);
    }

    private static void findBrent_withGradientPower4(final double x1, final double x2, final double x3,
                                                     final double tolerance) {
        assert x1 < x2;
        assert x2 < x3;
        assert x1 < 0.0;
        assert 0.0 < x3;
        final Min1To1.Bracket bracket = new Bracket(new Function1To1Value(x1, POWER_4.value(x1)),
                new Function1To1Value(x2, POWER_4.value(x2)), new Function1To1Value(x3, POWER_4.value(x3)));

        findBrent(POWER_4_WITH_GRADIENT, bracket, tolerance);
    }

    private static void findBrent_withGradientSquared(final double x1, final double x2, final double x3,
                                                      final double tolerance) {
        assert x1 < x2;
        assert x2 < x3;
        assert x1 < 0.0;
        assert 0.0 < x3;
        final Min1To1.Bracket bracket = new Bracket(new Function1To1Value(x1, SQUARED.value(x1)),
                new Function1To1Value(x2, SQUARED.value(x2)), new Function1To1Value(x3, SQUARED.value(x3)));

        findBrent(SQUARED_WITH_GRADIENT, bracket, tolerance);
    }

    @Test
    public void findBracket_nearMaxA() {
        findBracket(COS, 0.0, 0.1);
    }

    @Test
    public void findBracket_nearMaxB() {
        findBracket(COS, -0.1, 0.1);
    }

    @Test
    public void findBracket_notSmoothA() {
        findBracket(NOT_SMOOTH, -3.0, -2.0);
    }

    @Test
    public void findBracket_notSmoothB() {
        findBracket(NOT_SMOOTH, -4.0, -3.0);
    }

    @Test
    public void findBracket_order3A() {
        try {
            findBracket(ORDER_3, -1.0, 0.0);
        } catch (final PoorlyConditionedFunctionException e) {
            // Permitted
        }
    }

    @Test
    public void findBracket_order3B() {
        try {
            findBracket(ORDER_3, -1.6, 0.0);
        } catch (final PoorlyConditionedFunctionException e) {
            // Permitted
        }
    }

    @Test
    public void findBracket_power4Left() {
        findBracket(POWER_4, -2.0, -1.0);
    }

    @Test
    public void findBracket_power4LeftFar() {
        findBracket(POWER_4, -1E9, -0.9E9);
    }

    @Test
    public void findBracket_power4LeftReversed() {
        findBracket(POWER_4, -1.0, -2.0);
    }

    @Test
    public void findBracket_power4Right() {
        findBracket(POWER_4, 1.0, 2.0);
    }

    @Test
    public void findBracket_power4RightFar() {
        findBracket(POWER_4, 0.9E9, 1E9);
    }

    @Test
    public void findBracket_power4RightReversed() {
        findBracket(POWER_4, 2.0, 1.0);
    }

    @Test
    public void findBracket_power4Span() {
        findBracket(POWER_4, -1.0, 1.0);
    }

    @Test
    public void findBracket_squaredLeft() {
        findBracket(SQUARED, -2.0, -1.0);
    }

    @Test
    public void findBracket_squaredLeftFarA() {
        findBracket(SQUARED, -1E9, -0.9E9);
    }

    @Test
    public void findBracket_squaredLeftFarB() {
        findBracket(SQUARED, -1E9, -0.9999E9);
    }

    @Test
    public void findBracket_squaredLeftReversed() {
        findBracket(SQUARED, -1.0, -2.0);
    }

    @Test
    public void findBracket_squaredRight() {
        findBracket(SQUARED, 1.0, 2.0);
    }

    @Test
    public void findBracket_squaredRightFar() {
        findBracket(SQUARED, 0.9E9, 1E9);
    }

    @Test
    public void findBracket_squaredRightReversed() {
        findBracket(SQUARED, 2.0, 1.0);
    }

    @Test
    public void findBracket_squaredSpan() {
        findBracket(SQUARED, -1.0, 1.0);
    }

    @Test
    public void findBrent_power4Centre() {
        final double x1 = -1.0;
        final double x2 = 0.0;
        final double x3 = 1.0;
        final double xTolerance = 1E-3;

        findBrent_power4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_power4Left() {
        final double x1 = -3.0;
        final double x2 = -1.0;
        final double x3 = 2.0;
        final double xTolerance = 1E-3;

        findBrent_power4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_power4Right() {
        final double x1 = -2.0;
        final double x2 = 1.0;
        final double x3 = 3.0;
        final double xTolerance = 1E-3;

        findBrent_power4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_squaredCentre() {
        final double x1 = -1.0;
        final double x2 = 0.0;
        final double x3 = 1.0;
        final double xTolerance = 1E-3;

        findBrent_squared(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_squaredLeft() {
        final double x1 = -3.0;
        final double x2 = -1.0;
        final double x3 = 2.0;
        final double xTolerance = 1E-3;

        findBrent_squared(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_squaredRight() {
        final double x1 = -2.0;
        final double x2 = 1.0;
        final double x3 = 3.0;
        final double xTolerance = 1E-3;

        findBrent_squared(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientPower4Centre() {
        final double x1 = -1.0;
        final double x2 = 0.0;
        final double x3 = 1.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientPower4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientPower4Left() {
        final double x1 = -3.0;
        final double x2 = -1.0;
        final double x3 = 2.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientPower4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientPower4Right() {
        final double x1 = -2.0;
        final double x2 = 1.0;
        final double x3 = 3.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientPower4(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientSquaredCentre() {
        final double x1 = -1.0;
        final double x2 = 0.0;
        final double x3 = 1.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientSquared(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientSquaredLeft() {
        final double x1 = -3.0;
        final double x2 = -1.0;
        final double x3 = 2.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientSquared(x1, x2, x3, xTolerance);
    }

    @Test
    public void findBrent_withGradientSquaredRight() {
        final double x1 = -2.0;
        final double x2 = 1.0;
        final double x3 = 3.0;
        final double xTolerance = 1E-3;

        findBrent_withGradientSquared(x1, x2, x3, xTolerance);
    }

}
