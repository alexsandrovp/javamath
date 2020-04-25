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

public class IntMatrixUTest {

	private static Map<String, Object> testData = new Hashtable<String, Object>();

	private static void areMatricesEqual(String testName, IntMatrix reference,
		IntMatrix transformed, IntMatrix expected) throws Exception {

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
				if (transformed.get(i, j) != expected.get(i, j))
					throw new Exception(testName + ": element mismatch @(" + i + "," + j + ")");
			}
		}
	}

	private static void testConstructor(int[][] m) throws Exception {
		IntMatrix ma = new IntMatrix(m);
		if (ma.rowCount() != m.length)
			throw new Exception("constructor(array): wrong row count");
		if (ma.columnCount() != m[0].length)
			throw new Exception("constructor(array): wrong column count");
		for (int i = 0; i < ma.rowCount(); ++i) {
			for (int j = 0; j < ma.columnCount(); ++j) {
				if (ma.get(i, j) != m[i][j])
					throw new Exception("constructor(array): element mismatch @(" + i + "," + j + ")");
			}
		}

		IntMatrix mb = new IntMatrix(ma);
		if (mb.rowCount() != ma.rowCount())
			throw new Exception("constructor(copy): wrong row count");
		if (mb.columnCount() != ma.columnCount())
			throw new Exception("constructor(copy): wrong column count");
		for (int i = 0; i < mb.rowCount(); ++i) {
			for (int j = 0; j < mb.columnCount(); ++j) {
				if (mb.get(i, j) != ma.get(i, j))
					throw new Exception("constructor(copy): element mismatch @(" + i + "," + j + ")");
			}
		}
	}
	
	private static void testConstructor() throws Exception {
		int[][] m4x5 = (int[][])testData.get("m4x5");
		testConstructor(m4x5);
	}

	private static void testGetElement() throws Exception {
		IntMatrix ma;
		int[][] m4x5 = (int[][])testData.get("m4x5");

		ma = new IntMatrix(m4x5);
		if (ma.get(1, 3) != 9)
			throw new Exception("get: wrong element @(1, 3), expected 9");

		if (ma.get(3, 1) != 17)
			throw new Exception("get: wrong element @(3, 1), expected 17");
	}

	private static void testTranspose() throws Exception {
		int[][] m5x4 = (int[][])testData.get("m5x4");
		int[][] transpose_m5x4_Expected = (int[][])testData.get("transpose_m5x4_Expected");
		IntMatrix ma = new IntMatrix(m5x4).transpose();
		areMatricesEqual("transpose", ma, ma, new IntMatrix(transpose_m5x4_Expected));
	}

	private static void testHadamardProduct() throws Exception {
		int[][] m2x3 = (int[][])testData.get("m2x3");
		int[][] hadamard_Multiplier = (int[][])testData.get("hadamard_Multiplier");
		IntMatrix hadamard_Multiplier_X_m2x3_Expected = new IntMatrix(
			(int[][])testData.get("hadamard_Multiplier_X_m2x3_Expected"));
		
		IntMatrix ma = new IntMatrix(m2x3);
		IntMatrix result = ma.hadamard_product(hadamard_Multiplier);
		areMatricesEqual("hadamard_product(array)", ma, result, hadamard_Multiplier_X_m2x3_Expected);

		result = ma.hadamard_product(new IntMatrix(hadamard_Multiplier));
		areMatricesEqual("hadamard_product(matrix)", ma, result, hadamard_Multiplier_X_m2x3_Expected);
	}

	private static void testAddScalar() throws Exception {
		int[][] m2x2 = (int[][])testData.get("m2x2");
		int addScalar_Addend = (int)testData.get("addScalar_Addend");
		IntMatrix addScalar_Addend_p_m2x2_Expected = new IntMatrix(
			(int[][])testData.get("addScalar_Addend_+_m2x2_Expected"));

		IntMatrix ma = new IntMatrix(m2x2);
		IntMatrix result = ma.add(addScalar_Addend);
		areMatricesEqual("add(scalar int)", ma, result, addScalar_Addend_p_m2x2_Expected);
	}

	private static void testAdd() throws Exception {
		int[][] m2x2 = (int[][])testData.get("m2x2");
		int[][] addMatrix_Addend = (int[][])testData.get("addMatrix_Addend");
		IntMatrix addMatrix_Addend_p_m2x2_Expected = new IntMatrix(
			(int[][])testData.get("addMatrix_Addend_+_m2x2_Expected"));

		IntMatrix ma = new IntMatrix(m2x2);
		IntMatrix result = ma.add(addMatrix_Addend);
		areMatricesEqual("add(array)", ma, result, addMatrix_Addend_p_m2x2_Expected);

		result = ma.add(new IntMatrix(addMatrix_Addend));
		areMatricesEqual("add(matrix)", ma, result, addMatrix_Addend_p_m2x2_Expected);
	}

	private static void testMultiplyScalar() throws Exception {
		int[][] m2x2 = (int[][])testData.get("m2x2");
		int multiplyScalar_Multiplier = (int) testData.get("multiplyScalar_Multiplier");
		IntMatrix expected = new IntMatrix(
			(int[][])testData.get("multiplyScalar_Multiplier_X_m2x2_Expected"));

		IntMatrix ma = new IntMatrix(m2x2);
		IntMatrix result = ma.multiply(multiplyScalar_Multiplier);
		areMatricesEqual("multiply(scalar)", ma, result, expected);
	}

	private static void testMultiply() throws Exception {
		int[][] m4x5 = (int[][])testData.get("m4x5");
		int[][] m5x4 = (int[][])testData.get("m5x4");

		IntMatrix multiplyMatrix_m4x5_X_m5x4_Expected = new IntMatrix(
			(int[][])testData.get("multiplyMatrix_m4x5_X_m5x4_Expected"));
		IntMatrix multiplyMatrix_m5x4_X_m4x5_Expected = new IntMatrix(
			(int[][])testData.get("multiplyMatrix_m5x4_X_m4x5_Expected"));

		IntMatrix ma = new IntMatrix(m4x5);
		IntMatrix mb = new IntMatrix(m5x4);
		
		IntMatrix result = ma.multiply(mb);
		areMatricesEqual("multiply(4x4)", result, result, multiplyMatrix_m4x5_X_m5x4_Expected);

		result = mb.multiply(ma);
		areMatricesEqual("multiply(5x5)", result, result, multiplyMatrix_m5x4_X_m4x5_Expected);
	}

	private static void testDeterminantInternal(String testDataKey) throws Exception {
		int[][] arr = (int[][])testData.get(testDataKey);
		int expected = (int)testData.get("determinant_" + testDataKey + "_Expected");
		
		IntMatrix ma = new IntMatrix(arr);
		int determinant = ma.determinant();
		if (determinant != expected)
			throw new Exception("determinant (" + testDataKey + "): wrong value");
	}

	private static void testDeterminant() throws Exception {
		testDeterminantInternal("m1x1");
		testDeterminantInternal("m2x2");
		testDeterminantInternal("m3x3");
		testDeterminantInternal("m5x5");

		boolean didntThrow = false;
		int[][] notsquare = (int[][])testData.get("m4x5");
		IntMatrix ma = new IntMatrix(notsquare);
		try {
			System.out.println(ma.determinant());
			didntThrow = true;
		} catch (Exception ex) {}

		if (didntThrow)
			throw new Exception("determinant: exception not thrown for non-square matrix");
	}

	private static void testAdjugateInternal(String testDataKey) throws Exception {

		int[][] arr = (int[][])testData.get(testDataKey);
		int[][] identity = (int[][]) testData.get("identity" + testDataKey.substring(1));
		int[][] expected = (int[][])testData.get("adjugate_" + testDataKey + "_Expected");

		IntMatrix ma = new IntMatrix(arr);
		IntMatrix result = ma.adjugate();
		
		areMatricesEqual("adjugate (" + testDataKey + "):", ma, result, new IntMatrix(expected));

		IntMatrix identityXdeterminant = ma.multiply(result);
		areMatricesEqual("adjugate (" + testDataKey + " - test identity 1)", ma, identityXdeterminant,
			new IntMatrix(identity).multiply(ma.determinant()));

		identityXdeterminant = result.multiply(ma);
		areMatricesEqual("adjugate (" + testDataKey + " - test identity 2)", result, identityXdeterminant,
			new IntMatrix(identity).multiply(ma.determinant()));
	}

	private static void testAdjugate() throws Exception {
		testAdjugateInternal("m1x1");
		testAdjugateInternal("m2x2");
		testAdjugateInternal("m3x3");
		testAdjugateInternal("m5x5");

		boolean didntThrow = false;
		int[][] m5x4 = (int[][])testData.get("m5x4");
		try {
			System.out.println(new IntMatrix(m5x4).adjugate().rowCount());
			didntThrow = true;
		}
		catch (Exception ex) { }
		if (didntThrow)
			throw new Exception("adjugate: exception not throw for non-square matrix");
	}

	public static int test(final String[] args) {

		try {
			testData = DataReaderUTest.parseTestData("gist/Math/Matrix/IntMatrixUTest.txt");
		}
		catch (Exception ex) {
			System.err.println("IntMatrixUTest: exception parsing test data\n" + ex);
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
			testDeterminant();
			testAdjugate();
			System.out.println("IntMatrixUTest: all tests passed");
			return 0;
		} catch (Exception ex) {
			System.err.println("IntMatrixUTest: test failed\n" + ex);
			return 1;
		}
	}

	public static void main(final String[] args) {
		test(args);
	}
}