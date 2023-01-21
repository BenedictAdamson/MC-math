package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018,22-23.
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
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * Functions and auxiliary classes for minimization of a {@linkplain Function1To1
 * one dimensional function}.
 * </p>
 */
public final class Min1 {

    /**
     * <p>
     * The typical fractional tolerance possible for minimization of a
     * {@linkplain Function1To1 one dimensional function}.
     * </p>
     * <p>
     * If the true minimum occurs where the variable has value <var>x</var>, a
     * numeric procedure will locate the minimum with an accuracy no better than
     * this tolerance multiplied by <var>x</var>.
     * </p>
     */
    public static final double TOLERANCE = Math.sqrt(Math.nextAfter(1.0, 2.0) - 1.0);
    private static final double GOLD = (1.0 + Math.sqrt(5.0)) * 0.5;
    private static final double MAX_STEP = 100;

    private Min1() {
        throw new AssertionError("Class should not be instantiated");
    }

    /*
     * Rounding errors mean that it is never worthwhile taking tiny steps. Instead,
     * try stepping into the largest interval.
     */
    private static double avoidTinyStep(final double xNew, final double xTolerance, final double xLeft,
                                        final double xInner, final double xRight) {
        final double dx = xNew - xInner;
        if (Math.abs(dx) < xTolerance) {
            return stepIntoLargestInterval(xLeft, xInner, xRight);
        } else {
            return xNew;
        }
    }

    private static double bisect(final Function1WithGradientValue left, final Function1WithGradientValue inner,
                                 final Function1WithGradientValue right, final double xTolerance, final double fTolerance) {
        final double xi = inner.x();
        final double dfdxi = inner.dfdx();
        if (fTolerance / xTolerance < Math.abs(dfdxi)) {
            /*
             * Use the gradient at the inner point to guess which side probably contains the
             * minimum
             */
            if (0.0 <= dfdxi) {
                return xi + (left.x() - xi) * 0.5;
            } else {
                return xi + (right.x() - xi) * 0.5;
            }
        } else {
            /*
             * Gradient is very small (the inner point is very close to the minimum), and so
             * does not point clearly to one side or the other. Instead, try stepping into
             * the largest interval, to more dramatically reduce the bracket size.
             */
            return stepIntoLargestInterval(left.x(), xi, right.x());
        }
    }

