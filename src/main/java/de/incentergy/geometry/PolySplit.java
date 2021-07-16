package de.incentergy.geometry;

import de.incentergy.geometry.impl.RandomPolygonSplitter;
import de.incentergy.geometry.impl.GreedyPolygonSplitter;
import de.incentergy.geometry.utils.GeometryFactoryUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PolySplit {

    private static final String url = "https://github.com/chris-james-git/polysplit";
    private static final String authors = "Gediminas Rimša and Chris James";

    private static final String OPTIONS = "\nOptions:";

    private static final String RANDOM_NAME = RandomPolygonSplitter.class.getSimpleName();
    private static final String GREEDY_NAME = "Khetarpal Splitter";

    private static final String MAIN_OPTION_1_RANDOM = "1. " + RANDOM_NAME;
    private static final String MAIN_OPTION_2_GREEDY = "2. " + GREEDY_NAME;
    private static final String MAIN_OPTION_WAIT = "Enter choice: [ 1 or 2 ]";

    private static final String RANDOM_INFO = "Split polygons into roughly equal parts.";
    private static final String RANDOM_OPTION_1_STANDARD = "1. Standard (any number of vertices / parts)";
    private static final String RANDOM_OPTION_2_DEMO = "2. 5-part split demo (4 vertex polygon split into 5 parts)";
    private static final String RANDOM_OPTION_WAIT = "Enter choice: [ 1 or 2 ]";

    private static final String GREEDY_INFO = "Split polygons into exactly equal parts using the shortest cuts.";
    private static final String GREEDY_OPTION_1_STANDARD = "1. Standard (any number of vertices / parts)";
    private static final String GREEDY_OPTION_2_DEMO = "2. 5-part split demo (4 vertex polygon split into 5 parts)";
    private static final String GREEDY_OPTION_WAIT = "Enter choice: [ 1 or 2 ]";

    public static void main(String args[]) {
        println("\nPolySplit");
        println("Authors: " + authors);
        println(url);
        List<Integer> opts = List.of(1, 2);
        Integer option = null;
        do {
            nonNullIntegerIsInvalid(option);
            option = getMainInput();
        } while (!opts.contains(option));

        switch (option) {
            case 1:
                runRandom();
                break;
            case 2:
                runGreedy();
        }
    }

    private static void runrRandomStandard() {
        println(RANDOM_OPTION_1_STANDARD);
        String wkt = getStandardInputGeometry();
        Integer parts = null;
        int min = 2;
        do {
            nonNullIntegerIsInvalid(parts);
            parts = getStandardInputParts(min);
        } while (parts < min);

        PolygonSplitter splitter = new RandomPolygonSplitter();
        try {
            println(splitter.split(wkt, parts));
        } catch (ParseException e) {
            println("ParseException: " + e.getMessage());
            runrRandomStandard();
        }
    }

    private static int getStandardInputParts(int min) {
        Scanner sc = new Scanner(System.in);
        try {
            println("Enter the number of parts (min " + min + "):");
            return sc.nextInt();
        } catch (InputMismatchException e) {
            handleInputException();
            return getStandardInputParts(min);
        }
    }

    private static String getStandardInputGeometry() {
        Scanner sc = new Scanner(System.in);
        try {
            println("Enter a polygon in WKT format:");
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            handleInputException();
            return getStandardInputGeometry();
        }
    }

    private static void runRandomDemo() {
        println(RANDOM_OPTION_2_DEMO);
        Coordinate[] coords = new Coordinate[4];
        for (int i = 0; i < 4; i++) {
            coords[i] = new Coordinate();
            coords[i].x = getPointFor(i + " X");
            coords[i].y = getPointFor(i + " Y");
        }
        Polygon polygon = GeometryFactoryUtils.createPolygon(coords);
        PolygonSplitter splitter = new RandomPolygonSplitter();
        try {
            println(splitter.split(polygon.toString(), 5));
        } catch (ParseException e) {
            println("ParseException: " + e.getMessage());
            runRandomDemo();
        }
    }

    private static double getPointFor(String vertexName) {
        Scanner sc = new Scanner(System.in);
        println("Enter double value for vertex " + vertexName + ":");
        try {
            return sc.nextDouble();
        } catch (NoSuchElementException e) {
            handleInputException();
            return getPointFor(vertexName);
        }
    }

    private static void runGreedyStandard() {
        println(GREEDY_OPTION_1_STANDARD);
        String wkt = getStandardInputGeometry();
        Integer parts = null;
        int min = 2;
        do {
            nonNullIntegerIsInvalid(parts);
            parts = getStandardInputParts(min);
        } while (parts < min);

        PolygonSplitter splitter = new GreedyPolygonSplitter();
        try {
            println(splitter.split(wkt, parts));
        } catch (ParseException e) {
            println("ParseException: " + e.getMessage());
            runGreedyStandard();
        }
    }

    private static void runGreedyDemo() {
        println(GREEDY_OPTION_2_DEMO);
        Coordinate[] coords = new Coordinate[4];
        for (int i = 0; i < 4; i++) {
            coords[i] = new Coordinate();
            coords[i].x = getPointFor(i + " X");
            coords[i].y = getPointFor(i + " Y");
        }
        Polygon polygon = GeometryFactoryUtils.createPolygon(coords);
        PolygonSplitter splitter = new GreedyPolygonSplitter();
        try {
            println(splitter.split(polygon.toString(), 5));
        } catch (ParseException e) {
            println("ParseException: " + e.getMessage());
            runGreedyDemo();
        }
    }

    private static void runRandom() {
        println(RANDOM_NAME);
        println(RANDOM_INFO);
        List<Integer> opts = List.of(1, 2);
        Integer option = null;
        do {
            nonNullIntegerIsInvalid(option);
            option = getRandomInput();
        } while (!opts.contains(option));

        switch (option) {
            case 1:
                runrRandomStandard();
                break;
            case 2:
                runRandomDemo();
        }
    }

    private static void nonNullIntegerIsInvalid(Integer option) {
        if (option != null) {
            println("Invalid option");
            pause();
        }
    }

    private static void runGreedy() {
        println(GREEDY_NAME);
        println(GREEDY_INFO);
        List<Integer> opts = List.of(1, 2);
        Integer option = null;
        do {
            nonNullIntegerIsInvalid(option);
            option = getGreedyInput();
        } while (!opts.contains(option));

        switch (option) {
            case 1:
                runGreedyStandard();
                break;
            case 2:
                runGreedyDemo();
        }
    }

    private static int getGreedyInput() {
        Scanner sc = new Scanner(System.in);
        try {
            println(OPTIONS);
            println(GREEDY_OPTION_1_STANDARD);
            println(GREEDY_OPTION_2_DEMO);
            println(GREEDY_OPTION_WAIT);
            return sc.nextInt();
        } catch (InputMismatchException e) {
            handleInputException();
            return getGreedyInput();
        }
    }

    private static int getRandomInput() {
        Scanner sc = new Scanner(System.in);
        try {
            println(OPTIONS);
            println(RANDOM_OPTION_1_STANDARD);
            println(RANDOM_OPTION_2_DEMO);
            println(RANDOM_OPTION_WAIT);
            return sc.nextInt();
        } catch (InputMismatchException e) {
            handleInputException();
            return getRandomInput();
        }
    }

    private static int getMainInput() {
        Scanner sc = new Scanner(System.in);
        try {
            println(OPTIONS);
            println(MAIN_OPTION_1_RANDOM);
            println(MAIN_OPTION_2_GREEDY);
            println(MAIN_OPTION_WAIT);
            return sc.nextInt();
        } catch (InputMismatchException e) {
            handleInputException();
            return getMainInput();
        }
    }

    private static void handleInputException() {
        println("Invalid input");
        pause();
    }

    private static void pause() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void println(String msg) {
        System.out.println(msg);
    }
}
