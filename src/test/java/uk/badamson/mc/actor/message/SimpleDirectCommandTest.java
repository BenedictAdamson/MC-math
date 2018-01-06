package uk.badamson.mc.actor.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collections;

import org.junit.Test;

/**
 * <p>
 * Unit tests of the {@link SimpleDirectCommand} class and its enumerated
 * objects.
 * </p>
 */
public class SimpleDirectCommandTest {

    public static void assertInvariants(SimpleDirectCommand command) {
	CommandTest.assertInvariants(command);// inherited

	assertEquals("The information content exceeds the total for the message elements by the same extra amount.",
		SimpleDirectCommand.EXTRA_INFORMATION_CONTENT + command.getSubject().getInformationContent()
			+ command.getVerb().getInformationContent()
			+ SentenceTest.totalInformationContent(command.getObjects()),
		command.getInformationContent(), 1.0E-3);
	assertSame("subject", Pronoun.YOU, command.getSubject());
    }

    public static UnusableIncompleteMessage getPartialMessage(SimpleDirectCommand message, double partLength) {
	final UnusableIncompleteMessage partialMessage = (UnusableIncompleteMessage) MessageTest
		.getPartialMessage(message, partLength);

	assertInvariants(message);
	UnusableIncompleteMessageTest.assertInvariants(partialMessage);

	return partialMessage;
    }

    @Test
    public void getPartialMessage_DISPERSE_A() {
	getPartialMessage(SimpleDirectCommand.DISPERSE, SimpleDirectCommand.DISPERSE.getInformationContent() * 0.5);
    }

    @Test
    public void getPartialMessage_DISPERSE_B() {
	getPartialMessage(SimpleDirectCommand.DISPERSE, SimpleDirectCommand.DISPERSE.getInformationContent() * 0.25);
    }

    @Test
    public void static_DISPERSE() {
	assertInvariants(SimpleDirectCommand.DISPERSE);
	assertSame("verb", SimpleVerb.CHANGE_FORMATION, SimpleDirectCommand.DISPERSE.getVerb());
	assertEquals("object", Collections.singleton(SimpleFormationName.DISPERSE),
		SimpleDirectCommand.DISPERSE.getObjects());
    }
}
