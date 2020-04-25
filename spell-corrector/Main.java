package spell;

public class Main {

	public static void main(String[] args) {

		if (args.length == 2) {
			String dictionaryFileName = args[0];
			String inputWord = args[1];
			SpellCorrector corrector = new SpellCorrector();
			corrector.useDictionary(dictionaryFileName);
			String suggestion = corrector.suggestSimilarWord(inputWord);

			if (suggestion == null) {
				suggestion = "No similar word found";
			}

			System.out.println("Suggestion is: " + suggestion);
		}
		else {
			System.out.println("Wrong number of args");
		}
	}

}
