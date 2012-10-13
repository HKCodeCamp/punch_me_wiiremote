package wiiremote;

import java.io.PrintWriter;
import java.net.Socket;

import org.wiigee.control.WiimoteWiigee;
import org.wiigee.device.Wiimote;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;

public class WiiRemoteClient {
    private static final float NOISE = 0;

    public static void main(String[] args) {

	WiimoteWiigee wiigee = new WiimoteWiigee();

	wiigee.setTrainButton(Wiimote.BUTTON_A);
	wiigee.setCloseGestureButton(Wiimote.BUTTON_HOME);
	wiigee.setRecognitionButton(Wiimote.BUTTON_B);

	wiigee.addDeviceListener(new AccelerationListener() {
	    double mLastX;
	    double mLastY;
	    double mLastZ;

	    @Override
	    public void motionStopReceived(MotionStopEvent event) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void motionStartReceived(MotionStartEvent event) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void accelerationReceived(AccelerationEvent event) {
		// TODO Auto-generated method stub
		System.out.println("accelerationReceived " + event.getAbsValue());

		analyzeGesture(mLastX, mLastY, mLastZ, event.getX(), event.getY(), event.getZ());
	    }
	});
	wiigee.addGestureListener(new GestureListener() {

	    @Override
	    public void gestureReceived(GestureEvent event) {
		// TODO Auto-generated method stub
		System.out.println("gestureReceived " + event.getId() + " ");

	    }

	});
    }

    public static final void analyzeGesture(double mLastX, double mLastY, double mLastZ, double x, double y, double z) {
	double deltaX = mLastX - x;
	double deltaY = mLastY - y;
	double deltaZ = mLastZ - z;

	if (Math.abs(deltaX) < NOISE)
	    deltaX = (float) 0.0;
	if (Math.abs(deltaY) < NOISE)
	    deltaY = (float) 0.0;
	if (Math.abs(deltaZ) < NOISE)
	    deltaZ = (float) 0.0;

	mLastX = x;
	mLastY = y;
	mLastZ = z;

	if ((deltaX + deltaY + deltaZ) != 0) {
	    if (deltaY == 0 && deltaZ == 0) {
		int force = (int) ((int) ((Math.abs(deltaX) - 12) / 18 * 10));
		if (deltaX > 0) {
		    Runnable r = new Punch("RIGHT", force);
		    new Thread(r).start();
		} else {
		    Runnable r = new Punch("LEFT", force);
		    new Thread(r).start();
		}
	    }

	    if (deltaX == 0 && deltaZ == 0) {
		int force = (int) ((int) ((Math.abs(deltaY) - 12) / 18 * 10));
		if (deltaY > 0) {
		    Runnable r = new Punch("UP", force);
		    new Thread(r).start();
		} else {
		    Runnable r = new Punch("DOWN", force);
		    new Thread(r).start();
		}

	    }

	}
    }

    public static class Punch implements Runnable {

	private String direction;
	private int force;

	public Punch(String direction, int force) {
	    // store parameter for later user
	    this.direction = direction;
	    this.force = force;
	}

	public void run() {
	    if (force > 2) {
		try {
		    Socket skt = new Socket("192.168.100.73", 9999);
		    PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
		    out.print(direction + " " + force);
		    out.close();
		    skt.close();
		} catch (Exception e) {
		    // Log.e("punch", "CANNOT CONNECT ", e);
		}
	    }
	}
    }
}
