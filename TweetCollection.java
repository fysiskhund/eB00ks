

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class TweetCollection{
	ArrayList<String> tweetsCSV = new ArrayList<String>();

	boolean includeReplies = false;
	boolean includeRTs = false;
	Random r = new Random();
	
	String[] joinWords = {
		" and ",
		" or ",
		" so ",
		" that ",
		",",
		" but "
	};
	

	public void LoadTweets()
	{
		String path="tweetData/tweets.csv";

		try {

			InputStreamReader in = new InputStreamReader(new FileInputStream(path), "UTF-8");


			boolean inComment = false;
			boolean fieldHasData = false;



			String tweetText = "";
			String currentFieldText = "";
			String lineSoFar = "";



			
			
				boolean tweetIsRT = false;
				boolean tweetIsReply = false;
				int currentField = 0;



				int r;
				while((r = in.read()) != -1)
				{

					char c = (char)r;
					if (c == '"')
					{
						if ( inComment && currentField == 5)
						{
							tweetText += c;
						}

						inComment = !inComment;
					} else if (((c == ',')|| (c== '\n')) && (!inComment))
					{
						// switching currentField.

						switch(currentField)
						{
						case 2:
							tweetIsReply = (fieldHasData);

							break;

						case 6:
							tweetIsRT = fieldHasData;
							
							if (tweetText.length() > 0 && (((!tweetIsRT) || (includeRTs)) && ((!tweetIsReply || includeReplies))))
							{
								
								tweetText = tweetText.substring(0, tweetText.length()-1);
								//System.out.println("adding "+tweetText);
								tweetsCSV.add(tweetText);
								
								tweetText = "";
							}
							
							break;
						case 9:
							currentField = -1;
							tweetText = "";
							lineSoFar = "";
							break;

						default:
							break;
						}

						currentField++;
						currentFieldText = "";
						fieldHasData = false;
					} else
					{
						currentFieldText += c;
						lineSoFar += c;
						if (currentField == 5)
							tweetText += c;
						fieldHasData = true;
					}
				}
				

		}catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		System.out.println("Remix: "+Remix());

	}
	
	
	public String Remix()
	{
		String tweet0 = null;
		String tweet1 = null;
		
		String joinWordDef ="";
		
		
		while (tweet0 == null)
		{
			String tentative = tweetsCSV.get(r.nextInt(tweetsCSV.size()));
			
			String joinWord = joinWords[r.nextInt(joinWords.length)];
			
			int joinPlace = tentative.indexOf(joinWord); 
			if (joinPlace > -1)
			{
				
				tweet0 = tentative.substring(0, joinPlace);
				joinWordDef = joinWord;
			}
		}
		while (tweet1 == null)
		{
			String tentative = tweetsCSV.get(r.nextInt(tweetsCSV.size()));
			
			String joinWord = joinWords[r.nextInt(joinWords.length)];
			
			
			int joinPlace = tentative.indexOf(joinWord);
			
			if (joinPlace > -1)
			{
				joinPlace += joinWord.length();
				tentative = tentative.substring(joinPlace);
				tweet1 = tentative;
			}
		}
		String defTweet = tweet0 + joinWordDef + tweet1;
		
		int shorteningTries = 0;
		while (defTweet.length() > 140 && shorteningTries < 10)
		{
			int whichStrategy = 0; // r.nextInt();
			
			switch(whichStrategy)
			{
			case 0:
			{
				// Find puntuation; cut there
				int cutHere = defTweet.lastIndexOf('.');
				if (cutHere < -1)
				{
					cutHere = defTweet.lastIndexOf(';');
				}
				
				if (cutHere > -1)
				{
					defTweet.substring(0, cutHere);
				}
				
			}
				break;
			}
			
			shorteningTries++;
		}
		return defTweet;
	}



}