package com.gmail.warrengodonemaresca.evilhangman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

public class EvilHangman {

	public static final String PREFS_NAME = "MyPrefsFile";

	private String hidden;

	private int guessesRemaining, initialGuesses, wordLength;

	private char guess;

	private List<Character> usedChars;

	private List<String> dictionary;

	private Map<String, ArrayList<String>> groups;//Possibly make this local

	public EvilHangman(int wordLength, int numOfGuesses, Context c){
		usedChars = new ArrayList<Character>();
		dictionary = new ArrayList<String>();
		groups = new HashMap<String, ArrayList<String>>();

		initialGuesses = numOfGuesses;
		guessesRemaining = numOfGuesses;

		this.wordLength = wordLength;
		Log.w("Hello", "Constructor");

		createHiddenWord();
		createDict(c);
	}

	private void createDict(Context c){
		BufferedReader dict = null; //Holds the dictionary file
		AssetManager am = c.getAssets();

		try {
			SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, 0);
			int diff = prefs.getInt("difficulty", 2);

			if(diff == 1){
				dict = new BufferedReader(new //Cont. next line
						InputStreamReader(am.open("easy.txt")));
			} else if(diff == 2) {
				dict = new BufferedReader(new //Cont. next line
						InputStreamReader(am.open("medium.txt")));
			} else if(diff == 3){
				dict = new BufferedReader(new //Cont. next line
						InputStreamReader(am.open("hard.txt")));
			} else {
				dict = new BufferedReader(new //Cont. next line
						InputStreamReader(am.open("very_hard.txt")));
			}
			
			String word;
			while((word = dict.readLine()) != null){
				if(word.length() == wordLength){
					dictionary.add(word);
				}
			}

		} catch(FileNotFoundException e){
			System.out.println("File not found, re-download.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			dict.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createHiddenWord(){
		hidden = "";
		for(int i = 0; i < wordLength; i++){
			hidden += "_";
		}
	}

	/**
	 * precondition: createDict() and eliminateGroups() should have been called.
	 * The latter is so that the first element is not to be eliminated yet.
	 */
	private void updateHiddenWord(){
		System.out.println("Updating hidden word");

		String temp = "";

		//TODO change to for-each loop
		for(int i = 0; i < wordLength; i++){

			if((dictionary.get(0)).charAt(i) == guess){
				temp += guess;
			} else {
				temp += hidden.charAt(i);
			}
		}

		hidden = temp;
	}


	public void guess(char guess){
		this.guess = guess;

		if(!usedChars.contains(guess)){
			createGroups();
			eliminateGroups();
			updateHiddenWord();
			usedChars.add(guess);
		}

		boolean containsGuess = false;
		for(int i = 0; i < dictionary.get(0).length() && !containsGuess; i++){
			if(dictionary.get(0).charAt(i) == guess){
				containsGuess = true;
			}
		}

		if(!containsGuess){
			guessesRemaining--;
		}
	}

	private void createGroups(){
		for(int i = 0; i < dictionary.size(); i++){
			String sequence = findSequence(dictionary.get(i), guess);

			if(!groups.containsKey(sequence)){
				groups.put(sequence, new ArrayList<String>());
			}

			(groups.get(sequence)).add(dictionary.get(i));
		}
	}

	private void eliminateGroups(){
		Log.i("Hello", "Eliminating Groups");
		ArrayList<String> largestGroup = null;

		for(Entry<String, ArrayList<String>> group : groups.entrySet()){

			if((largestGroup == null || 
					group.getValue().size() > largestGroup.size())){
				largestGroup = group.getValue();	
			}
		}

		//For three letter words, there are many words (groups of size 1) that
		//share two letters. Eg. lab, lad, lag, lap, law, lax, and lay.
		//The following code enclosed by the if-statement will avoid the
		//group with the guessed letter. Without it, the group will be selected
		//without considering if it contains the guessed letter.
		if(largestGroup.get(0).length() == 3){//Special case, and if difficult > easy
			for(Entry<String, ArrayList<String>> group : groups.entrySet()){

				//Checks the first and middle char of the other groups.
				if((group.getValue().size() == largestGroup.size()) && 
						(group.getValue().get(0).charAt(0) != guess || 
						group.getValue().get(0).charAt(1) != guess)){
					largestGroup = group.getValue();	
				}
			}
		}

		dictionary = largestGroup;
		groups = new HashMap<String, ArrayList<String>>();
	}

	//TODO REWORD
	/**
	 * Returns a string of length <code>str</code> where all characters but
	 * the <code>c</code> in both cases are replaced with "_".
	 * 
	 * @param str
	 * @param c
	 * @return The sequence of <code>c</code> (see above).
	 */
	private String findSequence(String str, char c){
		str = str.toUpperCase(Locale.getDefault());
		c = Character.toUpperCase(c);

		String temp = "";

		for(int i = 0; i < str.length(); i++){
			if(str.charAt(i) == c){
				temp += c;
			} else {
				temp += "_";
			}
		}

		return temp;
	}

	public boolean hasWon(){
		boolean isHidden = false;

		for(int i = 0; i < hidden.length() && !isHidden; i++){
			if(hidden.charAt(i) == '_'){
				isHidden = true;
			}
		}

		return !isHidden;
	}

	public boolean hasLost(){
		return guessesRemaining < 0;
	}

	public char getGuess(){
		return Character.toUpperCase(guess);
	}

	public String getHidden(){
		return hidden.toUpperCase(Locale.getDefault());
	}

	public boolean isNewGuess(char c){
		return usedChars.contains(c);
	}

	public boolean isNewGuess(){
		return usedChars.contains(guess);
	}

	//TODO remove later
	public int getWordPoolSize(){
		return dictionary.size();
	}

	public int getGuessesRemaining(){
		return guessesRemaining;
	}
	
	public int getInitialGuesses(){
		return initialGuesses;
	}
	
}//END
