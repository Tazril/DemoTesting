package com.crossover;


import com.crossover.services.ExternalRatingApprovalService;
import com.crossover.services.NotificationService;
import com.crossover.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@PrepareForTest(fullyQualifiedNames = "com.crossover.HardClass")
public class HardClassTest {

    public static final String DECOR = "-Mocked";

    @Mock
    private Utils utils;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ExternalRatingApprovalService externalRatingApprovalService;
    @InjectMocks
    private HardClass hardClass;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(HardClass.class, "HARD_CACHE", 0);
        Whitebox.setInternalState(HardClass.class, "UTILS", utils);
        given(utils.getRatingDecoration()).willReturn(DECOR);
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(true);

    }

    @Test
    public void shouldReturnTOP_WhenRatingEqualsCeiling() {
        //given
        int rating = 10;
        int ceilingRating = 10;
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("TOP+10" + DECOR, ratingStr);
        verify(externalRatingApprovalService).isApproved(rating);
        verify(notificationService).notify(rating);
    }

    @Test
    public void shouldReturnLOW_WhenRatingIsLessThenMidCeiling() {
        //given
        int rating = 1;
        int ceilingRating = 10;
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr,is("LOW-1" + DECOR));
        verify(externalRatingApprovalService).isApproved(rating);
        verify(notificationService).notify(rating);
    }

    @Test
    public void shouldReturnHIGH_WhenRatingEqualsMidCeiling() {
        //given
        int rating = 5;
        int ceilingRating = 10;
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertEquals("HIGH=5" + DECOR, ratingStr);
        verify(externalRatingApprovalService).isApproved(rating);
        verify(notificationService).notify(rating);
    }

    @Test
    public void shouldReturnIllegalArgumentException_WhenRatingExceedsCeiling() {
        //given
        int rating = 11;
        int ceilingRating = 10;
        //when & then
        assertThatThrownBy(() -> {
            hardClass.createRatingString(rating, ceilingRating);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot be over the hard ceiling");

    }

    @Test
    public void shouldReturnCACHED_WhenRatingEqualsCachedValue() {
        //given
        int rating = 8;
        int ceilingRating = 10;
        Whitebox.setInternalState(HardClass.class, "HARD_CACHE", rating);
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr, is("HIGH=8-CACHED" + DECOR));
        verify(externalRatingApprovalService).isApproved(rating);
        verify(notificationService).notify(rating);
    }

    @Test
    public void shouldReturnNOTAPP_WhenRatingIsNotApproved() {
        //given
        int rating = 8;
        int ceilingRating = 10;
        given(externalRatingApprovalService.isApproved(anyInt())).willReturn(false);
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr, is("NOT-APP"));
        verify(externalRatingApprovalService).isApproved(rating);
    }

    @Test
    public void shouldSendNotification_WhenRatingIsValid() {
        //given
        int rating = 2;
        int ceilingRating = 10;
        //when
        String ratingStr = hardClass.createRatingString(rating, ceilingRating);
        //then
        assertThat(ratingStr, is("LOW-2" + DECOR));
        verify(externalRatingApprovalService).isApproved(rating);
        verify(notificationService).notify(rating);

    }
}
