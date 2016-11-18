package org.jschwarz.vlcHelper;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class vlcHelperRequestStreamHandler extends
		SpeechletRequestStreamHandler {

	private static final Set<String> supportedApplicationIds = new HashSet<String>();
	static {
		/*
		 * This Id can be found on https://developer.amazon.com/edw/home.html#/
		 * "Edit" the relevant Alexa Skill and put the relevant Application Ids
		 * in this Set.
		 */
		supportedApplicationIds
				.add("amzn1.ask.skill.e7dbd308-1ae6-4672-9359-f72cad6a6ac6");
	}

	public vlcHelperRequestStreamHandler() {
		super(new VLCHelper(), supportedApplicationIds);
	}

	public static void main(String[] args) {
		new vlcHelperRequestStreamHandler();
	}
}