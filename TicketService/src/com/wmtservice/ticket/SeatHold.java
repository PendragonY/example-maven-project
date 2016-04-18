package com.wmtservice.ticket;



import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class SeatHold {

	private static AtomicLong counter = new AtomicLong(0);
	 
    public static long nextId() {
        return counter.incrementAndGet();     
    }
    
    public Vector<Seat> seats;
    
    public long holdId;
    
    public String holderEmail;
    
    public SeatHold(String email)
    {
    	holdId = nextId();
    	holderEmail = email;
    	seats = new Vector<Seat>();
    
    }
}
