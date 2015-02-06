package edu.unm.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ID3 {

	public static void main(String[] args) {

		char pm;
		double m = 0;
		double p = 0;

		double a = 0;
		double g = 0;
		double c = 0;
		double t = 0;

		BufferedReader br = null;
		String sCurrentLine;
		try {

			br = new BufferedReader(new FileReader("resources/training.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				pm = sCurrentLine.charAt(sCurrentLine.length() - 1);
				if (pm == '+')
					p++;
				else if (pm == '-')
					m++;
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

		System.out.println("+ -> " + p);
		System.out.println("- -> " + m);
		double sEntropyMain = 0;

		sEntropyMain = calculateEntropy(p, m);

		System.out.println("sEntropyMain : " + sEntropyMain);

		double[] gain = new double[57];

		m = 0;
		p = 0;
		for (int i = 0; i < 57; i++) {
			try {
				br = new BufferedReader(new FileReader("resources/training.txt"));
				Record rca = new Record();
				Record rcg = new Record();
				Record rcc = new Record();
				Record rct = new Record();
				a = 0;
				g = 0;
				c = 0;
				t = 0;
				while ((sCurrentLine = br.readLine()) != null) {
					pm = sCurrentLine.charAt(sCurrentLine.length() - 1);
					if (sCurrentLine.charAt(i) == 'a') {
						a++;
						rca.tm.put(sCurrentLine.charAt(i), pm);
						if (pm == '+')
							rca.pmp++;
						else if (pm == '-')
							rca.pmm++;
					}
					if (sCurrentLine.charAt(i) == 'g') {
						g++;
						rcg.tm.put(sCurrentLine.charAt(i), pm);
						if (pm == '+')
							rcg.pmp++;
						else if (pm == '-')
							rcg.pmm++;
					}
					if (sCurrentLine.charAt(i) == 'c') {
						c++;
						rcc.tm.put(sCurrentLine.charAt(i), pm);
						if (pm == '+')
							rcc.pmp++;
						else if (pm == '-')
							rcc.pmm++;
					}
					if (sCurrentLine.charAt(i) == 't') {
						t++;
						rct.tm.put(sCurrentLine.charAt(i), pm);
						if (pm == '+')
							rct.pmp++;
						else if (pm == '-')
							rct.pmm++;
					}
				} // while
				double totalrows = a + g + c + t;
				gain[i] = sEntropyMain - (a / totalrows) * calculateEntropy(rca.pmp, rca.pmm) - (g / totalrows) * calculateEntropy(rcg.pmp, rcg.pmm) - (c / totalrows)
						* calculateEntropy(rcc.pmp, rcc.pmm) - (t / totalrows) * calculateEntropy(rct.pmp, rct.pmm);
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
		} // for
		double largest = gain[0];
		int largestIndex = 0;
		
		for (int i = 0; i < gain.length; i++) {
			System.out.println("gain[" + i + "] -> " + gain[i]);
			if (gain[i] > largest) {
				largestIndex = i;
				largest = gain[i];
			}
		}
		System.out.println("Gain (S, " + largestIndex + ") = " + largest + " is highest");
	}

	private static double calculateEntropy(double p, double m) {
		if (p == 0)
			p = 1;
		if (m == 0)
			m = 1;
		return 0 - (m / (m + p)) * log2(m / (m + p)) - (p / (m + p)) * log2(p / (m + p));
	}

	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

}
