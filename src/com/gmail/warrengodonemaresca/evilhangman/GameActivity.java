package com.gmail.warrengodonemaresca.evilhangman;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";

	private TextView output, guessesTxt;

	private Map<Character, Button> keyButtons;

	private EvilHangman game;

	private GameSurfaceView gameView;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		output = (TextView)this.findViewById(R.id.text_output_game);

		initKeyButtons();

		gameView = (GameSurfaceView)this.findViewById(R.id.surface_game);
		guessesTxt = (TextView)this.findViewById(R.id.text_guesses);

		//Hides the options fragment
		((OptionsFragment)this.getFragmentManager().
				findFragmentById(R.id.fragment_options_game)).getView().
				setVisibility(View.GONE);

		initGame();
		guessesTxt.setText(getResources().getString(R.string.guesses_left)
				+ " " + Integer.toString(game.getGuessesRemaining()));
	}

	@Override
	protected void onPause(){
		super.onPause();
		gameView.pause();
	}


	@Override
	protected void onResume(){
		super.onResume();
		gameView.resume();
	}



	private void initKeyButtons(){
		keyButtons = new HashMap<Character, Button>();

		keyButtons.put('a', (Button)this.findViewById(R.id.button_a));
		keyButtons.put('b', (Button)this.findViewById(R.id.button_b));
		keyButtons.put('c', (Button)this.findViewById(R.id.button_c));
		keyButtons.put('d', (Button)this.findViewById(R.id.button_d));
		keyButtons.put('e', (Button)this.findViewById(R.id.button_e));
		keyButtons.put('f', (Button)this.findViewById(R.id.button_f));
		keyButtons.put('g', (Button)this.findViewById(R.id.button_g));
		keyButtons.put('h', (Button)this.findViewById(R.id.button_h));
		keyButtons.put('i', (Button)this.findViewById(R.id.button_i));
		keyButtons.put('j', (Button)this.findViewById(R.id.button_j));
		keyButtons.put('k', (Button)this.findViewById(R.id.button_k));
		keyButtons.put('l', (Button)this.findViewById(R.id.button_l));
		keyButtons.put('m', (Button)this.findViewById(R.id.button_m));
		keyButtons.put('n', (Button)this.findViewById(R.id.button_n));
		keyButtons.put('o', (Button)this.findViewById(R.id.button_o));
		keyButtons.put('p', (Button)this.findViewById(R.id.button_p));
		keyButtons.put('q', (Button)this.findViewById(R.id.button_q));
		keyButtons.put('r', (Button)this.findViewById(R.id.button_r));
		keyButtons.put('s', (Button)this.findViewById(R.id.button_s));
		keyButtons.put('t', (Button)this.findViewById(R.id.button_t));
		keyButtons.put('u', (Button)this.findViewById(R.id.button_u));
		keyButtons.put('v', (Button)this.findViewById(R.id.button_v));
		keyButtons.put('w', (Button)this.findViewById(R.id.button_w));
		keyButtons.put('x', (Button)this.findViewById(R.id.button_x));
		keyButtons.put('y', (Button)this.findViewById(R.id.button_y));
		keyButtons.put('z', (Button)this.findViewById(R.id.button_z));
		
		for(int i = 97; i <= 122; i++){ //The ASCII codes for a and z
			keyButtons.get((char)i).setOnClickListener(onClickListener);
		}
	}

	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v){
			boolean buttonFound = false;

			for(int i = 97; i <= 122 && !buttonFound; i++){
				if(keyButtons.get((char)i).getId() == ((Button)v).getId() &&
						!game.hasLost() && !game.hasWon()){

					buttonFound = true;

					guess((char)i);

					if(game.hasLost()){
						onLose();
					} else if(game.hasWon()){
						onWin();
					}
				}

			}
		}

	};

	private void guess(char guess){
		//"removes" the clicked letter
		keyButtons.get(guess).setVisibility(View.INVISIBLE);

		game.guess(guess);

		String word = game.getHidden();

		gameView.setGuesses(game.getGuessesRemaining());
		output.setText(word);

		guessesTxt.setText(getResources().getString(R.string.guesses_left)
				+ " " + Integer.toString(game.getGuessesRemaining()));

		Log.i("On click", "clicked");
	}

	private void onWin(){
		View winView = (View)this.findViewById(R.id.win_subview);
		winView.setVisibility(View.VISIBLE);

		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);

		int guessesBonus = Math.max(0, 52 + game.getGuessesRemaining()
				- (int)Math.pow(game.getInitialGuesses(), 1.25));
		int difficultyBonus = prefs.getInt("difficulty", 2);
		int total = (10 + guessesBonus) * difficultyBonus;
		int streak = prefs.getInt("streak", 0);

		//We display the results
		((TextView)winView.findViewById(R.id.text_guesses_bonus_value)).setText(
				"+" + Integer.toString(guessesBonus));
		((TextView)winView.findViewById(R.id.text_difficulty_bonus_value)).setText(
				"x" + Integer.toString(difficultyBonus));
		((TextView)winView.findViewById(R.id.text_total_value)).setText(
				Integer.toString(total));

		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("streak", streak + total);
		editor.commit();

		((TextView)winView.findViewById(R.id.text_streak_value)).setText(
				Integer.toString(streak + total));

		((Button)winView.findViewById(R.id.button_more_options)).setOnClickListener(
				new OnClickListener(){
					public void onClick(View v){

						View optionsView = ((OptionsFragment)GameActivity.this.
								getFragmentManager().
								findFragmentById(R.id.fragment_options_game)).getView();

						if(optionsView.getVisibility() == View.GONE){				
							optionsView.setVisibility(View.VISIBLE);
						} else {
							optionsView.setVisibility(View.GONE);
						}
					}
				});

		//What to do when "play again" is pressed.
		((Button)winView.findViewById(R.id.button_play_again)).setOnClickListener(new 
				OnClickListener(){

			@Override
			public void onClick(View v){
				resetActivity();
			}

		});
	}

	private void onLose(){
		View loseView = (View)this.findViewById(R.id.lose_subview);
		loseView.setVisibility(View.VISIBLE);

		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("streak", 0);
		editor.commit();
		
		((Button)loseView.findViewById(R.id.button_more_options)).setOnClickListener(
				new OnClickListener(){
					public void onClick(View v){

						View optionsView = ((OptionsFragment)GameActivity.this.
								getFragmentManager().
								findFragmentById(R.id.fragment_options_game)).getView();

						optionsView.setVisibility(View.VISIBLE);
					}
				});

		//What to do when "play again" is pressed.
		((Button)loseView.findViewById(R.id.button_play_again)).setOnClickListener(new 
				OnClickListener(){
			public void onClick(View v){
				resetActivity();
			}
		});
	}

	/**
	 *TODO Preconditions!
	 */
	private void initGame(){
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);

		//Uses the word length from preferences
		game = new EvilHangman(prefs.getInt("word_length", 7),
				prefs.getInt("num_of_guesses", 13), this);

		String word = game.getHidden();
		//word += "\n" + game.getWordPoolSize() + " words left";
		output.setText(word);

		gameView.setGuesses(game.getGuessesRemaining());
		guessesTxt.setText(Integer.toString(game.getGuessesRemaining()));
	}


	public void resetActivity(){
		initGame();
		gameView.resetGallows();
		System.out.println("Okay so far");
		for(int i = 97; i <= 122; i++){ //The ASCII codes for a and z
			keyButtons.get((char)i).setVisibility(View.VISIBLE);
		}
		
		guessesTxt.setText(getResources().getString(R.string.guesses_left)
				+ " " + Integer.toString(game.getGuessesRemaining()));
		
		((View)this.findViewById(R.id.win_subview)).setVisibility(View.GONE);
		((View)this.findViewById(R.id.lose_subview)).setVisibility(View.GONE);
	}
}//end
