package multimodal.schedule;

import java.util.Date;

public class Booking {
	final private Date startTime;
	final private Date endTime;
	final private Schedule schedule;
	private boolean booked;

	public Booking(Schedule schedule, Date startTime, Date endTime){
		this.schedule = schedule;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booked = false;
	}
	
	public void book(){
		if(this.schedule.book(this)){
			this.booked = true;
		} else {
			this.booked = false;
		}
	}
	
	public void unbook(){
		if(this.schedule.unbook(this)){
			this.booked = false;
		} else {
			this.booked = true;
		}
	}

	public boolean overlaps(Date startTime, Date endTime) {
		//overlaps in time?
		if(	startTime.after(this.getEndTime()) || 
			this.getStartTime().after(endTime) ){
			return false;
		}
		return true;
	}

	Date getEndTime() {
		return (Date) endTime.clone();
	}

	Date getStartTime() {
		return (Date) startTime.clone();
	}
	
	@Override
	public String toString() {
		return "Booking: "+this.startTime.toString()+" - "+this.endTime.toString();
	}
}
