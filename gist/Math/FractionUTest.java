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

package gist.Math;

import java.util.ArrayList;
import java.util.List;

public class FractionUTest {

	private static void testFractionCctor() throws Exception {
		Fraction frac;

		boolean didntThrow = false;
		try {
			frac = new Fraction(1, 0);
			didntThrow = true;
		} catch (Exception ex) {
		}
		if (didntThrow)
			throw new Exception("constructor: exception not thrown (division by 0)");

		frac = new Fraction(0, 1);
		if (frac.getNumerator() != 0)
			throw new Exception("contructor: wrong numerator, expected 0");
		if (frac.getDenominator() != 1)
			throw new Exception("contructor: wrong denominator, expected 1");

		frac = new Fraction(2, 1);
		if (frac.getNumerator() != 2)
			throw new Exception("contructor: wrong numerator, expected 2");
		if (frac.getDenominator() != 1)
			throw new Exception("contructor: wrong denominator, expected 1");

		frac = new Fraction(22, -15);
		if (frac.getNumerator() != -22)
			throw new Exception("contructor: wrong numerator, expected -22");
		if (frac.getDenominator() != 15)
			throw new Exception("contructor: wrong denominator, expected 15");

		frac = new Fraction(-17, 13);
		if (frac.getNumerator() != -17)
			throw new Exception("contructor: wrong numerator, expected -17");
		if (frac.getDenominator() != 13)
			throw new Exception("contructor: wrong denominator, expected 13");

		frac = new Fraction(-23, -53);
		if (frac.getNumerator() != 23)
			throw new Exception("contructor: wrong numerator, expected 23");
		if (frac.getDenominator() != 53)
			throw new Exception("contructor: wrong denominator, expected 53");

		frac = new Fraction(15, 5);
		if (frac.getNumerator() != 3)
			throw new Exception("contructor: wrong numerator, expected 3");
		if (frac.getDenominator() != 1)
			throw new Exception("contructor: wrong denominator, expected 1");

		frac = new Fraction(16, 7);
		if (frac.getNumerator() != 16)
			throw new Exception("contructor: wrong numerator, expected 16");
		if (frac.getDenominator() != 7)
			throw new Exception("contructor: wrong denominator, expected 7");

		Fraction frac2 = new Fraction(frac);
		if (frac == frac2)
			throw new Exception("copy contructor: same reference");
		if (frac2.getNumerator() != frac.getNumerator())
			throw new Exception("copy contructor: wrong numerator");
		if (frac2.getDenominator() != frac.getDenominator())
			throw new Exception("copy contructor: wrong denominator");
	}

	private static void testFractionToString() throws Exception {
		String s;
		Fraction frac;

		frac = new Fraction(2, 3);
		s = frac.toString();
		if (!s.equals("2/3"))
			throw new Exception("toString: expected 2/3");

		frac = new Fraction(-3, 4);
		s = frac.toString();
		if (!s.equals("-3/4"))
			throw new Exception("toString: expected -3/4");

		frac = new Fraction(5, -7);
		s = frac.toString();
		if (!s.equals("-5/7"))
			throw new Exception("toString: expected -5/7");

		frac = new Fraction(-5, -7);
		s = frac.toString();
		if (!s.equals("5/7"))
			throw new Exception("toString: expected 5/7");
	}

	private static void testFractionGetCopy() throws Exception {
		Fraction frac, frac2;

		frac = new Fraction(1, 2);
		frac2 = frac.getCopy();
		if (frac == frac2)
			throw new Exception("getCopy: same reference");
		if (frac2.getNumerator() != frac.getNumerator())
			throw new Exception("getCopy: wrong numerator");
		if (frac2.getDenominator() != frac.getDenominator())
			throw new Exception("getCopy: wrong denominator");
	}

	private static void testFractionCalc() throws Exception {
		Fraction frac;

		frac = new Fraction(2, 1);
		if (frac.calc() != 2.0)
			throw new Exception("calc: wrong result (expected 2.0)");

		frac = new Fraction(1, 2);
		if (frac.calc() != 0.5)
			throw new Exception("calc: wrong result (expected 0.5)");
	}

