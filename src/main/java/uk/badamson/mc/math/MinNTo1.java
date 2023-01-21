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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * <p>
 * Functions and auxiliary classes for minimization of a {@linkplain FunctionNTo1
 * multidimensional function}.
 * </p>
 */
public final class MinNTo1 {

    private MinNTo1() {
        throw new AssertionError("Class should not be instantiated");
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static double basicPowell(
            @Nonnull final FunctionNTo1 f,
            @Nonnull final double[] x0,
            @Nonnull final double[] dx0,
            @Nonnull final double[] x,
            @Nonnull final double[][] dx
    ) throws PoorlyConditionedFunctionException {
        final int n = f.getDimension();
        assert n == x0.length;
        assert n == dx0.length;
        assert n == x.length;
        assert n == dx.length;
        copyTo(x0, x);
        for (int i = 0; i < n; ++i) {
            copyTo(dx0, dx[i]);
            minimiseAlongLine(f, x, dx0);
        }
        dx[n - 1] = dx[0];// recycle array
        for (int i = 0; i < n - 1; ++i) {
            dx[i] = dx[i + 1];
        }
        double xNewMax = 0;
        for (int j = 0; j < n; ++j) {
            final double xNew = x[j] - x0[j];
            dx[n - 1][j] = xNew;
            xNewMax = Math.max(xNewMax, Math.abs(xNew));
        }
        if (xNewMax < Min1To1.TOLERANCE) {
            /*
             * We have converged on the minimum, or the search directions have degenerated.
             */
            resetSearchDirections(dx);
        }
        copyTo(dx0, dx[n - 1]);
        return minimiseAlongLine(f, x, dx0);
    }

    private static void copyTo(@Nonnull final double[] x, @Nonnull final double[] y) {
        for (int j = 0, n = x.length; j < n; ++j) {
            x[j] = y[j];
        }
    }

    /**
     * <p>
     * Create a {@linkplain Function1To1 functor for a one-dimensional function of a
     * continuous variable} that is the evaluation of a {@linkplain FunctionNTo1
     * functor for a multi-dimensional function of continuous variables} along a
     * given line.
     * </p>
     * <p>
     * The created functor retains references to the given objects. Those objects
     * should therefore not be changed while the created function is in use.
     * </p>
     *
     * @param f  The multi-dimensional function
     * @param x0 The origin point; the position in the space of the
     *           multidimensional function corresponding to the origin point of the
     *           created function.
     * @param dx The direction vector of the line in the space of the
     *           multidimensional function; (x + dx) corresponds to the value for
     *           1.0 of the created function.
     * @throws NullPointerException     If {@code f} is null.
     *                                  If {@code x0} is null.
     *                                  If {@code dx} is null.
     * @throws IllegalArgumentException If the length of {code x0} is 0.
     *                                  If the length of {code x0} is different from the length of
     *                                  {@code dx}.
     *                                  If the length of {code x0} is different from the
     *                                  {@linkplain FunctionNTo1#getDimension() number of dimensions} of
     *                                  {@code f}.
     */
    static @Nonnull Function1To1 createLineFunction(
            @Nonnull final FunctionNTo1 f,
            @Nonnull final double[] x0,
            @Nonnull final double[] dx
    ) {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(x0, "x0");
        Objects.requireNonNull(dx, "dx");
        final int n = x0.length;
        if (n == 0) {
            throw new IllegalArgumentException("x0.length == 0");
        }
        if (n != dx.length || n != f.getDimension()) {
            throw new IllegalArgumentException(
                    "Inconsistent lengths, x0 " + n + ", dx " + dx.length + ", f.dimensions " + f.getDimension());
        }

        return w -> {
            final double[] x = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = x0[i] + w * dx[i];
            }
            return f.value(x);
        };
    }

