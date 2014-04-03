package com.demo.xmppchatactivity;

import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class XMPPChatDemoService extends Service {

	public static final String HOST = "162.242.243.182";
	public static final int PORT = 5222;
	public static final String SERVICE = "localhost";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "password";
	public static Context con;
	private XMPPConnection connection;
	ResultReceiver resultReceiver;
	private Intent intent;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Let it continue running until it is stopped.
		//resultReceiver = intent.getParcelableExtra("receiver");
		//Log.d("XMPPChatDemoService", "resultReceiver: " + resultReceiver.toString());
		this.intent = intent;
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		Log.d("MyService", "Service Started.");
		// resultReceiver = intent.getParcelableExtra("receiver");
		connect();
	    return START_STICKY;
	}
	
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
		
	    if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message .getFrom());
						Log.i("MyService", "Text Recieved " + message.getBody() + " from " + fromName );
						// intent.putExtra("Text Received", message.getBody() + " from " + fromName);
					}
				}
			}, filter);
		}
		
	}
	
	public void connect() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create a connection
				SmackAndroid.init(getApplicationContext());
				Log.d("XMPPChatActivityDemo", "Init successful.");
				AndroidConnectionConfiguration connConfig = new AndroidConnectionConfiguration(HOST, PORT, SERVICE);
				 //ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
				XMPPConnection connection = new XMPPConnection(connConfig);

				try {
					connection.connect();
					Log.i("MyService", "Connected to " + connection.getHost());
				} catch (XMPPException ex) {
					Log.e("MyService", "Failed to connect to " + connection.getHost());
					Log.e("MyService", ex.toString());
					setConnection(null);
				}
				try {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					connection.login(USERNAME, PASSWORD);
					Log.i("MyService",
							"Logged in as " + connection.getUser());

					// Set the status to available
					Presence presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					setConnection(connection);

					Roster roster = connection.getRoster();
					Collection<RosterEntry> entries = roster.getEntries();
					for (RosterEntry entry : entries) {
						Log.d("MyService", "--------------------------------------");
						Log.d("MyService", "RosterEntry " + entry);
						Log.d("MyService", "User: " + entry.getUser());
						Log.d("MyService", "Name: " + entry.getName());
						Log.d("MyService", "Status: " + entry.getStatus());
						Log.d("MyService", "Type: " + entry.getType());
						
						Presence entryPresence = roster.getPresence(entry.getUser());

						Log.d("MyService", "Presence Status: " + entryPresence.getStatus());
						Log.d("MyService", "Presence Type: " + entryPresence.getType());
						
						Presence.Type type = entryPresence.getType();
						
						if (type == Presence.Type.available) {
							Log.d("MyService", "Presence AVIALABLE");
						}
						
						Log.d("MyService", "Presence : " + entryPresence);
					}
					
				} catch (XMPPException ex) {
					Log.e("MyService", "Failed to log in as " + USERNAME);
					Log.e("MyService", ex.toString());
					setConnection(null);
				}
			}
		});
		
		t.start();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onTaskRemoved(Intent rootIntent) {
	    Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
	    Intent restartServiceIntent = new Intent(getApplicationContext(),
	            this.getClass());
	    restartServiceIntent.setPackage(getPackageName());

	    PendingIntent restartServicePendingIntent = PendingIntent.getService(
	            getApplicationContext(), 1, restartServiceIntent,
	            PendingIntent.FLAG_ONE_SHOT);
	    AlarmManager alarmService = (AlarmManager) getApplicationContext()
	            .getSystemService(Context.ALARM_SERVICE);
	    alarmService.set(AlarmManager.ELAPSED_REALTIME,
	            SystemClock.elapsedRealtime() + 1000,
	            restartServicePendingIntent);

	    super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onDestroy() {
		/*super.onDestroy();
		try {
			if (connection != null)
				connection.disconnect();
		} catch (Exception e) {}
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		Log.d("MyService", "Service Stopped.");*/
		Log.d("MyService", "Service Stopped.");
		sendBroadcast(new Intent("StartKill"));
	}	
}
//package com.demo.xmppchat;
//
//import java.util.Collection;
//import java.util.HashMap;
//
//import org.jivesoftware.smack.AccountManager;
//import org.jivesoftware.smack.AndroidConnectionConfiguration;
//import org.jivesoftware.smack.PacketListener;
//import org.jivesoftware.smack.Roster;
//import org.jivesoftware.smack.RosterEntry;
//import org.jivesoftware.smack.SmackAndroid;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.filter.MessageTypeFilter;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Packet;
//import org.jivesoftware.smack.packet.Presence;
//import org.jivesoftware.smack.util.StringUtils;
//
//import android.annotation.SuppressLint;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ServiceInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.ResultReceiver;
//import android.os.SystemClock;
//import android.util.Log;
//import android.widget.Toast;
//
//public class XMPPChatDemoService extends Service {
//
//	public static final String USERNAME = "admin";
//	public static final String PASSWORD = "password";
//	private SendifyManager sendifyManager;
//	private SendifyConnectionManager sendifyConnectionManager;
//	private Intent intent;
//	
//	
//
//	
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		
//		return null;
//	}
//	
//	
//	
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// Let it continue running until it is stopped.
//		//resultReceiver = intent.getParcelableExtra("receiver");
//		//Log.d("XMPPChatDemoService", "resultReceiver: " + resultReceiver.toString());
//		this.intent = intent;
//		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//		Log.d("MyService", "Service Started.");
//		// resultReceiver = intent.getParcelableExtra("receiver");
//		Thread t= new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				sendifyManager = new SendifyManager();
//				sendifyConnectionManager = new SendifyConnectionManager(sendifyManager, getApplicationContext());
//				sendifyConnectionManager.SendifyConnect();
//				sendifyConnectionManager.SendifyLogin(USERNAME, PASSWORD);
//				sendifyConnectionManager.setStatus(Presence.Type.available);
//			}			
//		});
//		t.start();
//		return START_REDELIVER_INTENT;
//	}
//	
//	/*public void setConnection(XMPPConnection connection) {		
//	    if (connection != null) {
//			// Add a packet listener to get messages sent to us
//			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//			connection.addPacketListener(new PacketListener() {
//				@Override
//				public void processPacket(Packet packet) {
//					Message message = (Message) packet;
//					if (message.getBody() != null) {
//						String fromName = StringUtils.parseBareAddress(message .getFrom());
//						Log.i("MyService", "Text Recieved " + message.getBody() + " from " + fromName );
//						// intent.putExtra("Text Received", message.getBody() + " from " + fromName);
//					}
//				}
//			}, filter);
//		}
//		
//	}*/
//			
//	@SuppressLint("NewApi")
//	@Override
//	public void onTaskRemoved(Intent rootIntent) {
//	    Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
//	    Intent restartServiceIntent = new Intent(getApplicationContext(),
//	            this.getClass());
//	    restartServiceIntent.setPackage(getPackageName());
//
//	    PendingIntent restartServicePendingIntent = PendingIntent.getService(
//	            getApplicationContext(), 1, restartServiceIntent,
//	            PendingIntent.FLAG_ONE_SHOT);
//	    AlarmManager alarmService = (AlarmManager) getApplicationContext()
//	            .getSystemService(Context.ALARM_SERVICE);
//	    alarmService.set(AlarmManager.ELAPSED_REALTIME,
//	            SystemClock.elapsedRealtime() + 1000,
//	            restartServicePendingIntent);
//
//	    super.onTaskRemoved(rootIntent);
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		try {
//			sendifyConnectionManager.closeConnection();
//		} catch (Exception e) {}
//		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//		Log.d("MyService", "Service Stopped.");
//	}	
//}