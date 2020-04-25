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

import java.util.Hashtable;
import java.util.Map;

import gist.DataReaderUTest;

public class DecimalMatrixUTest {

	private static Map<String, Object> testData = new Hashtable<String, Object>();

	// because java sucks
	private static boolean doubleEquals(double a, double b) {
		if (a == b)
			return true;
		a = Math.abs(a - b);
		return a < 0.00000000000001;
	}

	private static void areMatricesEqual(String testName, DecimalMatrix reference, DecimalMatrix transformed,
			DecimalMatrix expected) throws Exception {
		if (transformed.rowCount() != reference.rowCount())
			throw new Exception(testName + ": wrong row count (1)");

		if (transformed.rowCount() != expected.rowCount())
			throw new Exception(testName + ": wrong row count (2)");

		if (transformed.columnCount() != reference.columnCount())
			throw new Exception(testName + ": wrong column count (1)");

		if (transformed.columnCount() != expected.columnCount())
			throw new Exception(testName + ": wrong column count (2)");

		for (int i = 0; i < transformed.rowCount(); ++i) {
			for (int j = 0; j < transformed.columnCount(); ++j) {
				if (!doubleEquals(transformed.get(i, j), expected.get(i, j)))
					throw new Exception(testName + ": element mismatch @(" + i + "," + j + ")");
			}
		}
	}

	private static void testConstructorIntArray(int[][] m) throws Exception {

		DecimalMatrix ma = new DecimalMatrix(m);
		if (ma.rowCount() != m.length)
			throw new Exception("constructor(array of ints): wrong row count");
		if (ma.columnCount() != m[0].length)
			throw new Exception("constructor(array of ints): wrong column count");
		for (int i = 0; i < ma.rowCount(); ++i) {
			for (int j = 0; j < ma.columnCount(); ++j) {
				if (!doubleEquals(ma.get(i, j), m[i][j]))
					throw new Exception("constructor(array of ints): element mismatch @(" + i + "," + j + ")");
			}
		}

		DecimalMatrix mb = new DecimalMatrix(new IntMatrix(m));
		if (mb.rowCount() != m.length)
			throw new Exception("constructor(copy IntMatrix): wrong row count");
		if (mb.columnCount() != m[0].length)
			throw new Exception("constructor(copy IntMatrix): wrong column count");
		for (int i = 0; i < mb.rowCount(); ++i) {
			for (int j = 0; j < mb.columnCount(); ++j) {
				if (!doubleEquals(mb.get(i, j), m[i][j]))
					throw new Exception("constructor(copy IntMatrix): element mismatch @(" + i + "," + j + ")");
			}
		}
	}

	private static void testConstructor(double[][] m) throws Exception {
		DecimalMatrix ma = new DecimalMatrix(m);
		if (ma.rowCount() != m.length)
			throw new Exception("constructor(array): wrong row count");
		if (ma.columnCount() != m[0].length)
			throw new Exception("constructor(array): wrong column count");
		for (int i = 0; i < ma.rowCount(); ++i) {
			for (int j = 0; j < ma.columnCount(); ++j) {
				if (!doubleEquals(ma.get(i, j), m[i][j]))
					throw new Exception("constructor(array): element mismatch @(" + i + "," + j + ")");
			}
		}

		DecimalMatrix mb = new DecimalMatrix(ma);
		if (mb.rowCount() != ma.rowCount())
			throw new Exception("constructor(copy): wrong row count");
		if (mb.columnCount() != ma.columnCount())
			throw new Exception("constructor(copy): wrong column count");
		for (int i = 0; i < mb.rowCount(); ++i) {
			for (int j = 0; j < mb.columnCount(); ++j) {
				if (!doubleEquals(mb.get(i, j), ma.get(i, j)))
					throw new Exception("constructor(copy): element mismatch @(" + i + "," + j + ")");
			}
		}
	}

	private static void testConstructor() throws Exception {
		int[][] im = (int[][]) testData.get("mi4x5");
		double[][] m = (double[][]) testData.get("m4x5");
		testConstructorIntArray(im);
		testConstructor(m);
	}

