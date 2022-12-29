package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2022.
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

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class VectorFieldTest {

    public static void assertInvariants(@Nonnull VectorField v) {
        assertAll(
                () -> assertThat("valueDimension", v.getValueDimension(), greaterThan(0)),
                () -> assertThat("spaceDimension", v.getSpaceDimension(), greaterThan(0))
        );
    }

    @SuppressWarnings("EmptyMethod")
    public static void assertInvariants(@Nonnull VectorField v1, @Nonnull VectorField v2) {
        // Do nothing
    }
}
