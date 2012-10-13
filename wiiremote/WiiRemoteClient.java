package wiiremote;

import org.wiigee.control.WiimoteWiigee;
import org.wiigee.device.Wiimote;
import org.wiigee.event.AccelerationEvent;
import org.wiigee.event.AccelerationListener;
import org.wiigee.event.GestureEvent;
import org.wiigee.event.GestureListener;
import org.wiigee.event.MotionStartEvent;
import org.wiigee.event.MotionStopEvent;

public class WiiRemoteClient {
    public static void main(String[] args) {
	WiimoteWiigee wiigee = new WiimoteWiigee();

	wiigee.setTrainButton(Wiimote.BUTTON_A);
	wiigee.setCloseGestureButton(Wiimote.BUTTON_HOME);
	wiigee.setRecognitionButton(Wiimote.BUTTON_B);

	wiigee.addDeviceListener(new AccelerationListener() {
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
}
