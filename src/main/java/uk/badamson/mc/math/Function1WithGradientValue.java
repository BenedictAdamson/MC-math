package uk.badamson.mc.math;
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

import net.jcip.annotations.Immutable;

/**
 * <p>
 * One value from the domain of a {@linkplain Function1WithGradient single
 * dimensional function of a continuous variable that also has a computable
 * gradient} to the corresponding value in the codomain of the function and the
 * gradient of the function.
 * </p>
 */
@Immutable
public final class Function1WithGradientValue {

    private final double x;
    private final double f;
    private final double dfdx;

    /**
     * <p>
     * Construct an object with given attribute values.
     * </p>
     *
     * @param x
     *            The domain value.
     * @param f
     *            The codomain value.
     * @param dfdx
     *            The gradient value
     */
    public Function1WithGradientValue(final double x, final double f, final double dfdx) {
        this.x = x;
        this.f = f;
        this.dfdx = dfdx;
    }

    /**
     * <p>
     * Whether this object is <dfn>equivalent</dfn> another object.
     * </p>
     * <p>
     * The {@link Function1WithGradientValue} class has <i>value semantics</i>: this
     * object is equivalent to another object if, and only if, the other object is
     * also a {@link Function1WithGradientValue} object, and the two objects have
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
        final Function1WithGradientValue other = (Function1WithGradientValue) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(f) == Double.doubleToLongBits(other.f)
                && Double.doubleToLongBits(dfdx) == Double.doubleToLongBits(other.dfdx);
    }

    /**
     * <p>
     * The gradient value.
     * </p>
     */
    public final double getDfDx() {
        return dfdx;
    }

    /**
     * <p>
     * The codomain value.
     * </p>
     */
    public final double getF() {
        return f;
    }

    /**
     * <p>
     * The domain value
     * </p>
     */
    public final double getX() {
        return x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final long xBits = Double.doubleToLongBits(x);
        final long fBits = Double.doubleToLongBits(f);
        final long dfdxBits = Double.doubleToLongBits(dfdx);
        result = prime * result + (int) (xBits ^ xBits >>> 32);
        result = prime * result + (int) (fBits ^ fBits >>> 32);
        result = prime * result + (int) (dfdxBits ^ dfdxBits >>> 32);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + f + ", dfdx " + dfdx + ")";
    }

}
