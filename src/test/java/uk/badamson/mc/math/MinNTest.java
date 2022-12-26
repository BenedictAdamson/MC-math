package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018.
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

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

/**
 * <p>
 * Unit tests of the {@link MinN} class.
 * </p>
 */
@SuppressWarnings({"UnnecessaryLocalVariable", "UnusedReturnValue", "SameParameterValue"})
public class MinNTest {

    private static final FunctionN CONSTANT_1 = new FunctionN() {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public double value(@Nonnull final double[] x) {
            return 1.0;
        }
    };

    private static final FunctionN BILINEANR_1 = new FunctionN() {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double value(@Nonnull final double[] x) {
            return x[0] + x[1];
        }
    };

    private static final FunctionN PARABOLOID = new FunctionN() {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double value(@Nonnull final double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }
    };

    private static final FunctionNWithGradient PARABOLOID_WITH_GRADIENT = new FunctionNWithGradient() {

        @Override
        public int getDimension() {
            return 2;
        }

        @Nonnull
        @Override
        public FunctionNWithGradientValue value(@Nonnull final ImmutableVectorN x) {
            final double x0 = x.get(0);
            final double x1 = x.get(1);
            return new FunctionNWithGradientValue(x, x0 * x0 + x1 * x1, ImmutableVectorN.create(2.0 * x0, 2.0 * x1));
        }
    };

    private static double adjacentPrecision(final double x) {
        final double next = Math.nextAfter(x, Double.POSITIVE_INFINITY);
        final double precision = Math.max(next - x, Min1.TOLERANCE * Math.abs(x));
        assert 0.0 < precision;
        return precision;
    }

    private static Function1 createLineFunction(final FunctionN f, final double[] x0, final double[] dx) {
        final Function1 lineFunction = MinN.createLineFunction(f, x0, dx);

        assertNotNull(lineFunction, "Not null, result");

        return lineFunction;
    }

    private static Function1WithGradient createLineFunction(final FunctionNWithGradient f, final ImmutableVectorN x0,
            final ImmutableVectorN dx) {
        final Function1WithGradient lineFunction = MinN.createLineFunction(f, x0, dx);

        assertNotNull(lineFunction, "Not null, result");

        return lineFunction;
    }

    private static void createLineFunction_paraboloidWithGradient(final double x00, final double x01, final double dx0,
            final double dx1, final double w, final double expectedF, final double expectedDfDw,
            final double toleranceF, final double toleranceDfDw) {
        final ImmutableVectorN x = ImmutableVectorN.create(x00, x01);
        final ImmutableVectorN dx = ImmutableVectorN.create(dx0, dx1);

        final Function1WithGradient f = createLineFunction(PARABOLOID_WITH_GRADIENT, x, dx);

        final Function1WithGradientValue fw = Function1WithGradientTest.value(f, w);
        assertEquals(expectedF, fw.getF(), toleranceF, "f(" + w + ")");
        assertEquals(expectedDfDw, fw.getDfDx(), toleranceDfDw, "dfdw(" + w + ")");
    }

    private static FunctionNWithGradientValue findFletcherReevesPolakRibere(final FunctionNWithGradient f,
            final ImmutableVectorN x, final double tolerance) throws PoorlyConditionedFunctionException {
        final FunctionNWithGradientValue min = MinN.findFletcherReevesPolakRibere(f, x, tolerance);

        assertNotNull(min, "Not null, result");// guard
        FunctionNWithGradientValueTest.assertInvariants(min);

        return min;
    }

    private static void findFletcherReevesPolakRibere_paraboloid(final double x0, final double x1,
            final double tolerance) {
        final ImmutableVectorN x = ImmutableVectorN.create(x0, x1);
        final double precision = Math.sqrt(tolerance);

        final FunctionNWithGradientValue min = findFletcherReevesPolakRibere(PARABOLOID_WITH_GRADIENT, x, tolerance);

        final ImmutableVectorN minX = min.getX();
        assertEquals(0.0, minX.get(0), precision, "x[0]");
        assertEquals(0.0, min.getX().get(1), precision, "x[1]");
    }

