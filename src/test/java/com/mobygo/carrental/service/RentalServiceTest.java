package com.mobygo.carrental.service;

import com.mobygo.carrental.model.*;
import com.mobygo.carrental.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the core booking business rules: price calculation,
 * double-booking prevention, and input validation. Repositories are mocked
 * so these tests are fast and isolated from the database.
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private RentalRepository rentalRepository;
    @Mock private CarRepository carRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private RentalService rentalService;

    private Car suvCar() {
        return new Car("BMW", "X5", "BS 300 CC", CarCategory.SUV, null);
    }

    @Test
    void calculatesTotalPriceFromDailyRateTimesDays() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(3); // 3 days, SUV = CHF 120/day

        when(carRepository.findById(1L)).thenReturn(Optional.of(suvCar()));
        when(rentalRepository.findOverlappingRentals(eq(1L), any(), any()))
            .thenReturn(Collections.emptyList());
        when(locationRepository.findById(anyLong()))
            .thenReturn(Optional.of(new Location("MobyGo Basel", "Basel", "Centralbahnplatz 10")));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> inv.getArgument(0));

        Rental rental = rentalService.createRental(
            1L, 1L, 1L, "Maria Müller", "maria@example.com", null, start, end);

        assertThat(rental.getTotalPrice()).isEqualTo(360.0);
    }

    @Test
    void rejectsOverlappingBookingForSameCar() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(2);

        when(carRepository.findById(1L)).thenReturn(Optional.of(suvCar()));
        Rental existing = new Rental();
        existing.setStartDate(start);
        existing.setEndDate(end);
        when(rentalRepository.findOverlappingRentals(eq(1L), any(), any()))
            .thenReturn(List.of(existing));

        assertThatThrownBy(() -> rentalService.createRental(
                1L, 1L, 1L, "Maria", "maria@example.com", null, start, end))
            .isInstanceOf(IllegalStateException.class);

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void rejectsNullDates() {
        assertThatThrownBy(() -> rentalService.createRental(
                1L, 1L, 1L, "Maria", "maria@example.com", null, null, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsStartDateNotBeforeEndDate() {
        LocalDate sameDay = LocalDate.now().plusDays(2);
        assertThatThrownBy(() -> rentalService.createRental(
                1L, 1L, 1L, "Maria", "maria@example.com", null, sameDay, sameDay))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsStartDateInThePast() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        assertThatThrownBy(() -> rentalService.createRental(
                1L, 1L, 1L, "Maria", "maria@example.com", null, start, end))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsGuestBookingWithoutName() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(1);
        assertThatThrownBy(() -> rentalService.createRental(
                1L, 1L, 1L, "   ", null, null, start, end))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
