package org.jschwarz.vlcHelper;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class vlcHelperRequestStreamHandler extends SpeechletRequestStreamHandler {

	private static final Set<String> supportedApplicationIds = new HashSet<String>();
	static {
		/*
		 * This Id can be found on https://developer.amazon.com/edw/home.html#/
		 * "Edit" the relevant Alexa Skill and put the relevant Application Ids
		 * in this Set.
		 */
		supportedApplicationIds
				.add("amzn1.ask.skill.e55ac3d4-e6f5-418b-add2-bcefa9e0f575");
	}

	public vlcHelperRequestStreamHandler() {
		super(new VLCHelper(), supportedApplicationIds);
	}

	public static void main(String[] args) {
		new vlcHelperRequestStreamHandler();
	}
}