    private static double findPowell(final FunctionN f, final double[] x, final double tolerance) {
        final double min = MinN.findPowell(f, x, tolerance);

        assertEquals(f.value(x), min, adjacentPrecision(min), "Minimum value");

        return min;
    }

    private static void findPowell_paraboloid(final double x0, final double x1, final double tolerance) {
        final double[] x = { x0, x1 };
        final double precision = Math.sqrt(tolerance);

        findPowell(PARABOLOID, x, tolerance);

        assertEquals(0.0, x[0], precision, "x[0]");
        assertEquals(0.0, x[1], precision, "x[1]");
    }

    private static double magnitude(final double[] x) {
        double m2 = 0.0;
        for (final double xi : x) {
            m2 += xi * xi;
        }
        return Math.sqrt(m2);
    }

    private static double minimiseAlongLine(final FunctionN f, final double[] x, final double[] dx) {
        final int n = x.length;
        final double[] x0 = Arrays.copyOf(x, n);
        final double[] e0 = normalized(dx);

        final double min = MinN.minimiseAlongLine(f, x, dx);

        final double[] e = normalized(dx);
        final double em = magnitude(e);
        assertEquals(f.value(x), min, adjacentPrecision(min), "Minimum value");
        for (int i = 0; i < n; ++i) {
            assertEquals(x[i] - x0[i], dx[i], adjacentPrecision(magnitude(dx)), "dx[" + i + "]");
            assertEquals(em < Double.MIN_NORMAL ? 0.0 : e0[i], e[i], adjacentPrecision(e0[i]), "direction[" + i + "]");
        }

        return min;
    }

    private static FunctionNWithGradientValue minimiseAlongLine(final FunctionNWithGradient f, final ImmutableVectorN x,
            final ImmutableVectorN dx) {
        final FunctionNWithGradientValue min = MinN.minimiseAlongLine(f, x, dx);

        assertNotNull(min, "Not null, result");// guard
        FunctionNWithGradientValueTest.assertInvariants(min);

        return min;
    }

    private static void minimiseAlongLine_paraboloid(final double x0, final double x1, final double dx0,
            final double dx1, final double expectedXMin0, final double expectedXMin1) {
        final double[] x = { x0, x1 };
        final double[] dx = { dx0, dx1 };
        final double precision = adjacentPrecision(magnitude(dx));

        minimiseAlongLine(PARABOLOID, x, dx);

        assertEquals(expectedXMin0, x[0], precision, "x[0]");
        assertEquals(expectedXMin1, x[1], precision, "x[1]");
    }

