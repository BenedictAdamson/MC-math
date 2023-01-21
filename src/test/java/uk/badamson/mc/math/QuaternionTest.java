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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

/**
 * <p>
 * Unit tests of the {@link Quaternion} class.
 * </p>
 */
@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public class QuaternionTest {

    private static class IsCloseTo extends TypeSafeMatcher<Quaternion> {
        private final double tolerance;
        private final Quaternion value;

        private IsCloseTo(final Quaternion value, final double tolerance) {
            this.tolerance = tolerance;
            this.value = value;
        }

        @Override
        public void describeMismatchSafely(final Quaternion item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" differed by ")
                    .appendValue(distance(item));
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a quaternion within ").appendValue(tolerance).appendText(" of ")
                    .appendValue(value);
        }

        private double distance(final Quaternion item) {
            return value.distance(item);
        }

        @Override
        public boolean matchesSafely(final Quaternion item) {
            return distance(item) <= tolerance;
        }
    }// class

    public static void assertInvariants(final Quaternion q) {
        ObjectVerifier.assertInvariants(q);// inherited
    }

    public static void assertInvariants(final Quaternion q1, final Quaternion q2) {
        ObjectVerifier.assertInvariants(q1, q2);// inherited

        final boolean equals = q1.equals(q2);
        assertFalse(equals && Double.doubleToLongBits(q1.getA()) != Double.doubleToLongBits(q2.getA()),
                "Equality requires equivalent attributes, a");
        assertFalse(equals && Double.doubleToLongBits(q1.getB()) != Double.doubleToLongBits(q2.getB()),
                "Equality requires equivalent attributes, b");
        assertFalse(equals && Double.doubleToLongBits(q1.getC()) != Double.doubleToLongBits(q2.getC()),
                "Equality requires equivalent attributes, c");
        assertFalse(equals && Double.doubleToLongBits(q1.getD()) != Double.doubleToLongBits(q2.getD()),
                "Equality requires equivalent attributes, d");
    }

    public static Matcher<Quaternion> closeToQuaternion(final Quaternion operand, final double tolerance) {
        return new IsCloseTo(operand, tolerance);
    }

    private static Quaternion conjugate(final Quaternion q) {
        final Quaternion c = q.conjugate();

        assertNotNull(c, "Not null, conjugate");// guard
        assertInvariants(q);
        assertInvariants(c);
        assertInvariants(q, c);

        assertEquals(q, c.conjugate(), "Conjugation is an involution (self inverse)");
        assertEquals(q.getA(), c.getA(), Double.MIN_NORMAL, "conjugate a");
        assertEquals(-q.getB(), c.getB(), Double.MIN_NORMAL, "conjugate b");
        assertEquals(-q.getC(), c.getC(), Double.MIN_NORMAL, "conjugate c");
        assertEquals(-q.getD(), c.getD(), Double.MIN_NORMAL, "conjugate d");

        return c;
    }

    public static Quaternion conjugation(final Quaternion q, final Quaternion p) {
        final double tolerance = Math.max(q.norm() * p.norm(), Double.MIN_NORMAL);
        final Quaternion expected = q.product(p).product(q.reciprocal());

        final Quaternion c = q.conjugation(p);

        assertNotNull(c, "Not null, result");
        assertInvariants(q);// check for side effects
        assertInvariants(p);// check for side effects
        assertInvariants(p, q);// check for side effects
        assertInvariants(c);
        assertInvariants(p, c);
        assertInvariants(q, c);
        assertTrue(expected.distance(c) < tolerance, "Expected result");

        return c;
    }

    private static Quaternion constructor(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        assertInvariants(q);
        assertEquals(Double.doubleToLongBits(a), Double.doubleToLongBits(q.getA()), "a");
        assertEquals(Double.doubleToLongBits(b), Double.doubleToLongBits(q.getB()), "b");
        assertEquals(Double.doubleToLongBits(c), Double.doubleToLongBits(q.getC()), "c");
        assertEquals(Double.doubleToLongBits(d), Double.doubleToLongBits(q.getD()), "d");

        return q;
    }

    private static void constructor_2equivalent(final double a, final double b, final double c, final double d) {
        final Quaternion q1 = Quaternion.create(a, b, c, d);
        final Quaternion q2 = Quaternion.create(a, b, c, d);

        assertInvariants(q1, q2);
        assertEquals(q1, q2);
    }

    private static double distance(final Quaternion q, final Quaternion that) {
        final double d = q.distance(that);

        assertInvariants(q);

        return d;
    }

    private static void distance_0(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        final double distance = distance(q, Quaternion.ZERO);

        assertEquals(q.norm(), distance, Double.MIN_NORMAL, "The distance of a quaternion from zero is its norm");
    }

    private static void distance_self(final double a, final double b, final double c, final double d) {
        final Quaternion p = Quaternion.create(a, b, c, d);

        final double distance = distance(p, p);

        assertEquals(0.0, distance, Double.MIN_NORMAL, "The distance of a quaternion from itself is zero");
    }

    private static double dot(final Quaternion q, final Quaternion that) {
        final double p = q.dot(that);

        assertInvariants(q);// check for side effects
        assertInvariants(that);// check for side effects
        assertInvariants(q, that);// check for side effects

        return p;
    }

    private static Quaternion exp(final Quaternion q) {
        final Quaternion eq = q.exp();

        assertNotNull(eq, "Not null, result");// guard
        assertInvariants(q);// check for side effects
        assertInvariants(eq);
        assertInvariants(eq, q);

        return eq;
    }

    private static void exp_finite(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);
        final Quaternion m = Quaternion.create(-a, -b, -c, -d);
        final double precision = q.norm();

        final Quaternion eq = exp(q);

        final Quaternion em = m.exp();
        final Quaternion eqem = eq.product(em);
        final Quaternion logexp = eq.log();

        assertInvariants(eq, em);
        assertTrue(Quaternion.ONE.distance(eqem) < precision, "exp(q)*exp(-q) = exp(q-q) = exp(0) = 1");
        assertTrue(q.distance(logexp) < precision, "exp and log are inverse operations");
    }

    private static void exp_finiteScalar(final double a) {
        final double precision = Double.MIN_NORMAL * Math.max(Math.abs(a), 1.0);
        final Quaternion q = Quaternion.create(a, 0, 0, 0);

        final Quaternion eq = exp(q);

        assertEquals(Math.exp(a), eq.getA(), precision, "exponential a");
        assertEquals(0.0, eq.getB(), precision, "exponential b");
        assertEquals(0.0, eq.getC(), precision, "exponential c");
        assertEquals(0.0, eq.getD(), precision, "exponential d");
    }

    private static Quaternion log(final Quaternion q) {
        final Quaternion log = q.log();

        assertNotNull(log, "Not null, result");// guard
        assertInvariants(q);// check for side effects
        assertInvariants(log);
        assertInvariants(log, q);

        return log;
    }

    private static void log_finite(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);
        final double precision = q.norm();

        final Quaternion log = log(q);

        final Quaternion explog = log.exp();

        assertInvariants(log, explog);
        assertTrue(q.distance(explog) < precision, "log and exp are inverse operations");
    }

    private static void log_finitePositiveScalar(final double a) {
        final double precision = Double.MIN_NORMAL * Math.abs(a);
        final Quaternion q = Quaternion.create(a, 0, 0, 0);

        final Quaternion log = log(q);

        assertEquals(Math.log(a), log.getA(), precision, "log a");
        assertEquals(0.0, log.getB(), precision, "log b");
        assertEquals(0.0, log.getC(), precision, "log c");
        assertEquals(0.0, log.getD(), precision, "log d");
    }

    public static Quaternion mean(final Quaternion x, final Quaternion that) {
        final Quaternion mean = x.mean(that);

        assertNotNull(mean, "Not null, mean");// guard
        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);

        return mean;
    }

    private static Quaternion minus(final Quaternion q, final Quaternion that) {
        final Quaternion sum = q.minus(that);

        assertInvariants(sum);
        assertInvariants(sum, q);
        assertInvariants(sum, that);

        return sum;
    }

    private static void minus_0(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        final Quaternion sum = minus(q, Quaternion.ZERO);

        assertEquals(q, sum, "Unchanged");
    }

    private static void minus_self(final double a, final double b, final double c, final double d) {
        final Quaternion p = Quaternion.create(a, b, c, d);

        final Quaternion sum = minus(p, p);

        assertEquals(Quaternion.ZERO, sum, "sum");
    }

    private static Quaternion plus(final Quaternion q, final Quaternion that) {
        final Quaternion sum = q.plus(that);

        assertInvariants(sum);
        assertInvariants(sum, q);
        assertInvariants(sum, that);

        return sum;
    }

    private static void plus_0(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        final Quaternion sum = plus(q, Quaternion.ZERO);

        assertEquals(q, sum, "Unchanged");
    }

    private static void plus_minus(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        final Quaternion result = Quaternion.ZERO.plus(q).minus(q);

        assertEquals(Quaternion.ZERO, result, "plus and minus are inverse operations");
    }

    private static void plus_negative(final double a, final double b, final double c, final double d) {
        final Quaternion p = Quaternion.create(a, b, c, d);
        final Quaternion m = Quaternion.create(-a, -b, -c, -d);

        final Quaternion sum = plus(p, m);

        assertEquals(Quaternion.ZERO, sum, "sum");
    }

    private static Quaternion pow(final Quaternion q, final double p) {
        final Quaternion qp = q.pow(p);

        assertNotNull(qp, "Not null, result");// guard
        assertInvariants(q);// checks for side effects
        assertInvariants(qp);
        assertInvariants(q);

        return qp;
    }

    private static void pow_finite(final double a, final double b, final double c, final double d, final double p) {
        final Quaternion q = Quaternion.create(a, b, c, d);
        final double precision = q.norm() * 4.0;

        final Quaternion qp = pow(q, p);
        final Quaternion qm = pow(q, -p);
        final Quaternion qpqm = qp.product(qm);
        final Quaternion qprp = qp.pow(1.0 / p);

        assertTrue(Quaternion.ONE.distance(qpqm) <= precision, "q^p*q^-p = q^(p-p) = q^0 = 1 <" + qpqm + ">");
        assertTrue(q.distance(qprp) <= precision, "(q^p)^(1/p) = q^(p/p) = q^1 = q <" + qprp + ">");
    }

    private static void pow_finiteScalar(final double a, final double p) {
        final Quaternion q = Quaternion.create(a, 0, 0, 0);
        final double precision = Math.max(Math.abs(a) * 4.0, Double.MIN_VALUE);

        final Quaternion qp = pow(q, p);

        assertEquals(Math.pow(a, p), qp.getA(), precision, "pow a");
        assertEquals(0.0, qp.getB(), precision, "pow b");
        assertEquals(0.0, qp.getC(), precision, "pow c");
        assertEquals(0.0, qp.getD(), precision, "pow d");
    }

    private static Quaternion product(final Quaternion q, final Quaternion that) {
        final Quaternion p = q.product(that);

        assertNotNull(p, "product");// guard
        assertInvariants(q);
        assertInvariants(that);
        assertInvariants(p);
        assertInvariants(p, q);
        assertInvariants(p, that);

        return p;
    }

    private static void product_0(final Quaternion q) {
        final Quaternion p = product(q, Quaternion.ZERO);

        assertEquals(Quaternion.ZERO, p, "product a");
    }

    private static void product_a(final Quaternion q, final double a) {
        final Quaternion multiplier = Quaternion.create(a, 0, 0, 0);

        final Quaternion p = product(q, multiplier);

        assertEquals(q.getA() * a, p.getA(), Double.MIN_NORMAL, "product a");
        assertEquals(q.getB() * a, p.getB(), Double.MIN_NORMAL, "product b");
        assertEquals(q.getC() * a, p.getC(), Double.MIN_NORMAL, "product c");
        assertEquals(q.getD() * a, p.getD(), Double.MIN_NORMAL, "product d");
    }

    private static void product_b(final Quaternion q, final double b) {
        final Quaternion multiplier = Quaternion.create(0, b, 0, 0);

        final Quaternion p = product(q, multiplier);

        assertEquals(q.getB() * -b, p.getA(), Double.MIN_NORMAL, "product a");
        assertEquals(q.getA() * b, p.getB(), Double.MIN_NORMAL, "product b");
        assertEquals(q.getD() * b, p.getC(), Double.MIN_NORMAL, "product c");
        assertEquals(q.getC() * -b, p.getD(), Double.MIN_NORMAL, "product d");
    }

    private static void product_c(final Quaternion q, final double c) {
        final Quaternion multiplier = Quaternion.create(0, 0, c, 0);

        final Quaternion p = product(q, multiplier);

        assertEquals(q.getC() * -c, p.getA(), Double.MIN_NORMAL, "product a");
        assertEquals(q.getD() * -c, p.getB(), Double.MIN_NORMAL, "product b");
        assertEquals(q.getA() * c, p.getC(), Double.MIN_NORMAL, "product c");
        assertEquals(q.getB() * c, p.getD(), Double.MIN_NORMAL, "product d");
    }

    private static void product_d(final Quaternion q, final double d) {
        final Quaternion multiplier = Quaternion.create(0, 0, 0, d);

        final Quaternion p = product(q, multiplier);

        assertEquals(q.getD() * -d, p.getA(), Double.MIN_NORMAL, "product a");
        assertEquals(q.getC() * d, p.getB(), Double.MIN_NORMAL, "product b");
        assertEquals(q.getB() * -d, p.getC(), Double.MIN_NORMAL, "product c");
        assertEquals(q.getA() * d, p.getD(), Double.MIN_NORMAL, "product d");
    }

    private static Quaternion reciprocal(final Quaternion q) {
        final Quaternion r = q.reciprocal();

        assertNotNull(r, "Not null, reciprocal");// guard
        assertInvariants(r);
        assertInvariants(q);
        assertInvariants(r, q);

        return r;
    }

    private static void reciprocal_finite(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);

        final Quaternion r = reciprocal(q);

        final Quaternion qr = q.product(r);
        assertTrue(qr.distance(Quaternion.ONE) < Double.MIN_NORMAL * 4.0,
                "The product of a quaternion with its reciprocal <" + qr + "> is one");
        assertTrue(r.distance(q.conjugate().scale(1.0 / q.norm2())) < Double.MIN_NORMAL,
                "The reciprocal of a quaternion is its conjugate divided by the square of its norm");
    }

    private static Quaternion scale(final Quaternion x, final double f) {
        final Quaternion scaled = x.scale(f);

        assertNotNull(scaled, "Not null, result");
        assertInvariants(scaled);
        assertInvariants(x, scaled);

        return scaled;
    }

    private static Quaternion vector(final Quaternion q) {
        final Quaternion v = q.vector();

        assertNotNull(v, "Not null, vector part");// guard
        assertInvariants(q);// side-effects
        assertInvariants(v);
        assertInvariants(q, v);

        assertEquals(0, v.getA(), Double.MIN_NORMAL, "vector a");
        assertEquals(Double.doubleToLongBits(q.getB()), Double.doubleToLongBits(v.getB()), "vector b");
        assertEquals(Double.doubleToLongBits(q.getC()), Double.doubleToLongBits(v.getC()), "vector c");
        assertEquals(Double.doubleToLongBits(q.getD()), Double.doubleToLongBits(v.getD()), "vector d");

        return v;
    }

    private static Quaternion versor(final Quaternion q) {
        final Quaternion v = q.versor();

        assertNotNull(v, "Not null, versor");// guard
        assertInvariants(q);
        assertInvariants(v);
        assertInvariants(q, v);

        return v;
    }

    private static void versor_finite(final double a, final double b, final double c, final double d) {
        final Quaternion q = Quaternion.create(a, b, c, d);
        final double n = q.norm();

        final Quaternion v = versor(q);

        assertEquals(1.0, v.norm(), Math.nextAfter(1.0, Double.POSITIVE_INFINITY) - 1.0, "versor has unit norm");
        assertTrue(q.distance(v.scale(n)) < Math.max(1.0, n) * Double.MIN_NORMAL,
                "quaternion is equivalent to its versor <" + v + "> scaled by its norm <" + n + ">");
    }

    @Test
    public void conjugate_0() {
        conjugate(Quaternion.ZERO);
    }

    @Test
    public void conjugate_1() {
        conjugate(Quaternion.create(1, 1, 1, 1));
    }

    @Test
    public void conjugate_A() {
        conjugate(Quaternion.create(2, 3, 4, 5));
    }

    @Test
    public void conjugation_11() {
        conjugation(Quaternion.ONE, Quaternion.ONE);
    }

    @Test
    public void conjugation_12() {
        conjugation(Quaternion.ONE, Quaternion.create(2, 0, 0, 0));
    }

    @Test
    public void conjugation_21() {
        conjugation(Quaternion.create(2, 0, 0, 0), Quaternion.ONE);
    }

    @Test
    public void conjugation_ii() {
        conjugation(Quaternion.I, Quaternion.I);
    }

    @Test
    public void conjugation_ij() {
        conjugation(Quaternion.I, Quaternion.J);
    }

    @Test
    public void conjugation_ik() {
        conjugation(Quaternion.I, Quaternion.K);
    }

    @Test
    public void conjugation_ji() {
        conjugation(Quaternion.J, Quaternion.I);
    }

    @Test
    public void conjugation_jj() {
        conjugation(Quaternion.J, Quaternion.J);
    }

    @Test
    public void conjugation_jk() {
        conjugation(Quaternion.J, Quaternion.K);
    }

    @Test
    public void conjugation_ki() {
        conjugation(Quaternion.K, Quaternion.I);
    }

    @Test
    public void conjugation_kj() {
        conjugation(Quaternion.K, Quaternion.J);
    }

    @Test
    public void conjugation_kk() {
        conjugation(Quaternion.K, Quaternion.K);
    }

    @Test
    public void constructor_1() {
        final Quaternion q = constructor(1, 1, 1, 1);

        assertEquals(4.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(2.0, q.norm(), Double.MIN_NORMAL, " norm");
    }

    @Test
    public void constructor_2differentA() {
        final double b = 2.0;
        final double c = 3.0;
        final double d = 4.0;
        final Quaternion q1 = Quaternion.create(1.0, b, c, d);
        final Quaternion q2 = Quaternion.create(-1.0, b, c, d);

        assertInvariants(q1, q2);
        assertNotEquals(q1, q2);
    }

    @Test
    public void constructor_2differentB() {
        final double a = 1.0;
        final double c = 3.0;
        final double d = 4.0;
        final Quaternion q1 = Quaternion.create(a, 2.0, c, d);
        final Quaternion q2 = Quaternion.create(a, -2.0, c, d);

        assertInvariants(q1, q2);
        assertNotEquals(q1, q2);
    }

    @Test
    public void constructor_2differentC() {
        final double a = 1.0;
        final double b = 2.0;
        final double d = 4.0;
        final Quaternion q1 = Quaternion.create(a, b, 3.0, d);
        final Quaternion q2 = Quaternion.create(a, b, -3.0, d);

        assertInvariants(q1, q2);
        assertNotEquals(q1, q2);
    }

    @Test
    public void constructor_2differentD() {
        final double a = 1.0;
        final double b = 2.0;
        final double c = 3.0;
        final Quaternion q1 = Quaternion.create(a, b, c, 4.0);
        final Quaternion q2 = Quaternion.create(a, b, c, -4.0);

        assertInvariants(q1, q2);
        assertNotEquals(q1, q2);
    }

    @Test
    public void constructor_2equivalentA() {
        constructor_2equivalent(1.0, 2.0, 3.0, 4.0);
    }

    @Test
    public void constructor_2equivalentB() {
        constructor_2equivalent(9.0, 7.0, 6.0, 5.0);
    }

    @Test
    public void constructor_a1() {
        final Quaternion q = constructor(1, 0, 0, 0);

        assertEquals(1.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(1.0, q.norm(), Double.MIN_NORMAL, " norm");
    }

    @Test
    public void constructor_a2() {
        final Quaternion q = constructor(-2, 0, 0, 0);

        assertEquals(4.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(2.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_aMax() {
        final Quaternion q = constructor(Double.MAX_VALUE, 0, 0, 0);

        assertEquals(Double.MAX_VALUE, q.norm(), (Math.nextAfter(1.0, Double.MAX_VALUE) - 1.0) * Double.MAX_VALUE,
                "norm");
    }

    @Test
    public void constructor_b1() {
        final Quaternion q = constructor(0, 1, 0, 0);

        assertEquals(1.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(1.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_b2() {
        final Quaternion q = constructor(0, -2, 0, 0);

        assertEquals(4.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(2.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_c1() {
        final Quaternion q = constructor(0, 0, 1, 0);

        assertEquals(1.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(1.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_c2() {
        final Quaternion q = constructor(0, 0, -2, 0);

        assertEquals(4.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(2.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_d1() {
        final Quaternion q = constructor(0, 0, 0, 1);

        assertEquals(1.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(1.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void constructor_d2() {
        final Quaternion q = constructor(0, 0, 0, -2);

        assertEquals(4.0, q.norm2(), Double.MIN_NORMAL, "norm^2");
        assertEquals(2.0, q.norm(), Double.MIN_NORMAL, "norm");
    }

    @Test
    public void distance_0A() {
        distance_0(1, 2, 3, 4);
    }

    @Test
    public void distance_0B() {
        distance_0(-9, -8, -7, -6);
    }

    @Test
    public void distance_selfA() {
        distance_self(1, 2, 3, 4);
    }

    @Test
    public void distance_selfB() {
        distance_self(-9, -8, -7, -6);
    }

    @Test
    public void dot_a() {
        final double p = dot(Quaternion.ONE, Quaternion.create(5, 4, 3, 2));

        assertThat("result", p, closeTo(5, Double.MIN_NORMAL));
    }

    @Test
    public void dot_b() {
        final double p = dot(Quaternion.I, Quaternion.create(4, 5, 3, 2));

        assertThat("result", p, closeTo(5, Double.MIN_NORMAL));
    }

    @Test
    public void dot_c() {
        final double p = dot(Quaternion.J, Quaternion.create(3, 4, 5, 2));

        assertThat("result", p, closeTo(5, Double.MIN_NORMAL));
    }

    @Test
    public void dot_d() {
        final double p = dot(Quaternion.K, Quaternion.create(2, 4, 3, 5));

        assertThat("result", p, closeTo(5, Double.MIN_NORMAL));
    }

    @Test
    public void dot_mixed() {
        final double p = dot(Quaternion.create(1, 2, 3, 4), Quaternion.create(5, 4, 3, 2));

        assertThat("result", p, closeTo(30, Double.MIN_NORMAL));
    }

    @Test
    public void exp_b1() {
        exp_finite(0, 1, 0, 0);
    }

    @Test
    public void exp_b2() {
        exp_finite(0, 2, 0, 0);
    }

    @Test
    public void exp_bMinus1() {
        exp_finite(0, -1, 0, 0);
    }

    @Test
    public void exp_c1() {
        exp_finite(0, 0, 1, 0);
    }

    @Test
    public void exp_c2() {
        exp_finite(0, 0, 2, 0);
    }

    @Test
    public void exp_cMinus1() {
        exp_finite(0, 0, -1, 0);
    }

    @Test
    public void exp_combined() {
        exp_finite(1, 1, 1, 1);
    }

    @Test
    public void exp_d1() {
        exp_finite(0, 0, 0, 1);
    }

    @Test
    public void exp_d2() {
        exp_finite(0, 0, 0, 2);
    }

    @Test
    public void exp_dMinus1() {
        exp_finite(0, 0, 0, -1);
    }

    @Test
    public void exp_scalar0() {
        exp_finiteScalar(0.0);
    }

    @Test
    public void exp_scalar1() {
        exp_finiteScalar(1.0);
    }

    @Test
    public void exp_scalar2() {
        exp_finiteScalar(2.0);
    }

    @Test
    public void exp_scalarMinus1() {
        exp_finiteScalar(-1.0);
    }

    @Test
    public void log_b1() {
        log_finite(0, 1, 0, 0);
    }

    @Test
    public void log_b2() {
        log_finite(0, 2, 0, 0);
    }

    @Test
    public void log_bMinus1() {
        log_finite(0, -1, 0, 0);
    }

    @Test
    public void log_c1() {
        log_finite(0, 0, 1, 0);
    }

    @Test
    public void log_c2() {
        log_finite(0, 0, 2, 0);
    }

    @Test
    public void log_cMinus1() {
        log_finite(0, 0, -1, 0);
    }

    @Test
    public void log_combined() {
        log_finite(1, 1, 1, 1);
    }

    @Test
    public void log_d1() {
        log_finite(0, 0, 0, 1);
    }

    @Test
    public void log_d2() {
        log_finite(0, 0, 0, 2);
    }

    @Test
    public void log_dMinus1() {
        log_finite(0, 0, 0, -1);
    }

    @Test
    public void log_scalar1() {
        log_finitePositiveScalar(1.0);
    }

    @Test
    public void log_scalar2() {
        log_finitePositiveScalar(2.0);
    }

    @Test
    public void mean_all() {
        final Quaternion mean = mean(Quaternion.create(3, 4, 5, 6), Quaternion.create(7, 8, 9, 10));
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(5, 6, 7, 8), Double.MIN_NORMAL));
    }

    @Test
    public void mean_x01() {
        final Quaternion mean = mean(Quaternion.ZERO, Quaternion.ONE);
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(0.5, 0, 0, 0), Double.MIN_NORMAL));
    }

    @Test
    public void mean_x02() {
        final Quaternion mean = mean(Quaternion.ZERO, Quaternion.create(2, 0, 0, 0));
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(1, 0, 0, 0), Double.MIN_NORMAL));
    }

    @Test
    public void mean_x0i() {
        final Quaternion mean = mean(Quaternion.ZERO, Quaternion.I);
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(0, 0.5, 0, 0), Double.MIN_NORMAL));
    }

    @Test
    public void mean_x0j() {
        final Quaternion mean = mean(Quaternion.ZERO, Quaternion.J);
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(0, 0, 0.5, 0), Double.MIN_NORMAL));
    }

    @Test
    public void mean_x0k() {
        final Quaternion mean = mean(Quaternion.ZERO, Quaternion.K);
        assertThat("mean", mean, closeToQuaternion(Quaternion.create(0, 0, 0, 0.5), Double.MIN_NORMAL));
    }

    @Test
    public void minus_0A() {
        minus_0(1, 2, 3, 4);
    }

    @Test
    public void minus_0B() {
        minus_0(-9, -8, -7, -6);
    }

    @Test
    public void minus_selfA() {
        minus_self(1, 2, 3, 4);
    }

    @Test
    public void minus_selfB() {
        minus_self(-9, -8, -7, -6);
    }

    @Test
    public void plus_0A() {
        plus_0(1, 2, 3, 4);
    }

    @Test
    public void plus_0B() {
        plus_0(-9, -8, -7, -6);
    }

    @Test
    public void plus_minusA() {
        plus_minus(1, 2, 3, 4);
    }

    @Test
    public void plus_minusB() {
        plus_minus(-9, -8, -7, -6);
    }

    @Test
    public void plus_negativeA() {
        plus_negative(1, 2, 3, 4);
    }

    @Test
    public void plus_negativeB() {
        plus_negative(-9, -8, -7, -6);
    }

    @Test
    public void pow_0A() {
        pow_finiteScalar(0, 2);
    }

    @Test
    public void pow_b13() {
        pow_finite(0, 1, 0, 0, 3);
    }

    @Test
    public void pow_b23() {
        pow_finite(0, 2, 0, 0, 3);
    }

    @Test
    public void pow_b25() {
        pow_finite(0, 2, 0, 0, 5);
    }

    @Test
    public void pow_c13() {
        pow_finite(0, 0, 1, 0, 3);
    }

    @Test
    public void pow_c23() {
        pow_finite(0, 0, 2, 0, 3);
    }

    @Test
    public void pow_c25() {
        pow_finite(0, 0, 2, 0, 5);
    }

    @Test
    public void pow_d13() {
        pow_finite(0, 0, 0, 1, 3);
    }

    @Test
    public void pow_d23() {
        pow_finite(0, 0, 0, 2, 3);
    }

    @Test
    public void pow_d25() {
        pow_finite(0, 0, 0, 2, 5);
    }

    @Test
    public void pow_finiteScalarA() {
        pow_finiteScalar(2, 2);
    }

    @Test
    public void pow_finiteScalarB() {
        pow_finiteScalar(2, 3);
    }

    @Test
    public void pow_finiteScalarC() {
        pow_finiteScalar(-2, 2);
    }

    @Test
    public void product_0A() {
        product_0(Quaternion.create(1, 2, 3, 4));
    }

    @Test
    public void product_0B() {
        product_0(Quaternion.create(8, 7, 6, 5));
    }

    @Test
    public void product_a1() {
        product_a(Quaternion.create(1, 2, 3, 4), 1);
    }

    @Test
    public void product_a2() {
        product_a(Quaternion.create(-1, -2, -3, -4), 2);
    }

    @Test
    public void product_b1() {
        product_b(Quaternion.create(1, 2, 3, 4), 1);
    }

    @Test
    public void product_b2() {
        product_b(Quaternion.create(-1, -2, -3, -4), 2);
    }

    @Test
    public void product_c1() {
        product_c(Quaternion.create(1, 2, 3, 4), 1);
    }

    @Test
    public void product_c2() {
        product_c(Quaternion.create(-1, -2, -3, -4), 2);
    }

    @Test
    public void product_d1() {
        product_d(Quaternion.create(1, 2, 3, 4), 1);
    }

    @Test
    public void product_d2() {
        product_d(Quaternion.create(-1, -2, -3, -4), 2);
    }

    @Test
    public void reciprocal_1() {
        reciprocal_finite(1, 1, 1, 1);
    }

    @Test
    public void reciprocal_a1() {
        reciprocal_finite(1, 0, 0, 0);
    }

    @Test
    public void reciprocal_b1() {
        reciprocal_finite(0, 1, 0, 0);
    }

    @Test
    public void reciprocal_c1() {
        reciprocal_finite(0, 0, 1, 0);
    }

    @Test
    public void reciprocal_d1() {
        reciprocal_finite(0, 0, 0, 1);
    }

    @Test
    public void scale_0A() {
        final Quaternion scaled = scale(Quaternion.ZERO, 0.0);

        assertEquals(Quaternion.ZERO, scaled, "scaled");
    }

    @Test
    public void scale_0B() {
        final Quaternion scaled = scale(Quaternion.ZERO, 1.0);

        assertEquals(Quaternion.ZERO, scaled, "scaled");
    }

    @Test
    public void scale_1A() {
        final Quaternion one = Quaternion.create(1, 1, 1, 1);

        final Quaternion scaled = scale(one, 1);

        assertEquals(one, scaled, "scaled");
    }

    @Test
    public void scale_1B() {
        final Quaternion one = Quaternion.create(1, 1, 1, 1);

        final Quaternion scaled = scale(one, -2.0);

        assertEquals(-2.0, scaled.getA(), Double.MIN_NORMAL, "scaled a");
        assertEquals(-2.0, scaled.getB(), Double.MIN_NORMAL, "scaled b");
        assertEquals(-2.0, scaled.getC(), Double.MIN_NORMAL, "scaled c");
        assertEquals(-2.0, scaled.getD(), Double.MIN_NORMAL, "scaled d");
    }

    @Test
    public void scale_B() {
        final Quaternion original = Quaternion.create(2, 3, 4, 5);

        final Quaternion scaled = scale(original, 4);

        assertEquals(8, scaled.getA(), Double.MIN_NORMAL, "scaled a");
        assertEquals(12, scaled.getB(), Double.MIN_NORMAL, "scaled b");
        assertEquals(16, scaled.getC(), Double.MIN_NORMAL, "scaled c");
        assertEquals(20, scaled.getD(), Double.MIN_NORMAL, "scaled d");
    }

    @Test
    public void static_values() {
        assertInvariants(Quaternion.ZERO);

        assertEquals(0.0, Quaternion.ZERO.norm2(), Double.MIN_NORMAL, "zero norm^2");
        assertEquals(0.0, Quaternion.ZERO.norm(), Double.MIN_NORMAL, "zero norm");
    }

    @Test
    public void vector_0() {
        vector(Quaternion.ZERO);
    }

    @Test
    public void vector_1() {
        vector(Quaternion.ONE);
    }

    @Test
    public void vector_a2() {
        vector(Quaternion.create(2, 0, 0, 0));
    }

    @Test
    public void vector_b2() {
        vector(Quaternion.create(0, 2, 0, 0));
    }

    @Test
    public void vector_c2() {
        vector(Quaternion.create(0, 0, 2, 0));
    }

    @Test
    public void vector_d2() {
        vector(Quaternion.create(0, 0, 0, 2));
    }

    @Test
    public void versor_0() {
        assertEquals(Quaternion.ZERO, Quaternion.ZERO.versor(), "versor of zero is taken to be zero");
    }

    @Test
    public void versor_a1() {
        versor_finite(1, 0, 0, 0);
    }

    @Test
    public void versor_a2() {
        versor_finite(2, 0, 0, 0);
    }

    @Test
    public void versor_aMinus1() {
        versor_finite(-1, 0, 0, 0);
    }

    @Test
    public void versor_aSmall() {
        versor_finite(Double.MIN_NORMAL, 0, 0, 0);
    }

    @Test
    public void versor_b1() {
        versor_finite(0, 1, 0, 0);
    }

    @Test
    public void versor_b2() {
        versor_finite(0, 2, 0, 0);
    }

    @Test
    public void versor_bMinus1() {
        versor_finite(0, -1, 0, 0);
    }

    @Test
    public void versor_bSmall() {
        versor_finite(0, Double.MIN_NORMAL, 0, 0);
    }

    @Test
    public void versor_c1() {
        versor_finite(0, 0, 1, 0);
    }

    @Test
    public void versor_c2() {
        versor_finite(0, 0, 2, 0);
    }

    @Test
    public void versor_cMinus1() {
        versor_finite(0, 0, -1, 0);
    }

    @Test
    public void versor_cSmall() {
        versor_finite(0, 0, Double.MIN_NORMAL, 0);
    }

    @Test
    public void versor_d1() {
        versor_finite(0, 0, 0, 1);
    }

    @Test
    public void versor_d2() {
        versor_finite(0, 0, 0, 2);
    }

    @Test
    public void versor_dMinus1() {
        versor_finite(0, 0, 0, -1);
    }

    @Test
    public void versor_dSmall() {
        versor_finite(0, 0, 0, Double.MIN_NORMAL);
    }
}
