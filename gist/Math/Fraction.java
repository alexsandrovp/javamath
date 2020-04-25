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

public class Fraction implements Comparable<Fraction> {

	private long numerator;
	private long denominator;

	public Fraction(long numerator, long denominator) {
		if (denominator == 0)
			throw new ArithmeticException("division by 0");

		this.numerator = numerator;
		this.denominator = denominator;
		simplify();
	}

	public Fraction(Fraction f) {
		long[] fv = f.getPair();
		numerator = fv[0];
		denominator = fv[1];
		simplify();
	}

	@Override
	public String toString() {
		long n = numerator;
		long d = denominator;
		if (d < 0) {
			n *= -1;
			d *= -1;
		}
		return n + "/" + d;
	}

	public static Fraction parseFraction(String s) {
		String fs = s.trim();
		int pos = fs.indexOf("/");
		if (pos < 0) {
			return new Fraction(Long.parseLong(fs), 1);
		}
		long n = Long.parseLong(fs.substring(0, pos));
		long d = Long.parseLong(fs.substring(pos + 1));
		return new Fraction(n, d);
	}

	public Fraction getCopy() {
		return new Fraction(numerator, denominator);
	}

	public double calc() {
		return 1.0 * numerator / denominator;
	}

	public long[] getPair() {
		return new long[] { numerator, denominator };
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	public Fraction add(long i) {
		return new Fraction(numerator + i * denominator, denominator);
	}

	public Fraction add(Fraction f) {
		long[] fv = f.getPair();
		if (denominator == fv[1])
			return new Fraction(numerator + fv[0], denominator);

		//// faster, but overflows too easily
		// return new Fraction(numerator * fv[1] + fv[0] * denominator, denominator * fv[1]);

		// slower, but overflows less often
		long mcm = minCommonMultiple(denominator, fv[1]);
		return new Fraction(mcm / denominator * numerator + mcm / fv[1] * fv[0], mcm);
	}

	public Fraction subtract(long i) {
		return new Fraction(numerator - i * denominator, denominator);
	}

	public Fraction subtract(Fraction f) {
		long[] fv = f.getPair();

		//// faster, but overflows too easily
		// return new Fraction(numerator * fv[1] - fv[0] * denominator, denominator * fv[1]);

		// slower, but overflows less often
		long mcm = minCommonMultiple(denominator, fv[1]);
		return new Fraction(mcm / denominator * numerator - mcm / fv[1] * fv[0], mcm);
	}

	public Fraction multiply(long i) {
		// return new Fraction(numerator * i, denominator);

		long mcd = maxCommonDivisor(i, denominator);
		return new Fraction(i / mcd * numerator, denominator / mcd);
	}

	public Fraction multiply(Fraction f) {
		long[] fv = f.getPair();
		// return new Fraction(numerator * fv[0], denominator * fv[1]);

		long mcd = maxCommonDivisor(numerator, fv[1]);
		long n = numerator / mcd;
		fv[1] /= mcd;

		mcd = maxCommonDivisor(fv[0], denominator);
		fv[0] /= mcd;
		long d = denominator / mcd;

		return new Fraction(n * fv[0], d * fv[1]);
	}

	public Fraction divide(long i) {
		// return new Fraction(numerator, denominator * i);
		long mcd = maxCommonDivisor(numerator, i);
		return new Fraction(numerator / mcd, i / mcd * denominator);
	}

	public Fraction divide(Fraction f) {
		long[] fv = f.getPair();
		// return new Fraction(numerator * fv[1], denominator * fv[0]);

		long mcd = maxCommonDivisor(numerator, fv[0]);
		long n = numerator / mcd;
		fv[0] /= mcd;

		mcd = maxCommonDivisor(fv[1], denominator);
		fv[1] /= mcd;
		long d = denominator / mcd;

		return new Fraction(n * fv[1], d * fv[0]);
	}

	public Fraction invert() {
		return new Fraction(denominator, numerator);
	}

	public void unsimplify(long toDenominator) {
		if (toDenominator > denominator && toDenominator % denominator == 0) {
			numerator *= toDenominator / denominator;
			denominator = toDenominator;
		}
	}

	public void simplify() {
		long mcd = maxCommonDivisor(numerator, denominator);
		numerator /= mcd;
		denominator /= mcd;

		if (denominator < 0) {
			numerator *= -1;
			denominator *= -1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.getClass() == Fraction.class) {
			Fraction c = (Fraction) o;
			Long[] pair1 = simplify(numerator, denominator);
			Long[] pair2 = simplify(c.numerator, c.denominator);
			return Long.compare(pair1[0], pair2[0]) == 0 && Long.compare(pair1[1], pair2[1]) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		Long[] pair = simplify(numerator, denominator);
		return (13 * pair[0].hashCode() + 17 * pair[1].hashCode()) / 221;
	}

	@Override
	public int compareTo(Fraction f) {
		if (f == null)
			throw new NullPointerException();
		if (f == this)
			return 0;

		Long[] pair1 = simplify(numerator, denominator);
		Long[] pair2 = simplify(f.numerator, f.denominator);

		long mcm = pair1[1];
		mcm = minCommonMultiple(mcm, pair2[1]);

		pair1[0] *= mcm / pair1[1];
		pair2[0] *= mcm / pair2[1];

		return Long.compare(pair1[0], pair2[0]);
	}

	private static Long[] simplify(long i, long j) {
		long mcd = maxCommonDivisor(i, j);
		i /= mcd;
		j /= mcd;

		if (j < 0) {
			i *= -1;
			j *= -1;
		}
		return new Long[] { i, j };
	}

	public static Fraction add(Fraction[] terms) {
		Fraction ret = new Fraction(0, 1);
		for (int i = 0; i < terms.length; ++i)
			ret = ret.add(terms[i]);
		return ret;
	}

	public static Fraction multiply(Fraction[] factors) {
		Fraction ret = new Fraction(1, 1);
		for (int i = 0; i < factors.length; ++i)
			ret = ret.multiply(factors[i]);
		return ret;
	}

	public static long normalize(Fraction[] fractions) {
		if (fractions == null)
			return 0;
		if (fractions.length == 0)
			return 0;

		fractions[0].simplify();
		long mcm = fractions[0].getDenominator();
		if (fractions.length == 1)
			return mcm;

		for (int i = 1; i < fractions.length; ++i) {
			fractions[i].simplify();
			mcm = minCommonMultiple(mcm, fractions[i].getDenominator());
		}

		for (int i = 0; i < fractions.length; ++i)
			fractions[i].unsimplify(mcm);

		return mcm;
	}

	public static long maxCommonDivisor(long a, long b) {
		return b == 0 ? a : maxCommonDivisor(b, a % b);
	}

	public static long minCommonMultiple(long a, long b) {
		if (a == b)
			return a;
		if (a < b && b % a == 0)
			return b;
		else if (a > b && a % b == 0)
			return a;
		long mcd = maxCommonDivisor(a, b);
		return a / mcd * b;
	}
}
