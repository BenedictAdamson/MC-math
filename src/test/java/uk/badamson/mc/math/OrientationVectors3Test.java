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

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

/**
 * <p>
 * Unit tests of the class {@link OrientationVectors3}.
 * </p>
 */
public class OrientationVectors3Test {

    public static void assertInvariants(final OrientationVectors3 orientation) {
        ObjectVerifier.assertInvariants(orientation);// inherited

        final ImmutableVectorN e1 = orientation.getE1();
        final ImmutableVectorN e2 = orientation.getE2();
        final ImmutableVectorN e3 = orientation.getE3();

        assertNotNull(e1, "Not null, e1");// guard
        assertNotNull(e2, "Not null, e2");// guard
        assertNotNull(e3, "Not null, e3");// guard

        assertEquals(3, e1.getDimension(), "The e1 vector is 3 dimensional.");// guard
        assertEquals(3, e2.getDimension(), "The e2 vector is 3 dimensional.");// guard
        assertEquals(3, e3.getDimension(), "The e3 vector is 3 dimensional.");// guard

        assertEquals(1.0, e1.magnitude(), Double.MIN_NORMAL, "The e1 vector has unit magnitude.");
        assertEquals(1.0, e2.magnitude(), Double.MIN_NORMAL, "The e2 vector has unit magnitude.");
        assertEquals(1.0, e3.magnitude(), Double.MIN_NORMAL, "The e3 vector has unit magnitude.");
        assertEquals(0.0, e1.dot(e2), Double.MIN_NORMAL, "The e1 vector is orthogonal to vector e2.");
        assertEquals(0.0, e1.dot(e3), Double.MIN_NORMAL, "The e1 vector is orthogonal to vector e3.");
        assertEquals(0.0, e2.dot(e3), Double.MIN_NORMAL, "The e2 vector is orthogonal to vector e3.");
    }

    public static void assertInvariants(final OrientationVectors3 orientation1,
            final OrientationVectors3 orientation2) {
        ObjectVerifier.assertInvariants(orientation1, orientation2);// inherited
    }

    private static OrientationVectors3 createFromOrthogonalUnitBasisVectors(final ImmutableVectorN e1,
            final ImmutableVectorN e2, final ImmutableVectorN e3) {
        final OrientationVectors3 orientation = OrientationVectors3.createFromOrthogonalUnitBasisVectors(e1, e2, e3);

        assertNotNull(orientation, "Not null, result");// guard
        assertInvariants(orientation);
        assertEquals(e1, orientation.getE1(), "e1");
        assertEquals(e2, orientation.getE2(), "e2");
        assertEquals(e3, orientation.getE3(), "e3");

        return orientation;
    }

    @Test
    public void createFromOrthogonalUnitBasisVectors_A() {
        final ImmutableVectorN e1 = ImmutableVectorN.create(1, 0, 0);
        final ImmutableVectorN e2 = ImmutableVectorN.create(0, 1, 0);
        final ImmutableVectorN e3 = ImmutableVectorN.create(0, 0, 1);

        createFromOrthogonalUnitBasisVectors(e1, e2, e3);
    }

    @Test
    public void createFromOrthogonalUnitBasisVectors_B() {
        final ImmutableVectorN e1 = ImmutableVectorN.create(0, 1, 0);
        final ImmutableVectorN e2 = ImmutableVectorN.create(0, 0, 1);
        final ImmutableVectorN e3 = ImmutableVectorN.create(1, 0, 0);

        createFromOrthogonalUnitBasisVectors(e1, e2, e3);
    }

    @Test
    public void createFromOrthogonalUnitBasisVectors_C() {
        final double f = 1.0 / Math.sqrt(2.0);
        final ImmutableVectorN e1 = ImmutableVectorN.create(f, f, 0);
        final ImmutableVectorN e2 = ImmutableVectorN.create(-f, f, 0);
        final ImmutableVectorN e3 = ImmutableVectorN.create(0, 0, 1);

        createFromOrthogonalUnitBasisVectors(e1, e2, e3);
    }

    @Test
    public void createFromOrthogonalUnitBasisVectors_D() {
        final double f = 1.0 / Math.sqrt(2.0);
        final ImmutableVectorN e1 = ImmutableVectorN.create(1, 0, 0);
        final ImmutableVectorN e2 = ImmutableVectorN.create(0, f, f);
        final ImmutableVectorN e3 = ImmutableVectorN.create(0, -f, f);

        createFromOrthogonalUnitBasisVectors(e1, e2, e3);
    }

    @Test
    public void statics() {
        assertNotNull(OrientationVectors3.GLOBAL_BASIS, "Not null, blobal basis");// guard
        assertInvariants(OrientationVectors3.GLOBAL_BASIS);
        assertEquals(1.0, OrientationVectors3.GLOBAL_BASIS.getE1().get(0), Double.MIN_NORMAL, "Global basis e1 x.");
        assertEquals(1.0, OrientationVectors3.GLOBAL_BASIS.getE2().get(1), Double.MIN_NORMAL, "Global basis e2 y.");
        assertEquals(1.0, OrientationVectors3.GLOBAL_BASIS.getE3().get(2), Double.MIN_NORMAL, "Global basis e3 z.");
    }
}
