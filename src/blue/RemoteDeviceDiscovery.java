package blue;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class RemoteDeviceDiscovery {

    public static final Vector<RemoteDevice> devicesDiscovered = new Vector<RemoteDevice>();

    public static void main(String[] args) throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                devicesDiscovered.addElement(btDevice);
                /*
                try {
                    System.out.println(btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
                */
            }

            public void inquiryCompleted(int discType) {
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        while(true){
            
        	devicesDiscovered.clear();
        	
        	System.out.println("Detectando...");
	        
        	synchronized(inquiryCompletedEvent) {
	            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
	            if (started) {
	                inquiryCompletedEvent.wait();
	            }
	        }
        	
	        for (RemoteDevice btDevice : devicesDiscovered) {
	        	String name = btDevice.getFriendlyName(false);
	        	if (name.contains("BD")){
	        		String[] lista = name.split("-");
	        		System.out.println(lista[2]);	
	        	}
			}
	        
	        System.out.println("Fin.");
	        Thread.sleep(5000);
        }
    }

}
