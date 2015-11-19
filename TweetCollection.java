

import java.io.BufferedReader;
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
	Random rng = new Random();
	
	String[] joinWords = {
		" and ",
		" or ",
		" so ",
		" that ",
		" yet ",
		" however ",
		",",
		" but ",
		" & ",
		". ",
		"; ",
		": ",
		"?"
	};
	ArrayList<String> forbiddenWords = new ArrayList<String>();
	
	public void UpdateForbiddenWords()
	{
		
		String path="tweetData/forbiddenWords.txt";
		try {
			InputStreamReader in =  new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader br = new BufferedReader(in);
			String line;
			while((line = br.readLine()) != null)
			{
				if (!forbiddenWords.contains(line))
				{
					forbiddenWords.add(line);
				}
			}
			br.close();

		} catch( Exception e)
		{
			System.out.println("Unable to load forbiddenWords.txt");
		}
	}

	public void LoadTweets()
	{
		UpdateForbiddenWords();
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
				boolean tweetContainsForbiddenWord = false;
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
							for (String forbiddenWord:forbiddenWords)
							{
								if (tweetText.contains(forbiddenWord))
								{
									System.out.println("Tweet contained " +forbiddenWord+"; discarded.");
									tweetContainsForbiddenWord = true;
								}
							}
							
							if ((tweetText.length() > 0 && !tweetContainsForbiddenWord) && (((!tweetIsRT) || (includeRTs)) && ((!tweetIsReply || includeReplies))))
							{
								
								tweetText = tweetText.substring(0, tweetText.length()-1);
								System.out.println("adding "+tweetText);
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
		
		//System.out.println("Remix: "+Remix());

	}
	
	
	public String Remix()
	{
		String tweet0 = null;
		String tweet1 = null;
		
		String joinWordDef ="";
		
		
		while (tweet0 == null)
		{
			int nextInt = rng.nextInt(tweetsCSV.size());
			//System.out.println("firstPartIndex: "+nextInt+" of "+ tweetsCSV.size());
			String tentative = tweetsCSV.get(nextInt);
			
			ArrayList<String> tmpJoinWords = new ArrayList<String>();
			
			// Copy array
			for(int i = 0; i < joinWords.length; i++)
			{
				tmpJoinWords.add(joinWords[i]);
			}
			
			int joinPlace = -1;
			do{
				nextInt = rng.nextInt(tmpJoinWords.size());
				//System.out.println("joinWordIndex: " + nextInt);
				String joinWord = tmpJoinWords.get(nextInt);
				tmpJoinWords.remove(nextInt);
			
				joinPlace = tentative.indexOf(joinWord); 
				if (joinPlace > -1)
				{
				
					tweet0 = tentative.substring(0, joinPlace);
					joinWordDef = joinWord;
				} else
				{
					//System.out.println("No joining place; redo [0]");
				}
			} while(tmpJoinWords.size() > 0 && joinPlace == -1);
		}
		while (tweet1 == null)
		{
			int nextInt = rng.nextInt(tweetsCSV.size());
			//System.out.println("secondPartIndex: "+nextInt);
                        String tentative = tweetsCSV.get(nextInt);
			



			ArrayList<String> tmpJoinWords = new ArrayList<String>();

                        // Copy array
                        for(int i = 0; i < joinWords.length; i++)
                        {
                                tmpJoinWords.add(joinWords[i]);
                        }

                        int joinPlace = -1;
                        do{
                                nextInt = rng.nextInt(tmpJoinWords.size());
                                //System.out.println("joinWordIndex: " + nextInt);
                                String joinWord = tmpJoinWords.get(nextInt);
				tmpJoinWords.remove(nextInt);

				joinPlace = tentative.indexOf(joinWord);
			
				if (joinPlace > -1)
				{
					joinPlace += joinWord.length();
					tentative = tentative.substring(joinPlace);
					tweet1 = tentative;
				} else
                	        {
        	                        //System.out.println("No joining place; redo [1]");
	                        }

			 } while( tmpJoinWords.size() > 0 && joinPlace == -1);
		} 
		String defTweet = tweet0 + joinWordDef + tweet1;
		
		defTweet = defTweet.trim();

		defTweet = defTweet.replace("  ", " ");
		defTweet = defTweet.replace("&amp;", "&");
		defTweet = defTweet.replace("&gt;", ">");
		defTweet = defTweet.replace("&lt;", "<");

		int shorteningTries = 0;
		while (defTweet.length() > 140 && shorteningTries < 10)
		{
			int whichStrategy = 0; // rnf.nextInt();
			
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
