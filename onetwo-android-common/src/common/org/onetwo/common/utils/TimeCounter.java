package org.onetwo.common.utils;

import java.util.Date;

public class TimeCounter {

	private Date start;
	private Date stop;
	private long costTime;

	public TimeCounter() {
	}

	public Date start() {
		long start = System.currentTimeMillis();
		this.start = new Date(start);
		System.out.println(" ----->>> start time : " + this.start.toLocaleString());
		return this.start;
	}

	public Date stop() {
		long stop = System.currentTimeMillis();
		this.stop = new Date(stop);
		this.costTime = this.stop.getTime() - this.start.getTime();
		System.out.println(" ----->>> stop time : " + this.start.toLocaleString() +" cost total time : " + this.costTime+", (second): " + (this.costTime / 1000));
		return this.stop;
	}

}
