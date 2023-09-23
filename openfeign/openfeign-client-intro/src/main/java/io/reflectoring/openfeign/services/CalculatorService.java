package io.reflectoring.openfeign.services;

import feign.Param;
import feign.RequestLine;

/**
 * RESTFul calculator service.
 */
public interface CalculatorService {
    /**
     * Adds two whole numbers.
     *
     * @param firstNumber  first whole number
     * @param secondNumber second whole number
     * @return sum of two numbers
     */
    @RequestLine("POST /operations/add?firstNumber={firstNumber}&secondNumber={secondNumber}")
    Long add(@Param("firstNumber") Long firstNumber, @Param("secondNumber") Long secondNumber);

    /**
     * Subtracts two whole numbers.
     *
     * @param firstNumber  first whole number
     * @param secondNumber second whole number
     * @return subtraction of two numbers
     */
    @RequestLine("POST /operations/subtract?firstNumber={firstNumber}&secondNumber={secondNumber}")
    Long subtract(@Param("firstNumber") Long firstNumber, @Param("secondNumber") Long secondNumber);

    /**
     * Multiplies two whole numbers.
     *
     * @param firstNumber  first whole number
     * @param secondNumber second whole number
     * @return multiplication of two numbers
     */
    @RequestLine("POST /operations/multiply?firstNumber={firstNumber}&secondNumber={secondNumber}")
    Long multiply(@Param("firstNumber") Long firstNumber, @Param("secondNumber") Long secondNumber);

    /**
     * Divides two whole numbers.
     *
     * @param firstNumber  first whole number
     * @param secondNumber second whole number, should not be zero
     * @return division of two numbers
     */
    @RequestLine("POST /operations/divide?firstNumber={firstNumber}&secondNumber={secondNumber}")
    Long divide(@Param("firstNumber") Long firstNumber, @Param("secondNumber") Long secondNumber);

}