    /**
     * <p>
     * Find a {@linkplain Min1.Bracket bracket} of a {@linkplain Function1To1 one
     * dimensional function of a continuous variable}, given two values of the
     * continuous variable.
     * </p>
     * <p>
     * The method optimistically assumes that the two given values are close to a
     * minimum, but copes if they are not near the minimum. The function will
     * therefore be more efficient if the two given values <em>are</em> close to a
     * minimum.
     * </p>
     * <ul>
     * <li>The returned bracket has {@linkplain Function1To1Value#f() y (ordinate)}
     * values calculated from the corresponding {@linkplain Function1To1Value#x() x
     * (abscissa)} values using the given function.</li>
     * </ul>
     *
     * @param f  The function for which a bracket is to be found.
     * @param x1 A guess for the position of a minimum
     * @param x2 A second guess for the position of the minimum
     * @throws NullPointerException               If {@code f} is null.
     * @throws IllegalArgumentException           If {@code x1} equals {@code x2}.
     *                                            If {@code x1} is {@linkplain Double#isNaN(double) is not a number}.
     *                                            If {@code x2} is not a number.
     * @throws PoorlyConditionedFunctionException If {@code f} does not have a minimum.
     *                                            If {@code f} has a minimum, but it is impossible to find a
     *                                            bracket for {@code f} using {@code x1} and {@code x2} because the
     *                                            function has an odd-powered high order term that causes the
     *                                            iterative procedure to diverge.
     */
    @Nonnull
    public static Bracket findBracket(@Nonnull final Function1To1 f, final double x1, final double x2)
            throws PoorlyConditionedFunctionException {
        Objects.requireNonNull(f, "f");
        if (x1 == x2) {
            throw new IllegalArgumentException("x1 == x2 <" + x1 + ">");
        }
        if (Double.isNaN(x1)) {
            throw new IllegalArgumentException("x1 NAN <" + x1 + ">");
        }
        if (Double.isNaN(x2)) {
            throw new IllegalArgumentException("x2 NAN <" + x2 + ">");
        }

        Function1To1Value p1 = Function1To1Value.createFor(f, x1);
        Function1To1Value p2 = Function1To1Value.createFor(f, x2);
        if (p1.f() < p2.f()) {
            // Swap
            final Function1To1Value pTemp = p1;
            p1 = p2;
            p2 = pTemp;
        }
        assert !(p1.f() < p2.f());
        /*
         * First guess is to step in the same direction:
         */
        Function1To1Value p3 = stepFurther(f, p1, p2);

        while (p3.f() <= p2.f() || p1.f() <= p2.f()) {
            final double xLimit = p2.x() + MAX_STEP * (p3.x() - p2.x());
            final double xNew = parabolicExtrapolation(p1, p2, p3);
            if (isBetween(p2.x(), xNew, p3.x())) {
                final double fNew = f.value(xNew);
                if (fNew < p3.f()) {
                    // Found a minimum between p2 and p3
                    // !Double.isNaN(fNew);
                    p1 = p2;
                    p2 = new Function1To1Value(xNew, fNew);
                    // p3 unchanged
                    break;
                } else if (p2.f() < fNew) {
                    /*
                     * Function has higher order terms. We have p1.y > p2.y and fNew > p2.y, which
                     * can form a bracket
                     */
                    // !Double.isNaN(fNew);
                    // p1 unchanged
                    // p2 unchanged
                    p3 = new Function1To1Value(xNew, fNew);
                    break;
                } else {
                    /*
                     * Parabolic fit failed; Step even further in the same direction and try again.
                     */
                    final Function1To1Value pNew = stepFurther(f, p2, p3);
                    assert !Double.isNaN(pNew.f());
                    p1 = p2;
                    p2 = p3;
                    p3 = pNew;
                }
            } else if (isBetween(p3.x(), xNew, xLimit)) {
                /* Extrapolation is not excessive. */
                final Function1To1Value pNew = Function1To1Value.createFor(f, xNew);
                assert !Double.isNaN(pNew.f());
                if (pNew.f() < p3.f()) {
                    /*
                     * Have stepped further down hill; continue stepping.
                     */
                    p1 = p2;
                    p2 = p3;
                    p3 = pNew;
                } else if (p3.f() < p2.f()) {
                    /*
                     * Function has higher order terms. We have p3.y < p2.y and p3.y < pNew.y, which
                     * can form a bracket.
                     */
                    return new Bracket(p2, p3, pNew);
                } else {
                    /*
                     * We have p2.y < p1.y and p3.y = p2.y and p3.y <= fNew. Unclear where the
                     * minimum might be, but is perhaps between p2 and p3 (it will be, if the higher
                     * order terms do not contribute). If we use (p2, p3, pNew) as our next guess,
                     * parabolic extrapolation will guess a point between p2 and p3. Hard to test.
                     */
                    p1 = p2;
                    p2 = p3;
                    p3 = pNew;
                }
            } else if (isBeyond(p3.x(), xLimit, xNew)) {
                /*
                 * Extrapolation was excessive; clamp to the limit. In particular, this handles
                 * cases when the extrapolated value is infinite. Assume it is nevertheless a
                 * step in the right direction
                 */
                final Function1To1Value pNew = Function1To1Value.createFor(f, xLimit);
                assert !Double.isNaN(pNew.f());
                p1 = p2;
                p2 = p3;
                p3 = pNew;
            } else if (isBetween(p1.x(), xNew, p2.x())) {
                final double fNew = f.value(xNew);
                if (fNew < p1.f() && fNew < p2.f()) {
                    /* p1, pNew, p2 form a bracket. */
                    // p1 unchanged
                    p3 = p2;
                    p2 = new Function1To1Value(xNew, fNew);
                    break;
                } else {
                    /*
                     * xNew seems to be near a local maximum. Step even further in the same
                     * direction to try to get away from it.
                     */
                    final Function1To1Value pNew = stepFurther(f, p2, p3);
                    assert !Double.isNaN(pNew.f());
                    p1 = p2;
                    p2 = p3;
                    p3 = pNew;
                }
            } else {// xNew <= p1.x
                /*
                 * Parabolic extrapolation stepped backwards. Step even further in the same
                 * direction and try again.
                 */
                final Function1To1Value pNew = stepFurther(f, p2, p3);
                assert !Double.isNaN(pNew.f());
                p1 = p2;
                p2 = p3;
                p3 = pNew;
            }
        }
        if (p1.x() < p2.x()) {
            return new Bracket(p1, p2, p3);
        } else {
            return new Bracket(p3, p2, p1);
        }
    }

