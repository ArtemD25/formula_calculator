package com.shpp.p2p.cs.adavydenko.assignment11;

/*
 * File: Checker.java
 * ------------------
 * Auxiliary class whose methods are used by other classes
 * to determine the type of characters (operators, functions,
 * or parentheses).
 */
public class Checker {

    /**
     * An object providing access to constants common for all classes.
     */
    Constants constants;

    public Checker(Constants constants) {
        this.constants = constants;
    }

    /**
     * Checks whether a particular symbol is a math operations symbol.
     *
     * @param character is symbol from the formula string to be checked for being valid.
     * @return answer whether the particular symbol from the formula string is valid.
     */
    protected boolean isMathSymbol(String character) {
        for (String mathOperator : constants.OPERATIONS) {
            if (character.equals(mathOperator)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a particular token is a math function.
     *
     * @param characters particular characters from the formula tokens array.
     * @return answer whether the particular token from the formula string is a math function.
     */
    protected boolean isFunction(String characters) {
        for (String function : constants.FUNCTIONS) {
            if (characters.equals(function)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a particular character is a bracket.
     *
     * @param character is the character in the formula string to be checked.
     * @return true if the particular character is a bracket.
     */
    protected boolean isBrackets(String character) {
        for (String brackets : constants.BRACKETS) {
            if (character.equals(brackets)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a particular character is an opening bracket.
     *
     * @param character is the character in the formula string to be checked.
     * @return true if the particular character is an opening bracket.
     */
    protected boolean isOpenBracket(String character) {
        return character.equals(constants.BRACKETS[0]);
    }

    /**
     * Checks whether a particular character is a closing bracket.
     *
     * @param character is the character in the formula string to be checked.
     * @return true if the particular character is a closing bracket.
     */
    protected boolean isClosBracket(String character) {
        return character.equals(constants.BRACKETS[1]);
    }

    /**
     * Checks whether a particular math symbol is a minus indicating a negative value
     *
     * @param currentCharacter is the character in the formula string to be checked
     * @param nextCharacter    is the next character in the formula string to be checked
     * @param prevCharacter    is the previous checked character from the formula string
     * @param i                index of the character in the formula string to be checked
     * @return true if the minus indicates negative value
     */
    protected boolean isNegativeValueSign(String currentCharacter, String nextCharacter, String prevCharacter, int i) {
        if (isMinus(currentCharacter)
                && !isMathSymbol(nextCharacter)
                && !isBrackets(nextCharacter)
                && i == 0) {
            return true;
        } else return isMinus(currentCharacter)
                && !isMathSymbol(nextCharacter)
                && !isBrackets(nextCharacter)
                && (isMathSymbol(prevCharacter)
                || prevCharacter.equals(constants.BRACKETS[0]));
    }

    /**
     * Checks whether a particular character is a minus sign.
     *
     * @param character is symbol from the formula string to be checked for being valid.
     * @return answer whether the particular symbol from the formula string is valid.
     */
    protected boolean isMinus(String character) {
        return character.equals(constants.MINUS);
    }
}
