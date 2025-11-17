package com.healthycoderapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DietPlannerTest {
    private DietPlanner dietPlanner;

    @BeforeEach
    void setup() {
        dietPlanner = new DietPlanner(20, 30, 50);
    }

    @AfterEach
    void afterEach() {
        System.out.println("A unit test vas finished.");
    }

    @Test
    void should_ReturnCorrectDietPlanner_When_CorrectCoder() {
        // given
        Coder coder = new Coder(1.68, 72, 25, Gender.MALE);
        int calories = 2076;
        int protein = 104;
        int fat = 69;
        int carbohydrate = 260;
        DietPlan expected = new DietPlan(calories, protein, fat, carbohydrate);

        // when
        DietPlan actual = dietPlanner.calculateDiet(coder);

        // then
        assertAll(
                () -> assertEquals(expected.getCalories(), actual.getCalories()),
                () -> assertEquals(expected.getProtein(), actual.getProtein()),
                () -> assertEquals(expected.getFat(), actual.getFat()),
                () -> assertEquals(expected.getCarbohydrate(), actual.getCarbohydrate())
        );
    }
}