	private static void testGetElement() throws Exception {
		double[][] m4x5 = (double[][]) testData.get("m4x5");
		double get_m4x5_1_3_Expected = (double) testData.get("get_m4x5_1_3_Expected");
		double get_m4x5_3_1_Expected = (double) testData.get("get_m4x5_3_1_Expected");
		DecimalMatrix ma = new DecimalMatrix(m4x5);
		if (!doubleEquals(ma.get(1, 3), get_m4x5_1_3_Expected))
			throw new Exception("get: wrong element @(1, 3), expected " + get_m4x5_1_3_Expected);

		if (!doubleEquals(ma.get(3, 1), get_m4x5_3_1_Expected))
			throw new Exception("get: wrong element @(3, 1), expected " + get_m4x5_3_1_Expected);
	}

	private static void testTranspose() throws Exception {
		double[][] m5x4 = (double[][]) testData.get("m5x4");
		double[][] transpose_m5x4_Expected = (double[][]) testData.get("transpose_m5x4_Expected");
		DecimalMatrix ma = new DecimalMatrix(m5x4).transpose();
		areMatricesEqual("transpose", ma, ma, new DecimalMatrix(transpose_m5x4_Expected));
	}

	private static void testHadamardProduct() throws Exception {

		double[][] m2x3 = (double[][]) testData.get("m2x3");
		double[][] hadamard_Multiplier = (double[][]) testData.get("hadamard_Multiplier");
		int[][] hadamard_iMultiplier = (int[][]) testData.get("hadamard_iMultiplier");

		DecimalMatrix hadamard_Multiplier_X_m2x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("hadamard_Multiplier_X_m2x3_Expected"));

		DecimalMatrix hadamard_iMultiplier_X_m2x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("hadamard_iMultiplier_X_m2x3_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m2x3);

		DecimalMatrix result = ma.hadamard_product(hadamard_iMultiplier);
		areMatricesEqual("hadamard_product(array)", ma, result, hadamard_iMultiplier_X_m2x3_Expected);

		result = ma.hadamard_product(new IntMatrix(hadamard_iMultiplier));
		areMatricesEqual("hadamard_product(int array)", ma, result, hadamard_iMultiplier_X_m2x3_Expected);

		result = ma.hadamard_product(hadamard_Multiplier);
		areMatricesEqual("hadamard_product(array)", ma, result, hadamard_Multiplier_X_m2x3_Expected);

		result = ma.hadamard_product(new DecimalMatrix(hadamard_Multiplier));
		areMatricesEqual("hadamard_product(matrix)", ma, result, hadamard_Multiplier_X_m2x3_Expected);
	}

	private static void testAddScalar() throws Exception {

		double[][] m2x3 = (double[][]) testData.get("m2x3");

		double addScalar_Addend = (double) testData.get("addScalar_Addend");
		DecimalMatrix addScalar_Addend_p_m2x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("addScalar_Addend_+_m2x3_Expected"));

		int addScalar_iAddend = (int) testData.get("addScalar_iAddend");
		DecimalMatrix addScalar_iAddend_p_m2x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("addScalar_iAddend_+_m2x3_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m2x3);
		DecimalMatrix result = ma.add(addScalar_iAddend);
		areMatricesEqual("add(int scalar)", ma, result, addScalar_iAddend_p_m2x3_Expected);

		result = ma.add(addScalar_Addend);
		areMatricesEqual("add(scalar)", ma, result, addScalar_Addend_p_m2x3_Expected);
	}

	private static void testAdd() throws Exception {
		double[][] m2x2 = (double[][]) testData.get("m2x2");

		double[][] addMatrix_Addend = (double[][]) testData.get("addMatrix_Addend");
		DecimalMatrix addMatrix_Addend_p_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("addMatrix_Addend_+_m2x2_Expected"));