	private static void testFractionGetPair() throws Exception {
		Fraction frac;

		frac = new Fraction(3, 2);
		long[] pair = frac.getPair();
		if (pair.length != 2)
			throw new Exception("getPair: wrong length");
		if (pair[0] != 3)
			throw new Exception("getPair: wrong numerator");
		if (pair[1] != 2)
			throw new Exception("getPair: wrong denominator");

		if (pair[0] != frac.getNumerator())
			throw new Exception("getNumerator: wrong value");
		if (pair[1] != frac.getDenominator())
			throw new Exception("getDenominator: wrong value");
	}

	private static void testFractionAdd() throws Exception {
		Fraction frac, result;

		frac = new Fraction(1, 2);
		result = frac.add(0);
		if (result == frac)
			throw new Exception("add(int): returned same reference");
		if (frac.getNumerator() != 1)
			throw new Exception("add(int): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("add(int): changed input denominator");
		if (result.getNumerator() != 1)
			throw new Exception("add(int): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("add(int): wrong result denominator, expected 2");

		result = frac.add(2);
		if (result.getNumerator() != 5)
			throw new Exception("add(int): wrong result numerator, expected 5");
		if (result.getDenominator() != 2)
			throw new Exception("add(int): wrong result denominator, expected 2 (*)");

		result = frac.add(-1);
		if (result.getNumerator() != -1)
			throw new Exception("add(int): wrong result numerator, expected -1");
		if (result.getDenominator() != 2)
			throw new Exception("add(int): wrong result denominator, expected 2 (**)");

		frac = new Fraction(2, 1);
		result = frac.add(1);
		if (result.getNumerator() != 3)
			throw new Exception("add(int): wrong result numerator, expected 3");
		if (result.getDenominator() != 1)
			throw new Exception("add(int): wrong result denominator, expected 1");

		// now add fractions
		frac = new Fraction(1, 2);
		result = frac.add(new Fraction(0, 1));
		if (result == frac)
			throw new Exception("add(Fraction): returned same reference");
		if (frac.getNumerator() != 1)
			throw new Exception("add(Fraction): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("add(Fraction): changed input denominator");
		if (result.getNumerator() != 1)
			throw new Exception("add(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("add(Fraction): wrong result denominator, expected 2");

		result = frac.add(new Fraction(2, 1));
		if (result.getNumerator() != 5)
			throw new Exception("add(Fraction): wrong result numerator, expected 5");
		if (result.getDenominator() != 2)
			throw new Exception("add(Fraction): wrong result denominator, expected 2 (*)");

		result = frac.add(new Fraction(1, -1));
		if (result.getNumerator() != -1)
			throw new Exception("add(Fraction): wrong result numerator, expected -1");
		if (result.getDenominator() != 2)
			throw new Exception("add(Fraction): wrong result denominator, expected 2 (**)");

		result = frac.add(new Fraction(2, 3));
		if (result.getNumerator() != 7)
			throw new Exception("add(Fraction): wrong result numerator, expected 7");
		if (result.getDenominator() != 6)
			throw new Exception("add(Fraction): wrong result denominator, expected 6");

		frac = new Fraction(7, 6);
		result = frac.add(new Fraction(-5, 6));
		if (result.getNumerator() != 1)
			throw new Exception("add(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 3)
			throw new Exception("add(Fraction): wrong result denominator, expected 3");

		frac = new Fraction(1, 6);
		result = frac.add(new Fraction(1, 3));
		if (result.getNumerator() != 1)
			throw new Exception("add(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("add(Fraction): wrong result denominator, expected 2");
	}

	private static void testFractionSubtract() throws Exception {
		Fraction frac, result;

		frac = new Fraction(1, 2);
		result = frac.subtract(0);
		if (result == frac)
			throw new Exception("subtract(int): returned same reference");
		if (frac.getNumerator() != 1)
			throw new Exception("subtract(int): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("subtract(int): changed input denominator");
		if (result.getNumerator() != 1)
			throw new Exception("subtract(int): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(int): wrong result denominator, expected 2");

		result = frac.subtract(-2);
		if (result.getNumerator() != 5)
			throw new Exception("subtract(int): wrong result numerator, expected 5");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(int): wrong result denominator, expected 2 (*)");

		result = frac.subtract(1);
		if (result.getNumerator() != -1)
			throw new Exception("subtract(int): wrong result numerator, expected -1");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(int): wrong result denominator, expected 2 (**)");

		frac = new Fraction(2, 1);
		result = frac.subtract(-1);
		if (result.getNumerator() != 3)
			throw new Exception("subtract(int): wrong result numerator, expected 3");
		if (result.getDenominator() != 1)
			throw new Exception("subtract(int): wrong result denominator, expected 1");

		// now subtract fractions
		frac = new Fraction(1, 2);
		result = frac.subtract(new Fraction(0, 1));
		if (result == frac)
			throw new Exception("subtract(Fraction): returned same reference");
		if (frac.getNumerator() != 1)
			throw new Exception("subtract(Fraction): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("subtract(Fraction): changed input denominator");
		if (result.getNumerator() != 1)
			throw new Exception("subtract(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 2");

		result = frac.subtract(new Fraction(-2, 1));
		if (result.getNumerator() != 5)
			throw new Exception("subtract(Fraction): wrong result numerator, expected 5");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 2 (*)");

		result = frac.subtract(new Fraction(-1, -1));
		if (result.getNumerator() != -1)
			throw new Exception("subtract(Fraction): wrong result numerator, expected -1");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 2 (**)");

		result = frac.subtract(new Fraction(2, 3));
		if (result.getNumerator() != -1)
			throw new Exception("subtract(Fraction): wrong result numerator, expected -1");
		if (result.getDenominator() != 6)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 6");

		frac = new Fraction(7, 6);
		result = frac.subtract(new Fraction(5, 6));
		if (result.getNumerator() != 1)
			throw new Exception("subtract(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 3)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 3");

		frac = new Fraction(1, 6);
		result = frac.subtract(new Fraction(-1, 3));
		if (result.getNumerator() != 1)
			throw new Exception("subtract(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("subtract(Fraction): wrong result denominator, expected 2");
	}

	private static void testFractionMultiply() throws Exception {
		Fraction frac, result;

		frac = new Fraction(3, 2);
		result = frac.multiply(1);
		if (result == frac)
			throw new Exception("multiply(int): returned same reference");
		if (frac.getNumerator() != 3)
			throw new Exception("multiply(int): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("multiply(int): changed input denominator");
		if (result.getNumerator() != 3)
			throw new Exception("multiply(int): wrong result numerator, expected 3");
		if (result.getDenominator() != 2)
			throw new Exception("multiply(int): wrong result denominator, expected 2");

		frac = new Fraction(3, 2);
		result = frac.multiply(0);
		if (result.getNumerator() != 0)
			throw new Exception("multiply(int): wrong result numerator, expected 0");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(int): wrong result denominator, expected 1");

		frac = new Fraction(3, 2);
		result = frac.multiply(2);
		if (result.getNumerator() != 3)
			throw new Exception("multiply(int): wrong result numerator, expected 3");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(int): wrong result denominator, expected 1");

		frac = new Fraction(3, 2);
		result = frac.multiply(-2);
		if (result.getNumerator() != -3)
			throw new Exception("multiply(int): wrong result numerator, expected -3");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(int): wrong result denominator, expected 1");

		frac = new Fraction(3, 2);
		result = frac.multiply(4);
		if (result.getNumerator() != 6)
			throw new Exception("multiply(int): wrong result numerator, expected 6");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(int): wrong result denominator, expected 1");

		frac = new Fraction(3, 2);
		result = frac.multiply(3);
		if (result.getNumerator() != 9)
			throw new Exception("multiply(int): wrong result numerator, expected 9");
		if (result.getDenominator() != 2)
			throw new Exception("multiply(int): wrong result denominator, expected 2");

		// now test fraction inputs
		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(1, 1));
		if (result == frac)
			throw new Exception("multiply(Fraction): returned same reference");
		if (frac.getNumerator() != 3)
			throw new Exception("multiply(Fraction): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("multiply(Fraction): changed input denominator");
		if (result.getNumerator() != 3)
			throw new Exception("multiply(Fraction): wrong result numerator, expected 3");
		if (result.getDenominator() != 2)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 2");

		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(0, 1));
		if (result.getNumerator() != 0)
			throw new Exception("multiply(Fraction): wrong result numerator, expected 0");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 1");

		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(2, 3));
		if (result.getNumerator() != 1)
			throw new Exception("multiply(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 1 (*)");

		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(-2, 3));
		if (result.getNumerator() != -1)
			throw new Exception("multiply(Fraction): wrong result numerator, expected -1");
		if (result.getDenominator() != 1)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 1 (**)");

		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(1, 3));
		if (result.getNumerator() != 1)
			throw new Exception("multiply(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 2 (**)");

		frac = new Fraction(3, 2);
		result = frac.multiply(new Fraction(2, 5));
		if (result.getNumerator() != 3)
			throw new Exception("multiply(Fraction): wrong result numerator, expected 3");
		if (result.getDenominator() != 5)
			throw new Exception("multiply(Fraction): wrong result denominator, expected 5");
	}

	private static void testFractionDivide() throws Exception {
		Fraction frac, result;

		frac = new Fraction(3, 2);
		result = frac.divide(1);
		if (result == frac)
			throw new Exception("divide(int): returned same reference");
		if (frac.getNumerator() != 3)
			throw new Exception("divide(int): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("divide(int): changed input denominator");
		if (result.getNumerator() != 3)
			throw new Exception("divide(int): wrong result numerator, expected 3");
		if (result.getDenominator() != 2)
			throw new Exception("divide(int): wrong result denominator, expected 2");

		boolean didntThrow = false;
		frac = new Fraction(3, 2);
		try {
			result = frac.divide(0);
			didntThrow = true;
		} catch (Exception ex) {
		}
		if (didntThrow)
			throw new Exception("divide(int): exception not thrown (division by 0)");

		frac = new Fraction(3, 2);
		result = frac.divide(3);
		if (result.getNumerator() != 1)
			throw new Exception("divide(int): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("divide(int): wrong result denominator, expected 2");

		frac = new Fraction(3, 2);
		result = frac.divide(-2);
		if (result.getNumerator() != -3)
			throw new Exception("divide(int): wrong result numerator, expected -3");
		if (result.getDenominator() != 4)
			throw new Exception("divide(int): wrong result denominator, expected 4");

		frac = new Fraction(3, 2);
		result = frac.divide(-3);
		if (result.getNumerator() != -1)
			throw new Exception("divide(int): wrong result numerator, expected -1");
		if (result.getDenominator() != 2)
			throw new Exception("divide(int): wrong result denominator, expected 2");

		frac = new Fraction(3, 2);
		result = frac.divide(6);
		if (result.getNumerator() != 1)
			throw new Exception("divide(int): wrong result numerator, expected 1");
		if (result.getDenominator() != 4)
			throw new Exception("divide(int): wrong result denominator, expected 4");

		// now test fraction inputs
		frac = new Fraction(3, 2);
		result = frac.divide(new Fraction(1, 1));
		if (result == frac)
			throw new Exception("divide(Fraction): returned same reference");
		if (frac.getNumerator() != 3)
			throw new Exception("divide(Fraction): changed input numerator");
		if (frac.getDenominator() != 2)
			throw new Exception("divide(Fraction): changed input denominator");
		if (result.getNumerator() != 3)
			throw new Exception("divide(Fraction): wrong result numerator, expected 3");
		if (result.getDenominator() != 2)
			throw new Exception("divide(Fraction): wrong result denominator, expected 2");

		didntThrow = false;
		frac = new Fraction(3, 2);
		try {
			result = frac.divide(new Fraction(0, 1));
			didntThrow = true;
		} catch (Exception ex) {
		}
		if (didntThrow)
			throw new Exception("divide(Fraction): exception not thrown (division by 0)");

		frac = new Fraction(3, 2);
		result = frac.divide(new Fraction(3, 2));
		if (result.getNumerator() != 1)
			throw new Exception("divide(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 1)
			throw new Exception("divide(Fraction): wrong result denominator, expected 1 (*)");

		frac = new Fraction(3, 2);
		result = frac.divide(new Fraction(-3, 2));
		if (result.getNumerator() != -1)
			throw new Exception("divide(Fraction): wrong result numerator, expected -1");
		if (result.getDenominator() != 1)
			throw new Exception("divide(Fraction): wrong result denominator, expected 1 (**)");

		frac = new Fraction(3, 2);
		result = frac.divide(new Fraction(3, 1));
		if (result.getNumerator() != 1)
			throw new Exception("divide(Fraction): wrong result numerator, expected 1");
		if (result.getDenominator() != 2)
			throw new Exception("divide(Fraction): wrong result denominator, expected 2 (**)");

		frac = new Fraction(3, 2);
		result = frac.divide(new Fraction(5, 2));
		if (result.getNumerator() != 3)
			throw new Exception("divide(Fraction): wrong result numerator, expected 3");
		if (result.getDenominator() != 5)
			throw new Exception("divide(Fraction): wrong result denominator, expected 5");
	}

	private static void testFractionInvert() throws Exception {
		Fraction frac, result;

		frac = new Fraction(3, 5);
		result = frac.invert();
		if (result == frac)
			throw new Exception("invert: returned same reference");
		if (frac.getNumerator() != 3)
			throw new Exception("invert: changed input numerator");
		if (frac.getDenominator() != 5)
			throw new Exception("invert: changed input denominator");
		if (result.getNumerator() != 5)
			throw new Exception("invert: wrong result numerator, expected 5");
		if (result.getDenominator() != 3)
			throw new Exception("invert: wrong result denominator, expected 3");
	}

	private static void testFractionSimplification() throws Exception {
		Fraction frac = new Fraction(3, 5);

		frac.unsimplify(6);
		if (frac.getNumerator() != 3)
			throw new Exception("unsimplify: achieved an impossible convertion (numerator)");
		if (frac.getDenominator() != 5)
			throw new Exception("unsimplify: achieved an impossible convertion (denominator)");

		frac.unsimplify(10);
		if (frac.getNumerator() != 6)
			throw new Exception("unsimplify: wrong numerator value, expected 6");
		if (frac.getDenominator() != 10)
			throw new Exception("unsimplify: wrong denominator value, expected 10");

		frac.simplify();
		if (frac.getNumerator() != 3)
			throw new Exception("simplify: wrong numerator value, expected 3");
		if (frac.getDenominator() != 5)
			throw new Exception("simplify: wrong denominator value, expected 5");
	}

	private static void testFractionArrays() throws Exception {
		Fraction result;
		Fraction[] items;

		items = new Fraction[] { new Fraction(1, 3), new Fraction(2, 3), new Fraction(3, 5), new Fraction(7, 4),
				new Fraction(3, 2) };

		result = Fraction.add(items);
		if (result.getNumerator() != 97)
			throw new Exception("Fraction.add(static): wrong numerator value, expected 97");
		if (result.getDenominator() != 20)
			throw new Exception("Fraction.add(static): wrong denominator value, expected 20");

		result = Fraction.multiply(items);
		if (result.getNumerator() != 7)
			throw new Exception("Fraction.multiply(static): wrong numerator value, expected 7");
		if (result.getDenominator() != 20)
			throw new Exception("Fraction.multiply(static): wrong denominator value, expected 20");

		Fraction.normalize(items);
		if (items[0].getDenominator() != 60)
			throw new Exception("Fraction.normalize: wrong denominator (i=0)");
		if (items[1].getDenominator() != 60)
			throw new Exception("Fraction.normalize: wrong denominator (i=1)");
		if (items[2].getDenominator() != 60)
			throw new Exception("Fraction.normalize: wrong denominator (i=2)");
		if (items[3].getDenominator() != 60)
			throw new Exception("Fraction.normalize: wrong denominator (i=3)");
		if (items[4].getDenominator() != 60)
			throw new Exception("Fraction.normalize: wrong denominator (i=4)");

		if (items[0].getNumerator() != 20)
			throw new Exception("Fraction.normalize: wrong numerator (i=0)");
		if (items[1].getNumerator() != 40)
			throw new Exception("Fraction.normalize: wrong numerator (i=1)");
		if (items[2].getNumerator() != 36)
			throw new Exception("Fraction.normalize: wrong numerator (i=2)");
		if (items[3].getNumerator() != 105)
			throw new Exception("Fraction.normalize: wrong numerator (i=3)");
		if (items[4].getNumerator() != 90)
			throw new Exception("Fraction.normalize: wrong numerator (i=4)");
	}

	private static void testFractionMaxCommonDivisor() throws Exception {
		if (Fraction.maxCommonDivisor(1, 2) != 1)
			throw new Exception("Fraction.maxCommonDivisor (1)");
		if (Fraction.maxCommonDivisor(2, 2) != 2)
			throw new Exception("Fraction.maxCommonDivisor (2)");
		if (Fraction.maxCommonDivisor(2, 4) != 2)
			throw new Exception("Fraction.maxCommonDivisor (3)");
		if (Fraction.maxCommonDivisor(4, 2) != 2)
			throw new Exception("Fraction.maxCommonDivisor (4)");
		if (Fraction.maxCommonDivisor(3, 2) != 1)
			throw new Exception("Fraction.maxCommonDivisor (5)");
		if (Fraction.maxCommonDivisor(2, 3) != 1)
			throw new Exception("Fraction.maxCommonDivisor (6)");
		if (Fraction.maxCommonDivisor(12, 6) != 6)
			throw new Exception("Fraction.maxCommonDivisor (7)");
		if (Fraction.maxCommonDivisor(6, 12) != 6)
			throw new Exception("Fraction.maxCommonDivisor (8)");
		if (Fraction.maxCommonDivisor(23, 11) != 1)
			throw new Exception("Fraction.maxCommonDivisor (9)");
		if (Fraction.maxCommonDivisor(49, 21) != 7)
			throw new Exception("Fraction.maxCommonDivisor (10)");
	}

	private static void testFractionMinCommonMultiple() throws Exception {
		if (Fraction.minCommonMultiple(1, 2) != 2)
			throw new Exception("Fraction.minCommonMultiple (1)");
		if (Fraction.minCommonMultiple(2, 2) != 2)
			throw new Exception("Fraction.minCommonMultiple (2)");
		if (Fraction.minCommonMultiple(2, 4) != 4)
			throw new Exception("Fraction.minCommonMultiple (3)");
		if (Fraction.minCommonMultiple(4, 2) != 4)
			throw new Exception("Fraction.minCommonMultiple (4)");
		if (Fraction.minCommonMultiple(3, 2) != 6)
			throw new Exception("Fraction.minCommonMultiple (5)");
		if (Fraction.minCommonMultiple(2, 3) != 6)
			throw new Exception("Fraction.minCommonMultiple (6)");
		if (Fraction.minCommonMultiple(12, 6) != 12)
			throw new Exception("Fraction.minCommonMultiple (7)");
		if (Fraction.minCommonMultiple(6, 12) != 12)
			throw new Exception("Fraction.minCommonMultiple (8)");
		if (Fraction.minCommonMultiple(23, 11) != 253)
			throw new Exception("Fraction.minCommonMultiple (9)");
		if (Fraction.minCommonMultiple(49, 21) != 147)
			throw new Exception("Fraction.minCommonMultiple (10)");
		if (Fraction.minCommonMultiple(4, 10) != 20)
			throw new Exception("Fraction.minCommonMultiple (11)");
	}

	private static void testFractionEquals() throws Exception {
		Fraction f1, f2;

		f1 = new Fraction(1, 1);
		f2 = new Fraction(1, 1);
		if (!f1.equals(f2))
			throw new Exception("equals: returned false for 1 == 1");

		f1 = new Fraction(1, 2);
		f2 = new Fraction(1, 2);
		if (!f1.equals(f2))
			throw new Exception("equals: returned false for 1/2 == 1/2");

		f1 = new Fraction(2, 1);
		f2 = new Fraction(1, 2);
		if (f1.equals(f2))
			throw new Exception("equals: returned true for 2 != 1/2");
		if (!f1.equals(f1))
			throw new Exception("equals: returned false for 2 == 2");

		f1 = new Fraction(1, 2);
		f2 = new Fraction(1, 2);
		f2.unsimplify(4);
		if (!f1.equals(f2))
			throw new Exception("equals: returned false for 1/2 == 1/2");
	}

	private static void testFractionSort() throws Exception {
		List<Fraction> set = new ArrayList<Fraction>();
		set.add(new Fraction(1, 1));
		set.add(new Fraction(4, 2));
		set.add(new Fraction(2, 3));
		set.add(new Fraction(1, 2));
		set.add(new Fraction(3, 2));
		set.sort(null);

		if (!set.get(0).equals(new Fraction(1, 2)))
			throw new Exception("sort: wrong value @0");

		if (!set.get(1).equals(new Fraction(2, 3)))
			throw new Exception("sort: wrong value @1");

		if (!set.get(2).equals(new Fraction(1, 1)))
			throw new Exception("sort: wrong value @2");

		if (!set.get(3).equals(new Fraction(3, 2)))
			throw new Exception("sort: wrong value @3");

		if (!set.get(4).equals(new Fraction(2, 1)))
			throw new Exception("sort: wrong value @4");
	}

	private static void testParseFraction() throws Exception {
		Fraction f = Fraction.parseFraction("2");
		if (f.getNumerator() != 2)
			throw new Exception("parseFraction: wrong numerator for string '2'");
		if (f.getDenominator() != 1)
			throw new Exception("parseFraction: wrong denominator for string '2'");

		f = Fraction.parseFraction("-3");
		if (f.getNumerator() != -3)
			throw new Exception("parseFraction: wrong numerator for string '-3'");
		if (f.getDenominator() != 1)
			throw new Exception("parseFraction: wrong denominator for string '-3'");

		f = Fraction.parseFraction("5/3");
		if (f.getNumerator() != 5)
			throw new Exception("parseFraction: wrong numerator for string '5/3'");
		if (f.getDenominator() != 3)
			throw new Exception("parseFraction: wrong denominator for string '5/3'");

		f = Fraction.parseFraction("5/-3");
		if (f.getNumerator() != -5)
			throw new Exception("parseFraction: wrong numerator for string '5/-3'");
		if (f.getDenominator() != 3)
			throw new Exception("parseFraction: wrong denominator for string '5/-3'");

		f = Fraction.parseFraction("0/9");
		if (f.getNumerator() != 0)
			throw new Exception("parseFraction: wrong numerator for string '0/9'");
		if (f.getDenominator() != 1)
			throw new Exception("parseFraction: wrong denominator for string '0/9'");

		f = Fraction.parseFraction("-1/2");
		if (f.getNumerator() != -1)
			throw new Exception("parseFraction: wrong numerator for string '-1/2'");
		if (f.getDenominator() != 2)
			throw new Exception("parseFraction: wrong denominator for string '-1/2'");
	}

	public static int test(final String[] args) {
		try {
			testFractionCctor();
			testFractionToString();
			testFractionGetCopy();
			testFractionCalc();
			testFractionGetPair();
			testFractionAdd();
			testFractionSubtract();
			testFractionMultiply();
			testFractionDivide();
			testFractionInvert();
			testFractionSimplification();
			testFractionArrays();
			testFractionMaxCommonDivisor();
			testFractionMinCommonMultiple();
			testFractionEquals();
			testFractionSort();
			testParseFraction();
			System.out.println("FractionUTest: all tests passed");
			return 0;
		} catch (Exception ex) {
			System.err.println("FractionUTest: test failed\n" + ex);
			return 1;
		}
	}

	public static void main(final String[] args) {
		test(args);
	}
}