    /**
     * <p>
     * Find a minimum of a {@linkplain Function1To1 one dimensional function} using
     * <i>Brent's method</i>.
     * </p>
     * <ul>
     * <li>The returned point is consistent with the given function.</li>
     * <li>The {@linkplain Function1To1Value#f() codomain value} of the returned
     * point is not larger than the {@linkplain Bracket#getMin() minimum value} of
     * the given bracket.</li>
     * </ul>
     *
     * @param f         The function for which a minimum is to be found.
     * @param bracket   A bracket of the minimum to be found.
     * @param tolerance The convergence tolerance. This should not normally exceed the
     *                  {@linkplain #TOLERANCE recommended minimum tolerance}.
     * @return a minimum of the function.
     * @throws NullPointerException     If {@code f} is null.
     *                                  If {@code bracket} is null.
     * @throws IllegalArgumentException If {@code tolerance} is not in the range (0.0, 1.0).
     *                                  (Optional) if {@code bracket} is not consistent with
     *                                  {@code f}.
     */
    @Nonnull
    public static Function1To1Value findBrent(
            @Nonnull final Function1To1 f,
            @Nonnull final Bracket bracket,
            @Nonnegative final double tolerance) {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(bracket, "bracket");
        if (!(0.0 < tolerance && tolerance < 1.0)) {
            throw new IllegalArgumentException("tolerance <" + tolerance + ">");
        }

        Function1To1Value left = bracket.getLeft();
        Function1To1Value inner = bracket.getInner();
        Function1To1Value right = bracket.getRight();
        Function1To1Value secondLeast;
        Function1To1Value previousSecondLeast;
        if (left.f() < right.f()) {
            secondLeast = left;
            previousSecondLeast = right;
        } else {
            secondLeast = right;
            previousSecondLeast = left;
        }
        double width = right.x() - left.x();
        // Recent step sizes (start with guesses)
        double dx2 = width * (GOLD - 1.0);
        double dx3 = dx2 * GOLD;

        while (true) {
            final double xTolerance = Double.max(Math.abs(inner.x()) * tolerance, Double.MIN_NORMAL);
            if (width <= xTolerance * 2.0) {
                // Converged
                break;
            }
            double xNew;
            if (xTolerance < Math.abs(dx3)) {
                /*
                 * We are making big steps, which are not close to the round-off limits, so
                 * parabolic extrapolation is worth trying.
                 */
                xNew = parabolicExtrapolation(inner, secondLeast, previousSecondLeast);
                /*
                 * But did parabolic extrapolation produce a reliable result? If not, fall back
                 * to golden section search.
                 */
                if (!Double.isFinite(xNew) || xNew <= left.x() || right.x() <= xNew
                        || Math.abs(dx3) * 0.5 <= Math.abs(xNew - inner.x())) {
                    xNew = goldenSectionSearch(left, inner, right);
                }
            } else {
                /*
                 * We are making small steps, which can be vulnerable to round-off error. So use
                 * golden section search instead.
                 */
                xNew = goldenSectionSearch(left, inner, right);
            }
            xNew = avoidTinyStep(xNew, xTolerance, left.x(), inner.x(), right.x());
            final double dx = xNew - inner.x();
            assert left.x() < xNew && xNew < right.x();
            final Function1To1Value pNew = Function1To1Value.createFor(f, xNew);
            if (pNew.f() < inner.f()) {
                // Found a new minimum.
                previousSecondLeast = secondLeast;
                secondLeast = inner;
                if (xNew < inner.x()) {
                    // To the left of the current minimum
                    // left unchanged
                    right = inner;
                } else {
                    // To the right of the current minimum
                    // right unchanged
                    left = inner;
                }
                inner = pNew;
            } else {
                /*
                 * Not a new minimum; but have narrowed the bracket.
                 */
                if (xNew < inner.x()) {
                    // To the left of the current minimum
                    left = pNew;
                } else {
                    right = pNew;
                }
                /*
                 * And might be a new value for the second-smallest value.
                 */
                if (pNew.f() < secondLeast.f()) {
                    previousSecondLeast = secondLeast;
                    secondLeast = pNew;
                }
            }
            dx3 = dx2;
            dx2 = dx;
            width = right.x() - left.x();
        }

        return inner;
    }

