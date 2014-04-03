package com.demo.xmppchatactivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

import com.demo.xmppchat.R;
import com.demo.xmppchat.R.id;
import com.demo.xmppchat.R.layout;
import com.demo.xmppchatactivity.XMPPChatDemoService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class XMPPChatDemoActivity extends Activity {

	private XMPPConnection connection;
	private ArrayList<String> messages = new ArrayList<String>();
	private Handler mHandler = new Handler();

	private EditText recipient;
	private EditText textMessage;
	private ListView listview;
	
	Intent intent;
	//MyResultReceiver resultReceiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		Log.d("XMPPChatDemoActivity", "XMPPChatDemoActivity Started.");
		
		//resultReceiver = new MyResultReceiver(null);
		intent = new Intent(this, XMPPChatDemoService.class);
		//intent.putExtra("receiver", resultReceiver);
		this.startService(intent);
		/*Log.i("dgjn","fsdfsd");
		recipient = (EditText) this.findViewById(R.id.toET);
		textMessage = (EditText) this.findViewById(R.id.chatET);
		listview = (ListView) this.findViewById(R.id.listMessages);
		setListAdapter();

		// Set a listener to send a chat text message
		Button send = (Button) this.findViewById(R.id.sendBtn);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String to = recipient.getText().toString();
				String text = textMessage.getText().toString();

				Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + to);
				Message msg = new Message(to, Message.Type.chat);
				msg.setBody(text);
				connection = (XMPPConnection) intent.getParcelableExtra("connection");
				Log.d("XMPPChatDemoActivity", "XMPPConnection: " + connection.toString());
				if (connection != null) {
					// Log.d("XMPPChatDemoActivity", "XMPPConnection: " + connection.toString());
					connection.sendPacket(msg);
					messages.add(connection.getUser() + ":");
					messages.add(text);
					setListAdapter();
				}
			}
		});*/
	}

	private void setListAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listitem, messages);
		listview.setAdapter(adapter);
	}
	
	  
		
		@Override
		 protected void onDestroy() {
		  super.onDestroy();
		  Log.d("HelloWorld", "Activity Stopped.");
		 }  
}

