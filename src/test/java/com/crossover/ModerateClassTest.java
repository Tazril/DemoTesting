package com.crossover;


import com.crossover.services.ExternalRatingApprovalService;
import com.crossover.services.NotificationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ModerateClassTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ExternalRatingApprovalService externalRatingApprovalService;
    @InjectMocks
    private ModerateClass moderateClass;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(moderateClass,"lastRating",8);
    }

    @Test
    public void shouldReturnTOP_WhenRatingEqualsCeiling() {
        //given
        int rating = 10;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("TOP+10", ratingStr);
    }

    @Test
    public void shouldReturnLOW_WhenRatingIsLessThenMidCeiling() {
        //given
        int rating = 1;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("LOW-1", ratingStr);
    }

    @Test
    public void shouldReturnHIGH_WhenRatingEqualsMidCeiling() {
        //given
        int rating = 5;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
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
            moderateClass.createRatingString(rating, ceilingRating);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot be over the hard ceiling");

    }

    @Test
    public void shouldReturnCACHED_WhenRatingEqualsLastRating() {
        //given
        int rating = 8;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr,is("HIGH=8-CACHED"));
    }

    @Test
    public void shouldReturnNOTAPP_WhenRatingIsNotApproved() {
        //given
        int rating = 8;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(false);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr,is("NOT-APP"));
    }

    @Test
    public void shouldSendNotification_WhenRatingIsValid() {
        //given
        int rating = 2;
        int ceilingRating = 10;
        ArgumentCaptor<Integer> ratingCaptor = ArgumentCaptor.forClass(Integer.class);
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);
        //when
        String ratingStr = moderateClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr,is("LOW-2"));
        verify(notificationService).notify(ratingCaptor.capture());
        assertThat(ratingCaptor.getValue(),is(2));

    }
}
