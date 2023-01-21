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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

/**
 * <p>
 * Unit tests of the class {@link FunctionNWithGradientValue}.
 * </p>
 */
@SuppressWarnings("UnusedReturnValue")
public class FunctionNTo1WithGradientValueTest {

    public static void assertInvariants(final FunctionNWithGradientValue f) {
        ObjectVerifier.assertInvariants(f);

        final ImmutableVectorN x = f.getX();
        final ImmutableVectorN dfdx = f.getDfDx();

        assertNotNull(x, "Not null, x");// guard
        assertNotNull(dfdx, "Not null, dfdx");// guard
        assertEquals(x.getDimension(), dfdx.getDimension(),
                "The dimension of the gradient vector is equal to the dimension of the domain vector");
    }

    public static void assertInvariants(final FunctionNWithGradientValue f1, final FunctionNWithGradientValue f2) {
        ObjectVerifier.assertInvariants(f1, f2);

        final boolean equals = f1.equals(f2);
        assertFalse(equals && Double.doubleToLongBits(f1.getF()) != Double.doubleToLongBits(f2.getF()),
                "Equality requires equivalent attributes, f");
        assertFalse(equals && !f1.getX().equals(f2.getX()), "Equality requires equivalent attributes, x");
        assertFalse(equals && !f1.getDfDx().equals(f2.getDfDx()), "Equality requires equivalent attributes, dfdx");
    }

    private static FunctionNWithGradientValue constructor(final ImmutableVectorN x, final double f,
            final ImmutableVectorN dfdx) {
        final FunctionNWithGradientValue v = new FunctionNWithGradientValue(x, f, dfdx);

        assertInvariants(v);
        assertEquals(Double.doubleToLongBits(f), Double.doubleToLongBits(v.getF()), "f (bits)");
        assertEquals(x, v.getX(), "x");
        assertEquals(dfdx, v.getDfDx(), "dfdx");

        return v;
    }

    private static void constructor_equals(final ImmutableVectorN x, final double f, final ImmutableVectorN dfdx) {
        final FunctionNWithGradientValue v1 = new FunctionNWithGradientValue(x, f, dfdx);
        final FunctionNWithGradientValue v2 = new FunctionNWithGradientValue(x, f, dfdx);

        assertInvariants(v1, v2);
        assertEquals(v1, v2);
    }

    @Test
    public void constructor_1A() {
        constructor(ImmutableVectorN.create(0.0), 0.0, ImmutableVectorN.create(0.0));
    }

    @Test
    public void constructor_1B() {
        constructor(ImmutableVectorN.create(1.0), 2.0, ImmutableVectorN.create(3.0));
    }

    @Test
    public void constructor_2() {
        constructor(ImmutableVectorN.create(0.0, 1.0), 2.0, ImmutableVectorN.create(3.0, 4.0));
    }

    @Test
    public void constructor_equals1A() {
        constructor_equals(ImmutableVectorN.create(0.0), 0.0, ImmutableVectorN.create(0.0));
    }

    @Test
    public void constructor_equals1B() {
        constructor_equals(ImmutableVectorN.create(1.0), 2.0, ImmutableVectorN.create(3.0));
    }

    @Test
    public void constructor_equals2() {
        constructor_equals(ImmutableVectorN.create(0.0, 1.0), 2.0, ImmutableVectorN.create(3.0, 4.0));
    }

    @Test
    public void constructor_notEqualsDfDx() {
        final ImmutableVectorN x = ImmutableVectorN.create(1.0);
        final double f = 2.0;
        final FunctionNWithGradientValue v1 = new FunctionNWithGradientValue(x, f, ImmutableVectorN.create(3.0));
        final FunctionNWithGradientValue v2 = new FunctionNWithGradientValue(x, f, ImmutableVectorN.create(4.0));

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2);
    }

    @Test
    public void constructor_notEqualsF() {
        final ImmutableVectorN x = ImmutableVectorN.create(1.0);
        final ImmutableVectorN dfdx = ImmutableVectorN.create(4.0);
        final FunctionNWithGradientValue v1 = new FunctionNWithGradientValue(x, 2.0, dfdx);
        final FunctionNWithGradientValue v2 = new FunctionNWithGradientValue(x, 3.0, dfdx);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2);
    }

    @Test
    public void constructor_notEqualsX() {
        final double f = 3.0;
        final ImmutableVectorN dfdx = ImmutableVectorN.create(4.0);
        final FunctionNWithGradientValue v1 = new FunctionNWithGradientValue(ImmutableVectorN.create(1.0), f, dfdx);
        final FunctionNWithGradientValue v2 = new FunctionNWithGradientValue(ImmutableVectorN.create(2.0), f, dfdx);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2);
    }
}
