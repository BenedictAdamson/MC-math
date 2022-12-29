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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

abstract class ArrayMatrix implements Matrix {

    @Nonnull
    protected final double[] elements;
    @Nonnegative
    protected final int rows;
    @Nonnegative
    protected final int columns;

    ArrayMatrix(final int rows, final int columns, @Nonnull final double[] elements) {
        this.rows = rows;
        this.columns = columns;
        this.elements = elements;
    }

    static void requireValidCreationArguments(int rows, int columns, double[] elements) {
        Objects.requireNonNull(elements, "elements");
        if (rows < 1) {
            throw new IllegalArgumentException("rows " + rows);
        }
        if (columns < 1) {
            throw new IllegalArgumentException("columns " + columns);
        }
        if (elements.length != rows * columns) {
            throw new IllegalArgumentException(
                    "Inconsistent rows " + rows + " columns " + columns + " elements.length " + elements.length);
        }
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        }
        if (!(obj instanceof final ArrayMatrix other)) {
            return false;
        }
        return rows == other.rows && Arrays.equals(elements, other.elements);
    }

    @Override
    @Nonnull
    public final String toString() {
        final var str = new StringBuilder("(");
        for (int i = 0; i < getRows(); ++i) {
            if (0 < i) {
                str.append('\n');
            }
            for (int j = 0; j < getColumns(); ++j) {
                str.append(get(i, j));
                if (i < getRows() - 1 || j < getColumns() - 1) {
                    str.append(',');
                }
            }
        }
        str.append(')');
        return str.toString();
    }

    @Override
    @Nonnegative
    public final int getColumns() {
        return columns;
    }

    @Override
    public final double get(@Nonnegative final int i, @Nonnegative final int j) {
        requireRowInBounds(i);
        requireColumnInBounds(j);
        return elements[i * columns + j];
    }

    protected final void requireColumnInBounds(int j) {
        if (j < 0 || columns <= j) {
            throw new IndexOutOfBoundsException("j " + j);
        }
    }

    protected final void requireRowInBounds(int i) {
        if (i < 0 || rows <= i) {
            throw new IndexOutOfBoundsException("i " + i);
        }
    }

    @Override
    @Nonnegative
    public final int getRows() {
        return rows;
    }

    @Override
    public final int hashCode() {
        final int prime = 37;
        int result = columns;
        result = prime * result + rows;
        result = prime * result + Arrays.hashCode(elements);
        return result;
    }

    @Nonnull
    @Override
    public final ImmutableVectorN multiply(@Nonnull final Vector x) {
        Objects.requireNonNull(x, "x");
        final int columns = getColumns();
        if (columns != x.getRows()) {
            throw new IllegalArgumentException("Inconsistent numbers of columns and rows");
        }
        final int n = getRows();
        final double[] ax = new double[n];
        for (int i = 0; i < n; ++i) {
            double dot = 0.0;
            final int j0 = i * columns;
            for (int j = 0; j < columns; ++j) {
                dot += elements[j0 + j] * x.get(j);
            }
            ax[i] = dot;
        }
        return new ImmutableVectorN(ax);
    }

    final double vectorDotProduct(@Nonnull final Vector that) {
        if (that instanceof ArrayMatrix) {
            return vectorDotProduct((ArrayMatrix) that);
        } else {
            double d = 0.0;
            for (int i = 0, n = elements.length; i < n; ++i) {
                d += elements[i] * that.get(i);
            }
            return d;
        }
    }

    private double vectorDotProduct(@Nonnull final ArrayMatrix that) {
        double d = 0.0;
        for (int i = 0, n = elements.length; i < n; ++i) {
            d += elements[i] * that.elements[i];
        }
        return d;
    }

    final double vectorMagnitude() {
        final double scale = getScale();
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale;
        } else {
            final double r = 1.0 / scale;
            double m2 = 0.0;
            for (final double xI : elements) {
                final double xIScaled = xI * r;
                m2 += xIScaled * xIScaled;
            }
            return Math.sqrt(m2) * scale;
        }
    }

    final double vectorMagnitude2() {
        final double scale = getScale();
        final double scale2 = scale * scale;
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale2;
        } else {
            final double r = 1.0 / scale;
            double m2 = 0.0;
            for (final double xI : elements) {
                final double xIScaled = xI * r;
                m2 += xIScaled * xIScaled;
            }
            return m2 * scale2;
        }
    }

    @Nonnegative
    private double getScale() {
        double scale = 0.0;
        for (final double xI : elements) {
            scale = Math.max(scale, Math.abs(xI));
        }
        return scale;
    }

    final double[] minusElements() {
        final var length = this.elements.length;
        final var elements = new double[length];
        for (int i = 0; i < length; ++i) {
            elements[i] = -this.elements[i];
        }
        return elements;
    }

    final double[] minusElements(@Nonnull final Matrix that) {
        Objects.requireNonNull(that, "that");
        Matrix.requireConsistentDimensions(this, that);
        if (that instanceof ArrayMatrix) {
            return minusElements((ArrayMatrix) that);
        } else {
            final int n = elements.length;
            final double[] minus = new double[n];
            int k = 0;
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < columns; ++j) {
                    minus[k] = elements[k] - that.get(i, j);
                    k++;
                }
            }
            return minus;
        }
    }

    private double[] minusElements(@Nonnull final ArrayMatrix that) {
        final int n = elements.length;
        final double[] minus = new double[n];
        for (int i = 0; i < n; ++i) {
            minus[i] = elements[i] - that.elements[i];
        }
        return minus;
    }

    final double[] plusElements(@Nonnull final Matrix that) {
        Objects.requireNonNull(that, "that");
        Matrix.requireConsistentDimensions(this, that);
        if (that instanceof ArrayMatrix) {
            return plusElements((ArrayMatrix) that);
        } else {
            final int n = elements.length;
            final double[] minus = new double[n];
            int k = 0;
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < columns; ++j) {
                    minus[k] = elements[k] + that.get(i, j);
                    k++;
                }
            }
            return minus;
        }
    }

    private double[] plusElements(@Nonnull final ArrayMatrix that) {
        final int n = elements.length;
        final double[] minus = new double[n];
        for (int i = 0; i < n; ++i) {
            minus[i] = elements[i] + that.elements[i];
        }
        return minus;
    }

    final double[] scaleElements(final double f) {
        final int n = elements.length;
        final double[] s = new double[n];
        for (int i = 0; i < n; ++i) {
            s[i] = elements[i] * f;
        }
        return s;
    }

    final double[] meanElements(@Nonnull final Matrix that) {
        Objects.requireNonNull(that, "that");
        Matrix.requireConsistentDimensions(this, that);
        if (that instanceof ArrayMatrix) {
            return meanElements((ArrayMatrix) that);
        } else {
            final int n = elements.length;
            final double[] mean = new double[n];
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < columns; ++j) {
                    int k = i * columns + j;
                    mean[k] = (elements[k] + that.get(i, j)) * 0.5;
                }
            }
            return mean;
        }
    }

    private double[] meanElements(@Nonnull final ArrayMatrix that) {
        final int n = elements.length;
        final double[] mean = new double[n];
        for (int i = 0; i < n; i++) {
            mean[i] = (elements[i] + that.elements[i]) * 0.5;
        }
        return mean;
    }

}
