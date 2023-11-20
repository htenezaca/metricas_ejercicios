package editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

interface SpellChecker {
	List<String> check(String text);
}

class BasicSpellChecker implements SpellChecker {
	private final List<String> realWords = Arrays.asList("foo", "bar");

	@Override
	public List<String> check(String text) {
		List<String> words = Arrays.asList(text.split(" "));
		return words.stream()
				.filter(word -> !realWords.contains(word.toLowerCase()))
				.collect(Collectors.toList());
	}
}

class ConsoleOutput {
	public void displayErrors(List<String> errors) {
		errors.forEach(error -> System.out.println("ERROR: " + error));
	}

	public void displaySuccessMessage() {
		System.out.println("No errors found!");
	}
}

public class Editor {
	private final SpellChecker spellChecker;
	private final ConsoleOutput consoleOutput;

	public Editor(SpellChecker spellChecker, ConsoleOutput consoleOutput) {
		this.spellChecker = spellChecker;
		this.consoleOutput = consoleOutput;
	}

	public void checkSpelling(String text) {
		List<String> errors = spellChecker.check(text);
		if (!errors.isEmpty()) {
			consoleOutput.displayErrors(errors);
		} else {
			consoleOutput.displaySuccessMessage();
		}
	}

	public void runEditor() {
		System.out.println("Running editor...");
		System.out.println("Enter text:");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			String text = br.readLine();
			checkSpelling(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpellChecker spellChecker = new BasicSpellChecker();
		ConsoleOutput consoleOutput = new ConsoleOutput();
		Editor editor = new Editor(spellChecker, consoleOutput);
		editor.runEditor();
	}
}
