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

import static org.megadix.jfcm.samples.ExampleUtils.printMapHeader;
import static org.megadix.jfcm.samples.ExampleUtils.printMapState;

/**
 * FCM applied to software development.
 *
 */
public class SWDevelopment {

    CognitiveMap map;
    FcmRunner runner;
    NumberFormat nf;

    public SWDevelopment() throws Exception {
        nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
        map = FcmIO.loadXml(getClass().getResourceAsStream("SWDevelopment.xml")).get(0);
        runner = new SimpleFcmRunner(map, 0.1, 1000);
    }

    public void run() {
        System.out.print("Scenario\tConverged\t");
        printMapHeader(map, "\t");

        scenario_1();
        scenario_2();
        scenario_3();
        scenario_4();
    }

    /*
     * Low resources, very high communication
     */
    private void scenario_1() {
        resetMap();
        // fixed concepts
        map.getConcept("c1").setFixedOutput(true);
        map.getConcept("c1").setOutput(0.0);
        map.getConcept("c3").setFixedOutput(true);
        map.getConcept("c3").setOutput(1.0);
        map.getConcept("c7").setFixedOutput(true);
        map.getConcept("c7").setOutput(-0.8);

        map.getConcept("c9").setFixedOutput(true);

        showResults("scenario 1", runner.converge());
    }

    /*
     * Very high resources, low communication
     */
    private void scenario_2() {
        resetMap();
        // fixed concepts
        map.getConcept("c1").setFixedOutput(true);
        map.getConcept("c1").setOutput(0.0);
        map.getConcept("c3").setFixedOutput(true);
        map.getConcept("c3").setOutput(-0.8);
        map.getConcept("c7").setFixedOutput(true);
        map.getConcept("c7").setOutput(1.0);

        map.getConcept("c9").setFixedOutput(true);

        showResults("scenario 2", runner.converge());
    }

    /*
     * Experienced team, low resources
     */
    private void scenario_3() {
        resetMap();
        // fixed concepts
        map.getConcept("c9").setFixedOutput(true);
        map.getConcept("c9").setOutput(0.8);
        map.getConcept("c7").setFixedOutput(true);
        map.getConcept("c7").setOutput(-0.5);

        showResults("scenario 3", runner.converge());
    }

    /*
     * Un-experienced team, high resources
     */
    private void scenario_4() {
        resetMap();
        // fixed concepts
        map.getConcept("c9").setFixedOutput(true);
        map.getConcept("c9").setOutput(-0.5);
        map.getConcept("c7").setFixedOutput(true);
        map.getConcept("c7").setOutput(0.8);

        showResults("scenario 4", runner.converge());
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
        System.out.print(scenario + "\t" + converged + "\t");
        printMapState(map, "\t", nf);
    }

    public static void main(String[] args) {
        SWDevelopment swd;
        try {
            swd = new SWDevelopment();
            swd.run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
