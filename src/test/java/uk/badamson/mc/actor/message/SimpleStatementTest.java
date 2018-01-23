package uk.badamson.mc.actor.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;

import uk.badamson.mc.actor.message.SimpleStatement.SimplePredicate;

/**
 * <p>
 * Unit tests of the s{@link SimpleStatement} class.
 * </p>
 */
public class SimpleStatementTest {

    public static void assertInvariants(SimpleStatement statement) {
	MessageTest.assertInvariants(statement);// inherited

	final Noun subject = statement.getSubject();
	final SimplePredicate predicate = statement.getPredicate();

	assertNotNull("Not null, subject", subject);// guard
	assertNotNull("Not null, predicate", predicate);

	assertEquals("The information content exceeds the total for the message elements by the same extra amount.",
		SimpleStatement.EXTRA_INFORMATION_CONTENT + subject.getInformationContent()
			+ statement.getPredicate().getInformationContent(),
		statement.getInformationContent(), 1.0E-3);

	NounTest.assertInvariants(subject);

	assertTrue("One of the finite array of values of the type",
		Arrays.asList(SimpleStatement.values()).indexOf(statement) != -1);
    }

    public static void assertInvariants(SimpleStatement statement1, SimpleStatement statement2) {
	MessageTest.assertInvariants(statement1, statement2);// inherited

	final boolean equals = statement1.equals(statement2);
	assertFalse("Value semantics (subject)",
		equals && !Objects.equals(statement1.getSubject(), statement2.getSubject()));
	assertFalse("Value semantics (predicate)",
		equals && !Objects.equals(statement1.getPredicate(), statement2.getPredicate()));
    }

    public static final SimpleStatement getEnemyInSight(SimpleRelativeLocation location) {
	final SimpleStatement statement = SimpleStatement.getEnemyInSight(location);

	assertNotNull("Always returns an instance.", statement);// guard
	assertInvariants(statement);
	assertSame("subject", location, statement.getSubject());
	assertSame("predicate", SimpleStatement.SimplePredicate.HAS_ENEMY_IN_SIGHT, statement.getPredicate());

	return statement;
    }

    public static UnusableIncompleteMessage getPartialMessage(SimpleStatement statement, double partLength) {
	final UnusableIncompleteMessage partialMessage = (UnusableIncompleteMessage) MessageTest
		.getPartialMessage(statement, partLength);

	assertInvariants(statement);
	UnusableIncompleteMessageTest.assertInvariants(partialMessage);

	return partialMessage;
    }

    @Test
    public void getEnemyInSight_all() {
	for (SimpleRelativeLocation location : SimpleRelativeLocation.values()) {
	    getEnemyInSight(location);
	}
    }

    @Test
    public void getPartialMessage_EnemyInSight_A() {
	final SimpleStatement statement = SimpleStatement.getEnemyInSight(SimpleRelativeLocation.BACK_FAR);
	getPartialMessage(statement, statement.getInformationContent() * 0.5);
    }

    @Test
    public void getPartialMessage_EnemyInSight_B() {
	final SimpleStatement statement = SimpleStatement.getEnemyInSight(SimpleRelativeLocation.FRONT_NEAR);
	getPartialMessage(statement, statement.getInformationContent() * 0.25);
    }

    @Test
    public void static_ACKNOWLEDGE_MESSAGE() {
	final SimpleStatement statement = SimpleStatement.ACKNOWLEDGE_MESSAGE;
	assertNotNull("Not null", statement);// guard
	assertInvariants(statement);
	assertSame("subject", Pronoun.ME, statement.getSubject());
	assertSame("predicate", SimpleStatement.SimplePredicate.ACKNOWLEDGE_MESSAGE, statement.getPredicate());
    }

    @Test
    public void static_DANGER_AREA() {
	final SimpleStatement statement = SimpleStatement.DANGER_AREA;
	assertNotNull("Not null", statement);// guard
	assertInvariants(statement);
	assertSame("subject", Pronoun.IT, statement.getSubject());
	assertSame("predicate", SimpleStatement.SimplePredicate.IS_DANGER_AREA, statement.getPredicate());
    }

    @Test
    public void values() {
	final SimpleStatement[] values = SimpleStatement.values();

	assertNotNull("Always returns an array of values.", values);// guard
	for (int i = 0; i < values.length; ++i) {
	    final SimpleStatement value = values[i];
	    assertNotNull("The array of values has no null elements.", value);// guard
	    assertInvariants(value);
	    for (int j = i; j < values.length; ++j) {
		final SimpleStatement value2 = values[j];
		assertInvariants(value, value2);
		assertEquals("The array of values has no duplicate elements<" + value + ", " + value2 + ">.", i == j,
			value.equals(value2));
	    }
	}
    }
}