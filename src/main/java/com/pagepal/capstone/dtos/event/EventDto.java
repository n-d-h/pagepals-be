package com.pagepal.capstone.dtos.event;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.enums.EventStateEnum;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
	private UUID id;
	private String startAt;
	private String createdAt;
	private Integer limitCustomer;
	private Integer activeSlot;
	private Boolean isFeatured;
	private EventStateEnum state;
	private Integer price;
	private Seminar seminar;
	private List<BookingDto> bookings;
}
