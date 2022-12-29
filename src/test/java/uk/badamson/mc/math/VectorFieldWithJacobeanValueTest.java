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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.badamson.dbc.assertions.EqualsSemanticsVerifier;
import uk.badamson.dbc.assertions.ObjectVerifier;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class VectorFieldWithJacobeanValueTest {

    private static final ImmutableVectorN V_1A = ImmutableVectorN.create(1.0);
    private static final ImmutableVectorN V_1B = ImmutableVectorN.create(3.0);
    private static final ImmutableVectorN V_2 = ImmutableVectorN.create(1.0, 2.0);
    private static final ImmutableVectorN V_3 = ImmutableVectorN.create(3.0, 4.0, 5.0);
    private static final ImmutableMatrixN M_1x1 = ImmutableMatrixN.create(1, 1, new double[]{4.0});
    private static final ImmutableMatrixN M_3x2 = ImmutableMatrixN.create(3, 2, new double[]{6.0, 7.0, 8.0, 9.0, 10.0, 11.0});

    public static void assertInvariants(@Nonnull VectorFieldWithJacobeanValue v) {
        ObjectVerifier.assertInvariants(v);// inherited

        final var x = v.getX();
        final var f = v.getF();
        final var j = v.getJ();
        assertAll(
                () -> assertThat("x", x, notNullValue()),
                () -> assertThat("f", f, notNullValue()),
                () -> assertThat("j", j, notNullValue())
        );
        assertAll(
                () -> ImmutableVectorNTest.assertInvariants(x),
                () -> ImmutableVectorNTest.assertInvariants(f),
                () -> ImmutableMatrixNTest.assertInvariants(j)
        );
        assertAll(
                () -> assertThat("j.rows", j.getRows(), is(f.getDimension())),
                () -> assertThat("j.columns", j.getColumns(), is(x.getDimension()))
        );
    }

    public static void assertInvariants(@Nonnull VectorFieldWithJacobeanValue v1, @Nonnull VectorFieldWithJacobeanValue v2) {
        ObjectVerifier.assertInvariants(v1, v2);// inherited

        assertAll(
                () -> EqualsSemanticsVerifier.assertValueSemantics(v1, v2, "x", VectorFieldWithJacobeanValue::getX),
                () -> EqualsSemanticsVerifier.assertValueSemantics(v1, v2, "f", VectorFieldWithJacobeanValue::getF),
                () -> EqualsSemanticsVerifier.assertValueSemantics(v1, v2, "j", VectorFieldWithJacobeanValue::getJ)
        );
    }

    private static void constructor(
            @Nonnull final ImmutableVectorN x,
            @Nonnull final ImmutableVectorN f,
            @Nonnull final ImmutableMatrixN j
    ) {
        final var v = new VectorFieldWithJacobeanValue(x, f, j);
        assertInvariants(v);
        assertAll("attributes",
                () -> assertThat("x", v.getX(), sameInstance(x)),
                () -> assertThat("f", v.getF(), sameInstance(f)),
                () -> assertThat("j", v.getJ(), sameInstance(j))
        );
    }

    @Nested
    public class Constructor {

        @Nested
        public class One {

            @Test
            public void construct_1x1() {
                constructor(V_1A, V_1B, M_1x1);
            }

            @Test
            public void construct_3x2() {
                constructor(V_2, V_3, M_3x2);
            }

        }

        @SuppressWarnings("NewObjectEquality")
        @Nested
        public class Two {

            @Test
            public void sameValues() {
                final var v1 = new VectorFieldWithJacobeanValue(V_1A, V_1B, M_1x1);
                final var v2 = new VectorFieldWithJacobeanValue(V_1A, V_1B, M_1x1);
                assertInvariants(v1, v2);
                assertThat(v1, is(v2));
            }

            @Test
            public void equivalentValues() {
                final var x1 = ImmutableVectorN.create(1.0);
                final var x2 = ImmutableVectorN.create(1.0);
                final var f1 = ImmutableVectorN.create(2.0);
                final var f2 = ImmutableVectorN.create(2.0);
                final var j1 = ImmutableMatrixN.create(1, 1, new double[]{4.0});
                final var j2 = ImmutableMatrixN.create(1, 1, new double[]{4.0});
                assert x1 != x2 && x1.equals(x2);
                assert f1 != f2 && f1.equals(f2);
                assert j1 != j2 && j1.equals(j2);

                final var v1 = new VectorFieldWithJacobeanValue(x1, f1, j1);
                final var v2 = new VectorFieldWithJacobeanValue(x2, f2, j2);
                assertInvariants(v1, v2);
                assertThat(v1, is(v2));
            }

            @Test
            public void differentX() {
                final var v1 = new VectorFieldWithJacobeanValue(V_1A, V_1B, M_1x1);
                final var v2 = new VectorFieldWithJacobeanValue(V_1B, V_1B, M_1x1);
                assertInvariants(v1, v2);
                assertThat(v1, not(v2));
            }

            @Test
            public void differentF() {
                final var v1 = new VectorFieldWithJacobeanValue(V_1A, V_1B, M_1x1);
                final var v2 = new VectorFieldWithJacobeanValue(V_1A, V_1A, M_1x1);
                assertInvariants(v1, v2);
                assertThat(v1, not(v2));
            }

            @Test
            public void differentJ() {
                final var j1 = ImmutableMatrixN.create(1, 1, new double[]{4.0});
                final var j2 = ImmutableMatrixN.create(1, 1, new double[]{5.0});
                final var v1 = new VectorFieldWithJacobeanValue(V_1A, V_1B, j1);
                final var v2 = new VectorFieldWithJacobeanValue(V_1A, V_1B, j2);
                assertInvariants(v1, v2);
                assertThat(v1, not(v2));
            }

        }
    }
}
