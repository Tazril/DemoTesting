package com.crossover;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class SimpleClassTest {

    private final SimpleClass simpleClass = new SimpleClass();

    @Test
    public void shouldReturnTOP_WhenRatingEqualsCeiling() {
        //given
        int rating = 10;
        int ceilingRating = 10;
        //when
        String ratingStr = simpleClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("TOP+10", ratingStr);
    }

    @Test
    public void shouldReturnLOW_WhenRatingIsLessThenMidCeiling() {
        //given
        int rating = 1;
        int ceilingRating = 10;
        //when
        String ratingStr = simpleClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("LOW-1", ratingStr);
    }

    @Test
    public void shouldReturnHIGH_WhenRatingEqualsMidCeiling() {
        //given
        int rating = 5;
        int ceilingRating = 10;
        //when
        String ratingStr = simpleClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("HIGH=5", ratingStr);
    }

    @Test
    public void shouldReturnIllegalArgumentException_WhenRatingExceedsCeiling() {
        //given
        int rating = 11;
        int ceilingRating = 10;
        //when & then
        assertThatThrownBy(() -> {
            simpleClass.createRatingString(rating, ceilingRating);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot be over the hard ceiling");

    }
}