    /**
     * <p>
     * Create a {@linkplain Function1To1 functor for a one-dimensional function of a
     * continuous variable} that is the evaluation of a {@linkplain FunctionNTo1
     * functor for a multi-dimensional function of continuous variables} along a
     * given line.
     * </p>
     * <p>
     * The created functor retains references to the given objects. Those objects
     * should therefore not be changed while the created function is in use.
     * </p>
     *
     * @param f  The multi-dimensional function
     * @param x0 The origin point; the position in the space of the
     *           multidimensional function corresponding to the origin point of the
     *           created function.
     * @param dx The direction vector of the line in the space of the
     *           multidimensional function; (x + dx) corresponds to the value for
     *           1.0 of the created function.
     * @throws NullPointerException     If {@code f} is null.
     *                                  If {@code x0} is null.
     *                                  If {@code dx} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVectorN#getDimension() dimension}
     *                                  of {code x0} is different from the dimension of
     *                                  {@code dx}.
     *                                  If the dimension of {code x0} is different from the
     *                                  {@linkplain FunctionNTo1#getDimension() number of dimensions} of
     *                                  {@code f}.
     */
    @Nonnull
    public static Function1To1WithGradient createLineFunction(
            @Nonnull final FunctionNTo1WithGradient f,
            @Nonnull final ImmutableVectorN x0,
            @Nonnull final ImmutableVectorN dx
    ) {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(x0, "x0");
        Objects.requireNonNull(dx, "dx");
        final int n = x0.getDimension();
        if (n != dx.getDimension() || n != f.getDimension()) {
            throw new IllegalArgumentException("Inconsistent lengths, x0 " + n + ", dx " + dx.getDimension()
                    + ", f.dimensions " + f.getDimension());
        }

        return new Function1To1WithGradient() {

            @Override
            public String toString() {
                return f +
                        " along " +
                        x0 +
                        " + w*" +
                        dx;
            }

            @Override
            @Nonnull
            public Function1To1WithGradientValue value(final double w) {
                final ImmutableVectorN x = ImmutableVectorN.createOnLine(x0, dx, w);
                final FunctionNTo1WithGradientValue v = f.value(x);
                return new Function1To1WithGradientValue(w, v.getF(), v.getDfDx().dot(dx));
            }
        };
    }

    @Nonnull
    private static ImmutableVectorN downSlope(@Nonnull final FunctionNTo1WithGradientValue fx) {
        final ImmutableVectorN dfDx = fx.getDfDx();
        if (dfDx.magnitude2() < Double.MIN_NORMAL) {
            /* Avoid division by zero when close to a minimum */
            final double[] x = new double[fx.getX().getDimension()];
            x[0] = 1.0;
            return ImmutableVectorN.create(x);
        } else {
            return dfDx.minus();
        }
    }

    /**
     * <p>
     * Find a minimum of a {@linkplain FunctionNTo1WithGradient scalar function of a
     * vector that also has a computable gradient} using the Polak-Ribere's
     * modification of the Fletcher-Reeves conjugate gradient algorithm.
     * </p>
     *
     * @param f         The function for which a minimum is to be found.
     * @param x0        A point at which to start the search.
     * @param tolerance The convergence tolerance; the dimensionless measure of the
     *                  maximum error of the position of the minimum (the returned
     *                  {@linkplain FunctionNTo1WithGradientValue#getX() x} value).
     * @throws NullPointerException               If {@code f} is null.
     *                                            If {@code x0} is null.
     * @throws IllegalArgumentException           If the {@linkplain ImmutableVectorN#getDimension() dimension}
     *                                            of {code x0} is different from the
     *                                            {@linkplain FunctionNTo1#getDimension() dimension} of
     *                                            {@code f}.
     *                                            If {@code tolerance} is not in the range (0.0, 1.0).
     * @throws PoorlyConditionedFunctionException If {@code f} does not have a minimum
     *                                            If {@code f} has a minimum, but it is impossible to find
     *                                            using {@code x0} because the function has an odd-powered high
     *                                            order term that causes the iterative procedure to diverge.
     */
    @Nonnull
    public static FunctionNTo1WithGradientValue findFletcherReevesPolakRibere(
            @Nonnull final FunctionNTo1WithGradient f,
            @Nonnull final ImmutableVectorN x0,
            @Nonnegative final double tolerance
    )
            throws PoorlyConditionedFunctionException {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(x0, "x0");
        requireToleranceInRange(tolerance);
        final int n = f.getDimension();
        if (x0.getDimension() != n) {
            throw new IllegalArgumentException("Inconsistent dimensions f <" + n + "> x <" + x0.getDimension() + ">");
        }

        FunctionNTo1WithGradientValue fx = f.value(x0);
        ImmutableVectorN g = downSlope(fx);
        ImmutableVectorN dx = g;
        ImmutableVectorN h = g;
        double fScale = 0.0;

        while (true) {
            final ImmutableVectorN x = fx.getX();
            FunctionNTo1WithGradientValue fXNew;
            try {
                fXNew = minimiseAlongLine(f, x, dx);
            } catch (final PoorlyConditionedFunctionException e) {
                /*
                 * Can indicate that g has become a zero vector because we have reached the
                 * minimum.
                 */
                break;
            }
            final double df = fx.getF() - fXNew.getF();
            assert 0.0 <= df;
            fScale = Math.max(fScale, df);
            final double fTolerance = fScale * tolerance * tolerance * 0.5;
            if (df <= fTolerance) {
                fx = fXNew;
                break;// converged
            }

            final ImmutableVectorN gNew = downSlope(fXNew);
            final double gamma = gNew.minus(g).dot(gNew) / g.magnitude2();

            if (Math.abs(gamma) <= tolerance) {
                /*
                 * The gamma value is a dimensionless measure of the change in the search
                 * vector. When that becomes very small, the search vector is effectively zero,
                 * and we have located the minimum.
                 */
                fx = fXNew;
                break;// converged
            }
            final ImmutableVectorN hNew = ImmutableVectorN.createOnLine(gNew, h, gamma);

            g = gNew;
            h = hNew;
            fx = fXNew;
            dx = hNew;
        }
        return fx;
    }

