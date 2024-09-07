<p align="center">
<img align="center" width="432" height="960" src="https://raw.githubusercontent.com/AM71113363/logger/master/info.png">
</p>
</p>

## About<br>
I use Android Studio only for the GUI part,
but for debug I compile it using JAVA Compilers for Android,because its more easy to test Sensors,NFC,etc.. on my phone.
The Compilers I use are not so efficient in finding errors.<br>
SOOOOOOOOOOOooo I made this to help me.
The truth is that I was tired using "Toast(^_^).show()".<br>

## How to use it<br>
●install the apk or re-build a new one and start the Server.<br>
•the default port is 19132<br>
●import "LOGGER.java" in your project.<br>
● then in "onCreate" call->LOGGER.start(this);<br>
●anywhere in you code use LOGGER.SendMessage("String to Send"); to send it to the Server.<br>
••in the photo above I used LOGGER.SendMessage("time="+System.currentTimeMillis());<br>
●call LOGGER.close() in onDestroy/onPause<br>

## Build.
●the source of Apk is MainActivity only<br>


# NOTE.
it will pick up minecraft discovery packets.I did it on purpose😅
