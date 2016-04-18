package com.wmtservice.ticket;
import java.util.concurrent.TimeUnit;;

public class ElapsedTime {
	   
    long starts;

    public static ElapsedTime start() {
        return new ElapsedTime();
    }

    private ElapsedTime() {
        reset();
    }

    public ElapsedTime reset() {
        starts = System.nanoTime();
        return this;
    }

    public long time() {
        long ends = System.nanoTime();
        return ends - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
    
    public long timePassed()
    {
    	return time(TimeUnit.SECONDS);
    }
   

}
