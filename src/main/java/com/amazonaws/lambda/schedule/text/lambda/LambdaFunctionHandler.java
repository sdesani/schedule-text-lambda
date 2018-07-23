package com.amazonaws.lambda.schedule.text.lambda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.apache.commons.lang3.text.WordUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Class to handle the request and execute the lambda function
 * 
 * @author Santosh Desani
 *
 */
public class LambdaFunctionHandler implements RequestHandler<RequestDTO, String> {

	private static final String TSID = ""; // Your Twilio SID
	private static final String TAUTHTOKEN = ""; // Your Twilio Auth Token
	private static final String FROM_NUMBER = ""; // Twilio generated number for sending Messages. Example format =
													// +11234567890
	private static final int TWILIO_TEXT_LIMIT = 1500;

	private final List<String> messageIds = new ArrayList<String>();

	@Override
	public String handleRequest(RequestDTO input, Context context) {
		context.getLogger().log("Input: " + input);

		Twilio.init(TSID, TAUTHTOKEN);

		String name = input.getName();
		String toNumber = input.getToNumber();

		String message = name + ", " + getMessage();

		sendMessage(toNumber, FROM_NUMBER, message);

		StringBuilder sb = new StringBuilder();
		sb.append(" ");

		// If the message is large than 1500 characters, Twilio wont be able to send it.
		// In that case we need to create separate text messages and send them
		// individually.
		// In order to keep track of all the chunks of text we need hold on to all the
		// messageIds, hence its a list

		for (String messageId : messageIds) {
			sb.append(messageId + "  ");
		}

		messageIds.clear();

		return "Success and the MessageId are- " + sb.toString();
	}

	/**
	 * Method to get random message from the available list
	 * 
	 * @return Random Message as String
	 */
	private String getMessage() {
		Random random = new Random();
		List<String> msgList = getList();
		return msgList.get(random.nextInt(msgList.size()));
	}

	/**
	 * Method to send message(s) using Twilio API
	 * 
	 * @param toNumber
	 *            - Number to which we want to send the message
	 * @param fromNumber
	 *            - Twilio generated number
	 * @param text
	 *            - Actual Message
	 */
	private void sendMessage(String toNumber, String fromNumber, String text) {

		Queue<String> msgQueue = splitIntoChunks(text);
		for (String textFromQueue : msgQueue) {
			Message message = Message.creator(new PhoneNumber(toNumber), // To number
					new PhoneNumber(fromNumber), // From number
					textFromQueue// SMS body
			).create();
			messageIds.add(message.getSid());
		}
	}

	/**
	 * Method to split the string into Twilio text limit ~1500
	 * 
	 * @param text
	 *            - Actual Message
	 * @return Queue of messages which are split by the size limit
	 */
	public Queue<String> splitIntoChunks(String text) {

		Queue<String> msgQueue = new LinkedList<String>();
		String wrappedText = WordUtils.wrap(text, TWILIO_TEXT_LIMIT, "*", true);
		for (String textWithLimit : wrappedText.split("\\*")) {
			msgQueue.add(textWithLimit);
		}
		return msgQueue;
	}

	/**
	 * This method just creates and returns list of messages. The example below
	 * holds messages related to belated birthday wishes but it can be any other
	 * type of messages.
	 * 
	 * @return List of messages
	 */
	private List<String> getList() {
		List<String> list = new ArrayList<String>();
		list.add("I was hundreds of miles away on your special day. I hope you enjoyed your day with a big cake.");
		list.add("Though I’m little late, but yet I’ve a chance to wish you a very happy late birthday.");
		list.add(
				"There is no hurry to wish you; I am your one true friend anyway. Happy birthday, Buddy. Hope you partied hard.");
		list.add(
				"In my circle of friends, you are tops! I hope you had a wonderful birthday celebration. Happy belated birthday!");
		list.add(
				"Here’s to you and the wonderful year you have ahead of you. May all of your wishes come true! Happy belated birthday!");
		list.add(
				"Even though I wasn’t there, I hope you had one of the best birthdays ever. Wish you a belated happy birthday, dude.");
		list.add(
				"A belated happy birthday to you. May all the good things that life has to offer always find their way to your doorstep.");
		list.add("I didn't forget about your birthday; I just wanted to help prolong the celebration.");
		list.add("My best wishes to you, even though are belated.");

		return list;
	}


}
