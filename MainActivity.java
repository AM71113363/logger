package sss.am71113363.logger;

import android.app.Activity;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import java.net.*;
import java.io.*;
import android.os.*;
import android.graphics.*;

public class MainActivity extends Activity
{
    private Button startServer;
    static LinearLayout container;
	
    private static float dpi=2.625f;
	
    private int dpi2px(float px)
    {
	Float calc=Float.valueOf(Math.round(dpi*px));
      return calc.intValue();
    }
  
    private Handler hnd=new Handler()
    {
	public void handleMessage(Message msg)
	{
	   Bundle b=msg.getData();
	   if(b!=null)
	   {
	       Object o= b.get("LOG");
	       if(o!=null)
		  addElement(o.toString());
	       //addElement((String)o);
	   }
	}
    };
	
    private byte[] rcv=new byte[512];
    DatagramSocket dgram=null;
    private DatagramSocket begin()
    {
	try
	{
	    dgram = new DatagramSocket(19132);
	    dgram.setReuseAddress(true);
	}catch(Throwable e)
	{ 
	    startServer.setText("Start Server");
	    addElement(e.toString());
	  return null;
	}
      return dgram;
    }
    private void listen()
    {
	final DatagramSocket udp=begin();
	if(udp==null)
	    return;
	startServer.setText("Stop");
        while(true)
	{
           try
	   {
	        DatagramPacket pack=new DatagramPacket(rcv,rcv.length);
                udp.receive(pack);
		if(pack.getLength()>0)
		{
		    String str=new String(rcv,0,pack.getLength());
		    Message ms=new Message();
		    Bundle data=new Bundle();
		    data.putString("LOG",str);
		    ms.setData(data);
		    hnd.sendMessage(ms);
		}
	    }catch(IOException e){break;}
	 }
	 startServer.setText("Start Server");
         if(udp!=null)
	 {
	     try{ udp.close();}catch(Throwable e){}
	     dgram=null;
	 }
     }
     public void start()
     {
	new Thread(new Runnable(){public void run(){listen();}}).start();
     }
     private void CloseSocket()
     {
	if(dgram==null)
	   return;
        try
	{
	   if(dgram!=null)  //I know,but its a thread,who knows what could happen,
	     dgram.close(); //maybe I should use synchronized(this)
	   }catch(Throwable e){}
	   dgram=null;
     }
     public void close()
     {
	new Thread(new Runnable(){public void run(){CloseSocket();}}).start();
     }
	
	
	public void addElement(String msg)
	{
		FrameLayout f=new FrameLayout(this);
		FrameLayout.LayoutParams txtPrm=new FrameLayout.LayoutParams(-1,-2);
		txtPrm.bottomMargin=dpi2px(20);
		txtPrm.leftMargin=dpi2px(2);
		txtPrm.rightMargin=dpi2px(2);
		LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,-2);
		TextView txt=new TextView(this);
		txt.setTextSize((float)dpi2px(10));
		txt.setText(msg);
		f.addView(txt,txtPrm);
		container.addView(f,fp);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dpi=this.getResources().getDisplayMetrics().density;


		FrameLayout frm=new FrameLayout(this);
		FrameLayout.LayoutParams startServerPrm=new FrameLayout.LayoutParams(0,0);
		startServerPrm.width=-1;
		startServerPrm.height=dpi2px(50);
		startServerPrm.gravity=Gravity.CENTER_HORIZONTAL|Gravity.TOP;
		startServerPrm.leftMargin=dpi2px(10);
		startServerPrm.rightMargin=dpi2px(10);
		startServerPrm.topMargin=dpi2px(8);
	    startServer=new Button(this);
		startServer.setText("Start Server");
		frm.addView(startServer,startServerPrm);
		
		
		ScrollView hScroll=new ScrollView(this);
		FrameLayout.LayoutParams scrollParam=new FrameLayout.LayoutParams(-1,-1);
		scrollParam.topMargin=dpi2px(64.761f);
		scrollParam.bottomMargin=dpi2px(60);
		scrollParam.leftMargin=dpi2px(2);
		scrollParam.rightMargin=dpi2px(2);
		hScroll.setBackgroundColor(Color.TRANSPARENT);
		container=new LinearLayout(this);
		container.setOrientation(LinearLayout.VERTICAL);
		hScroll.addView(container);
	
		hScroll.setVerticalScrollBarEnabled(true);
		frm.addView(hScroll,scrollParam);
		
		Button clean=new Button(this);
		FrameLayout.LayoutParams cleanPrm=new FrameLayout.LayoutParams(0,0);
		cleanPrm.width=-1;
		cleanPrm.height=dpi2px(50);
		cleanPrm.gravity=Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
		cleanPrm.leftMargin=dpi2px(10);
		cleanPrm.rightMargin=dpi2px(10);
		cleanPrm.bottomMargin=dpi2px(2);
		clean.setText("Clear Messages");
		frm.addView(clean,cleanPrm);
		setContentView(frm);
		
		startServer.setOnClickListener(new OnClickListener()
		{
                   @Override
		   public void onClick(View v)
		   {
			if(dgram==null)
			   start();
			else
			  close();
		   }
		});
		clean.setOnClickListener(new OnClickListener()
		{
                    @Override
	            public void onClick(View v) {
			container.removeAllViews();
		    }
		});
	   }

	@Override
	protected void onDestroy()
	{
		close();
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		close();//no run in background
		super.onPause();
	}
}