    /**
     * <p>
     * Find a minimum of a {@linkplain FunctionNTo1 multidimensional function} using
     * <i>basic Powell's method</i> with periodic resetting of the search
     * directions.
     * </p>
     * <p>
     * This method is appropriate for a function that is approximately a quadratic
     * form.
     * </p>
     *
     * @param f         The function for which a minimum is to be found.
     * @param x         A point at which to start the search. The method changes this
     *                  value to record the minimum point.
     * @param tolerance The convergence tolerance; the minimum fractional change in the
     *                  value of the minimum for which continuing to iterate is
     *                  worthwhile.
     * @throws NullPointerException               If {@code f} is null.
     *                                            If {@code x} is null.
     * @throws IllegalArgumentException           If the length of {code x} is different from the
     *                                            {@linkplain FunctionNTo1#getDimension() number of dimensions} of
     *                                            {@code f}.
     *                                            If {@code tolerance} is not in the range (0.0, 1.0).
     * @throws PoorlyConditionedFunctionException If {@code f} does not have a minimum.
     *                                            If {@code f} has a minimum, but it is impossible to find
     *                                            using {@code x} because the function has an odd-powered high
     *                                            order term that causes the iterative procedure to diverge.
     */
    public static double findPowell(
            @Nonnull final FunctionNTo1 f,
            @Nonnull final double[] x,
            @Nonnegative final double tolerance
    )
            throws PoorlyConditionedFunctionException {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(x, "x");
        requireToleranceInRange(tolerance);
        final int n = f.getDimension();
        if (x.length != n) {
            throw new IllegalArgumentException("Inconsistent dimensions f <" + n + "> x <" + x.length + ">");
        }

        final double[] x0 = new double[n];
        final double[] dx0 = new double[n];
        final double[][] dx = new double[n][];
        for (int i = 0; i < n; ++i) {
            dx[i] = new double[n];
        }

        int iteration = 0;
        double min = Double.POSITIVE_INFINITY;
        while (true) {
            if (iteration % n == 0) {
                /*
                 * To prevent the search directions collapsing to a bundle of linearly dependent
                 * vectors, reset them to the basis vectors.
                 */
                resetSearchDirections(dx);
            }

            final double minNext = basicPowell(f, x0, dx0, x, dx);
            assert minNext <= min;
            final double dMin = minNext - min;
            min = minNext;
            iteration++;
            if (n <= iteration && dMin <= min * tolerance) {
                break;
            }
        }

        return min;
    }

