package com.wmtservice.ticket;

import java.util.concurrent.atomic.AtomicLong;

public class Seat {
	private int seatLevel;
	private int seatRow;
	private int seatNumber;
	private ElapsedTime holdTime;
	private String status = "Free";
	private long holdId;
	
	
	
	//create seats 
	public Seat(int level, int row, int number)
	{
		seatLevel = level;
		seatRow = row;
		seatNumber = number;
		holdTime = null;
		
	}
	
	//need a copy constructor too
	public Seat (Seat copy)
	{
		seatLevel = copy.seatLevel;
		seatRow = copy.seatRow;
		seatNumber = copy.seatNumber;
		holdTime = copy.holdTime;
		status = copy.status;
		holdId = copy.holdId;

	}
	
	public int getRow()
	{
		return seatRow;
		
	}
	
	public int getLevel()
	{
		return seatLevel;
		
	}
	
	public int getNumber()
	{
		return seatNumber;
		
	}
	
	public boolean seatAvalable()
	{
		if (status.equalsIgnoreCase("Free"))
			return true;
		else if (status.equalsIgnoreCase("Reserved"))
			return false;
		else if (holdTime == null)
			return true;
		else if (holdTime.timePassed() > 300)
			return true;
		else 
			return false;  //shouldn't get it but say the seats empty if it does
		
	}
	
	public void seatHold(long Id)
	{
		status = "Hold";
		holdTime = ElapsedTime.start();
		holdId = Id;
		
	}
	
	public boolean isSeatHeld(long holdID)
	{
		if (this.holdId == holdID )
			return true;
		
		return false;
	}
	
	public void seatReserve()
	{
		status = "Reserved";
		
	}

}