    /**
     * <p>
     * Find a minimum of a {@linkplain Function1To1WithGradient one dimensional
     * function that also has a computable gradient}, using <i>Brent's method</i>.
     * </p>
     * <ul>
     * <li>The returned point is consistent with the given function.</li>
     * <li>The {@linkplain Function1WithGradientValue#f() codomain value} of the
     * returned point is not larger than the {@linkplain Bracket#getMin() minimum
     * value} of the given bracket.</li>
     * </ul>
     *
     * @param f         The function for which a minimum is to be found.
     * @param bracket   A bracket of the minimum to be found.
     * @param tolerance The convergence tolerance. This should not normally exceed the
     *                  {@linkplain #TOLERANCE recommended minimum tolerance}.
     * @return a minimum of the function.
     * @throws NullPointerException     <ul>
     *                                                                                                                                                 <li>If {@code f} is null.</li>
     *                                                                                                                                                 <li>If {@code bracket} is null.</li>
     *                                                                                                                                                 </ul>
     * @throws IllegalArgumentException <ul>
     *                                                                                                                                                 <li>If {@code tolerance} is not in the range (0.0, 1.0).</li>
     *                                                                                                                                                 <li>(Optional) if {@code bracket} is not consistent with
     *                                                                                                                                                 {@code f}.</li>
     *                                                                                                                                                 </ul>
     */
    @Nonnull
    public static Function1WithGradientValue findBrent(
            @Nonnull final Function1To1WithGradient f,
            @Nonnull final Bracket bracket,
            @Nonnegative final double tolerance) {
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(bracket, "bracket");
        if (!(0.0 < tolerance && tolerance < 1.0)) {
            throw new IllegalArgumentException("tolerance <" + tolerance + ">");
        }

        Function1WithGradientValue left = f.value(bracket.getLeft().x());
        Function1WithGradientValue inner = f.value(bracket.getInner().x());
        Function1WithGradientValue right = f.value(bracket.getRight().x());
        Function1WithGradientValue secondLeast;
        Function1WithGradientValue previousSecondLeast;
        if (left.f() < right.f()) {
            secondLeast = left;
            previousSecondLeast = right;
        } else {
            secondLeast = right;
            previousSecondLeast = left;
        }
        double width = right.x() - left.x();
        // Recent step sizes (start with guesses)
        double dx2 = width * (GOLD - 1.0);
        double dx3 = dx2 * GOLD;

        while (true) {
            final double xTolerance = Double.max(Math.abs(inner.x()) * tolerance, Double.MIN_NORMAL);
            final double fTolerance = Double.max(Math.abs(inner.f()) * tolerance, Double.MIN_NORMAL);
            if (width <= xTolerance * 2.0) {
                // Converged
                break;
            }
            double xNew;
            if (2.0 * xTolerance < Math.abs(dx3)) {
                /*
                 * We are making big steps, which are not close to the round-off limits, so
                 * secant extrapolation of the gradient is worth trying.
                 */
                final double xNew1 = secantGradientExtrapolation(inner, secondLeast);
                final double xNew2 = secantGradientExtrapolation(inner, previousSecondLeast);
                /*
                 * But did the extrapolation produce reliable results?
                 */
                final boolean ok1 = secantExtrapolationOk(xNew1, left, inner, right);
                final boolean ok2 = secantExtrapolationOk(xNew2, left, inner, right);
                /*
                 * Choose between the two options.
                 */
                if (ok1 && ok2) {
                    /*
                     * Secant extrapolation seems to produce good results. Prefer the smallest
                     * change
                     */
                    final double dxNew1 = xNew1 - inner.x();
                    final double dxNew2 = xNew2 - inner.x();
                    final double dxNew;
                    if (Math.abs(dxNew1) <= Math.abs(dxNew2)) {
                        xNew = xNew1;
                        dxNew = dxNew1;
                    } else {
                        xNew = xNew2;
                        dxNew = dxNew2;
                    }
                    if (Math.abs(dxNew) <= xTolerance) {
                        /*
                         * However, if that is a very small step, that suggests we have found the
                         * minimum. All we need to do now is to narrow the bracket by making a small
                         * step into the biggest interval.
                         */
                        final double xr = right.x() - inner.x();
                        final double xl = inner.x() - left.x();
                        if (xl < xr) {
                            xNew = inner.x() + Math.min(0.5 * xr, xTolerance);
                        } else {
                            xNew = inner.x() - Math.min(0.5 * xl, xTolerance);
                        }
                    }
                } else if (ok1) {// && !ok2
                    xNew = xNew1;
                    xNew = avoidTinyStep(xNew, xTolerance, left.x(), inner.x(), right.x());
                } else if (ok2) {// && !ok1
                    xNew = xNew2;
                    xNew = avoidTinyStep(xNew, xTolerance, left.x(), inner.x(), right.x());
                } else { // !ok1 && !ok2
                    /*
                     * Fall back to bisection
                     */
                    xNew = bisect(left, inner, right, xTolerance, fTolerance);
                    xNew = avoidTinyStep(xNew, xTolerance, left.x(), inner.x(), right.x());
                }
            } else {
                /*
                 * We are making small steps, which can be vulnerable to round-off error. So use
                 * bisection instead.
                 */
                xNew = bisect(left, inner, right, xTolerance, fTolerance);
                xNew = avoidTinyStep(xNew, xTolerance, left.x(), inner.x(), right.x());
            }
            final double dx = xNew - inner.x();
            assert left.x() < xNew && xNew < right.x();
            final Function1WithGradientValue pNew = f.value(xNew);
            if (pNew.f() < inner.f()) {
                // Found a new minimum.
                previousSecondLeast = secondLeast;
                secondLeast = inner;
                if (xNew < inner.x()) {
                    // To the left of the current minimum
                    // left unchanged
                    right = inner;
                } else {
                    // To the right of the current minimum
                    // right unchanged
                    left = inner;
                }
                inner = pNew;
            } else {
                /*
                 * Not a new minimum; but have narrowed the bracket.
                 */
                if (xNew < inner.x()) {
                    left = pNew;
                } else {
                    right = pNew;
                }
                /*
                 * And might be a new value for the second-smallest value.
                 */
                if (pNew.f() < secondLeast.f()) {
                    previousSecondLeast = secondLeast;
                    secondLeast = pNew;
                }
            }
            dx3 = dx2;
            dx2 = dx;
            width = right.x() - left.x();
        }

        return inner;
    }

