package multimodal.schedule;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import multimodal.Constraint;

public class Schedule {
	
	//has to be sorted along the time axis!
	private LinkedList<Booking> bookings;

	public Schedule(){
		this.bookings = new LinkedList<Booking>();
	}
	
	private void removeFromSchedule(Booking booking) {
		this.bookings.remove(booking);
	}

	private boolean canUnbook(Booking booking) {
		return this.bookings.contains(booking);
	}
	
	
	private void addToSchedule(Booking booking) {
		this.bookings.add(booking);
		Collections.sort(this.bookings, new Comparator<Booking>(){
			@Override
			public int compare(Booking b1, Booking b2) {
				return (int) (b1.getStartTime().getTime()-b2.getStartTime().getTime());
			}
			
		});
	}

	private boolean canBook(Booking booking) {
		for(Booking b : this.bookings){
			if(b.overlaps(booking.getStartTime(),booking.getEndTime())){
				return false;
			}
		}
		return true;
	}

	
	protected boolean book(Booking booking) {
		if(canBook(booking)){
			addToSchedule(booking);
			return true;
		}
		return false;
	}
	
	protected boolean unbook(Booking booking) {
		if(canUnbook(booking)){
			removeFromSchedule(booking);
			return true;
		}
		return false;
	}

	public LinkedList<Booking> getPossibleBookings(Constraint c) {
		Date now = new Date();
		if(c.getEndTime().before(now)){
			throw new IllegalArgumentException("can't get a booking in the past!");
		}
		Date minimumStartDate = c.getStartTime().after(now) ? c.getStartTime() : now;
		return getNextPossibleBookings(minimumStartDate, c.getEndTime(), c.getDuration());		
	}
	
	private LinkedList<Booking> getNextPossibleBookings(Date walkAlongStartDate, Date endDate, long duration){
		LinkedList<Booking> possibleBookings = new LinkedList<Booking>();
		Date walkAlongEndDate = new Date(walkAlongStartDate.getTime()+duration*1000);
		if(this.bookings.size()==0){
			if(walkAlongStartDate.getTime()>= endDate.getTime()){
				return possibleBookings;
			}
			//if there are no bookings, there can be no collision
			possibleBookings.add(new Booking(this, walkAlongStartDate, walkAlongEndDate));
		} else {
			for(Booking b : this.bookings){
				//current Start Date after latest end date?
				if(walkAlongStartDate.getTime()>= endDate.getTime()){
					return possibleBookings;
				}
				//jump over booking if it overlaps
				if(b.overlaps(walkAlongStartDate, walkAlongEndDate)){
					walkAlongStartDate = new Date(b.getEndTime().getTime()+1);
					walkAlongEndDate = new Date(walkAlongStartDate.getTime()+duration*1000);
				}
				if(!b.overlaps(walkAlongStartDate, walkAlongEndDate)){
					//we've got a booking. That's freaking awesome.
					possibleBookings.add(new Booking(this, walkAlongStartDate, walkAlongEndDate));
				}
			}			
		}
		return possibleBookings;
	}

}
