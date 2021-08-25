package com.shpp.p2p.cs.adavydenko.assignment11;

import java.util.HashMap;

/*
 * File: Constants.java
 * --------------------
 * Auxiliary class containing all constants used
 * by other classes.
 */
public class Constants {

    /**
     * The first command line argument provided by user
     * (deemed to be a formula).
     */
    protected String FORMULA;

    /**
     * The first command line argument provided by user
     * with all white spaces deleted.
     */
    protected String SHORT_FORMULA;

    /**
     * Equals sign (=).
     */
    protected static String EQUALS_SIGN = "=";

    /**
     * Minus sign (-).
     */
    protected String MINUS = "-";

    /**
     * All functions that user can use in a formula.
     */
    protected String[] FUNCTIONS = {"sin", "-sin", "cos", "-cos", "tan", "-tan",
            "atan", "-atan", "log10", "-log10", "log2", "-log2", "sqrt", "-sqrt"};

    /**
     * Operators that user can use in a formula.
     */
    protected String[] OPERATIONS = {"^", "*", "/", "+", "-"};

    /**
     * Brackets that user can use in a formula to indicate the priority of particular operations.
     */
    protected String[] BRACKETS = {"(", ")"};

    /**
     * A hashmap with all variables and their values submitted by user in terminal.
     */
    protected static HashMap<String, String> VARIABLES = new HashMap<>();

    /**
     * Priority values to indicate which operation has higher priority.
     */
    protected int PRIORITY_VALUE_5 = 5;
    protected int PRIORITY_VALUE_4 = 4;
    protected int PRIORITY_VALUE_3 = 3;
    protected int PRIORITY_VALUE_2 = 2;
    protected int PRIORITY_VALUE_1 = 1;

    /**
     * Gets user formula and deletes all whitespaces from it.
     *
     * @param args command line arguments.
     */
    public Constants(String[] args) {
        FORMULA = args[0]; // Formula provided by user as first command line argument
        SHORT_FORMULA = FORMULA.replaceAll(" ", "");
    }
}
