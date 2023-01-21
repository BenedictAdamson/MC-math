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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * One vector from the domain of a {@linkplain FunctionNTo1WithGradient scalar
 * function of a vector that also has a computable gradient} to the
 * corresponding value in the codomain of the function and the gradient of the
 * function.
 * </p>
 */
@Immutable
public final class FunctionNWithGradientValue {

    private final double f;
    private final ImmutableVectorN x;
    private final ImmutableVectorN dfdx;

    /**
     * <p>
     * Construct an object with given attribute values.
     * </p>
     *
     * @param x    The domain vector.
     * @param f    The codomain value.
     * @param dfdx The gradient vector
     * @throws NullPointerException     <ul>
     *                                              <li>If {@code x} is null.</li>
     *                                              <li>If {@code dfdx} is null.</li>
     *                                              </ul>
     * @throws IllegalArgumentException If {@code x} and {@code dfdx} have different
     *                                  {@linkplain ImmutableVectorN#getDimension() dimensions}.
     */
    public FunctionNWithGradientValue(@Nonnull final ImmutableVectorN x,
                                      final double f,
                                      @Nonnull final ImmutableVectorN dfdx) {
        Objects.requireNonNull(x, "x");
        Objects.requireNonNull(dfdx, "dfdx");
        if (x.getDimension() != dfdx.getDimension()) {
            throw new IllegalArgumentException(
                    "Inconsistent dimensions x<" + x.getDimension() + ">, dfdx <" + dfdx.getDimension() + ">");
        }

        this.f = f;
        this.x = x;
        this.dfdx = dfdx;
    }

    /**
     * <p>
     * Whether this object is <dfn>equivalent</dfn> another object.
     * </p>
     * <p>
     * The {@link FunctionNWithGradientValue} class has <i>value semantics</i>: this
     * object is equivalent to another object if, and only if, the other object is
     * also a {@link FunctionNWithGradientValue} object, and the two objects have
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
        final FunctionNWithGradientValue other = (FunctionNWithGradientValue) obj;
        return Double.doubleToLongBits(f) == Double.doubleToLongBits(other.f) && x.equals(other.x)
                && dfdx.equals(other.dfdx);
    }

    /**
     * <p>
     * The gradient vector.
     * </p>
     * <ul>
     * <li>Always have a (non null) gradient vector.</li>
     * <li>The {@linkplain ImmutableVectorN#getDimension() dimension} of the
     * gradient vector is equal to the dimension of the {@linkplain #getX() domain
     * vector}.</li>
     * </ul>
     */
    public @Nonnull ImmutableVectorN getDfDx() {
        return dfdx;
    }

    /**
     * <p>
     * The codomain value.
     * </p>
     */
    public double getF() {
        return f;
    }

    /**
     * <p>
     * The domain vector
     * </p>
     *
     * <ul>
     * <li>Always have a (non null) domain vector.</li>
     * </ul>
     */
    public @Nonnull ImmutableVectorN getX() {
        return x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final long fBits = Double.doubleToLongBits(f);
        result = prime * result + (int) (fBits ^ fBits >>> 32);
        result = prime * result + x.hashCode();
        result = prime * result + dfdx.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + "->" + f + ", dfdx=" + dfdx + "]";
    }

}