		int[][] addMatrix_iAddend = (int[][]) testData.get("addMatrix_iAddend");
		DecimalMatrix addMatrix_iAddend_p_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("addMatrix_iAddend_+_m2x2_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m2x2);

		DecimalMatrix result = ma.add(addMatrix_iAddend);
		areMatricesEqual("add(array of int)", ma, result, addMatrix_iAddend_p_m2x2_Expected);

		result = ma.add(addMatrix_Addend);
		areMatricesEqual("add(array)", ma, result, addMatrix_Addend_p_m2x2_Expected);

		result = ma.add(new IntMatrix(addMatrix_iAddend));
		areMatricesEqual("add(IntMatrix)", ma, result, addMatrix_iAddend_p_m2x2_Expected);

		result = ma.add(new DecimalMatrix(addMatrix_iAddend));
		areMatricesEqual("add(DecimalMatrix 1)", ma, result, addMatrix_iAddend_p_m2x2_Expected);

		result = ma.add(new DecimalMatrix(addMatrix_Addend));
		areMatricesEqual("add(DecimalMatrix 2)", ma, result, addMatrix_Addend_p_m2x2_Expected);
	}

	private static void testMultiplyScalar() throws Exception {
		double[][] m2x2 = (double[][]) testData.get("m2x2");
		double multiplyScalar_Multiplyer = (double) testData.get("multiplyScalar_Multiplyer");
		int multiplyScalar_iMultiplyer = (int) testData.get("multiplyScalar_iMultiplyer");
		DecimalMatrix multiplyScalar_iMultiplyer_X_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyScalar_iMultiplyer_X_m2x2_Expected"));

		DecimalMatrix multiplyScalar_Multiplyer_X_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyScalar_Multiplyer_X_m2x2_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m2x2);
		DecimalMatrix result = ma.multiply(multiplyScalar_iMultiplyer);
		areMatricesEqual("multiply(int)", ma, result, multiplyScalar_iMultiplyer_X_m2x2_Expected);

