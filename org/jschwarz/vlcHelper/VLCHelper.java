/**
 * 
 */
package org.jschwarz.vlcHelper;

import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

/**
 * @author jschwarz
 * 
 */
public class VLCHelper implements Speechlet {

	private static final Logger log = LoggerFactory.getLogger(VLCHelper.class);

	private static String[] RESULT_TEXT_POSITIVE = { "OK.", "Geht los.",
			"Selbstverständlich.", "Bitte." };
	private static String[] RESULT_TEXT_NEGATIVE = { "Ups.",
			"Ein Fehler ist aufgetreten." };

	private final String HOST = "jschwarz.asuscomm.com";
	private final int PORT = 6969;
	private final String NC_PASSWORD = "5Jd0ksd43mp";

	private static final String DEVICE = "21";

	private final String[] CMD_PLAY = { "starten", "weiter", "los",
			"los gehts", "los geht es", "play" };
	private final String[] CMD_PAUSE = { "stop", "pause", "anhalten" };
	private final String[] CMD_FORWARD = { "vor", "vorwärts" };
	private final String[] CMD_BACKWARD = { "zurück", "wiederholen" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amazon.speech.speechlet.Speechlet#onSessionStarted(com.amazon.speech
	 * .speechlet.SessionStartedRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session)
			throws SpeechletException {
		BasicConfigurator.configure();
		org.apache.log4j.Logger.getRootLogger().setLevel(
				org.apache.log4j.Level.INFO);
		log.info("onSessionStarted requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session)
			throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		return null;// getExeceuteCommandResponse();
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session)
			throws SpeechletException {

		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = intent.getName();
		String command = intent.getSlot("Command").getValue();

		if ("ExecuteCommandIntent".equals(intentName)) {
			return getExeceuteCommandResponse(command);
		} else {
			throw new SpeechletException("unbekannter intent");
		}
	}

	private SpeechletResponse getExeceuteCommandResponse(String cmd) {

		String msgID = getMsgIDFromCommand(cmd.toLowerCase());

		boolean result = sendCommandMessage(msgID);
		SpeechletResponse speechletResponse = buildResponse(result);
		speechletResponse.setShouldEndSession(true);
		return speechletResponse;
	}

	private String getMsgIDFromCommand(String cmd) {
		for (String s : CMD_PLAY) {
			if (s.equals(cmd)) {
				return "1";
			}
		}
		for (String s : CMD_PAUSE) {
			if (s.equals(cmd)) {
				return "2";
			}
		}
		for (String s : CMD_FORWARD) {
			if (s.equals(cmd)) {
				return "3";
			}
		}
		for (String s : CMD_BACKWARD) {
			if (s.equals(cmd)) {
				return "4";
			}
		}
		return "-1";
	}

	private boolean sendCommandMessage(String cmd) {
		try {
			Socket clientSocket = new Socket(HOST, PORT);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.writeBytes("alexacmd;" + NC_PASSWORD + ";" + DEVICE
					+ ";" + cmd + '\n');
			clientSocket.close();
		} catch (Exception e) {

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amazon.speech.speechlet.Speechlet#onSessionEnded(com.amazon.speech
	 * .speechlet.SessionEndedRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session)
			throws SpeechletException {

		log.info("onSessionEnded requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());

	}

	private SpeechletResponse buildResponse(boolean result) {

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Ergebnis");
		card.setContent(result ? getResultText(RESULT_TEXT_POSITIVE)
				: getResultText(RESULT_TEXT_NEGATIVE));

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(result ? getResultText(RESULT_TEXT_POSITIVE)
				: getResultText(RESULT_TEXT_NEGATIVE));

		return SpeechletResponse.newTellResponse(speech, card);
	}

	private String getResultText(String[] stringArray) {
		return stringArray[(int) ((Math.random() * 100 % stringArray.length))];
	}

	public static void main(String[] args) {

	}
}