import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStreamReader;


public class PushTweet
{
	static String consumerKey;
	static String consumerSecret;
	static String accessToken;
	static String accessTokenSecret;
	

	public static void main(String[] args)
	{
		Twitter tw;

		int counter = 0;
		
		ReadTokens();

		tw = new TwitterFactory().getInstance();
		tw.setOAuthConsumer(consumerKey, consumerSecret);
		tw.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

		 // get request token.
                // this will throw IllegalStateException if access token is already available

AccessToken accessToken = null;
                
if (false) {
		try {
		RequestToken requestToken = tw.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                //AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    //try {
                        if (pin.length() > 0) {
                            accessToken = tw.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = tw.getOAuthAccessToken(requestToken);
                        }
                    /*} catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }*/
                }
		}catch (Exception e)
		{
//
		}
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
		for(String s:args){
			System.out.println("["+counter+"] "+s);
			counter++;
		}
	} else {
		try {
			if (args.length == 0)
			{
				BufferedReader br = 
                      			new BufferedReader(new InputStreamReader(System.in));
				String input = "";
				String inputln;
 
				while((inputln=br.readLine())!=null){
					input += inputln+"\n";
					//System.out.println(input);
				}
				//System.out.println(input);
				tw.updateStatus(input);
			} else if (args.length == 1)
			{
			
				tw.updateStatus(args[0]);
			} else
			{
				System.out.println("usage: pushtweet \"status message\"");
			}
		} catch (Exception e)
		{
			System.out.println("error when updating");
		}

	}
	}
	static void ReadTokens()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("accessTokens.txt"));
			consumerKey = in.readLine();
			consumerSecret = in.readLine();
			accessToken = in.readLine();
			accessTokenSecret = in.readLine();
		} catch (Exception e)
		{
			System.out.println("Error while reading tokens..");
		}
	}
}