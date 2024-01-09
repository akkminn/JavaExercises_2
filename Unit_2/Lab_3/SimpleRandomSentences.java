
/*
  Four Rules that capture the syntax of this program:
	<sentence> ::= <simple_sentence> [ <conjunction> <sentence> ]

    <simple_sentence> ::= <noun_phrase> <verb_phrase>

    <noun_phrase> ::= <proper_noun> | 
                      <determiner> [ <adjective> ]... <common_noun> [ who <verb_phrase> ]

    <verb_phrase> ::= <intransitive_verb> | 
                      <transitive_verb> <noun_phrase> |
                      is <adjective> |
                      believes that <simple_sentence>


  This program implements these rules to generate random sentences. 
  It can produce simple random sentence, but a lot of sentences that make no
  sense (but still follow the syntax).   Note that an optional item like
  [ <adjective> ].. has a chance of being used more than once, depending on 
  the value of some randomly generated number.

  The program generates and outputs one random sentence every three seconds until
  it is halted (for example, by typing Control-C in the terminal window where it is
  running).
 */


public class SimpleRandomSentences {

	static final private String[] conjunctions = { "and", "or", "but", "because"};

	static final private String[] proper_nouns = { "Fred", "Jane", "Richard Nixon", "Miss America" };

	static final private String[] common_nouns = { "man", "woman", "fish", "elephant", "unicorn", "cat", "dog" };

	static final private String[] determiners = { "a", "the", "every", "some" };

	static final private String[] adjectives = { "big", "tiny", "pretty", "bald" };

	static final private String[] intransitive_verbs = { "runs", "jumps", "talks", "sleeps" };

	static final private String[] transitive_verbs = { "loves", "hates", "sees", "knows", "looks for", "finds" };


	/**
	 * The main routine prints out one random sentence every three
	 * seconds, forever (or until the program is killed).
	 */
	public static void main(String[] args) {
		while (true) {
			randomSentence();
			System.out.println(".\n\n");
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Generate a random sentence, following the grammar rule for a sentence.
	 */
	static void randomSentence() {

		/* A simple sentence */
		randomSimpleSentence();
		if (Math.random() > 0.75) { // 25% chance of using a conjunction + sentence
			/* A random conjunction */
			randomItem(conjunctions);
			randomSimpleSentence();
		}

	}

	/**
	 * Generate a random simple_sentence, following the grammar rule for a simple_sentence.
	 */
	static void randomSimpleSentence() {

		/* A random noun phrase and a random verb phrase */
		randomNounPhrase();
		randomVerbPhrase();
	}

	/**
	 * Generates a random noun_phrase, following the grammar rule for a noun_phrase.
	 */

	static void randomNounPhrase() {

		/* A random noun proper_noun */
		if (Math.random() > 0.50) { // 50% chance of using a proper noun.
			randomItem(proper_nouns);
		}
		else {
			/* A random determiner */
			randomItem(determiners);

			/* A random adjective or adjectives */
			do {
				randomItem(adjectives);
			} while(Math.random() > 0.90); // 10% chance of having two or more adjectives

			/* A random common_noun */
			randomItem(common_nouns);

			if (Math.random() > 0.50) { // 50% chance of having verb phrase
				System.out.print(" who");
				randomVerbPhrase();
			}
		}
	}

	/**
	 * Generates a random verb_phrase, following the grammar rule for a verb_phrase.
	 */
	private static void randomVerbPhrase() {

		double ranInt = Math.random();
		/* A random intransitive verb */
		if (ranInt > 0.75) { // 25% chance of using a "intransitive verb".
			randomItem(intransitive_verbs);
		}
		/* A random transitive verb + noun phrase */
		else if (ranInt > 0.50) { // 25% chance of using a "transitive verb + noun phrase".
			randomItem(transitive_verbs);
			randomNounPhrase();
		}
		/* is + random adjective */
		else if (ranInt > 0.25) { // 25% chance of using "is + random adjective".
			System.out.print(" is");
			randomItem(adjectives);
		}
		/* believes that + random simple sentence */
		else { // 25% chance of using "believes that + random simple sentence".
			System.out.print(" believes that");
			randomSimpleSentence();
		}

	}

	/**
	 * Generates a random item
	 */
	static void randomItem(String[] listOfStrings) {

		/* A random item */
		int i = (int)(Math.random()*listOfStrings.length);
		System.out.print(" " +listOfStrings[i]);
	}

}