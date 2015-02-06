package edu.unm.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ID3 {

	public static void main(String[] args) {

		char pm;
		double m = 0;
		double p = 0;

		double a = 0;
		double g = 0;
		double c = 0;
		double t = 0;

		Set<String> exampleSet = new HashSet<String>();

		BufferedReader br = null;
		String example;
		int i = 0;
		try {
			br = new BufferedReader(new FileReader("resources/training.txt"));

			while ((example = br.readLine()) != null) {
				exampleSet.add(example);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		int x = 57;
		double sEntropyMain = 0;
		
		for (int j = 0; j < exampleSet.size(); j++) {
			for (String exmain : exampleSet) {
				pm = exmain.charAt(exmain.length() - 1);
				if (pm == '+')
					p++;
				else if (pm == '-')
					m++;
			} // inner for loop 1

			System.out.println("+ -> " + p);
			System.out.println("- -> " + m);			

			sEntropyMain = calculateEntropy(sEntropyMain, p, m);

			System.out.println("sEntropyMain : " + sEntropyMain);

			ArrayList<Double> gain = new ArrayList<Double>();

			m = 0;
			p = 0;

			boolean loopSuccess = false;
			for (int k = 0; k < x; k++) {
				Record rca = new Record();
				Record rcg = new Record();
				Record rcc = new Record();
				Record rct = new Record();
				a = 0;
				g = 0;
				c = 0;
				t = 0;
				for (String ex : exampleSet) {
					pm = ex.charAt(ex.length() - 1);
					if (ex.charAt(k) == 'a') {
						a++;
						rca.tm.put(ex.charAt(k), pm);
						if (pm == '+')
							rca.pmp++;
						else if (pm == '-')
							rca.pmm++;
					}
					if (ex.charAt(k) == 'g') {
						g++;
						rcg.tm.put(ex.charAt(k), pm);
						if (pm == '+')
							rcg.pmp++;
						else if (pm == '-')
							rcg.pmm++;
					}
					if (ex.charAt(k) == 'c') {
						c++;
						rcc.tm.put(ex.charAt(k), pm);
						if (pm == '+')
							rcc.pmp++;
						else if (pm == '-')
							rcc.pmm++;
					}
					if (ex.charAt(k) == 't') {
						t++;
						rct.tm.put(ex.charAt(k), pm);
						if (pm == '+')
							rct.pmp++;
						else if (pm == '-')
							rct.pmm++;
					}
				} // while
				double totalrows = a + g + c + t;
				gain.add(sEntropyMain - (a / totalrows)
						* calculateEntropy(0, rca.pmp, rca.pmm) - (g / totalrows)
						* calculateEntropy(0, rcg.pmp, rcg.pmm) - (c / totalrows)
						* calculateEntropy(0, rcc.pmp, rcc.pmm) - (t / totalrows)
						* calculateEntropy(0, rct.pmp, rct.pmm));
				loopSuccess = true;
			} // inner for loop 2

			if (loopSuccess == true) {
				double max = Collections.max(gain);
				int maxIndex = gain.indexOf(max);

				System.out.println("Gain (S, " + maxIndex + ") = " + max
						+ " is highest");

				Set<String> interimSet = new HashSet<String>();

				for (String ex : exampleSet) {
					String trimStr = ex.substring(0, maxIndex)
							+ ex.substring(maxIndex + 1);
					interimSet.add(trimStr);
				} // inner for loop 3
				exampleSet = interimSet;
			} // if
			x--;
		} // outer for loop
	} // main

	private static double calculateEntropy(double sEntropyMain, double p, double m) {
		if (p == 0)
			p = 1;
		if (m == 0)
			m = 1;
		return sEntropyMain - (m / (m + p)) * log2(m / (m + p)) - (p / (m + p))
				* log2(p / (m + p));
	}

	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

}
