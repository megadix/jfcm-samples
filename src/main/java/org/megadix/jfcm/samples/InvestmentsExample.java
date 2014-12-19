/*
JFCM (Java Fuzzy Congnitive Maps)
Copyright (C) 2009 De Franciscis Dimitri - www.megadix.it

This library is free software; you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 2.1 of the License, or (at your option) any
later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along
with this library; if not, write to the Free Software Foundation, Inc., 59
Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.megadix.jfcm.samples;

import org.megadix.jfcm.*;
import org.megadix.jfcm.act.SignumActivator;
import org.megadix.jfcm.conn.WeightedConnection;

/**
 * Example inspired by
 * "Reti neuronali - dal perceptron alle reti caotiche e neuro-fuzzy"
 * (1997) Etas Libri
 * 
 * @return
 */
public class InvestmentsExample {
	CognitiveMap map;

	public InvestmentsExample() {
		map = new CognitiveMap("Investments");
		ConceptActivator af = new SignumActivator();

		Concept c1 = new Concept("Interest rate", null, af, 0.0, 0.0, false);
		map.addConcept(c1);

		Concept c2 = new Concept("Productive investments", null, af, 0.0, 0.0, false);
		map.addConcept(c2);

		Concept c3 = new Concept("Occupation", null, af, 0.0, 0.0, false);
		map.addConcept(c3);

		Concept c4 = new Concept("Inflation", null, af, 0.0, 0.0, false);
		map.addConcept(c4);

		FcmConnection conn_1 = new WeightedConnection("Interest rate -> Productive investments", null, -0.8);
		map.addConnection(conn_1);
		FcmConnection conn_2 = new WeightedConnection("Productive investments -> Occupation", null, 1.0);
		map.addConnection(conn_2);
		FcmConnection conn_3 = new WeightedConnection("Occupation -> Inflation", null, 0.9);
		map.addConnection(conn_3);
		FcmConnection conn_4 = new WeightedConnection("Inflation -> Interest rate", null, 1.0);
		map.addConnection(conn_4);

		map.connect("Interest rate", "Interest rate -> Productive investments", "Productive investments");
		map.connect("Productive investments", "Productive investments -> Occupation", "Occupation");
		map.connect("Occupation", "Occupation -> Inflation", "Inflation");
		map.connect("Inflation", "Inflation -> Interest rate", "Interest rate");
	
	}

	void resetMap() {
		map.getConcepts().get("Interest rate").setOutput(0.0);
		map.getConcepts().get("Productive investments").setOutput(0.0);
		map.getConcepts().get("Occupation").setOutput(0.0);
		map.getConcepts().get("Inflation").setOutput(0.0);
	}

	public void test_clamp_1() {
		resetMap();
		Concept clamped = map.getConcepts().get("Interest rate");
		clamped.setOutput(1.0);
		clamped.setFixedOutput(true);
	}

	public void test_clamp_2() {
		resetMap();
		Concept clamped = map.getConcepts().get("Productive investments");
		clamped.setOutput(1.0);
		clamped.setFixedOutput(true);
	}

	public void test_clamp_3() {
		resetMap();
		Concept clamped = map.getConcepts().get("Occupation");
		clamped.setOutput(1.0);
		clamped.setFixedOutput(true);
	}

	public void test_clamp_4() {
		resetMap();
		Concept clamped = map.getConcepts().get("Inflation");
		clamped.setOutput(1.0);
		clamped.setFixedOutput(true);
	}

	private void execute(String msg) {
		System.out.println(msg);
		for (int i = 0; i < 5; i++) {
			map.execute();
			System.out.println(map.toString());
		}
	}

	public static void main(String[] args) {
		InvestmentsExample sample = new InvestmentsExample();
		sample.test_clamp_1();
		sample.execute("test_clamp_1");
		sample.test_clamp_2();
		sample.execute("test_clamp_2");
		sample.test_clamp_3();
		sample.execute("test_clamp_3");
		sample.test_clamp_4();
		sample.execute("test_clamp_4");
	}
}
