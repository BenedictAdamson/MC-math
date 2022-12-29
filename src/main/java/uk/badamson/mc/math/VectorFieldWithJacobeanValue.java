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
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * One point in a multi-dimensional space and the value of a {@linkplain VectorField vector field} at that point in space,
 * with the Jacobean (spatial rate-of-change) of the vector field at that point.
 * </p>
 */
@Immutable
public final class VectorFieldWithJacobeanValue {

    @Nonnull
    private final ImmutableVectorN x;
    @Nonnull
    private final ImmutableVectorN f;
    @Nonnull
    private final ImmutableMatrixN j;

    /**
     * <p>
     * Construct an object with given attribute values.
     * </p>
     *
     * @param x The position vector.
     * @param f The field value.
     * @param j The Jacobean
     * @throws NullPointerException     If any parameter is null
     * @throws IllegalArgumentException If the {@link Vector#getDimension() dimension} of {@code x}
     *                                  does not equal the {@linkplain Matrix#getColumns() number of columns} of {@code j}.
     *                                  If the {@link Vector#getDimension() dimension} of {@code f}
     *                                  * does not equal the {@linkplain Matrix#getRows()} number of rows of {@code j}.
     */
    public VectorFieldWithJacobeanValue(
            @Nonnull final ImmutableVectorN x,
            @Nonnull final ImmutableVectorN f,
            @Nonnull final ImmutableMatrixN j
    ) {
        Objects.requireNonNull(x, "x");
        Objects.requireNonNull(f, "f");
        Objects.requireNonNull(j, "j");
        if (x.getDimension() != j.getColumns()) {
            throw new IllegalArgumentException(
                    "Inconsistent dimensions x<" + x.getDimension() + ">, j <" + j.getColumns() + ">");
        }
        if (f.getDimension() != j.getRows()) {
            throw new IllegalArgumentException(
                    "Inconsistent dimensions f<" + f.getDimension() + ">, j <" + j.getRows() + ">");
        }

        this.x = x;
        this.f = f;
        this.j = j;
    }

    /**
     * <p>
     * Whether this object is <dfn>equivalent</dfn> to another object.
     * </p>
     * <p>
     * The {@link VectorFieldWithJacobeanValue} class has <i>value semantics</i>: this
     * object is equivalent to another object if, and only if, the other object is
     * also a {@link VectorFieldWithJacobeanValue} object, and the two objects have
     * equivalent attributes.
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
        final VectorFieldWithJacobeanValue other = (VectorFieldWithJacobeanValue) obj;
        return x.equals(other.x) && f.equals(other.f) && j.equals(other.j);
    }

    /**
     * <p>
     * The Jacobean.
     * </p>
     * <ul>
     * <li>The {@linkplain ImmutableMatrixN#getRows() number of rows} of the
     * Jacobean is equal to the {@linkplain Vector#getDimension()  dimension} of the {@linkplain #getF() field value}.</li>
     * <li>The {@linkplain ImmutableMatrixN#getColumns()  number of columns} of the
     * Jacobean is equal to the {@linkplain Vector#getDimension()  dimension} of the {@linkplain #getX() position vector]}.</li>
     * <li>Value {@linkplain Matrix#get(int, int) (i,j)} of the Jacobean matrix is the rate of change of the <var>i</var> component of the (vector) field value in the </var>j</var> direction, &part; f<sub>i</sub> / &part; x<sub>j</sub>.</li>
     * </ul>
     */
    public @Nonnull ImmutableMatrixN getJ() {
        return j;
    }

    /**
     * <p>
     * The vector field value at the {@linkplain #getX() point in space}.
     * </p>
     */
    @Nonnull
    public ImmutableVectorN getF() {
        return f;
    }

    /**
     * <p>
     * The position at which the vector field has the {@linkplain #getF() value}.
     * </p>
     */
    public @Nonnull ImmutableVectorN getX() {
        return x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x.hashCode();
        result = prime * result + f.hashCode();
        result = prime * result + j.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + "->" + f + ", J=" + j + "]";
    }

}