    private static double goldenSectionSearch(
            @Nonnull final Function1To1Value p1,
            @Nonnull final Function1To1Value p2,
            @Nonnull final Function1To1Value p3) {
        final double x2 = p2.x();
        final double x21 = x2 - p1.x();
        final double x32 = p3.x() - x2;
        assert 0.0 < x21;
        assert 0.0 < x32;
        if (x21 <= x32) {
            return x2 + (2.0 - GOLD) * x32;
        } else {
            return x2 - (2.0 - GOLD) * x21;
        }
    }

    private static boolean isBetween(final double x1, final double x2, final double x3) {
        if (x1 < x3) {
            return x1 < x2 && x2 < x3;
        } else {
            return x3 < x2 && x2 < x1;
        }
    }

    private static boolean isBeyond(final double x1, final double x2, final double x3) {
        if (x1 < x2) {
            return x2 < x3;
        } else {
            return x3 < x1;
        }
    }

    private static double parabolicExtrapolation(
            @Nonnull final Function1To1Value p1,
            @Nonnull final Function1To1Value p2,
            @Nonnull final Function1To1Value p3) {
        final double x2 = p2.x();
        final double y2 = p2.f();
        final double x21 = x2 - p1.x();
        final double x23 = x2 - p3.x();
        final double y23 = y2 - p3.f();
        final double y21 = y2 - p1.f();
        final double x21y23 = x21 * y23;
        final double x23y21 = x23 * y21;
        return x2 - (x21 * x21y23 - x23 * x23y21) / (2.0 * (x21y23 - x23y21));
    }

