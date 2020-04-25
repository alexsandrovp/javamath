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

import gist.Math.Fraction;

public final class FractionMatrix {
	private final Fraction[][] matrix;

	private FractionMatrix(Fraction[][] matrix, boolean assign, boolean check) {
		this.matrix = matrix;
		if (check && matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
			}
		}
	}

	public FractionMatrix(int[][] matrix, int denominator) {
		this.matrix = new Fraction[matrix.length][];
		if (matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
				this.matrix[i] = new Fraction[columnCount];
				for (int j = 0; j < columnCount; ++j) {
					this.matrix[i][j] = new Fraction(matrix[i][j], denominator);
				}
			}
		}
	}

	public FractionMatrix(Fraction[][] matrix) {
		this.matrix = new Fraction[matrix.length][];
		if (matrix.length > 0) {
			int columnCount = matrix[0].length;
			for (int i = 0; i < matrix.length; ++i) {
				if (matrix[i].length != columnCount)
					throw new ArithmeticException("invalid matrix (rows of different lengths)");
				this.matrix[i] = new Fraction[columnCount];
				for (int j = 0; j < columnCount; ++j) {
					this.matrix[i][j] = new Fraction(matrix[i][j]);
				}
			}
		}
	}

	public FractionMatrix(IntMatrix matrix, int denominator) {
		this.matrix = new Fraction[matrix.rowCount()][matrix.columnCount()];
		for (int i = 0; i < this.matrix.length; ++i) {
			for (int j = 0; j < this.matrix[i].length; ++j) {
				this.matrix[i][j] = new Fraction(matrix.get(i, j), denominator);
			}
		}
	}

	public FractionMatrix(FractionMatrix matrix) {
		this.matrix = new Fraction[matrix.rowCount()][matrix.columnCount()];
		for (int i = 0; i < this.matrix.length; ++i) {
			for (int j = 0; j < this.matrix[i].length; ++j) {
				this.matrix[i][j] = new Fraction(matrix.get(i, j));
			}
		}
	}

	public int rowCount() {
		return matrix.length;
	}

	public int columnCount() {
		return matrix.length == 0 ? 0 : matrix[0].length;
	}

	public Fraction get(int i, int j) {
		return matrix[i][j];
	}

	public FractionMatrix transpose() {
		final int colCount = columnCount();
		Fraction[][] m = new Fraction[colCount][matrix.length];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				m[j][i] = new Fraction(matrix[i][j]);
			}
		}
		return new FractionMatrix(m, true, false);
	}

	public FractionMatrix adjugate() throws ArithmeticException {
		final int colCount = columnCount();
		if (colCount == 0 || colCount != matrix.length)
			throw new ArithmeticException("matrix is not square");

		if (colCount == 1) {
			return new FractionMatrix(new Fraction[][] { { new Fraction(1, 1) } }, true, false);
		}

		FractionMatrix transposed = transpose();
		Fraction[][] adjoint = new Fraction[matrix.length][colCount];

		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				boolean skipRow = false, skipCol = false;
				Fraction[][] m = new Fraction[matrix.length - 1][colCount - 1];
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
				Fraction cofactor = new FractionMatrix(m).determinant();
				if ((i + j) % 2 != 0)
					cofactor = cofactor.multiply(-1);
				adjoint[i][j] = cofactor;
			}
		}

		return new FractionMatrix(adjoint, true, false);
	}

	public Fraction determinant() throws ArithmeticException {
		final int colCount = columnCount();
		if (matrix.length == 0 || matrix.length != colCount)
			throw new ArithmeticException("not a square matrix");

		if (matrix.length == 1) {
			return matrix[0][0];
		}

		if (matrix.length == 2) {
			return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
		}

		if (matrix.length == 3) {
			return matrix[0][0].multiply(matrix[1][1]).multiply(matrix[2][2])
					.add(matrix[1][0].multiply(matrix[2][1]).multiply(matrix[0][2])
							.add(matrix[2][0].multiply(matrix[0][1]).multiply(matrix[1][2]))
							.subtract(matrix[2][0].multiply(matrix[1][1]).multiply(matrix[0][2]))
							.subtract(matrix[0][0].multiply(matrix[2][1]).multiply(matrix[1][2]))
							.subtract(matrix[1][0].multiply(matrix[0][1]).multiply(matrix[2][2])));
		}

		Fraction result = new Fraction(0, 1);

		for (int i = 0; i < matrix.length; ++i) {
			if (matrix[i][0].getNumerator() == 0)
				continue;

			boolean skipRow = false;
			Fraction[][] m = new Fraction[matrix.length - 1][colCount - 1];

			for (int u = 0; u < matrix.length; ++u) {
				if (u == i) {
					skipRow = true;
					continue;
				}
				for (int v = 1; v < colCount; ++v) {
					int x = skipRow ? u - 1 : u;
					m[x][v - 1] = matrix[u][v].getCopy();
				}
			}

			Fraction cofactor = matrix[i][0].multiply(new FractionMatrix(m).determinant());
			if (i % 2 == 0)
				result = result.add(cofactor);
			else
				result = result.subtract(cofactor);
		}

		return result;
	}

	public FractionMatrix invert() throws ArithmeticException {
		Fraction det = determinant();
		if (det.getNumerator() == 0)
			throw new ArithmeticException("matrix cannot be inverted (determinant is zero)");
		return adjugate().multiply(det.invert());
	}

	public FractionMatrix hadamard_product(int[][] m) throws ArithmeticException {
		final int colCount = columnCount();
		final int checkColCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || checkColCount != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		Fraction[][] result = new Fraction[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j].multiply(m[i][j]);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix hadamard_product(Fraction[][] m) throws ArithmeticException {
		final int colCount = columnCount();
		final int checkColCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || checkColCount != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		Fraction[][] result = new Fraction[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j].multiply(m[i][j]);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix hadamard_product(IntMatrix m) throws ArithmeticException {
		final int colCount = columnCount();
		if (m.rowCount() != matrix.length || m.columnCount() != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		Fraction[][] result = new Fraction[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j].multiply(m.get(i, j));
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix hadamard_product(FractionMatrix m) throws ArithmeticException {
		final int colCount = columnCount();
		if (m.rowCount() != matrix.length || m.columnCount() != colCount)
			throw new ArithmeticException("matrices do not have the same dimensions");

		Fraction[][] result = new Fraction[matrix.length][colCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < colCount; ++j) {
				result[i][j] = matrix[i][j].multiply(m.get(i, j));
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(int n) {
		// adds n to each element
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].add(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(Fraction n) {
		// adds n to each element
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].add(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				if (m[i].length != mColumnCount)
					throw new ArithmeticException("invalid matrix adder (rows of different lengths)");
				result[i][j] = matrix[i][j].add(m[i][j]);
			}
		}

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(Fraction[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				if (m[i].length != mColumnCount)
					throw new ArithmeticException("invalid matrix adder (rows of different lengths)");
				result[i][j] = matrix[i][j].add(m[i][j]);
			}
		}

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mRowCount != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				result[i][j] = matrix[i][j].add(m.get(i, j));

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix add(FractionMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mRowCount != matrix.length || mColumnCount != columnCount())
			throw new ArithmeticException("cannot add matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				result[i][j] = matrix[i][j].add(m.get(i, j));

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix multiply(int n) {
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].multiply(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix multiply(Fraction n) {
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].multiply(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix multiply(int[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != columnCount() || mColumnCount != matrix.length)
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				for (int k = 0; k < m.length; ++k) {
					if (m[k].length != mColumnCount)
						throw new ArithmeticException("invalid matrix multiplier (rows of different lengths)");
					if (result[i][j] == null)
						result[i][j] = new Fraction(0, 1);
					result[i][j] = result[i][j].add(matrix[i][k].multiply(m[k][j]));
				}
			}
		}

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix multiply(Fraction[][] m) throws ArithmeticException {
		int mColumnCount = m.length > 0 ? m[0].length : 0;
		if (m.length != columnCount() || mColumnCount != matrix.length)
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < mColumnCount; ++j) {
				for (int k = 0; k < m.length; ++k) {
					if (m[k].length != mColumnCount)
						throw new ArithmeticException("invalid matrix multiplier (rows of different lengths)");
					if (result[i][j] == null)
						result[i][j] = new Fraction(0, 1);
					result[i][j] = result[i][j].add(matrix[i][k].multiply(m[k][j]));
				}
			}
		}

		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix multiply(IntMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mColumnCount != matrix.length || mRowCount != columnCount())
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				for (int k = 0; k < mRowCount; ++k) {
					if (result[i][j] == null)
						result[i][j] = new Fraction(0, 1);
					result[i][j] = result[i][j].add(matrix[i][k].multiply(m.get(k, j)));
				}

		return new FractionMatrix(result, true, false);
	}
	
	public FractionMatrix multiply(FractionMatrix m) throws ArithmeticException {
		int mRowCount = m.rowCount();
		int mColumnCount = m.columnCount();
		if (mColumnCount != matrix.length || mRowCount != columnCount())
			throw new ArithmeticException("cannot multiply matrices, incompatible dimensions");

		Fraction[][] result = new Fraction[matrix.length][mColumnCount];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < mColumnCount; ++j)
				for (int k = 0; k < mRowCount; ++k) {
					if (result[i][j] == null)
						result[i][j] = new Fraction(0, 1);
					result[i][j] = result[i][j].add(matrix[i][k].multiply(m.get(k, j)));
				}

		return new FractionMatrix(result, true, false);
	}
	
	public FractionMatrix divide(int n) {
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].divide(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public FractionMatrix divide(Fraction n) {
		Fraction result[][] = new Fraction[matrix.length][];
		for (int i = 0; i < matrix.length; ++i) {
			result[i] = new Fraction[matrix[i].length];
			for (int j = 0; j < matrix[i].length; ++j) {
				result[i][j] = matrix[i][j].divide(n);
			}
		}
		return new FractionMatrix(result, true, false);
	}

	public static FractionMatrix invert(IntMatrix m) throws ArithmeticException {
		int det = m.determinant();
		if (det == 0)
			throw new ArithmeticException("matrix cannot be inverted (determinant is zero)");
		return new FractionMatrix(m.adjugate(), det);
	}
}