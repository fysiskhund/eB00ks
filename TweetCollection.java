

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TweetCollection{
	ArrayList<String> tweetsCSV = new ArrayList<String>();

	boolean includeReplies = false;
	boolean includeRTs = false;

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

	}



}