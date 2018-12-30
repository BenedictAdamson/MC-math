package uk.badamson.mc;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * <p>
 * Unit tests for the {@link Object} class.
 * </p>
 */
public final class ObjectTest {

    public static void assertInvariants(Object object) {
        assert object != null;
        assertEquals("An object is always equivalent to itself", object, object);
        assertFalse("An object is never equivalent to null", object.equals(null));
    }

    public static void assertInvariants(Object object1, Object object2) {
        assert object1 != null;
        assert object2 != null;
        final boolean equals = object1.equals(object2);
        assertEquals("Equality is symmetric", equals, object2.equals(object1));
        assertFalse("hashCode() is consistent with equals()", equals && object1.hashCode() != object2.hashCode());
    }
}
