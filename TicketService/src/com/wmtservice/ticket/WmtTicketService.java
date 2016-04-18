package com.wmtservice.ticket;

/*
 * This service makes a couple of assumptions, the first is that there is just one
 * building for seats and just one event date, to support multiple dates or buidlings the methods would 
 * need a way to identify the building and/or the event date.
 * also, just to pick a number, a hold will expire after 5 minutes but only if
 * someone else requests a hold for the seat (or seats), so even if after 60 seconds has past
 * if someone else doesn't have a hold on the seat, the reservation will be granted
 * the final assumption is that a request for seats can be filled only by getting all the seats requeste
 * in the same level, also only 1 thread at a time will be able to access the venue
 */



public class WmtTicketService implements TicketService{

	public int numSeatsAvailable(int venueLevel)
	{
		//get the venue
		TicketVenue venue = TicketVenue.INSTANCE;
		
		int seatCount = 0;
		
		//step  thru the venueLevel and count the seats that do not have a reservation or an unexpired hold
		synchronized(venue)
		{
			seatCount = venue.seatsAvailable(venueLevel);
		}
		return seatCount;
	}
	
	public int numSeatsAvailable()
	{
		//get the venue
		
		//step  thru the venueLevel and count the seats that do not have a reservation or an unexpired hold
		TicketVenue venue = TicketVenue.INSTANCE;
		return venue.seatsAvailable();
	}
	
	//this method assumes that minLevel is used to exclude more expensive seats
	//and that maxLevel is used to exclude poorer view seats
	//this method looks to hold the best seats available
	
	public SeatHold findAndHoldSeats( int numSeats, int minLevel, int maxLevel, String customerEmail)
	{
		//get the venue
		TicketVenue venue = TicketVenue.INSTANCE;
		//step thru the requested levels, and find the first set of numSeats in a row that aren't held
		
		SeatHold seats = new SeatHold(customerEmail);
		
		//only 1 thread at a time will work on the venue
		synchronized(venue)
		{
			switch(minLevel)
			{
			case TicketVenue.ORCHASTRA:
				venue.holdSeats(TicketVenue.ORCHASTRA, seats, numSeats, customerEmail);
				if (!seats.seats.isEmpty() || maxLevel == TicketVenue.ORCHASTRA)
					break;
			case TicketVenue.MAIN:
				venue.holdSeats(TicketVenue.MAIN, seats, numSeats, customerEmail);
				if (!seats.seats.isEmpty() || maxLevel == TicketVenue.MAIN)
					break;
			case TicketVenue.BALCONY1:
				venue.holdSeats(TicketVenue.BALCONY1, seats, numSeats, customerEmail);
				if (!seats.seats.isEmpty() || maxLevel == TicketVenue.BALCONY1)
					break;
			case TicketVenue.BALCONY2:
				venue.holdSeats(TicketVenue.BALCONY2, seats, numSeats, customerEmail);
				if (!seats.seats.isEmpty() || maxLevel == TicketVenue.BALCONY2)
					break;
			}
		}
		return seats;
	}
	
	//this method assumes that minLevel is used to exclude more expensive seats
	public SeatHold findAndHoldSeats( int numSeats, int minLevel, String customerEmail)
	{
					
		return findAndHoldSeats( numSeats,minLevel, TicketVenue.BALCONY2, customerEmail);
	}
	
	public SeatHold findAndHoldSeats( int numSeats,  String customerEmail)
	{
		//get the venue
		
		return findAndHoldSeats( numSeats,  TicketVenue.ORCHASTRA, customerEmail);
	}
	
	//with just the holdId, can't know if all the held seats are still available, so need to check that
	//each identified hold seat still has an unexpired hold befor reserving it
	public String reserveSeats( long seatHoldId, String customerEmail)
	{
		int level;
		TicketVenue venue = TicketVenue.INSTANCE;
		
		//now going to assume that holds that haven't yet expired will not expire 
		//before being reserved
		//get the level of the held seats
		if (venue.heldSeatInLevel(seatHoldId, TicketVenue.ORCHASTRA))
			level = TicketVenue.ORCHASTRA;
		else if (venue.heldSeatInLevel(seatHoldId, TicketVenue.MAIN))
			level = TicketVenue.MAIN;
		else if (venue.heldSeatInLevel(seatHoldId, TicketVenue.BALCONY1))
			level = TicketVenue.BALCONY1;
		else if (venue.heldSeatInLevel(seatHoldId, TicketVenue.BALCONY2))
			level = TicketVenue.BALCONY2;
		else return "HOLD EXPIRED";
		
		//change status of each held set belonging to seatHoldId and set its status to reserved
		String confirmationCode = customerEmail + "Code:" + seatHoldId;
		
		boolean reserved = venue.reserveHeldSeats(seatHoldId, level);
		if(reserved)
			return confirmationCode;
		else
			return "NO SEATS RESERVED";
	}
	
}