    private static void minimiseAlongLine_paraboloidAtMin(final double x0, final double x1, final double dx0,
            final double dx1) {
        final double expectedXMin0 = x0;
        final double expectedXMin1 = x1;

        minimiseAlongLine_paraboloid(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    private static void minimiseAlongLine_paraboloidWithGradient(final double x0, final double x1, final double dx0,
            final double dx1, final double expectedXMin0, final double expectedXMin1) {
        final ImmutableVectorN x = ImmutableVectorN.create(x0, x1);
        final ImmutableVectorN dx = ImmutableVectorN.create(dx0, dx1);
        final double precision = adjacentPrecision(dx.magnitude());

        final FunctionNWithGradientValue min = minimiseAlongLine(PARABOLOID_WITH_GRADIENT, x, dx);

        final ImmutableVectorN xMin = min.getX();
        assertEquals(expectedXMin0, xMin.get(0), precision, "xMin[0]");
        assertEquals(expectedXMin1, min.getX().get(1), precision, "xMin[1]");
    }

    private static void minimiseAlongLine_paraboloidWithGradientAtMin(final double x0, final double x1,
            final double dx0, final double dx1) {
        final double expectedXMin0 = x0;
        final double expectedXMin1 = x1;

        minimiseAlongLine_paraboloidWithGradient(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    private static double[] normalized(final double[] x) {
        final int n = x.length;
        final double m = magnitude(x);
        final double f = 0 < m ? 1.0 / m : 1.0;
        final double[] e = new double[n];
        for (int i = 0; i < n; ++i) {
            e[i] = x[i] * f;
        }
        return e;
    }

    @Test
    public void createLineFunction_bilinearA() {
        final double[] x0 = { 0.0, 0.0 };
        final double[] dx = { 1.0, 0.0 };

        final Function1 lineFunction = createLineFunction(BILINEANR_1, x0, dx);

        assertEquals(0.0, lineFunction.value(0.0), 1E-3, "lineFunction[0]");
        assertEquals(1.0, lineFunction.value(1.0), 1E-3, "lineFunction[1.0]");
        assertEquals(-1.0, lineFunction.value(-1.0), 1E-3, "lineFunction[-1.0]");
    }

    @Test
    public void createLineFunction_bilinearB() {
        final double[] x0 = { 0.0, 0.0 };
        final double[] dx = { 0.0, 1.0 };

        final Function1 lineFunction = createLineFunction(BILINEANR_1, x0, dx);

        assertEquals(0.0, lineFunction.value(0.0), 1E-3, "lineFunction[0]");
        assertEquals(1.0, lineFunction.value(1.0), 1E-3, "lineFunction[1.0]");
        assertEquals(-1.0, lineFunction.value(-1.0), 1E-3, "lineFunction[-1.0]");
    }

    @Test
    public void createLineFunction_bilinearC() {
        final double[] x0 = { 0.0, 0.0 };
        final double[] dx = { 1.0, 1.0 };

        final Function1 lineFunction = createLineFunction(BILINEANR_1, x0, dx);

        assertEquals(0.0, lineFunction.value(0.0), 1E-3, "lineFunction[0]");
        assertEquals(2.0, lineFunction.value(1.0), 1E-3, "lineFunction[1.0]");
        assertEquals(-2.0, lineFunction.value(-1.0), 1E-3, "lineFunction[-1.0]");
    }

    @Test
    public void createLineFunction_constant() {
        final double[] x0 = { 0.0 };
        final double[] dx = { 1.0 };

        final Function1 lineFunction = createLineFunction(CONSTANT_1, x0, dx);

        assertEquals(1.0, lineFunction.value(0.0), 1E-3, "lineFunction[0]");
        assertEquals(1.0, lineFunction.value(1.0), 1E-3, "lineFunction[1.0]");
        assertEquals(1.0, lineFunction.value(-1.0), 1E-3, "lineFunction[-1.0]");
    }

    @Test
    public void createLineFunction_paraboloidWithGradientA() {
        final double x00 = 0.0;
        final double x01 = 0.0;
        final double dx0 = 1.0;
        final double dx1 = 0.0;
        final double w = 0.0;
        final double expectedF = 0.0;
        final double expectedDfDw = 0.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientB() {
        final double x00 = 1.0;
        final double x01 = 0.0;
        final double dx0 = 1.0;
        final double dx1 = 0.0;
        final double w = 0.0;
        final double expectedF = 1.0;
        final double expectedDfDw = 2.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientC() {
        final double x00 = 0.0;
        final double x01 = 0.0;
        final double dx0 = 2.0;
        final double dx1 = 0.0;
        final double w = 0.0;
        final double expectedF = 0.0;
        final double expectedDfDw = 0.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientD() {
        final double x00 = 0.0;
        final double x01 = 0.0;
        final double dx0 = 1.0;
        final double dx1 = 1.0;
        final double w = 0.0;
        final double expectedF = 0.0;
        final double expectedDfDw = 0.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientE() {
        final double x00 = 0.0;
        final double x01 = 0.0;
        final double dx0 = 1.0;
        final double dx1 = 0.0;
        final double w = 1.0;
        final double expectedF = 1.0;
        final double expectedDfDw = 2.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientF() {
        final double x00 = 1.0;
        final double x01 = 0.0;
        final double dx0 = 1.0;
        final double dx1 = 0.0;
        final double w = 1.0;
        final double expectedF = 4.0;
        final double expectedDfDw = 4.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void createLineFunction_paraboloidWithGradientG() {
        final double x00 = 0.0;
        final double x01 = 1.0;
        final double dx0 = 1.0;
        final double dx1 = 0.0;
        final double w = 1.0;
        final double expectedF = 2.0;
        final double expectedDfDw = 2.0;
        final double toleranceF = Double.MIN_NORMAL;
        final double toleranceDfDw = Double.MIN_NORMAL;

        createLineFunction_paraboloidWithGradient(x00, x01, dx0, dx1, w, expectedF, expectedDfDw, toleranceF,
                toleranceDfDw);
    }

    @Test
    public void findFletcherReevesPolakRibere_paraboloidA() {
        findFletcherReevesPolakRibere_paraboloid(0, 1, 1E-3);
    }

    @Test
    public void findFletcherReevesPolakRibere_paraboloidAtMin() {
        findFletcherReevesPolakRibere_paraboloid(0, 0, 1E-3);
    }

    @Test
    public void findFletcherReevesPolakRibere_paraboloidB() {
        findFletcherReevesPolakRibere_paraboloid(1, 0, 1E-3);
    }

    @Test
    public void findFletcherReevesPolakRibere_paraboloidC() {
        findFletcherReevesPolakRibere_paraboloid(1, 1, 1E-3);
    }

    @Test
    public void findFletcherReevesPolakRibere_paraboloidD() {
        findFletcherReevesPolakRibere_paraboloid(1, 1, 1E-5);
    }

    @Test
    public void findPowell_paraboloidA() {
        findPowell_paraboloid(0, 1, 1E-3);
    }

    @Test
    public void findPowell_paraboloidAtMin() {
        findPowell_paraboloid(0, 0, 1E-3);
    }

    @Test
    public void findPowell_paraboloidB() {
        findPowell_paraboloid(1, 0, 1E-3);
    }

    @Test
    public void findPowell_paraboloidC() {
        findPowell_paraboloid(1, 1, 1E-3);
    }

    @Test
    public void findPowell_paraboloidD() {
        findPowell_paraboloid(1, 1, 1E-5);
    }

    @Test
    public void minimiseAlongLine_paraboloidA() {
        final double x0 = -1;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        final double expectedXMin0 = 0;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloid(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    @Test
    public void minimiseAlongLine_paraboloidAtMinA() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        minimiseAlongLine_paraboloidAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidAtMinB() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 0;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidAtMinC() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidAtMinD() {
        final double x0 = 0;
        final double x1 = 1;
        final double dx0 = 1;
        final double dx1 = 0;
        minimiseAlongLine_paraboloidAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidAtMinE() {
        final double x0 = 1;
        final double x1 = 0;
        final double dx0 = 0;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidB() {
        final double x0 = -2;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        final double expectedXMin0 = 0;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloid(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    @Test
    public void minimiseAlongLine_paraboloidC() {
        final double x0 = 1;
        final double x1 = 1;
        final double dx0 = 0;
        final double dx1 = -5;
        final double expectedXMin0 = 1;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloid(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientA() {
        final double x0 = -1;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        final double expectedXMin0 = 0;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloidWithGradient(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientAtMinA() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        minimiseAlongLine_paraboloidWithGradientAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientAtMinB() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 0;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidWithGradientAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientAtMinC() {
        final double x0 = 0;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidWithGradientAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientAtMinD() {
        final double x0 = 0;
        final double x1 = 1;
        final double dx0 = 1;
        final double dx1 = 0;
        minimiseAlongLine_paraboloidWithGradientAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientAtMinE() {
        final double x0 = 1;
        final double x1 = 0;
        final double dx0 = 0;
        final double dx1 = 1;
        minimiseAlongLine_paraboloidWithGradientAtMin(x0, x1, dx0, dx1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientB() {
        final double x0 = -2;
        final double x1 = 0;
        final double dx0 = 1;
        final double dx1 = 0;
        final double expectedXMin0 = 0;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloidWithGradient(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

    @Test
    public void minimiseAlongLine_paraboloidWithGradientC() {
        final double x0 = 1;
        final double x1 = 1;
        final double dx0 = 0;
        final double dx1 = -5;
        final double expectedXMin0 = 1;
        final double expectedXMin1 = 0;

        minimiseAlongLine_paraboloidWithGradient(x0, x1, dx0, dx1, expectedXMin0, expectedXMin1);
    }

}
