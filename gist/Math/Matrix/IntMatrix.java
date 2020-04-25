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

public final class IntMatrix {
	private final int[][] matrix;

	private IntMatrix(int[][] matrix, boolean assign, boolean check) {
		this.matrix = matrix;
		if (check && matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
			}
		}
	}

	public IntMatrix(int[][] matrix) {
		this.matrix = new int[matrix.length][];
		if (matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
				this.matrix[i] = new int[columnCount];
				for (int j = 0; j < columnCount; ++j) {
					this.matrix[i][j] = matrix[i][j];
				}
			}
		}
	}

	public IntMatrix(IntMatrix matrix) {
		this.matrix = new int[matrix.rowCount()][matrix.columnCount()];
		for (int i = 0; i < this.matrix.length; ++i) {
			for (int j = 0; j < this.matrix[i].length; ++j) {
				this.matrix[i][j] = matrix.get(i, j);
			}
		}
	}

	public int rowCount() {
		return matrix.length;
	}

	public int columnCount() {
		return matrix.length == 0 ? 0 : matrix[0].length;
	}

	public int get(int i, int j) {
		return matrix[i][j];
	}

	public IntMatrix transpose() {
		final int colCount = columnCount();
		int[][] m = new int[colCount][matrix.length];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				m[j][i] = matrix[i][j];
			}
		}
		return new IntMatrix(m, true, false);
	}

	public IntMatrix adjugate() throws ArithmeticException {
		final int colCount = columnCount();
		if (colCount == 0 || colCount != matrix.length)
			throw new ArithmeticException("matrix is not square");

		if (colCount == 1) {
			return new IntMatrix(new int[][] { { 1 } }, true, false);
		}

		IntMatrix transposed = transpose();
		int[][] adjoint = new int[matrix.length][colCount];

		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				boolean skipRow = false, skipCol = false;
				int[][] m = new int[matrix.length - 1][colCount - 1];
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
				int cofactor = new IntMatrix(m).determinant();
				if ((i + j) % 2 != 0)
					cofactor *= -1;
				adjoint[i][j] = cofactor;
			}
		}

		return new IntMatrix(adjoint, true, false);
	}

	public int determinant() throws ArithmeticException {
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

		int result = 0;

		for (int i = 0; i < matrix.length; ++i) {
			if (matrix[i][0] == 0)
				continue;

			boolean skipRow = false;
			int[][] m = new int[matrix.length - 1][colCount - 1];
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
			int cofactor = new IntMatrix(m).determinant() * matrix[i][0];
			if (i % 2 == 0)
				result += cofactor;
			else
				result -= cofactor;
		}

		return result;
	}

	public IntMatrix hadamard_product(int[][] m) throws ArithmeticException {
		final int colCount = columnCount();
		final int checkColCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || checkColCount != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		int[][] result = new int[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m[i][j];
			}
		}
		return new IntMatrix(result, true, false);
	}

	public IntMatrix hadamard_product(IntMatrix m) throws ArithmeticException {
		final int colCount = columnCount();
		if (m.rowCount() != matrix.length || m.columnCount() != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		int[][] result = new int[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j] * m.get(i, j);
			}
		}
		return new IntMatrix(result, true, false);
	}

	public IntMatrix add(int n) {
		// adds n to each element
		int result[][] = new int[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new int[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j] + n;
			}
		}
		return new IntMatrix(result, true, false);
	}

	public IntMatrix add(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		int[][] result = new int[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				if (m[i].length != mColumnCount)
					throw new ArithmeticException("invalid matrix adder (rows of different lengths)");
				result[i][j] = matrix[i][j] + m[i][j];
			}
		}

		return new IntMatrix(result, true, false);
	}

	public IntMatrix add(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mRowCount != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		int[][] result = new int[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				result[i][j] = matrix[i][j] + m.get(i, j);

		return new IntMatrix(result, true, false);
	}

	public IntMatrix multiply(int n) {
		int result[][] = new int[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new int[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j] * n;
			}
		}
		return new IntMatrix(result, true, false);
	}

	public IntMatrix multiply(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != columnCount() || mColumnCount != matrix.length)
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		int[][] result = new int[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				for (int k = 0; k < m.length; ++k) {
					if (m[k].length != mColumnCount)
						throw new ArithmeticException("invalid matrix multiplier (rows of different lengths)");
					result[i][j] += matrix[i][k] * m[k][j];
				}
			}
		}

		return new IntMatrix(result, true, false);
	}

	public IntMatrix multiply(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mColumnCount != matrix.length || mRowCount != columnCount())
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		int[][] result = new int[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				for (int k = 0; k < mRowCount; ++k)
					result[i][j] += matrix[i][k] * m.get(k, j);

		return new IntMatrix(result, true, false);
	}
}