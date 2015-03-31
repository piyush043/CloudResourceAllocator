package com.vaannila.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//The task which you want to execute
public class MyTimerTask extends TimerTask
{

	loadBalancerAction loadBalancerAction = new loadBalancerAction();
    public void run()
    {
        //write your code here
    	try {
    		System.out.println("---");
//			loadBalancerAction.allocate2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


public void startTimer() {

    //the Date and time at which you want to execute
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = null;
	try {
		date = dateFormatter.parse("2012-07-06 13:05:45");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    //Now create the time and schedule it
    Timer timer = new Timer();

    //Use this if you want to execute it once
    //timer.schedule(new MyTimerTask(), date);

    //Use this if you want to execute it repeatedly
    int period = 120000;//10secs
    timer.schedule(new MyTimerTask(), date, period );
}
}