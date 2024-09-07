package put.your.package.here;

import android.widget.*;
import java.net.*;
import android.content.*;
import java.nio.charset.*;

public abstract class LOGGER
{
    final static int PORT= 19132;
    static InetAddress broadcast=null;
    static DatagramSocket dgram=null;
    static Context ctx=null;
	
     public static void start(Context c)
     {
	ctx=c;
	new Thread(new Runnable()
	{
	    public void run()
	    {
	        CreateSocket();
	    }
	}).start();
     }
     static void CreateSocket()
     {
	try
	{
	    dgram = new DatagramSocket(PORT);
	    broadcast = InetAddress.getByName("255.255.255.255");
	    dgram.setReuseAddress(true);
	    dgram.setSoTimeout(4000);
	    dgram.connect( broadcast,PORT);
	}catch(Throwable e)
	{ 
	    Toast.makeText(ctx,e.toString(),1).show();
	    if(dgram==null)
		return;
	    try
	    {
		if(!dgram.isClosed())
			dgram.close();
	    }catch(Throwable ee){}
	    dgram=null
	}
     }
     static void SendMessage(final String s)
     {
	if( dgram==null || broadcast==null)
		return;
	new Thread(new Runnable()
	{
	    public void run()
	    {
		try
		{
		byte[] b=s.getBytes(StandardCharsets.UTF_8);
		DatagramPacket log=new DatagramPacket(b,b.length,broadcast,PORT);
        dgram.send(log);
                }catch(Throwable e){}
	    }
	 }).start();
	}

	static void close()
	{
	    if(dgram== null)
		return;
	    new Thread(new Runnable()
	    {
		public void run()
		{
		    try
		    {
			if(!dgram.isClosed())
			   dgram.close();
		   }catch(Throwable e){}
		   dgram=null;
		}
	    }).start();
	}
}

