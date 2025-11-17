package com.healthycoderapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    private String environment = "prod";

    // Note: BeforeAll and AfterAll are used typically when connecting to a db/server and closing it
    // Because these operations are expensive, we do not want to repeat them in each single unit test
    // That's why we use BeforeAll and AfterAll
    
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all unit tests.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all unit tests.");
    }

    @Nested
    class IsDietRecommendedTests {
        // In parameterized test, we test the function for different weight values
        // instead of repeating the test for each value
        @ParameterizedTest
        @ValueSource(doubles = {89.0, 95.7, 110.0})
        void should_ReturnTrue_When_DietRecommended(Double coderWeight) {
            // given
            double weight = coderWeight;
            double height = 1.65;

            // when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertTrue(recommended);
        }

        // If we test the function for multiple variables,
        // then we can use CsvSource instead of ValueSource
        // we can name the variables in ParameterizedTest
        // so that we can see the variable names of the CsvSource in the test result
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0, 1.72", "95.7, 1.75", "110.0, 1.78"})
        void should_ReturnTrue_When_DietRecommendedV2(Double coderWeight, Double coderHeight) {
            // given
            double weight = coderWeight;
            double height = coderHeight;

            // when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertTrue(recommended);
        }

        // We can also use CsvFileSource to test values read from csv file
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommendedV3(Double coderWeight, Double coderHeight) {
            // given
            double weight = coderWeight;
            double height = coderHeight;

            // when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertTrue(recommended);
        }

        @Test
        void should_ReturnFalse_When_DietNotRecommended() {
            // given
            double weight = 50;
            double height = 1.65;

            // when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertFalse(recommended);
        }

        @Test
        void should_ThrowArithmeticException_When_HeightZero() {
            // given
            double weight = 81.2;
            double height = 0.0;

            // when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            // then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    class FindCoderWithWorstBMITests {
        @Test
        void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60));
            coders.add(new Coder(1.82, 98));
            coders.add(new Coder(1.82, 64.7));

            // when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98, coderWorstBMI.getWeight())
            );
            // If you do not assertAll and assertEquals one by one, then when the first fails,
            // you do not know if both fail.
        }

        @Test
        void should_ReturnNullWorstBMICoder_When_CoderListEmpty() {
            // given
            List<Coder> coders = new ArrayList<>();

            // when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertNull(coderWorstBMI);
        }

        @Test
        void should_ReturnCoderWithWorstBMIIN1Ms_When_CoderListHas10000Elements() {
            // given
            // we do not want to test timeout in dev environment as the dev local machine is not powerful
            // for this timeout test, so with assumeTrue, we skip the test if the environment is dev
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            // when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertTimeout(Duration.ofMillis(500), executable);
        }
    }

    @Nested
    class GetBMIScoresTests {
        @Test
        void should_ReturnBMIScoreArray_When_CoderListNotEmpty() {
            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60));
            coders.add(new Coder(1.82, 98));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};


            // when
            double[] scores = BMICalculator.getBMIScores(coders);

            // then
            assertArrayEquals(expected, scores);

            // Note: assertEquals fail as the expected and actual are different objects in memory,
            // therefore, we use assertArrayEquals
        }
    }
}