    /**
     * <p>
     * Perform <i>line minimisation</i> of a {@linkplain FunctionNTo1 multidimensional
     * function}.
     * </p>
     * <p>
     * That is, find the minimum value of the function along a straight line.
     * </p>
     * <ul>
     * <li>Moves the point on the line ({@code x}) to the position of the
     * minimum found.</li>
     * <li>Sets the direction vector to the amount the point of the line was
     * moved from the original position to the position of the minimum.</li>
     * </ul>
     *
     * @param f  The multi-dimensional function
     * @param x  A point on the line.
     * @param dx The direction vector of the line.
     * @throws NullPointerException               If {@code f} is null.
     *                                            If {@code x} is null.
     *                                            If {@code dx} is null.
     * @throws IllegalArgumentException           If the length of {code x} is 0.
     *                                            If the length of {code x} is different from the length of
     *                                            {@code dx}.
     *                                            If the length of {code x} is different from the
     *                                            {@linkplain FunctionNTo1#getDimension() number of dimensions} of
     *                                            {@code f}.
     * @throws PoorlyConditionedFunctionException If {@code f} does not have a minimum.
     *                                            If {@code f} has a minimum, but it is impossible to find a
     *                                            bracket for {@code f} using {@code x} and {@code dx} because the
     *                                            function has an odd-powered high order term that causes the
     *                                            iterative procedure to diverge.
     *                                            The magnitude of {@code dx} is zero (or very small).
     */
    static double minimiseAlongLine(
            @Nonnull final FunctionNTo1 f,
            @Nonnull final double[] x,
            @Nonnull final double[] dx
    )
            throws PoorlyConditionedFunctionException {
        final Function1To1 fLine = createLineFunction(f, x, dx);
        final Min1To1.Bracket bracket = Min1To1.findBracket(fLine, 0.0, 1.0);
        final Function1To1Value p = Min1To1.findBrent(fLine, bracket, Min1To1.TOLERANCE);
        final double w = p.x();
        for (int i = 0, n = x.length; i < n; i++) {
            final double dxi = dx[i] * w;
            dx[i] = dxi;
            x[i] += dxi;
        }
        return p.f();
    }

    /**
     * <p>
     * Perform <i>line minimisation</i> of a {@linkplain FunctionNTo1WithGradient
     * scalar function of a vector that also has a computable gradient}.
     * </p>
     * <p>
     * That is, find the minimum value of the function along a straight line.
     * </p>
     *
     * @param f  The function
     * @param x  A point on the line.
     * @param dx The direction vector of the line.
     * @throws NullPointerException               If {@code f} is null.
     *                                            If {@code x} is null.
     *                                            If {@code dx} is null.
     * @throws IllegalArgumentException           If the length of {code x} is 0.
     *                                            If the length of {code x} is different from the length of
     *                                            {@code dx}.
     *                                            If the length of {code x} is different from the
     *                                            {@linkplain FunctionNTo1#getDimension() number of dimensions} of
     *                                            {@code f}.
     * @throws PoorlyConditionedFunctionException If {@code f} does not have a minimum.
     *                                            If {@code f} has a minimum, but it is impossible to find a
     *                                            bracket for {@code f} using {@code x} and {@code dx} because the
     *                                            function has an odd-powered high order term that causes the
     *                                            iterative procedure to diverge.
     *                                            The magnitude of {@code dx} is zero (or very small).
     */
    @Nonnull
    static FunctionNTo1WithGradientValue minimiseAlongLine(
            @Nonnull final FunctionNTo1WithGradient f,
            @Nonnull final ImmutableVectorN x,
            @Nonnull final ImmutableVectorN dx
    )
            throws PoorlyConditionedFunctionException {
        final Function1To1WithGradient fLine = createLineFunction(f, x, dx);
        final Function1To1 f1Line = new Function1To1() {

            @Override
            public String toString() {
                return fLine.toString();
            }

            @Override
            public double value(final double x) {
                return fLine.value(x).f();
            }

        };
        final Min1To1.Bracket bracket = Min1To1.findBracket(f1Line, 0.0, 1.0);
        final Function1To1WithGradientValue p = Min1To1.findBrent(fLine, bracket, Min1To1.TOLERANCE);
        final ImmutableVectorN xMin = ImmutableVectorN.createOnLine(x, dx, p.x());
        return f.value(xMin);
    }

    private static void requireToleranceInRange(final double tolerance) {
        if (!(0.0 < tolerance && tolerance < 1.0)) {
            throw new IllegalArgumentException("tolerance <" + tolerance + ">");
        }
    }

    private static void resetSearchDirections(@Nonnull final double[][] dx) {
        final int n = dx.length;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                dx[i][j] = i == j ? 1 : 0;
            }
        }
    }

}
