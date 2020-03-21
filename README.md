# Feedify SDK (0.0.1)

Feedify SDK is Android API framework built top on [Feedify API](https://feedify.net). 
Implement your feedify notifications in your mobile apps rapidly. 

## Installation

Installation is very straightforward, you need to add app in your firebase console. You can follow
this guide [Firebase Setup](https://firebase.google.com/docs/android/setup). Skip apply google-services plugin,
this should be handled by Feedify SDK itself. Just download google-services.json.

Now copy this google-services.json into your root of app assets directory this is important step. 
Note this location is different than mentioned in official document, since it's handled by SDK not by plugin. 

On your project level gradle make sure to add classpath
    
    classpath 'com.google.gms:google-services:4.2.0'    
    
Ignore this line in your app level gradle. (do not add this line)

    apply plugin: 'com.google.gms.google-services'
    
## Dependencies
Add following dependencies in your app gradle.
    
    implementation 'com.github.feedify:feed-notify:0.0.1'
    
    //support dependencies (required by SDK)
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation "androidx.preference:preference:1.0.0"
    implementation 'com.google.firebase:firebase-messaging:20.0.0'
    implementation 'com.android.volley:volley:1.1.1'
     
## Credentials settings
Login to your feedify console and set your firebase cloud-messaging 
API Key and project-id under:

   Setting > Setting > Push Sending Settings
   
Finally add feedify credentials to your app under strings.xml as follow:
   
    <string name="feedify_user">YOR_USER</string>
    <string name="feedify_dkey">YOUR_DKEY</string>
    <string name="feedify_domain">YOUR_DOMAIN</string>
    
these strings will automatically detect and used by Feedify SDK. 
In case not mentioned you'll get exception. 
Make sure to change your credentials in string value.

That's all done!.
You can test push notification from your feedify console.


## Advanced user

You can extend application class as follow and change notification icon and on click event.
In case event not defined it will open notification url in browser. Don't forgot to specify
application class in your app AndroidManifest.xml.

      public class MyApplication extends FeedSDK {
          @Override
          public void onCreate() {
              super.onCreate();
              //set notification small icon (optional)
              setNotificationIcon(R.drawable.ic_stat_notifications);
              //this activity will be open when clicked on notification (optional)
              setStartActivity(MyActivity.class);
          }
      }
  
      
In case you need to handle other firebase notification you can extend 'FirebaseMessagingService' as follow:


      public class CustomMessageHandler extends FeedMessagingService {
      
          //Note all 'FirebaseMessagingService' feature is available since 'FeedMessagingService' extended by 'FeedMessagingService'        
          
          @Override
          public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
              super.onMessageReceived(remoteMessage);
          }
      }
      
In your app AndroidManifest.xml:

      <service android:name=".CustomMessageHandler">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
      </service>
      
Additionally you can turn off/on notification as follow:

      // turn off notification
      FeedSDK.setEnabled(false);
      
      //check status 
      boolean isEnabled = FeedSDK.isEnabled();
      
      
## Read more about Feedify

[Feedify](https://feedify.net)
      