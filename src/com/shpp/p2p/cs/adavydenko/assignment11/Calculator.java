package com.shpp.p2p.cs.adavydenko.assignment11;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: Calculator.java
 * ---------------------
 * Takes tokens array with parsed formula elements,
 * converts it to the postfix polish notation array
 * and calculates the result of the equation.
 */
public class Calculator {

    /**
     * An array with recognized tokens from the formula string.
     */
    ArrayList<String> tokens;

    /**
     * An array with operands and operators in written in the order
     * in which they can be calculated.
     */
    ArrayList<String> output = new ArrayList<>();

    /**
     * An array with operators (math operation signs, formulas and
     * opening brackets) which are stored here temporarily to
     * add them later to the output array in the right order.
     */
    ArrayList<String> stack = new ArrayList<>();

    /**
     * A copy of the ready made output array where the program
     * makes calculations and leaves only one value in the end - the result.
     */
    ArrayList<String> resultArray = new ArrayList<>();

    /**
     * An object providing access to constants common for all classes.
     */
    Constants constants;

    /**
     * An object checking characters for being operators, formulas or brackets.
     */
    Checker checker;

    /**
     * A hashmap storing information on the priority of math operations -
     * math operators like plus or minus, math formulas like cos or sin
     * and brackets.
     */
    private HashMap<String[], Integer> OPERATION_PRIORITY = new HashMap<>();

    /**
     * Gets external objects to load tokens array, to get constants and to check
     * characters. Also fills HashMap with operators priority values and creates
     * postfix polish notation.
     *
     * @param parser    is an object parsing formulas.
     * @param constants is an object providing access to constants common
     *                  for all classes.
     * @param checker   is an object checking characters for being operators,
     *                  formulas or brackets.
     */
    public Calculator(Parser parser, Constants constants, Checker checker) {
        this.tokens = parser.tokens;
        this.constants = constants;
        this.checker = checker;
        fillPriorityOperationMap(); // Creates HashMap OPERATION_PRIORITY
        createReversePolishNotation(); // Converts parsed user formula to postfix polish notation array
    }

    /**
     * Creates postfix polish notation from the tokens array.
     * All values (doubles or variables) are written to the output
     * array.
     */
    protected void createReversePolishNotation() {
        for (int i = 0; i < tokens.size(); i++) {
            StringBuffer token = new StringBuffer(tokens.get(i));
            if (!checker.isBrackets(token.toString()) && !checker.isMathSymbol(token.toString())
                    && !checker.isFunction(token.toString())) { // The current token is a double or a variable
                output.add(token.toString());

            } else {
                processOperators(token); // The current token is brackets, an operator or a function
            }
        }
        addRemainingOperators();
    }

    /**
     * Deals with token if it is a bracket, an operator or a function.
     * Operators will be put either to the stack or to the output array
     * depending on circumstances.
     *
     * @param token is a recognized group of characters taken from the formula string.
     */
    private void processOperators(StringBuffer token) {
        String lastStackElement = "";

        // Saves last stack element value if any
        if (stack.size() > 0) {
            lastStackElement = stack.get(stack.size() - 1);
        }
                /* Adds opening bracket, operator or function to the stack if the stack is empty or
                if it is opening bracket, or if its last element`s priority is less than the token`s one */
        if (stack.size() == 0 || checker.isOpenBracket(token.toString())
                || getPriority(lastStackElement) < getPriority(token.toString())
                || (checker.isOpenBracket(lastStackElement) && checker.isOpenBracket(token.toString()))) {
            stack.add(token.toString());
        } else {
            addValidStackOperatorsToOutput(token.toString());
        }
    }