    private static boolean secantExtrapolationOk(
            final double xNew,
            @Nonnull final Function1WithGradientValue left,
            @Nonnull final Function1WithGradientValue inner,
            @Nonnull final Function1WithGradientValue right) {
        return left.x() < xNew && xNew < right.x() && (xNew - inner.x()) * inner.dfdx() <= 0;
    }

    private static double secantGradientExtrapolation(
            @Nonnull final Function1WithGradientValue p1,
            @Nonnull final Function1WithGradientValue p2) {
        final double x1 = p1.x();
        final double x2 = p2.x();
        final double y1 = p1.dfdx();
        final double y2 = p2.dfdx();

        final double y21 = y2 - y1;
        final double x21 = x2 - x1;

        final double dxdy = x21 / y21;

        return x1 - y1 * dxdy;
    }

    @Nonnull
    private static Function1To1Value stepFurther(
            @Nonnull final Function1To1 f,
            @Nonnull final Function1To1Value p1,
            @Nonnull final Function1To1Value p2
    )
            throws PoorlyConditionedFunctionException {
        final double x2 = p2.x();
        final double dx = x2 - p1.x();
        double r = GOLD;
        double xNew;
        double fNew;
        do {
            xNew = x2 + r * dx;
            fNew = f.value(xNew);
            r *= 0.5;
        } while (Double.isNaN(fNew) && 0.0 < r && Double.doubleToLongBits(xNew) != Double.doubleToLongBits(x2));
        if (Double.doubleToLongBits(xNew) != Double.doubleToLongBits(x2)) {
            return new Function1To1Value(xNew, fNew);
        } else {
            throw new PoorlyConditionedFunctionException(f);
        }
    }

    /*
     * This is a fall-back for when more clever means of selecting the next value
     * for which to evaluate the function have failed. Should therefore be cautious
     * about being clever. So, simply bisect the largest interval.
     */
    private static double stepIntoLargestInterval(final double xLeft, final double xInner, final double xRight) {
        double dx;
        final double xr = xRight - xInner;
        final double xl = xInner - xLeft;
        if (xl <= xr) {
            dx = 0.5 * xr;
        } else {
            dx = -0.5 * xl;
        }
        return xInner + dx;
    }

    /**
     * <p>
     * A bracket of the minimum of a {@linkplain Function1To1 one dimensional
     * function}.
     * </p>
     * <p>
     * A Bracket indicates a range of values within which the minimum is known to be
     * located.
     * </p>
     * <p>
     * Each iteration of an iterative minimisation methods computes a new, smaller,
     * bracket located within the previous bracket. A complication of iterative
     * minimisation methods is that iterations should remember some of the previous
     * function evaluations. It is therefore useful if the bracket records the
     * function evaluations for the end points of the range of values. A further
     * complication is that, to be sure we have a minimum within the bracket, we
     * need to know that, somewhere within the range of values, there is a function
     * value smaller than the values at the end points of the range. Furthermore, it
     * is is useful to retain the position and the function value for that point.
     * Hence a bracket has three {@linkplain Function1To1Value points}:
     *
     * </p>
     */
    @Immutable
    public static final class Bracket {
        @Nonnull
        private final Function1To1Value left;
        @Nonnull
        private final Function1To1Value inner;
        @Nonnull
        private final Function1To1Value right;

