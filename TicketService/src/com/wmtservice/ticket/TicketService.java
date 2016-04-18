package com.wmtservice.ticket;

public interface TicketService {
	
	public int numSeatsAvailable(int venueLevel);

	public int numSeatsAvailable();
	
	public SeatHold findAndHoldSeats( int numSeats, int minLevel, int maxLevel, String customerEmail);

	public SeatHold findAndHoldSeats( int numSeats, int minLevel, String customerEmail);

	public SeatHold findAndHoldSeats( int numSeats,  String customerEmail);

	public String reserveSeats( long seatHoldId, String customerEmail);
		

}