    /**
     * If the program already iterated through all tokens from the the tokens array
     * and there are still any operators left in the stack array, the program
     * shall add them to the output array beginning with the last (the last added)
     * operator in the stack array.
     */
    private void addRemainingOperators() {
        if (stack.size() != 0) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                output.add(stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            }
        }
    }

    /**
     * If the program found a closing bracket in the tokens array, it shall add
     * all operators from the stack to the output array until it founds an opening
     * bracket in the stack. This opening bracket shall be removed from the stack.
     * <p>
     * If the program found an operator with higher or equal priority than the last
     * operator in the stack array, it shall remove all such operators from the stack
     * and add them to the output. The operator that caused this adding shall be added
     * at last to the stack array.
     *
     * @param token is the current token the program extracted from the tokens array
     *              and which is evaluated.
     */
    private void addValidStackOperatorsToOutput(String token) {
        StringBuffer currentToken = new StringBuffer(token);
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (checker.isClosBracket(currentToken.toString())) { // If the token is a closing bracket
                for (int j = stack.size() - 1; j >= 0; j--) { // Adds all operators to the output until you find an opening bracket
                    if (!checker.isOpenBracket(stack.get(stack.size() - 1))) {
                        removeOperatorFromStackToOutput();
                    }
                }
                removeOpeningBracket();
                return;
                // If priority of the current token is less than the stack element priority
            } else if (getPriority(currentToken.toString()) <= getPriority(stack.get(stack.size() - 1))
                    && !checker.isOpenBracket(stack.get(stack.size() - 1))) {
                // Get operators out of the stack and put to the output while this condition is true and you do not find an opening bracket
                putOperatorsToStack(currentToken);
                return;
                // If priority of the current token is more than the stack element priority, add current token to the stack
            } else {
                stack.add(currentToken.toString());
                return;
            }
        }
    }

    /**
     * Gets operators out of the stack and puts them to the output while the
     * priority of the stack operator remains equal or more than the priority
     * of the current operator (taken from the tokens array) or while the program
     * does not find an opening bracket in the stack array or while there are any
     * operators left in the stack array.
     *
     * @param currentToken is the current token the program extracted from the tokens array
     *                     and which is evaluated
     */
    private void putOperatorsToStack(StringBuffer currentToken) {
        while (stack.size() > 0 && getPriority(currentToken.toString()) <= getPriority(stack.get(stack.size() - 1))) {
            if (!checker.isOpenBracket(stack.get(stack.size() - 1))) {
                removeOperatorFromStackToOutput();
            }
        }
        stack.add(currentToken.toString());
    }

    /**
     * Fills the priority operations map.
     * Operators with the same priority value are grouped into arrays.
     * An array with operators is the key, and the value of their
     * common priority is the value.
     */
    private void fillPriorityOperationMap() {
        String[] firstPriorityOperation = constants.FUNCTIONS;
        String[] secondPriorityOperation = {"^"};
        String[] thirdPriorityOperation = {"*", "/"};
        String[] fourthPriorityOperation = {"+", "-"};
        String[] fifthPriorityOperation = {"("};

        final int PRIORITY_1 = constants.PRIORITY_VALUE_5;
        final int PRIORITY_2 = constants.PRIORITY_VALUE_4;
        final int PRIORITY_3 = constants.PRIORITY_VALUE_3;
        final int PRIORITY_4 = constants.PRIORITY_VALUE_2;
        final int PRIORITY_5 = constants.PRIORITY_VALUE_1;

        OPERATION_PRIORITY.put(firstPriorityOperation, PRIORITY_1);
        OPERATION_PRIORITY.put(secondPriorityOperation, PRIORITY_2);
        OPERATION_PRIORITY.put(thirdPriorityOperation, PRIORITY_3);
        OPERATION_PRIORITY.put(fourthPriorityOperation, PRIORITY_4);
        OPERATION_PRIORITY.put(fifthPriorityOperation, PRIORITY_5);
    }

    /**
     * Removes operators from stack and puts them to the output array.
     */
    private void removeOperatorFromStackToOutput() {
        output.add(stack.get(stack.size() - 1));
        stack.remove(stack.size() - 1);
    }

    /**
     * Removes the opening bracket from the stack.
     */
    private void removeOpeningBracket() {
        if (checker.isOpenBracket(stack.get(stack.size() - 1))) {
            stack.remove(stack.size() - 1);
        }
    }

    /**
     * Gets operator or a math function as a string and returns its priority
     * number as an integer. The larger the number, the higher the priority.
     *
     * @param token is an operator or a math function as a string.
     * @return operator`s or a math function`s priority from 5 to 1.
     */
    private int getPriority(String token) {
        for (String[] key : OPERATION_PRIORITY.keySet()) {
            for (String character : key) {
                if (token.equals(character)) {
                    return OPERATION_PRIORITY.get(key);
                }
            }
        }
        return 0;
    }

    /**
     * The program goes from left to right and calculates two left
     * values each time it finds a math operation sign and one left
     * value each time it finds math formula (like cos, log10 etc).
     * After that the program deletes operands and adds result
     * to the resultArray.
     *
     * @return the result of formula calculation.
     */
    protected String calculate() {
        resultArray.addAll(output); // Makes a copy of the output array

        for (int i = 0; i < resultArray.size(); i++) {
            if (checker.isMathSymbol(resultArray.get(i))) {
                i = removeThreeTokensFromArray(i);

            } else if (checker.isFunction(resultArray.get(i))) {
                i = removeTwoTokensFromArray(i);
            }
        }
        return resultArray.get(0); // Return the result of the formula calculation
    }

    /**
     * Takes math operator and two preceding operands and performs math operation.
     * All two operands and the operator are removed from the array and the result
     * is inserted to it with its index being the same as the index of the first operand.
     *
     * @param i index of the operator.
     * @return new index the parent function calculate() shall keep iterating with.
     */
    private int removeThreeTokensFromArray(int i) {
        String result = performMathWithTwoOperands(resultArray.get(i - 2),
                resultArray.get(i - 1), resultArray.get(i));
        resultArray.add(i - 2, result);
        resultArray.remove(i - 1);
        resultArray.remove(i - 1);
        resultArray.remove(i - 1);

        if (resultArray.size() > 1) {
            i -= 2;
        }
        return i;
    }

    /**
     * Takes math function and one preceding operand and performs math operation.
     * The operand and the operator are removed from the array and the result
     * is inserted to it with its index being the same as the index of the operand.
     *
     * @param i index of the math function.
     * @return new index the parent function calculate() shall keep iterating with.
     */
    private int removeTwoTokensFromArray(int i) {
        String result = performMathWithOneOperand(resultArray.get(i - 1), resultArray.get(i));
        resultArray.add(i - 1, result);
        resultArray.remove(i);
        resultArray.remove(i);

        if (resultArray.size() > 1) {
            i -= 2;
        }
        return i;
    }

    /**
     * Calculates two doubles depending on the math operation sign (operand) provided.
     *
     * @param num1     is the first operand (double or variable).
     * @param operator is the math operation sign (operand).
     * @param num2     is the second operand (double or variable).
     * @return result of the calculation.
     */
    private String performMathWithTwoOperands(String num1, String num2, String operator) {
        String result = "";
        String operand1 = getDoubleValue(num1);
        String operand2 = getDoubleValue(num2);

        switch (operator) {
            case "+" -> result = Double.toString(Double.parseDouble(operand1) + Double.parseDouble(operand2));
            case "-" -> result = Double.toString(Double.parseDouble(operand1) - Double.parseDouble(operand2));
            case "*" -> result = Double.toString(Double.parseDouble(operand1) * Double.parseDouble(operand2));
            case "/" -> result = Double.toString(Double.parseDouble(operand1) / Double.parseDouble(operand2));
            case "^" -> result = Double.toString(Math.pow(Double.parseDouble(operand1), Double.parseDouble(operand2)));
        }
        return result;
    }

    /**
     * Calculates two doubles depending on the math operation sign (operand) provided.
     *
     * @param num      is the first operand (double or variable).
     * @param operator is the math operation sign (operand).
     * @return result of the calculation.
     */
    private String performMathWithOneOperand(String num, String operator) {
        String result = "";
        String operand = getDoubleValue(num);

        switch (operator) {
            case "sin" -> result = Double.toString(Math.sin(Double.parseDouble(operand)));
            case "-sin" -> result = Double.toString(-1 * Math.sin(Double.parseDouble(operand)));
            case "cos" -> result = Double.toString(Math.cos(Double.parseDouble(operand)));
            case "-cos" -> result = Double.toString(-1 * Math.cos(Double.parseDouble(operand)));
            case "tan" -> result = Double.toString(Math.tan(Double.parseDouble(operand)));
            case "-tan" -> result = Double.toString(-1 * Math.tan(Double.parseDouble(operand)));
            case "atan" -> result = Double.toString(Math.atan(Double.parseDouble(operand)));
            case "-atan" -> result = Double.toString(-1 * Math.atan(Double.parseDouble(operand)));
            case "log10" -> result = Double.toString(Math.log10(Double.parseDouble(operand)));
            case "-log10" -> result = Double.toString(-1 * Math.log10(Double.parseDouble(operand)));
            case "log2" -> result = Double.toString(calcLog2(operand));
            case "-log2" -> result = Double.toString(-1 * calcLog2(operand));
            case "sqrt" -> result = Double.toString(Math.sqrt(Double.parseDouble(operand)));
            case "-sqrt" -> result = Double.toString(-1 * Math.sqrt(Double.parseDouble(operand)));
        }
        return result;
    }

    /**
     * Calculates a logarithm with base two of a given double.
     *
     * @param num is a double in a form of a string.
     * @return result of the calculation.
     */
    private double calcLog2(String num) {
        return Math.log10(Double.parseDouble(num)) / Math.log10(2);
    }

    /**
     * Provides a specific numerical value to the mathematical equation.
     * If a double is received as input, the program will return this double.
     * If a variable is received as input, the program will query this variable
     * in the hashmap with variables and return the value of the variable.
     *
     * @param num is a double in a form of a string.
     * @return double value in a form of a string.
     */
    private String getDoubleValue(String num) {
        try {
            Double.parseDouble(num);
            return num;
        } catch (Exception e) {
            // Changes variable value if this variable is a negative one
            if (checker.isMinus(num.substring(0, 1))) {
                String negativeVariable = num.replace("-", "");
                return Double.toString(-1 * Double.parseDouble(Constants.VARIABLES.get(negativeVariable)));
            } else {
                return Constants.VARIABLES.get(num);
            }
        }
    }

    /**
     * Displays result of the formula calculation to the console.
     */
    protected void displayResult() {
        System.out.println("Result: " + calculate());
    }
}
