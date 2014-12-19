package org.megadix.jfcm.samples;

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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;

import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;
import org.megadix.jfcm.utils.FcmIO;
import org.megadix.jfcm.utils.FcmRunner;
import org.megadix.jfcm.utils.SimpleFcmRunner;

/**
 * Modified example inspired by:
 * http://www.ochoadeaspuru.com/fuzcogmap/preyandpredator.php
 * by Guillermo Ochoa de Aspuru
 * 
 * Code by Dimitri De Franciscis - www.megadix.it
 *
 */
public class PredatorAndPrey {

    FcmRunner runner;
    CognitiveMap map;

    public PredatorAndPrey() throws ParseException {
        map = FcmIO.loadXml(getClass().getResourceAsStream("PredatorAndPrey.xml")).get(0);
        runner = new SimpleFcmRunner(map, 0.1, 1000);
    }

    public void test_scenario_1() throws Exception {
        resetMap();
        map.getConcepts().get("Food").setOutput(0.0);
        map.getConcepts().get("Prey").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 1", runner.converge());
    }

    public void test_scenario_2() throws Exception {
        resetMap();
        map.getConcepts().get("Food").setOutput(1.0);
        map.getConcepts().get("Prey").setOutput(1.0);
        map.getConcepts().get("Predators").setOutput(1.0);
        showResults("Scenario 2", runner.converge());
    }

    public void run() {
        try {
            System.out.print("Scenario\tConverged");
            for (Concept c : map.getConcepts().values()) {
                System.out.print("\t" + c.getName());
            }
            System.out.println();

            test_scenario_1();
            test_scenario_2();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void resetMap() {
        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept concept = iter.next();
            concept.setOutput(0.0);
            concept.setPrevOutput(null);
            concept.setFixedOutput(false);
        }
    }

    void showResults(String scenario, boolean converged) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
        System.out.print(scenario + "\t" + converged);

        for (Concept c : map.getConcepts().values()) {
            System.out.print("\t");
            System.out.print(c.getOutput() != null ? nf.format(c.getOutput()) : "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        PredatorAndPrey example;
        try {
            example = new PredatorAndPrey();
            example.run();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
