package com.shpp.p2p.cs.adavydenko.assignment11;

/*
 * File: Assignment11Part1.java
 * ----------------------------
 * Main class that starts the program and uses other classes to calculate and display
 * user formula or to display errors if any. To launch the program with no errors user
 * shall provide formula as the first command line argument and variables values as
 * subsequent arguments.
 */
public class Assignment11Part1 {

    public static void main(String[] args) {
        checkAndCalculateFormula(args);
    }

    /**
     * Method saves user input, checks formula for being valid, parses it,
     * calculates result and displays result to console.
     */
    private static void checkAndCalculateFormula(String[] args) {
        try {
            Constants constants = new Constants(args); // Saves user formula and deletes all white spaces
            Checker checker = new Checker(constants); // Class used by other classes to check symbols
            saveVariablesValues(args); // Saves user variables to  hashmap
            Parser parser = new Parser(constants, checker); // Parses formula
            parser.displayParsedFormula(); // Displays parsed formula
            parser.displayTokens();
            Calculator calc = new Calculator(parser, constants, checker); // Calculates parsed formula
            calc.displayResult(); // Displays calculation result to console
        } catch (Exception evt) {
            System.out.println("Your formula has errors. Please try again" + "\n" + evt);
        }
    }

    /**
     * Iterates through each variable expression provided by user as an argument
     * to define variable`s name and variable`s value.
     *
     * @param args are all variable expressions provided by user as command line arguments.
     */
    private static void saveVariablesValues(String[] args) {
        for (int i = 1; i < args.length; i++) {
            defineVariableAndValue(args[i]);
        }
    }

    /**
     * Takes particular variable expression, deletes all white spaces,
     * finds index of the equals sign, defines variable`s name and
     * variable`s value and saves them to the hashmap. Also displays
     * to console the variables the user typed in.
     *
     * @param variableExpression is particular variable expression
     *                           provided by user as command line argument.
     */
    private static void defineVariableAndValue(String variableExpression) {
        String shortVariableExpression = variableExpression.replaceAll(" ", "");
        int equalsSignIndex = shortVariableExpression.indexOf(Constants.EQUALS_SIGN);

        String variableName = defineVariableName(equalsSignIndex, shortVariableExpression);
        String variableValue = defineVariableValue(equalsSignIndex, shortVariableExpression);
        Constants.VARIABLES.put(variableName, variableValue);

        displayVariable(variableName, variableValue);
    }

    /**
     * Displays variable`s name and value to console to show user input.
     *
     * @param variableName  is the name of the variable the user typed in.
     * @param variableValue is the value of the variable the user typed in.
     */
    private static void displayVariable(String variableName, String variableValue) {
        System.out.println("Variable \"" + variableName + "\" is equal " + variableValue);
    }

    /**
     * Defines variables name as a text submitted by user in the command line argument
     * before the equals sign.
     *
     * @param equalsSignIndex         is the index of the equals sign in the command line argument
     *                                standing for variable
     * @param shortVariableExpression is the command line argument standing for variable
     *                                with all white spaces deleted
     * @return the name of the variable as a string
     */
    private static String defineVariableName(int equalsSignIndex, String shortVariableExpression) {
        String variableName = "";

        for (int i = 0; i < equalsSignIndex; i++) {
            String currentCharacter = shortVariableExpression.substring(i, i + 1);

            // Do not include minus sign to the variable name in the hashmap if it is negative value
            if (currentCharacter.equals("-") && i == 0) {
                continue;
            }
            variableName += currentCharacter;
        }
        return variableName;
    }

    /**
     * Defines variables value as a text submitted by user in the command line argument
     * after the equals sign.
     *
     * @param equalsSignIndex         is the index of the equals sign in the command line argument
     *                                standing for variable
     * @param shortVariableExpression is the command line argument standing for variable
     *                                with all white spaces deleted
     * @return the value of the variable as a string
     */
    private static String defineVariableValue(int equalsSignIndex, String shortVariableExpression) {
        String variableValue = "";

        for (int i = equalsSignIndex + 1; i < shortVariableExpression.length(); i++) {
            String currentCharacter = shortVariableExpression.substring(i, i + 1);
            variableValue += currentCharacter;
        }
        return variableValue;
    }
}
