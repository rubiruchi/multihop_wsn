import java.util.*;
import java.text.*;

import java.awt.*;
import javax.swing.*;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;


public class SensorMsgReader implements net.tinyos.message.MessageListener {

	private MoteIF moteIF;

	private static JLabel node1_data1;
	private static JLabel node1_data2;
	private static JLabel node1_data3;
	private static JLabel node1_data4;
	private static JLabel node1_data5;
	private static JLabel node1_Data6;

	private static JLabel node2_data1;
	private static JLabel node2_data2;
	private static JLabel node2_data3;
	private static JLabel node2_data4;
	private static JLabel node2_data5;
	private static JLabel node2_Data6;

	private static JLabel node3_data1;
	private static JLabel node3_data2;
	private static JLabel node3_data3;
	private static JLabel node3_data4;
	private static JLabel node3_data5;
	private static JLabel node3_Data6;

	private static JLabel node4_data1;
	private static JLabel node4_data2;
	private static JLabel node4_data3;
	private static JLabel node4_data4;
	private static JLabel node4_data5;
	private static JLabel node4_Data6;

	private static JLabel node5_data1;
	private static JLabel node5_data2;
	private static JLabel node5_data3;
	private static JLabel node5_data4;
	private static JLabel node5_data5;
	private static JLabel node5_Data6;
  
  	public SensorMsgReader(String source) throws Exception {
    		if (source != null) {
      			moteIF = new MoteIF(BuildSource.makePhoenix(source, PrintStreamMessenger.err));
    		}
    		else {
      			moteIF = new MoteIF(BuildSource.makePhoenix(PrintStreamMessenger.err));
    		}
  	}

	public void start() {}

	public void messageReceived(int to, Message message) {
		SensorMsg s_msg = new SensorMsg(message,0);

		if (s_msg.get_dest_id() != 255) return;

		DecimalFormat df = new DecimalFormat("#.00");

		double temp = 0.01*s_msg.get_temp() - 39.6;
		double light_voltage = (s_msg.get_light()/4096.0 * 1.5)/100000.0;
		double light = 0.625 * 1000000 * light_voltage * 1000;	
		double hum = -4 + 0.0405*s_msg.get_hum() + (-2.8 * Math.pow(10,-6))*(Math.pow(s_msg.get_hum(),2));
		double hum_true = (temp - 25) * (0.01 + 0.00008*s_msg.get_hum()) + hum;
		double volt = s_msg.get_volt()/4096.0 * 1.5 * 2;

		if (s_msg.get_node_id() == 14){
			node1_data1.setText(s_msg.get_node_id() + "");
			node1_data2.setText(s_msg.get_counter() + "");
			node1_data3.setText(df.format(temp) + "\u00b0C");
			node1_data4.setText(df.format(light) + " lx");
			node1_data5.setText(df.format(hum_true) + "%");
			node1_Data6.setText(df.format(volt) + " VCC");
		}
		else if (s_msg.get_node_id() == 16){
			node2_data1.setText(s_msg.get_node_id() + "");
			node2_data2.setText(s_msg.get_counter() + "");
			node2_data3.setText(df.format(temp) + "\u00b0C");
			node2_data4.setText(df.format(light) + " lx");
			node2_data5.setText(df.format(hum_true) + "%");
			node2_Data6.setText(df.format(volt) + " VCC");
		}
		else if (s_msg.get_node_id() == 18){
			node3_data1.setText(s_msg.get_node_id() + "");
			node3_data2.setText(s_msg.get_counter() + "");
			node3_data3.setText(df.format(temp) + "\u00b0C");
			node3_data4.setText(df.format(light) + " lx");
			node3_data5.setText(df.format(hum_true) + "%");
			node3_Data6.setText(df.format(volt) + " VCC");
		}
		else if (s_msg.get_node_id() == 19){
			node4_data1.setText(s_msg.get_node_id() + "");
			node4_data2.setText(s_msg.get_counter() + "");
			node4_data3.setText(df.format(temp) + "\u00b0C");
			node4_data4.setText(df.format(light) + " lx");
			node4_data5.setText(df.format(hum_true) + "%");
			node4_Data6.setText(df.format(volt) + " VCC");
		}
		else if (s_msg.get_node_id() == 22){
			node5_data1.setText(s_msg.get_node_id() + "");
			node5_data2.setText(s_msg.get_counter() + "");
			node5_data3.setText(df.format(temp) + "\u00b0C");
			node5_data4.setText(df.format(light) + " lx");
			node5_data5.setText(df.format(hum_true) + "%");
			node5_Data6.setText(df.format(volt) + " VCC");
		}
	}

  
  	private static void usage() {
    		System.err.println("usage: SensorMsgReader [-comm <source>] message-class [message-class ...]");
  	}

