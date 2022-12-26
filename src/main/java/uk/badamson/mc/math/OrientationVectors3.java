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

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * The orientation of a body in 3D space.
 * </p>
 * <p>
 * The orientation can be described by three orthogonal unit basis vectors,
 * {@linkplain #getE1() e1}, {@linkplain #getE2() e2} and {@linkplain #getE3()
 * e3}.
 * </p>
 */
@Immutable
public final class OrientationVectors3 {

    /**
     * <p>
     * The orientation corresponding to the global coordinate system.
     * </p>
     * <ul>
     * <li>The global basis is a (non null) orientation.</li>
     * <li>The {@linkplain OrientationVectors3#getE1() e1} vector of the global
     * basis is the global x axis.</li>
     * <li>The {@linkplain OrientationVectors3#getE2() e2} vector of the global
     * basis is the global y axis.</li>
     * <li>The {@linkplain OrientationVectors3#getE3() e3} vector of the global
     * basis is the global z axis.</li>
     * </ul>
     */
    public static final OrientationVectors3 GLOBAL_BASIS = new OrientationVectors3(ImmutableVectorN.create(1, 0, 0),
            ImmutableVectorN.create(0, 1, 0), ImmutableVectorN.create(0, 0, 1));

    private static final double TOLERANCE = Math.max(Math.nextAfter(1.0, 2.0) - 1.0, 1.0 - Math.nextAfter(1.0, 0.0));
    private final ImmutableVectorN e1;
    private final ImmutableVectorN e2;
    private final ImmutableVectorN e3;

    private OrientationVectors3(final ImmutableVectorN e1, final ImmutableVectorN e2, final ImmutableVectorN e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    /**
     * <p>
     * Create an orientation from three orthogonal unit basis vectors.
     * </p>
     *
     * <section>
     * <h4>Post Conditions</h4>
     * <ul>
     * <li>Always creates a (non null) object.</li>
     * <li>The created object has the given attributes.</li>
     * </ul>
     * </section>
     *
     * @param e1 The first orthogonal unit basis vector of this orientation; its
     *           local <i>x</i> direction.
     * @param e2 The second orthogonal unit basis vector of this orientation; its
     *           local <i>y</i> direction.
     * @param e3 The third orthogonal unit basis vector of this orientation; its
     *           local <i>z</i> direction.
     * @throws NullPointerException     <ul>
     *                                              <li>If {@code e1} is null.</li>
     *                                              <li>If {@code e2} is null.</li>
     *                                              <li>If {@code e3} is null.</li>
     *                                              </ul>
     * @throws IllegalArgumentException <ul>
     *                                              <li>If {@code e1} does not have unit
     *                                              {@linkplain ImmutableVectorN#magnitude() magnitude}.</li>
     *                                              <li>If {@code e2} does not have unit magnitude.</li>
     *                                              <li>If {@code e3} does not have unit magnitude.</li>
     *                                              <li>If {@code e1} is not 3
     *                                              {@linkplain ImmutableVectorN#getDimension() dimensional}.</li>
     *                                              <li>If {@code e2} is not 3
     *                                              {@linkplain ImmutableVectorN#getDimension() dimensional}.</li>
     *                                              <li>If {@code e3} is not 3
     *                                              {@linkplain ImmutableVectorN#getDimension() dimensional}.</li>
     *                                              <li>If {@code e1} and {@code e2} do not have a zero
     *                                              {@linkplain ImmutableVectorN#dot(ImmutableVectorN) dot
     *                                              product}.</li>
     *                                              <li>If {@code e1} and {@code e3} do not have a zero dot
     *                                              product.</li>
     *                                              <li>If {@code e2} and {@code e3} do not have a zero dot
     *                                              product.</li>
     *                                              </ul>
     */
    public static OrientationVectors3 createFromOrthogonalUnitBasisVectors(@NonNull final ImmutableVectorN e1,
                                                                           @NonNull final ImmutableVectorN e2, @NonNull final ImmutableVectorN e3) {
        requireUnit3Vector(e1, "e1");
        requireUnit3Vector(e2, "e2");
        requireUnit3Vector(e3, "e3");
        requireOrthogonal(e1, "e1", e2, "e2");
        requireOrthogonal(e1, "e1", e3, "e3");
        requireOrthogonal(e2, "e2", e3, "e3");

        return new OrientationVectors3(e1, e2, e3);
    }

    private static void requireOrthogonal(final ImmutableVectorN e1, final String name1, final ImmutableVectorN e2,
                                          final String name2) {
        if (0.0 < Math.abs(e1.dot(e2))) {
            throw new IllegalArgumentException("Not orthogonal " + name1 + " " + e1 + " " + name2 + " " + e1);
        }
    }

    private static void requireUnit3Vector(final ImmutableVectorN e, final String message) {
        Objects.requireNonNull(e, message);
        final double m = e.magnitude2();
        if (e.getDimension() != 3 || TOLERANCE < Math.abs(m - 1.0)) {
            throw new IllegalArgumentException(message + " " + e + " (magnitude " + m + ")");
        }
    }

    /**
     * <p>
     * The first orthogonal unit basis vector of this orientation; its local
     * <i>x</i> direction.
     * </p>
     * <ul>
     * <li>Always has a (non null) e1 vector</li>
     * <li>The e1 vector is 3 {@linkplain ImmutableVectorN#getDimension()
     * dimensional}.</li>
     * <li>The e1 vector has unit {@linkplain ImmutableVectorN#magnitude()
     * magnitude}</li>
     * <li>The e1 vector is orthogonal to (has zero
     * {@linkplain ImmutableVectorN#dot(ImmutableVectorN) dot product with}) vector
     * {@linkplain #getE2() e2}.</li>
     * </ul>
     *
     * @return the e1 vector
     */
    public @NonNull ImmutableVectorN getE1() {
        return e1;
    }

    /**
     * <p>
     * The second orthogonal unit basis vector of this orientation; its local
     * <i>y</i> direction.
     * </p>
     * <ul>
     * <li>Always has a (non null) e2 vector</li>
     * <li>The e2 vector is 3 {@linkplain ImmutableVectorN#getDimension()
     * dimensional}.</li>
     * <li>The e2 vector has unit {@linkplain ImmutableVectorN#magnitude()
     * magnitude}</li>
     * <li>The e2 vector is orthogonal to (has zero
     * {@linkplain ImmutableVectorN#dot(ImmutableVectorN) dot product with}) vector
     * {@linkplain #getE3() e3}.</li>
     * </ul>
     *
     * @return the e2 vector
     */
    public @NonNull ImmutableVectorN getE2() {
        return e2;
    }

    /**
     * <p>
     * The third orthogonal unit basis vector of this orientation; its local
     * <i>z</i> direction.
     * </p>
     * <ul>
     * <li>Always has a (non null) e3 vector</li>
     * <li>The e3 vector is 3 {@linkplain ImmutableVectorN#getDimension()
     * dimensional}.</li>
     * <li>The e3 vector has unit {@linkplain ImmutableVectorN#magnitude()
     * magnitude}</li>
     * </ul>
     *
     * @return the e3 vector
     */
    public @NonNull ImmutableVectorN getE3() {
        return e3;
    }

}
