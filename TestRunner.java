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

import java.util.Date;

import gist.Math.FractionUTest;
import gist.Math.Matrix.DecimalMatrixUTest;
import gist.Math.Matrix.FractionMatrixUTest;
import gist.Math.Matrix.IntMatrixUTest;

public class TestRunner {

	public static void main(final String[] args) {
		int failedTestCount = 0;

		Date start = new Date();
		System.out.println("");
		System.out.println("Tests started @ " + start);

		failedTestCount += FractionUTest.test(args);
		failedTestCount += IntMatrixUTest.test(args);
		failedTestCount += DecimalMatrixUTest.test(args);
		failedTestCount += FractionMatrixUTest.test(args);

		Date end = new Date();
		System.out.println("Tests finished within " + (end.getTime() - start.getTime()) + " milliseconds");
		System.out.println(failedTestCount + " tests failed");
		System.out.println("");
		System.exit(failedTestCount);
	}
}