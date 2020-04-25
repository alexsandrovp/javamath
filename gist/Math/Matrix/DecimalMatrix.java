/*
Copyright (c) 2020, Alex Vargas
This software is provided 'as-is', without any express or implied
warranty. In no event will the authors be held liable for any damages
arising from the use of this software.
Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
*/

package gist.Math.Matrix;

public final class DecimalMatrix {
	private final double[][] matrix;

	private DecimalMatrix(double[][] matrix, boolean assign, boolean check) {
		this.matrix = matrix;
		if (check && matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
			}
		}
	}

	public DecimalMatrix(int[][] matrix) {
		this.matrix = new double[matrix.length][];
		if (matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
				this.matrix[i] = new double[columnCount];
				for (int j = 0; j < columnCount; ++j) {
					this.matrix[i][j] = matrix[i][j];
				}
			}
		}
	}

	public DecimalMatrix(double[][] matrix) {
		this.matrix = new double[matrix.length][];
		if (matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
				this.matrix[i] = new double[columnCount];
				for (int j = 0; j < columnCount; ++j) {
					this.matrix[i][j] = matrix[i][j];
				}
			}
		}
	}

	public DecimalMatrix(IntMatrix matrix) {
		this.matrix = new double[matrix.rowCount()][matrix.columnCount()];
		for (int i = 0; i < this.matrix.length; ++i) {
			for (int j = 0; j < this.matrix[i].length; ++j) {
				this.matrix[i][j] = matrix.get(i, j);
			}
		}
	}

	public DecimalMatrix(DecimalMatrix matrix) {
		this.matrix = new double[matrix.rowCount()][matrix.columnCount()];
		for (int i = 0; i < this.matrix.length; ++i) {
			for (int j = 0; j < this.matrix[i].length; ++j) {
				this.matrix[i][j] = matrix.get(i, j);
			}
		}
	}

	//because java sucks
    private static boolean doubleEquals(double a, double b) {
        if (a == b) return true;
        a = Math.abs(a - b);
        return a < 0.00000000000001;
    }

	public int rowCount() {
		return matrix.length;
	}

	public int columnCount() {
		return matrix.length == 0 ? 0 : matrix[0].length;
	}

	public double get(int i, int j) {
		return matrix[i][j];
	}

	public DecimalMatrix transpose() {
		final int colCount = columnCount();
		double[][] m = new double[colCount][matrix.length];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				m[j][i] = matrix[i][j];
			}
		}
		return new DecimalMatrix(m, true, false);
	}

	public DecimalMatrix adjugate() throws ArithmeticException {
		final int colCount = columnCount();
		if (colCount == 0 || colCount != matrix.length)
			throw new ArithmeticException("matrix is not square");

		if (colCount == 1) {
			return new DecimalMatrix(new double[][] { { 1 } }, true, false);
		}

		DecimalMatrix transposed = transpose();
		double[][] adjoint = new double[matrix.length][colCount];

		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				boolean skipRow = false, skipCol = false;
				double[][] m = new double[matrix.length - 1][colCount - 1];
				for (int u = 0; u < matrix.length; ++u) {
					if (u == i) {
						skipRow = true;
						continue;
					}
					skipCol = false;
					for (int v = 0; v < colCount; ++v) {
						if (v == j) {
							skipCol = true;
							continue;
						}
						int x = skipRow ? u - 1 : u;
						int y = skipCol ? v - 1 : v;
						m[x][y] = transposed.get(u, v);
					}
				}
				double cofactor = new DecimalMatrix(m).determinant();
				if ((i + j) % 2 != 0)
					cofactor *= -1;
				adjoint[i][j] = cofactor;
			}
		}

		return new DecimalMatrix(adjoint, true, false);
	}

	public double determinant() throws ArithmeticException {
		final int colCount = columnCount();
		if (matrix.length == 0 || matrix.length != colCount)
			throw new ArithmeticException("not a square matrix");

		if (matrix.length == 1) {
			return matrix[0][0];
		}

		if (matrix.length == 2) {
			return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
		}

		if (matrix.length == 3) {
			return matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[1][0] * matrix[2][1] * matrix[0][2]
					+ matrix[2][0] * matrix[0][1] * matrix[1][2] - matrix[2][0] * matrix[1][1] * matrix[0][2]
					- matrix[0][0] * matrix[2][1] * matrix[1][2] - matrix[1][0] * matrix[0][1] * matrix[2][2];
		}

		double result = 0;

		for (int i = 0; i < matrix.length; ++i) {
			if (doubleEquals(matrix[i][0], 0))
				continue;

			boolean skipRow = false;
			double[][] m = new double[matrix.length - 1][colCount - 1];
			for (int u = 0; u < matrix.length; ++u) {
				if (u == i) {
					skipRow = true;
					continue;
				}
				for (int v = 1; v < colCount; ++v) {
					int x = skipRow ? u - 1 : u;
					m[x][v - 1] = matrix[u][v];
				}
			}
			double cofactor = new DecimalMatrix(m).determinant() * matrix[i][0];
			if (i % 2 == 0)
				result += cofactor;
			else
				result -= cofactor;
		}

		return result;
	}

	public DecimalMatrix invert() throws ArithmeticException {
		double det = determinant();
		if (doubleEquals(det, 0))
			throw new ArithmeticException("matrix cannot be inverted (determinant is zero)");
		return adjugate().multiply(1 / det);
	}

	public DecimalMatrix hadamard_product(int[][] m) throws ArithmeticException {
		final int colCount = columnCount();
		final int checkColCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || checkColCount != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		double[][] result = new double[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m[i][j];
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix hadamard_product(double[][] m) throws ArithmeticException {
		final int colCount = columnCount();
		final int checkColCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || checkColCount != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		double[][] result = new double[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m[i][j];
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix hadamard_product(IntMatrix m) throws ArithmeticException {
		final int colCount = columnCount();
		if (m.rowCount() != matrix.length || m.columnCount() != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		double[][] result = new double[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m.get(i, j);
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix hadamard_product(DecimalMatrix m) throws ArithmeticException {
		final int colCount = columnCount();
		if (m.rowCount() != matrix.length || m.columnCount() != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		double[][] result = new double[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m.get(i, j);
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix add(double n) {
		// adds n to each element
		double result[][] = new double[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new double[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j] + n;
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix add(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				if (m[i].length != mColumnCount)
					throw new ArithmeticException("invalid matrix adder (rows of different lengths)");
				result[i][j] = matrix[i][j] + m[i][j];
			}
		}

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix add(double[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				if (m[i].length != mColumnCount)
					throw new ArithmeticException("invalid matrix adder (rows of different lengths)");
				result[i][j] = matrix[i][j] + m[i][j];
			}
		}

		return new DecimalMatrix(result, true, false);
	}
	
	public DecimalMatrix add(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mRowCount != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				result[i][j] = matrix[i][j] + m.get(i, j);

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix add(DecimalMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mRowCount != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				result[i][j] = matrix[i][j] + m.get(i, j);

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix multiply(double n) {
		double result[][] = new double[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new double[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j] * n;
			}
		}
		return new DecimalMatrix(result, true, false);
	}
	
	public DecimalMatrix multiply(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != columnCount() || mColumnCount != matrix.length)
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				for (int k = 0; k < m.length; ++k) {
					if (m[k].length != mColumnCount)
						throw new ArithmeticException("invalid matrix multiplier (rows of different lengths)");
					result[i][j] += matrix[i][k] * m[k][j];
				}
			}
		}

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix multiply(double[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != columnCount() || mColumnCount != matrix.length)
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				for (int k = 0; k < m.length; ++k) {
					if (m[k].length != mColumnCount)
						throw new ArithmeticException("invalid matrix multiplier (rows of different lengths)");
					result[i][j] += matrix[i][k] * m[k][j];
				}
			}
		}

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix multiply(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mColumnCount != matrix.length || mRowCount != columnCount())
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				for (int k = 0; k < mRowCount; ++k)
					result[i][j] += matrix[i][k] * m.get(k, j);

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix multiply(DecimalMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mColumnCount != matrix.length || mRowCount != columnCount())
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		double[][] result = new double[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				for (int k = 0; k < mRowCount; ++k)
					result[i][j] += matrix[i][k] * m.get(k, j);

		return new DecimalMatrix(result, true, false);
	}

	public DecimalMatrix divide(double n) {
		double result[][] = new double[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new double[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j] / n;
			}
		}
		return new DecimalMatrix(result, true, false);
	}

	public static DecimalMatrix invert(IntMatrix m) throws ArithmeticException {
		int det = m.determinant();
		if (doubleEquals(det, 0))
			throw new ArithmeticException("matrix cannot be inverted (determinant is zero)");
		return new DecimalMatrix(m.adjugate()).multiply(1.0 / det);
	}
}