        /**
         * <p>
         * Construct a Bracket having given attribute values.
         * </p>
         *
         * <section>
         * <h4>Post Conditions</h4>
         * <ul>
         * <li>The constructed bracket has the given attribute values.</li>
         * </ul>
         * </section>
         *
         * @param left  The leftmost point of this Bracket.
         * @param inner The inner point of this Bracket.
         * @param right The right point of this Bracket.
         * @throws NullPointerException     If {@code left} is null.
         *                                  If {@code inner} is null.
         *                                  If {@code right} is null.
         * @throws IllegalArgumentException If {@code inner} is not to the right of {@code left}.
         *                                  If {@code inner} is not below {@code left}.
         *                                  If {@code right} is not to the right of {@code inner}.
         *                                  If {@code right} point is not above {@code inner}.
         */
        public Bracket(
                @Nonnull final Function1To1Value left,
                @Nonnull final Function1To1Value inner,
                @Nonnull final Function1To1Value right
        ) {
            Objects.requireNonNull(left, "left");
            Objects.requireNonNull(inner, "inner");
            Objects.requireNonNull(right, "right");
            /* Attention: precondition checks must handle NaN values. */
            final double innerX = inner.x();
            final double innerY = inner.f();
            if (!(left.x() < innerX)) {
                throw new IllegalArgumentException("inner <" + inner + "> not to the right of left <" + left + ">");
            }
            if (!(innerX < right.x())) {
                throw new IllegalArgumentException("right <" + right + "> not to the right of inner <" + inner + ">");
            }
            if (!(innerY < left.f())) {
                throw new IllegalArgumentException("inner <" + inner + "> not below left <" + left + ">");
            }
            if (!(innerY < right.f())) {
                throw new IllegalArgumentException("inner <" + inner + "> not below right <" + right + ">");
            }
            this.left = left;
            this.inner = inner;
            this.right = right;
        }

        /**
         * <p>
         * Whether this object is <dfn>equivalent</dfn> another object.
         * </p>
         * <p>
         * The {@link Min1.Bracket} class has <i>value semantics</i>: this object is
         * equivalent to another object if, and only if, the other object is also a
         * {@link Min1.Bracket} object, and thw two objects have equivalent attributes.
         * </p>
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Bracket other = (Bracket) obj;
            return left.equals(other.left) && inner.equals(other.inner) && right.equals(other.right);
        }

        /**
         * <p>
         * The inner point of this Bracket.
         * </p>
         * <ul>
         * <li>The inner point is to the right of the {@linkplain #getLeft() leftmost
         * point}; it has a larger {@linkplain Function1To1Value#x() abscissa}.</li>
         * <li>The inner point is below the leftmost point; it has a smaller
         * {@linkplain Function1To1Value#f() ordinate}.</li>
         * </ul>
         */
        @Nonnull
        public Function1To1Value getInner() {
            return inner;
        }

        /**
         * <p>
         * The leftmost point of this Bracket.
         * </p>
         */
        @Nonnull
        public Function1To1Value getLeft() {
            return left;
        }

        /**
         * <p>
         * The smallest function value of the points constituting this bracket.
         * </p>
         * <ul>
         * <li>The smallest function value of the points constituting this bracket is
         * the {@linkplain Function1To1Value#f() y value (ordinate)} of the
         * {@linkplain #getInner() inner point} of the bracket.</li>
         * </ul>
         */
        public double getMin() {
            return inner.f();
        }

        /**
         * <p>
         * The right point of of this Bracket.
         * </p>
         * <ul>
         * <li>The rightmost point is to the right of the {@linkplain #getInner() inner
         * point}; it has a smaller {@linkplain Function1To1Value#x() abscissa}.</li>
         * <li>The rightmost point is above the inner point; it has a larger
         * {@linkplain Function1To1Value#f() ordinate}.</li>
         * </ul>
         */
        @Nonnull
        public Function1To1Value getRight() {
            return right;
        }

        /**
         * <p>
         * The size of this bracket.
         * </p>
         * <ul>
         * <li>The width of a bracket is always positive.</li>
         * <li>The width is the difference between the {@linkplain Function1To1Value#x()
         * x coordinate (abscissa)} of the {@linkplain #getRight() rightmost} point and
         * the x coordinate of the {@linkplain #getLeft() leftmost} point.</li>
         * </ul>
         *
         * @return the width
         */
        @Nonnegative
        public double getWidth() {
            return right.x() - left.x();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + left.hashCode();
            result = prime * result + inner.hashCode();
            result = prime * result + right.hashCode();
            return result;
        }

    }

}