  	private void addMsgType(Message msg) {
    		moteIF.registerListener(msg, this);
  	}

	public static void main(String[] args) throws Exception {
		DataGUI app = new DataGUI();
		app.setVisible(true);

	    	String source = null;
	    	Vector v = new Vector();
	    if (args.length > 0) {
	      for (int i = 0; i < args.length; i++) {
		if (args[i].equals("-comm")) {
		  source = args[++i];
		}
		else {
		  String className = args[i];
		  try {
		    Class c = Class.forName(className);
		    Object packet = c.newInstance();
		    Message msg = (Message)packet;
		    if (msg.amType() < 0) {
			System.err.println(className + " does not have an AM type - ignored");
		    }
		    else {
			v.addElement(msg);
		    }
		  }
		  catch (Exception e) {
		    System.err.println(e);
		  }
		}
	      }
	    }
	    else if (args.length != 0) {
	      usage();
	      System.exit(1);
	    }

	    SensorMsgReader smr = new SensorMsgReader(source);
	    Enumeration msgs = v.elements();
	    while (msgs.hasMoreElements()) {
	      Message m = (Message)msgs.nextElement();
	      smr.addMsgType(m);
	    }
	    smr.start();
	}

	public static class DataGUI extends JFrame {
		public DataGUI() {
			super("Sensor Data");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(new Dimension(800, 600));
			setLayout(new GridLayout (2, 3));
			setResizable(true);

			JPanel node1 = new JPanel(new GridLayout (6, 2));
			node1.setBorder(BorderFactory.createLineBorder(Color.black));
			JPanel node2 = new JPanel(new GridLayout (6, 2));
			node2.setBorder(BorderFactory.createLineBorder(Color.black));
			JPanel node3 = new JPanel(new GridLayout (6, 2));
			node3.setBorder(BorderFactory.createLineBorder(Color.black));
			JPanel node4 = new JPanel(new GridLayout (6, 2));
			node4.setBorder(BorderFactory.createLineBorder(Color.black));
			JPanel node5 = new JPanel(new GridLayout (6, 2));
			node5.setBorder(BorderFactory.createLineBorder(Color.black));
			
			//NODE 1
			JLabel node1_Title1 = new JLabel("Node ID: ");
			JLabel node1_Title2 = new JLabel("Packet #: ");
			JLabel node1_Title4 = new JLabel("Light: ");
			JLabel node1_Title3 = new JLabel("Temperature: ");
			JLabel node1_Title5 = new JLabel("Humidity: ");
			JLabel node1_Title6 = new JLabel("Voltage: ");

			node1.add(node1_Title1);
			node1.add(node1_Title2);
			node1.add(node1_Title3);
			node1.add(node1_Title4);
			node1.add(node1_Title5);
			node1.add(node1_Title6);

			node1_data1 = new JLabel("");
			node1_data2 = new JLabel("");
			node1_data3 = new JLabel("");
			node1_data4 = new JLabel("");
			node1_data5 = new JLabel("");
			node1_Data6 = new JLabel("");

			node1.add(node1_data1);
			node1.add(node1_data2);
			node1.add(node1_data3);
			node1.add(node1_data4);
			node1.add(node1_data5);
			node1.add(node1_Data6);

			//NODE 2
			JLabel node2_Title1 = new JLabel("Node ID: ");
			JLabel node2_Title2 = new JLabel("Packet #: ");
			JLabel node2_Title4 = new JLabel("Light: ");
			JLabel node2_Title3 = new JLabel("Temperature: ");
			JLabel node2_Title5 = new JLabel("Humidity: ");
			JLabel node2_Title6 = new JLabel("Voltage: ");

			node2.add(node2_Title1);
			node2.add(node2_Title2);
			node2.add(node2_Title3);
			node2.add(node2_Title4);
			node2.add(node2_Title5);
			node2.add(node2_Title6);

			node2_data1 = new JLabel("");
			node2_data2 = new JLabel("");
			node2_data3 = new JLabel("");
			node2_data4 = new JLabel("");
			node2_data5 = new JLabel("");
			node2_Data6 = new JLabel("");

			node2.add(node2_data1);
			node2.add(node2_data2);
			node2.add(node2_data3);
			node2.add(node2_data4);
			node2.add(node2_data5);
			node2.add(node2_Data6);

			//NODE 3
			JLabel node3_Title1 = new JLabel("Node ID: ");
			JLabel node3_Title2 = new JLabel("Packet #: ");
			JLabel node3_Title4 = new JLabel("Light: ");
			JLabel node3_Title3 = new JLabel("Temperature: ");
			JLabel node3_Title5 = new JLabel("Humidity: ");
			JLabel node3_Title6 = new JLabel("Voltage: ");

			node3.add(node3_Title1);
			node3.add(node3_Title2);
			node3.add(node3_Title3);
			node3.add(node3_Title4);
			node3.add(node3_Title5);
			node3.add(node3_Title6);

			node3_data1 = new JLabel("");
			node3_data2 = new JLabel("");
			node3_data3 = new JLabel("");
			node3_data4 = new JLabel("");
			node3_data5 = new JLabel("");
			node3_Data6 = new JLabel("");

			node3.add(node3_data1);
			node3.add(node3_data2);
			node3.add(node3_data3);
			node3.add(node3_data4);
			node3.add(node3_data5);
			node3.add(node3_Data6);

			//NODE 4
			JLabel node4_Title1 = new JLabel("Node ID: ");
			JLabel node4_Title2 = new JLabel("Packet #: ");
			JLabel node4_Title4 = new JLabel("Light: ");
			JLabel node4_Title3 = new JLabel("Temperature: ");
			JLabel node4_Title5 = new JLabel("Humidity: ");
			JLabel node4_Title6 = new JLabel("Voltage: ");

			node4.add(node4_Title1);
			node4.add(node4_Title2);
			node4.add(node4_Title3);
			node4.add(node4_Title4);
			node4.add(node4_Title5);
			node4.add(node4_Title6);

			node4_data1 = new JLabel("");
			node4_data2 = new JLabel("");
			node4_data3 = new JLabel("");
			node4_data4 = new JLabel("");
			node4_data5 = new JLabel("");
			node4_Data6 = new JLabel("");

			node4.add(node4_data1);
			node4.add(node4_data2);
			node4.add(node4_data3);
			node4.add(node4_data4);
			node4.add(node4_data5);
			node4.add(node4_Data6);

			//NODE 5
			JLabel node5_Title1 = new JLabel("Node ID: ");
			JLabel node5_Title2 = new JLabel("Packet #: ");
			JLabel node5_Title4 = new JLabel("Light: ");
			JLabel node5_Title3 = new JLabel("Temperature: ");
			JLabel node5_Title5 = new JLabel("Humidity: ");
			JLabel node5_Title6 = new JLabel("Voltage: ");

			node5.add(node5_Title1);
			node5.add(node5_Title2);
			node5.add(node5_Title3);
			node5.add(node5_Title4);
			node5.add(node5_Title5);
			node5.add(node5_Title6);

			node5_data1 = new JLabel("");
			node5_data2 = new JLabel("");
			node5_data3 = new JLabel("");
			node5_data4 = new JLabel("");
			node5_data5 = new JLabel("");
			node5_Data6 = new JLabel("");

			node5.add(node5_data1);
			node5.add(node5_data2);
			node5.add(node5_data3);
			node5.add(node5_data4);
			node5.add(node5_data5);
			node5.add(node5_Data6);

			add(node1);
			add(node2);
			add(node3);
			add(node4);
			add(node5);
		}		
	}

}
