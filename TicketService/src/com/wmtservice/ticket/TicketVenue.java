package com.wmtservice.ticket;


import java.util.Iterator;

import java.util.Vector;

// since there is only 1 venue, and 1 event date, hard code the config
// real world would read this in from either a config file or a database table
public class TicketVenue {
	
	private Vector<Seat> orchastra;
	private Vector<Seat> main;
	private Vector<Seat> balcony1;
	private Vector<Seat> balcony2;
	
	private float priceOrchastraSeat = (float) 100.00;
	private float priceMainSeat = (float) 75.00;
	private float priceBalcony1Seat = (float) 50.00;
	private float priceBalcony2Seat = (float) 40.00;
	
	public static final int ORCHASTRA = 1;
	public static final int MAIN = 2;
	public static final int BALCONY1 = 3;
	public static final int BALCONY2 = 4;

	//this creates an instance of TicketVenue in a thread safe manner
	public final static TicketVenue INSTANCE = new TicketVenue();
	
	private TicketVenue()
	{
		orchastra = new Vector<Seat>();
		main = new Vector<Seat>();
		balcony1 = new Vector<Seat>();
		balcony2 = new Vector<Seat>();
		
		//fill each list of seats with the correct number of rows and seats
		int row = 1;
		int number = 1;
		int level = 1;
		
		for (row=1;row <=25; row++)
		{
			for (number=1;number <=100; number++)
			{
				if (number <=50)
					orchastra.add(new Seat(ORCHASTRA, row, number));
				if (row <= 20)
					main.add(new Seat(MAIN, row, number));
				if (row <=15)
					balcony1.add(new Seat(BALCONY1, row, number));
				if (row <=15)
					balcony2.add(new Seat(BALCONY2, row, number));
			}
		}
	}
	
	public int  seatsAvailable(int level)
	{
		int counter = 0;
		
		Vector <Seat> levelToCheck;
		
		//will find no seats available if level is out of range
		if (level == ORCHASTRA)
			levelToCheck = orchastra;
		else if (level == MAIN)
			levelToCheck = main;
		else if (level == BALCONY1)
			levelToCheck = balcony1;
		else if (level == BALCONY2)
			levelToCheck = balcony2;
		else
			return 0;
		
		//this works for multi-thread because there is only 1 instance of the TicketVenue class
		
		for(Iterator<Seat> i = levelToCheck.iterator(); i.hasNext();) //each Seat in leveltoCheck
		{
			if (i.next().seatAvalable())
				counter++;
			
		}
		
		return counter;
		
	}
	
	public int seatsAvailable()
	{
		return(seatsAvailable(ORCHASTRA) 
				+ seatsAvailable(MAIN) 
				+ seatsAvailable(BALCONY1) 
				+ seatsAvailable(BALCONY2) );
	}
	
	public void holdSeats(int level, SeatHold holding, int seatsToHold, String custEmail)
	{
		//get the list of seats for the level requested
		Vector <Seat> levelToCheck;
		
		//will find no seats available if level is out of range
		if (level == ORCHASTRA)
			levelToCheck = orchastra;
		else if (level == MAIN)
			levelToCheck = main;
		else if (level == BALCONY1)
			levelToCheck = balcony1;
		else if (level == BALCONY2)
			levelToCheck = balcony2;
		else
			return ;
		//count free seats in level
		int counter = 0;
		for(Iterator<Seat> i = levelToCheck.iterator(); i.hasNext();) //each Seat in leveltoCheck
		{
			if (i.next().seatAvalable())
				counter++;
		}
		
		if (counter < seatsToHold)
			return;
		
		long holdId = 0;
			
		for(Iterator<Seat> i = levelToCheck.iterator(); i.hasNext();) //each Seat in leveltoCheck
		{
			//if enough free, hold them
			Seat hold = i.next();
			if (hold.seatAvalable())
			{
				hold.seatHold(holding.holdId);
				//fill the holding object with the held seats
				holding.seats.add(new Seat(hold));
			}
			
		}
				
	}
	
	public boolean heldSeatInLevel(long holdId, int level)
	{

		Vector <Seat> levelToCheck;
		
		//will find no seats available if level is out of range
		if (level == ORCHASTRA)
			levelToCheck = orchastra;
		else if (level == MAIN)
			levelToCheck = main;
		else if (level == BALCONY1)
			levelToCheck = balcony1;
		else if (level == BALCONY2)
			levelToCheck = balcony2;
		else
			return false;
		
		for(Iterator<Seat> i = levelToCheck.iterator(); i.hasNext();) //each Seat in leveltoCheck
		{
			//if enough free, hold them
			Seat hold = i.next();
			if (hold.isSeatHeld(holdId))
			{
				//all the seats held are in the same level, so they are in this level
				//if the seat is available, then hold has expired, return false
				return !hold.seatAvalable();
				
			}
			
		}
		
		return false;  //its not here if it wasn't found already
	}
	
	//this method is called only after it is determined there are no expired holds
	public boolean reserveHeldSeats(long holdId, int level)
	{
		Vector <Seat> levelToCheck;
		
		//will find no seats available if level is out of range
		if (level == ORCHASTRA)
			levelToCheck = orchastra;
		else if (level == MAIN)
			levelToCheck = main;
		else if (level == BALCONY1)
			levelToCheck = balcony1;
		else if (level == BALCONY2)
			levelToCheck = balcony2;
		else
			return false;  //shouldn't get here, but ...
		
		for(Iterator<Seat> i = levelToCheck.iterator(); i.hasNext();) //each Seat in leveltoCheck
		{
			//if enough free, hold them
			Seat reserve = i.next();
			if (reserve.isSeatHeld(holdId))
			{
				reserve.seatReserve();
			}
			
		}
		
		return true;  //only get here if all held seats reserved
	}

}
