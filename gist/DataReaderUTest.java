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

package gist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;

import gist.Math.Fraction;

public class DataReaderUTest {

	public static String findTestData(String path) {
		if (System.getProperty("user.dir").endsWith("_build")) {
			return "../" + path;
		}
		return path;
	}

	/*
	 * private static Integer[] parseIntArray(String line) { List<Integer> ret = new
	 * ArrayList<Integer>(); String[] tokens = line.split(","); for (int i = 0; i <
	 * tokens.length; ++i) { String s = tokens[i].trim(); int n =
	 * Integer.parseInt(s); ret.add(n); } return ret.toArray(new
	 * Integer[ret.size()]); }
	 */

	private static Fraction[] parseFractionArray(String line) {
		List<Fraction> ret = new ArrayList<Fraction>();
		String[] tokens = line.split(",");
		for (int i = 0; i < tokens.length; ++i) {
			Fraction f = Fraction.parseFraction(tokens[i]);
			ret.add(f);
		}
		return ret.toArray(new Fraction[ret.size()]);
	}

	public static Map<String, Object> parseTestData(String file) {

		Map<String, String[]> data = readAll(findTestData(file));
		Map<String, Object> testData = new Hashtable<String, Object>();

		for (String s : data.keySet()) {

			String k = s.trim();
			String[] parts = k.split("\\s");

			Object val = null;
			String key = parts[0].trim();
			String t = parts[1].trim();

			String[] lines = data.get(s);

			boolean isScalar = t.indexOf("[", 0) < 0;
			boolean isMatrix = t.endsWith("[][]");
			boolean isArray = isScalar ? false : isMatrix ? false : t.endsWith("[]");

			List<Fraction[]> tempMatrix = new ArrayList<Fraction[]>();

			for (String line : lines) {
				if (line == null)
					break;
				if (isScalar) {
					val = Fraction.parseFraction(line);
					break;
				}
				if (isArray || isMatrix) {
					Fraction[] arr = parseFractionArray(line);
					if (isArray) {
						val = arr;
						break;
					}

					tempMatrix.add(arr);
				}
			}

			if (isMatrix) {
				int i = 0;
				Fraction[][] m = new Fraction[tempMatrix.size()][];
				for (Fraction[] arr : tempMatrix) {
					m[i] = new Fraction[arr.length];
					for (int j = 0; j < arr.length; ++j) {
						m[i][j] = arr[j];
					}
					++i;
				}
				tempMatrix = new ArrayList<Fraction[]>();
				val = m;
			}

			switch (t) {
				case "int":
					val = (int) ((Fraction) val).getNumerator();
					break;
				case "long":
					((Fraction) val).getNumerator();
					break;
				case "double":
					val = ((Fraction) val).calc();
					break;

				case "int[]": {
					Fraction[] arr = (Fraction[]) val;
					int[] newArr = new int[arr.length];
					for (int i = 0; i < arr.length; ++i) {
						newArr[i] = (int) (arr[i].getNumerator());
					}
					val = newArr;
				}
					break;
				case "long[]": {
					Fraction[] arr = (Fraction[]) val;
					long[] newArr = new long[arr.length];
					for (int i = 0; i < arr.length; ++i) {
						newArr[i] = (arr[i].getNumerator());
					}
					val = newArr;
				}
					break;
				case "double[]": {
					Fraction[] arr = (Fraction[]) val;
					double[] newArr = new double[arr.length];
					for (int i = 0; i < arr.length; ++i) {
						newArr[i] = arr[i].calc();
					}
					val = newArr;
				}
					break;

				case "int[][]": {
					Fraction[][] m = (Fraction[][]) val;
					int[][] newM = new int[m.length][];
					for (int i = 0; i < m.length; ++i) {
						newM[i] = new int[m[i].length];
						for (int j = 0; j < m[i].length; ++j) {
							newM[i][j] = (int) m[i][j].getNumerator();
						}
					}
					val = newM;
				}
					break;
				case "long[][]": {
					Fraction[][] m = (Fraction[][]) val;
					long[][] newM = new long[m.length][];
					for (int i = 0; i < m.length; ++i) {
						newM[i] = new long[m[i].length];
						for (int j = 0; j < m[i].length; ++j) {
							newM[i][j] = m[i][j].getNumerator();
						}
					}
					val = newM;
				}
					break;
				case "double[][]": {
					Fraction[][] m = (Fraction[][]) val;
					double[][] newM = new double[m.length][];
					for (int i = 0; i < m.length; ++i) {
						newM[i] = new double[m[i].length];
						for (int j = 0; j < m[i].length; ++j) {
							newM[i][j] = m[i][j].calc();
						}
					}
					val = newM;
				}
			}

			testData.put(key, val);
		}

		return testData;
	}

	private static Map<String, String[]> readAll(String path) {

		Map<String, String[]> ret = new Hashtable<String, String[]>();

		try {
			File file = new File(path);
			List<String> lines = new ArrayList<String>();
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

			String s, section = null;
			while ((s = in.readLine()) != null) {
				String line = s.trim();
				if (line.length() == 0)
					continue;
				if (line.startsWith("#")) {
					// end of last section
					if (section != null) {
						ret.put(section, lines.toArray(new String[lines.size()]));
					}

					section = line.substring(1);
					lines = new ArrayList<String>();
				} else {
					lines.add(line);
				}
			}

			// end of last section
			if (section != null) {
				ret.put(section, lines.toArray(new String[ret.size()]));
			}

			in.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return ret;
	}
}
