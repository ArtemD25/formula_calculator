package com.shpp.p2p.cs.adavydenko.assignment11;

import java.util.ArrayList;

/*
 * File: Parser.java
 * -----------------
 * Takes user formula with all white spaces deleted
 * and defines particular tokens - recognized groups
 * of characters taken from the formula string.
 */
public class Parser {

    /**
     * An array with recognized tokens from the formula string.
     */
    protected ArrayList<String> tokens = new ArrayList<>();

    /**
     * User formula with all white spaces deleted.
     */
    private String SHORT_FORMULA;

    /**
     * An object checking characters for being operators, formulas or brackets.
     */
    private final Checker checker;

    /**
     * Gets external object to check formula, loads user formula with all
     * white spaces deleted and defines tokens in it.
     *
     * @param constants is an object providing access to constants common
     *                  for all classes.
     * @param checker   is an object checking characters for being operators,
     *                  formulas or brackets.
     */
    public Parser(Constants constants, Checker checker) {
        this.checker = checker;
        this.SHORT_FORMULA = constants.SHORT_FORMULA;
        defineTokens();
    }

    /**
     * Analyses formula string and recognizes separate tokens like
     * math operations, math formulas etc.
     */
    private void defineTokens() {
        String token = ""; // Recognized group of characters taken from the formula string

        for (int i = 0; i < SHORT_FORMULA.length(); i++) {
            String character = SHORT_FORMULA.substring(i, i + 1); // Symbol to be parsed now
            String nextCharacter = defineNextCharacter(i); // Symbol to be parsed in the next iteration
            String prevCharacter = definePrevCharacter(i); // Symbol that was parsed before

            // Define doubles, variables or functions
            if (!checker.isMathSymbol(character) && !checker.isBrackets(character)
                    && !checker.isNegativeValueSign(character, nextCharacter, prevCharacter, i)) {
                token += character;

            } else if (i == SHORT_FORMULA.length() - 1) { // Add token if it is about last formula character
                if (token.length() != 0) {
                    tokens.add(token);
                }
                token = "";

            } else if (checker.isNegativeValueSign(character, nextCharacter, prevCharacter, i)) { // Define negative doubles
                int numOfChar = 0;
                token = character;

                // Add characters to the token string while there is no operator or bracket
                for (int j = i + 1; j < SHORT_FORMULA.length(); j++, numOfChar++) {
                    String character2 = SHORT_FORMULA.substring(j, j + 1);
                    if (j == SHORT_FORMULA.length() - 1) { // If it is last character in the formula string
                        token += character2;
                        token = addToken(token);
                        return;
                    } else if (!checker.isMathSymbol(character2) && !checker.isBrackets(character2)) {
                        token += character2;
                    } else {
                        i += numOfChar - 1;
                        break;
                    }
                }
                token = addToken(token); // Add ready made negative token (double or variable) to the tokens array
                i++;
            } else {
                if (token.length() != 0) {
                    token = addToken(token);
                }
                tokens.add(character);
            }
        }
        addLastTokenIfAny(token);
        addLastBracketIfAny();
    }

    /**
     * Defines whether there is any next symbol to work with.
     *
     * @param i is iterations count from the parent method.
     * @return either next symbol from the formula string or end-of-line message.
     */
    private String defineNextCharacter(int i) {
        if (i != SHORT_FORMULA.length() - 1) {
            return SHORT_FORMULA.substring(i + 1, i + 2);
        }
        return null;
    }

    /**
     * Defines whether there is any next symbol to work with.
     *
     * @param i is iterations count from the parent method.
     * @return either next symbol from the formula string or end-of-line message.
     */
    private String definePrevCharacter(int i) {
        if (i != 0) {
            return SHORT_FORMULA.substring(i - 1, i);
        }
        return null;
    }

    /**
     * If the program saved previously any characters to the token string,
     * it shall deem this token as a final one and add it to the tokens array.
     *
     * @param token is a recognized group of characters taken from the formula string.
     */
    private void addLastTokenIfAny(String token) {
        if (token.length() != 0) {
            tokens.add(token);
        }
    }

    /**
     * Adds token to the token array and resets the value of the token variable.
     *
     * @param token is a recognized group of characters taken from the formula string.
     * @return is an empty string that becomes the new value of the token variable.
     */
    private String addToken(String token) {
        tokens.add(token);
        return "";
    }

    /**
     * Adds closing bracket if any to the tokens array.
     * It is the only operator that can be added to this array
     * as a last token if user provided a correct formula.
     */
    private void addLastBracketIfAny() {
        if (checker.isClosBracket(SHORT_FORMULA.substring(SHORT_FORMULA.length() - 1))) {
            tokens.add(SHORT_FORMULA.substring(SHORT_FORMULA.length() - 1));
        }
    }

    /**
     * Displays all formula tokens to console.
     * Can be conveniently used for code debugging.
     */
    protected void displayTokens() {
        System.out.println(tokens);
    }

    /**
     * Displays parsed formula to console.
     */
    protected void displayParsedFormula() {
        String formulaMessage = "The formula you entered is:";
        for (int i = 0; i < tokens.size(); i++) {

            // Do not add white spaces if there are brackets or math function
            if (checker.isClosBracket(tokens.get(i)) || (i != 0 && checker.isOpenBracket(tokens.get(i - 1)))
                    || (i != 0 && checker.isFunction(tokens.get(i - 1)))) {
                formulaMessage = formulaMessage + tokens.get(i);

                // In all other cases add white spaces
            } else {
                formulaMessage = formulaMessage + " " + tokens.get(i);
            }
        }
        System.out.println(formulaMessage);
    }
}