/*package com.demo.sendify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.json.JSONArray;

import com.demo.xmppchat.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class XMPPChatDemoActivity extends Activity {

    public static final String HOST = "162.242.243.182";
    public static final int PORT = 5222;
    public static final String SERVICE = "sendify";
    public static final String USERNAME = "yash";
    public static final String PASSWORD = "password";

    private XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private Context context;
    //private SmackAndroid smackAndroid;JS
    private EditText recipient;
    private EditText textMessage;
    private ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getBaseContext();
		// smackAndroid = SmackAndroid.init(context);

		recipient = (EditText) this.findViewById(R.id.toET);
		textMessage = (EditText) this.findViewById(R.id.chatET);
		listview = (ListView) this.findViewById(R.id.listMessages);
		setListAdapter();

		// Set a listener to send a chat text message
		Button send = (Button) this.findViewById(R.id.sendBtn);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String to = recipient.getText().toString();
				String text = textMessage.getText().toString();

				Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + to);
				Message msg = new Message(to, Message.Type.chat);
				msg.setBody(text);				
				if (connection != null) {
					connection.sendPacket(msg);
					messages.add(connection.getUser() + ":");
					messages.add(text);
					setListAdapter();
				}
			}
		});

		connect();
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
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
						messages.add(fromName + ":");
						messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								setListAdapter();
							}
						});
					}
				}
			}, filter);
		}
    }

    private void setListAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listitem, messages);
		listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
		super.onDestroy();
		try {
			if (connection != null)
				connection.disconnect();
		} catch (Exception e) {

		}
    }

    /*public void connect() {

		final ProgressDialog dialog = ProgressDialog.show(this,
				"Connecting...", "Please wait...", false);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create a connection
				
				SmackAndroid.init(context);
				//SmackAndroid.init(getBaseContext());
				AndroidConnectionConfiguration connConfig = new AndroidConnectionConfiguration(HOST, PORT, SERVICE);
				// ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
				XMPPConnection connection = new XMPPConnection(connConfig);

				try {
					connection.connect();
					Log.i("XMPPChatDemoActivity",
							"Connected to " + connection.getHost());
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to connect to "
							+ connection.getHost());
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}
				try{
				createAccount();
				} catch(Exception e){
					
				}
				try {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					connection.login(USERNAME, PASSWORD);
					Log.i("XMPPChatDemoActivity",
							"Logged in as " + connection.getUser());

					// Set the status to available
					Presence presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					setConnection(connection);

					Roster roster = connection.getRoster();
					Log.i("XMPPChatDemoActivity"," "+roster.getPresence("yash"));
					Collection<RosterEntry> entries = roster.getEntries();
					for (RosterEntry entry : entries) {
						Log.d("XMPPChatDemoActivity",
								"--------------------------------------");
						Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
						Log.d("XMPPChatDemoActivity",
								"User: " + entry.getUser());
						Log.d("XMPPChatDemoActivity",
								"Name: " + entry.getName());
						Log.d("XMPPChatDemoActivity",
								"Status: " + entry.getStatus());
						Log.d("XMPPChatDemoActivity",
								"Type: " + entry.getType());
						Presence entryPresence = roster.getPresence(entry
								.getUser());

						Log.d("XMPPChatDemoActivity", "Presence Status: "
								+ entryPresence.getStatus());
						Log.d("XMPPChatDemoActivity", "Presence Type: "
								+ entryPresence.getType());
						Presence.Type type = entryPresence.getType();
						if (type == Presence.Type.available)
							Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
						Log.d("XMPPChatDemoActivity", "Presence : "
								+ entryPresence);
					}
					
				//	 createAccount();
					 //createNode();
					// getNode();
					// publish();
					//publish();
					//subscribe();
					
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to log in as "
							+ USERNAME);
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}

				dialog.dismiss();
			}
		});
		t.start();
		dialog.show();
    }
    public void connect() {

		final ProgressDialog dialog = ProgressDialog.show(this,
				"Connecting...", "Please wait...", false);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create a connection
				
				SmackAndroid.init(context);
				//SmackAndroid.init(getBaseContext());
				AndroidConnectionConfiguration connConfig = new AndroidConnectionConfiguration(HOST, PORT, SERVICE);
				// ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
				XMPPConnection connection = new XMPPConnection(connConfig);

				try {
					connection.connect();
					Log.i("XMPPChatDemoActivity",
							"Connected to " + connection.getHost());
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to connect to "
							+ connection.getHost());
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}
				
			      // Get the discovered items of the queried XMPP entity
			      
				
				try {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					connection.login(USERNAME, PASSWORD);
					Log.i("XMPPChatDemoActivity",
							"Logged in as " + connection.getUser());

					// Set the status to available
					Presence presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					setConnection(connection);

					Roster roster = connection.getRoster();
					Collection<RosterEntry> entries = roster.getEntries();
					for (RosterEntry entry : entries) {
						Log.d("XMPPChatDemoActivity",
								"--------------------------------------");
						Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
						Log.d("XMPPChatDemoActivity",
								"User: " + entry.getUser());
						Log.d("XMPPChatDemoActivity",
								"Name: " + entry.getName());
						Log.d("XMPPChatDemoActivity",
								"Status: " + entry.getStatus());
						Log.d("XMPPChatDemoActivity",
								"Type: " + entry.getType());
						Presence entryPresence = roster.getPresence(entry
								.getUser());

						Log.d("XMPPChatDemoActivity", "Presence Status: "
								+ entryPresence.getStatus());
						Log.d("XMPPChatDemoActivity", "Presence Type: "
								+ entryPresence.getType());
						Presence.Type type = entryPresence.getType();
						if (type == Presence.Type.available)
							Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
						Log.d("XMPPChatDemoActivity", "Presence : "
								+ entryPresence);
					}
					
				//	 createAccount();
					 //createNode();
					// getNode();
					// publish();
					//publish();
					//subscribe();
					
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to log in as "
							+ USERNAME);
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}

				dialog.dismiss();
			}
		});
		t.start();
		dialog.show();
    }

    public void createAccount() {
		
		AccountManager accountManager=new AccountManager(connection);
		HashMap<String, String> attr = new HashMap<String, String>(); 
		attr.put("user", "pubsub"); 
		attr.put("password", "pubsub");
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Log.i("XMPPChatDemoActivity",telephonyManager.getDeviceId());
		
		try {
		    accountManager.createAccount(telephonyManager.getDeviceId(), null, attr);
		} catch (XMPPException ex) {
		    Log.d("XMPPChatActivityDemo", ex.toString());
		}
    }
    
    public void createNode() {
		
		PubSubManager pubSubManager = new PubSubManager(this.connection);
		
        ConfigureForm form = new ConfigureForm(FormType.submit);
        form.setAccessModel(AccessModel.open);
        form.setDeliverPayloads(true);
        form.setNotifyRetract(true);
        form.setPersistentItems(true);
        form.setPublishModel(PublishModel.open);

        LeafNode leafNode;
        
		try {
			leafNode = pubSubManager.createNode("TestNode1");
			leafNode.sendConfigurationForm(form);
			Log.d("XMPPChatActivityDemo", "Node created succesfully.");
		} catch (XMPPException ex) {
			// TODO Auto-generated catch block
			Log.d("XMPPChatActivityDemo", ex.toString());
		}
    }
    
    public void getNode() {
		
		// Create a pubsub manager using an existing Connection
        PubSubManager pubSubManager = new PubSubManager(this.connection);
		LeafNode node;
		
		try {
			// Get the node
			node = pubSubManager.getNode("TestNode1");
			System.out.println("Subscribed Nodes are : "+node.getSubscriptions());
			Log.d("XMPPChatActivityDemo", "Node retrieved succesfully."+node.getId()+node.discoverInfo());
		} catch (XMPPException ex) {
			// TODO Auto-generated catch block
			Log.d("XMPPChatActivityDemo", ex.toString());
		}
		
    }

    public void publish() {
		
		// Create a pubsub manager using an existing Connection
        PubSubManager pubSubManager = new PubSubManager(this.connection);
        
        SimplePayload payload = new SimplePayload(
                "elementname",
                "pubsub:testnode:elementname",
                "<elementname>my content</elementname>");
        
        PayloadItem<SimplePayload> item = new PayloadItem<SimplePayload>(
                 null, payload);        
        
        LeafNode node;
		
        try {
			// Get the node
			node = (LeafNode) pubSubManager.getNode("TestNode1");
			Log.d("XMPPChatActivityDemo", "Node retrieved succesfully.");
			// Publish an Item with payload
			node.publish(item);
			Log.d("XMPPChatActivityDemo", "Item published succesfully.");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Log.d("XMPPChatActivityDemo", ex.toString());
		}        
    }
    
    public void subscribe() {
		
		// Create a pubsub manager using an existing Connection
        PubSubManager pubSubManager = new PubSubManager(this.connection);

        // Get the node
        LeafNode node;
		try {
			node = pubSubManager.getNode("TestNode1");
			Log.d("XMPPChatActivityDemo", "Node retrieved succesfully.");
			node.addItemEventListener(new ItemEventCoordinator());
			Log.d("XMPPChatActivityDemo", "ItemEventListener added succesfully.");
			node.subscribe("yash@sendify");
			Log.d("XMPPChatActivityDemo", "Node subscribed succesfully.");
		} catch (XMPPException ex) {
			// TODO Auto-generated catch block
			Log.d("XMPPChatActivityDemo", ex.toString());
		}
    }
    
    class ItemEventCoordinator implements ItemEventListener<Item>
    {
		@Override
		public void handlePublishedItems(ItemPublishEvent items) {
			// TODO Auto-generated method stub
			JSONArray j;
			messages.add(items.getItems().toString());
			//System.out.println("Item: " + items.getItems().toString());
            //System.out.println(items);
			
		}
    }
    
    
}*/