		result = ma.multiply(multiplyScalar_Multiplyer);
		areMatricesEqual("multiply(int)", ma, result, multiplyScalar_Multiplyer_X_m2x2_Expected);
	}

	private static void testMultiply() throws Exception {

		double[][] m4x5 = (double[][]) testData.get("m4x5");
		double[][] m5x4 = (double[][]) testData.get("m5x4");
		double[][] m3x3 = (double[][]) testData.get("m3x3");

		double[][] multiplyMatrix_Multiplyer3x3 = (double[][]) testData.get("multiplyMatrix_Multiplyer3x3");
		int[][] multiplyMatrix_iMultiplyer3x3 = (int[][]) testData.get("multiplyMatrix_iMultiplyer3x3");

		DecimalMatrix multiplyMatrix_m4x5_X_m5x4_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyMatrix_m4x5_X_m5x4_Expected"));

		DecimalMatrix multiplyMatrix_m5x4_X_m4x5_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyMatrix_m5x4_X_m4x5_Expected"));

		DecimalMatrix multiplyMatrix_iMultiplyer3x3_X_m3x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyMatrix_iMultiplyer3x3_X_m3x3_Expected"));

		DecimalMatrix multiplyMatrix_Multiplyer3x3_X_m3x3_Expected = new DecimalMatrix(
				(double[][]) testData.get("multiplyMatrix_Multiplyer3x3_X_m3x3_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m4x5);
		DecimalMatrix mb = new DecimalMatrix(m5x4);

		DecimalMatrix result = ma.multiply(mb);
		areMatricesEqual("multiply4x4", result, result, multiplyMatrix_m4x5_X_m5x4_Expected);

		result = mb.multiply(ma);
		areMatricesEqual("multiply5x5", result, result, multiplyMatrix_m5x4_X_m4x5_Expected);

		ma = new DecimalMatrix(m3x3);

		result = ma.multiply(multiplyMatrix_Multiplyer3x3);
		areMatricesEqual("multiply(array)", result, result, multiplyMatrix_Multiplyer3x3_X_m3x3_Expected);

		result = ma.multiply(multiplyMatrix_iMultiplyer3x3);
		areMatricesEqual("multiply(int array)", result, result, multiplyMatrix_iMultiplyer3x3_X_m3x3_Expected);

		result = ma.multiply(new IntMatrix(multiplyMatrix_iMultiplyer3x3));
		areMatricesEqual("multiply(IntMatrix)", result, result, multiplyMatrix_iMultiplyer3x3_X_m3x3_Expected);
	}

	private static void testDivide() throws Exception {
		double[][] m2x2 = (double[][]) testData.get("m2x2");
		double divideScalar_Divisor = (double) testData.get("divideScalar_Divisor");
		int divideScalar_iDivisor = (int) testData.get("divideScalar_iDivisor");

		DecimalMatrix divideScalar_Divisor_d_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("divideScalar_Divisor_/_m2x2_Expected"));

		DecimalMatrix divideScalar_iDivisor_d_m2x2_Expected = new DecimalMatrix(
				(double[][]) testData.get("divideScalar_iDivisor_/_m2x2_Expected"));

		DecimalMatrix ma = new DecimalMatrix(m2x2);
		DecimalMatrix result = ma.divide(divideScalar_Divisor);
		areMatricesEqual("divide(scalar)", ma, result, divideScalar_Divisor_d_m2x2_Expected);

		result = ma.divide(divideScalar_iDivisor);
		areMatricesEqual("divide(int scalar)", ma, result, divideScalar_iDivisor_d_m2x2_Expected);
	}

	private static void testDeterminantInternal(String testDataKey) throws Exception {
		double[][] arr = (double[][]) testData.get(testDataKey);
		double expected = (double) testData.get("determinant_" + testDataKey + "_Expected");

		DecimalMatrix ma = new DecimalMatrix(arr);
		double determinant = ma.determinant();
		if (!doubleEquals(determinant, expected))
			throw new Exception("determinant (" + testDataKey + "): wrong value");
	}

	private static void testDeterminant() throws Exception {

		testDeterminantInternal("m1x1");
		testDeterminantInternal("m2x2");
		testDeterminantInternal("m3x3");
		testDeterminantInternal("m5x5");

		boolean thrown = false;
		DecimalMatrix ma = new DecimalMatrix((double[][]) testData.get("m4x5"));
		try {
			System.out.println(ma.determinant());
		} catch (ArithmeticException ex) {
			thrown = true;
		}

		if (!thrown)
			throw new Exception("determinant: exception not thrown for non-square matrix");
	}

	private static void testAdjugateInternal(String testDataKey) throws Exception {

		double[][] arr = (double[][]) testData.get(testDataKey);
		DecimalMatrix ma = new DecimalMatrix(arr);

		boolean mustThrow = ma.rowCount() != ma.columnCount();
		if (mustThrow) {
			try {
				System.out.println(ma.adjugate().rowCount());
			} catch (ArithmeticException ex) {
				return;
			}
			throw new Exception("adjugate (" + testDataKey + "): exception not thrown for non-square matrix");
		}

		double[][] identity = (double[][]) testData.get("identity" + testDataKey.substring(1));
		double[][] expected = (double[][]) testData.get("adjugate_" + testDataKey + "_Expected");

		DecimalMatrix result = ma.adjugate();
		areMatricesEqual("adjugate (" + testDataKey + "):", ma, result, new DecimalMatrix(expected));

		DecimalMatrix identityXdeterminant = ma.multiply(result);
		areMatricesEqual("adjugate (" + testDataKey + " - test identity 1)", ma, identityXdeterminant,
				new DecimalMatrix(identity).multiply(ma.determinant()));

		identityXdeterminant = result.multiply(ma);
		areMatricesEqual("adjugate (" + testDataKey + " - test identity 2)", result, identityXdeterminant,
				new DecimalMatrix(identity).multiply(ma.determinant()));
	}

	private static void testAdjugate() throws Exception {
		testAdjugateInternal("m1x1");
		testAdjugateInternal("m2x2");
		testAdjugateInternal("m3x3");
		testAdjugateInternal("m5x4");
		testAdjugateInternal("m5x5");
		testAdjugateInternal("mZeroDet");
	}

	private static void testInvertInternal(String testDataKey) throws Exception {
		double[][] arr = (double[][]) testData.get(testDataKey);
		DecimalMatrix ma = new DecimalMatrix(arr);

		boolean mustThrow = ma.rowCount() != ma.columnCount();
		if (mustThrow) {
			try {
				System.out.println(ma.invert().rowCount());
			} catch (ArithmeticException ex) {
				return;
			}
			throw new Exception("invert (" + testDataKey + "): exception not thrown for non-square matrix");
		}

		mustThrow = doubleEquals(ma.determinant(), 0);
		if (mustThrow) {
			try {
				System.out.println(ma.invert().rowCount());
			} catch (ArithmeticException ex) {
				return;
			}
			throw new Exception("invert (" + testDataKey + "): exception not thrown for matrix with determinant == 0");
		}

		DecimalMatrix identity = new DecimalMatrix((double[][]) testData.get("identity" + testDataKey.substring(1)));

		DecimalMatrix expected = new DecimalMatrix((double[][]) testData.get("invert_" + testDataKey + "_Expected"));

		DecimalMatrix result = ma.invert();
		areMatricesEqual("invert (" + testDataKey + "):", ma, result, expected);

		DecimalMatrix prove = ma.multiply(result);
		areMatricesEqual("invert (" + testDataKey + " - test identity 1)", ma, prove, identity);

		prove = result.multiply(ma);
		areMatricesEqual("invert (" + testDataKey + " - test identity 2)", result, prove, identity);
	}

	private static void testInvert() throws Exception {
		testInvertInternal("m1x1");
		testInvertInternal("m2x2");
		testInvertInternal("m3x3");
		testInvertInternal("m5x4");
		testInvertInternal("m5x5");
	}

	private static void testInvertIntMatrixInternal(String testDataKey) throws Exception {
		int[][] arr = (int[][]) testData.get(testDataKey);
		IntMatrix im = new IntMatrix(arr);

		boolean mustThrow = im.rowCount() != im.columnCount();
		if (mustThrow) {
			try {
				System.out.println(DecimalMatrix.invert(im).rowCount());
			} catch (Exception ex) {
				return;
			}
			throw new Exception("invert (static): exception not thrown for non-square matrix");
		}

		mustThrow = im.determinant() == 0;
		if (mustThrow) {
			try {
				System.out.println(DecimalMatrix.invert(im).rowCount());
			} catch (Exception ex) {
				return;
			}
			throw new Exception("invert (static): exception not thrown for matrix with determinant == 0");
		}

		DecimalMatrix identity = new DecimalMatrix((double[][]) testData.get("identity" + testDataKey.substring(1)));

		DecimalMatrix expected = new DecimalMatrix((double[][]) testData.get("invert_" + testDataKey + "_Expected"));

		DecimalMatrix result = DecimalMatrix.invert(im);
		areMatricesEqual("invert (static)", result, result, expected);

		DecimalMatrix prove = new DecimalMatrix(im).multiply(result);
		areMatricesEqual("invert (static)", prove, prove, identity);

		prove = result.multiply(new DecimalMatrix(im));
		areMatricesEqual("invert (static)", prove, prove, identity);
	}

	private static void testInvertIntMatrix() throws Exception {
		testInvertIntMatrixInternal("mi3x3");
		testInvertIntMatrixInternal("mi4x4");
		testInvertIntMatrixInternal("mi4x5");
		testInvertIntMatrixInternal("mi5x5");
		testInvertIntMatrixInternal("miZeroDet");
	}

	public static int test(final String[] args) {
		try {
			testData = DataReaderUTest.parseTestData("gist/Math/Matrix/DecimalMatrixUTest.txt");
		} catch (Exception ex) {
			System.err.println("DecimalMatrixUTest: exception parsing test data\n" + ex);
			return -1;
		}

		try {
			testConstructor();
			testGetElement();
			testTranspose();
			testHadamardProduct();
			testAddScalar();
			testAdd();
			testMultiplyScalar();
			testMultiply();
			testDivide();
			testDeterminant();
			testAdjugate();
			testInvert();
			testInvertIntMatrix();
			System.out.println("DecimalMatrixUTest: all tests passed");
			return 0;
		} catch (Exception ex) {
			System.err.println("DecimalMatrixUTest: test failed\n" + ex);
			return 1;
		}
	}

	public static void main(final String[] args) {
		test(args);
	}
}