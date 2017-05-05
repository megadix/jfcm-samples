package org.megadix.jfcm.samples;

import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;

import java.text.NumberFormat;
import java.util.stream.Collectors;

/**
 * Various utility methods used by examples
 */
public class ExampleUtils {

    public static void printMapHeader(CognitiveMap map, String sep) {
        System.out.println(
                map.getConcepts().values().stream()
                        .map(concept -> concept.getName())
                        .collect(Collectors.joining(sep)));
    }

    public static void printMapState(CognitiveMap map) {
        printMapState(map, "\t", null);
    }

    public static void printMapState(final CognitiveMap map, final String sep, final NumberFormat nf) {
        System.out.println(
                map.getConcepts().values().stream()
                        .map(Concept::getOutput)
                        .map(out -> {
                            if (out == null) {
                                return "";
                            }
                            return nf != null ? nf.format(out) : out.toString();
                        })
                        .collect(Collectors.joining(sep)));
    }
}
