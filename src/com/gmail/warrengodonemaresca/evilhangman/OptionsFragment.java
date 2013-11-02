package com.gmail.warrengodonemaresca.evilhangman;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class OptionsFragment extends Fragment implements OnClickListener, 
RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

	public static final String PREFS_NAME = "MyPrefsFile";

	private Button playButton;
	private RadioGroup diffRadioGroup;
	private RadioButton easy, medium, hard, veryHard;
	private SeekBar wordLengthBar, guessesBar;
	private TextView wordLengthBarValue, guessesBarValue;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences prefs = this.getActivity().
				getSharedPreferences(PREFS_NAME, 0);

		View view = inflater.inflate(R.layout.dialog_start, container, false);

		initBars(prefs, view);
		initRadio(prefs, view);
		setWordLengthMax(prefs);

		playButton = (Button)view.findViewById(R.id.dialogStart_play);
		playButton.setOnClickListener(this);

		return view;
	}

	private void initBars(SharedPreferences prefs, View v){
		//Word length seekbar
		wordLengthBar = (SeekBar)v.findViewById(R.id.wordLength_seekBar);
		wordLengthBar.setProgress(prefs.getInt("word_length", 7));
		wordLengthBar.setOnSeekBarChangeListener(this);

		//Number of guesses seekbar
		guessesBar = (SeekBar)v.findViewById(R.id.numOfGuesses_seekBar);
		guessesBar.setProgress(prefs.getInt("num_of_guesses", 7));
		guessesBar.setOnSeekBarChangeListener(this);


		wordLengthBarValue = (TextView)v.findViewById(R.id.wordLength_seekBar_value);
		wordLengthBarValue.setText(Integer.toString(wordLengthBar.getProgress()));

		guessesBarValue = (TextView)v.findViewById(R.id.numOfGuesses_seekBar_value);
		guessesBarValue.setText(Integer.toString(guessesBar.getProgress()));
	}

	/**
	 * Initializes the radio buttons that sets the difficulty. Also, it sets
	 * the maximum for the word length seek bar
	 * @param prefs
	 * @param v
	 */
	private void initRadio(SharedPreferences prefs, View v){
		int diff = prefs.getInt("difficulty", 2);

		diffRadioGroup = (RadioGroup)v.findViewById(R.id.radioGroup_difficulties);
		easy = (RadioButton)v.findViewById(R.id.radio_easy);
		medium = (RadioButton)v.findViewById(R.id.radio_medium);
		hard = (RadioButton)v.findViewById(R.id.radio_hard);
		veryHard = (RadioButton)v.findViewById(R.id.radio_very_hard);

		if(diff == 1){
			diffRadioGroup.check(R.id.radio_easy);
		} else if(diff == 2){
			diffRadioGroup.check(R.id.radio_medium);
		} else if(diff == 3){
			diffRadioGroup.check(R.id.radio_hard);
		} else {
			diffRadioGroup.check(R.id.radio_very_hard);
		}

		diffRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	/**
	 * 
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();

		if(checkedId == easy.getId()){
			editor.putInt("difficulty", 1);
		} else if(checkedId == medium.getId()){
			editor.putInt("difficulty", 2);
		} else if(checkedId == hard.getId()){
			editor.putInt("difficulty", 3);
		} else {
			editor.putInt("difficulty", 4);
		}

		editor.commit();

		setWordLengthMax(prefs);
	}

	private void setWordLengthMax(SharedPreferences prefs){
		//1 is subtracted to match the indexes of the maxima array
		int diff = prefs.getInt("difficulty", 2) - 1;

		int[] maxima = {14, 16, 19, 24};

		wordLengthBar.setMax(maxima[diff]);
		wordLengthBar.invalidate();

		if(wordLengthBar.getProgress() > maxima[diff]){
			SharedPreferences.Editor editor = prefs.edit();

			editor.putInt("word_length", maxima[diff]);
			wordLengthBarValue.setText(Integer.toString(maxima[diff]));

			editor.commit();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == playButton.getId()){
			if(getActivity() instanceof GameActivity){
				((GameActivity)getActivity()).resetActivity();
				//We hide this view.
				this.getView().setVisibility(View.GONE);
			} else {
				startActivity(new Intent(getActivity(), GameActivity.class));
			}
		}
	}

	@Override
	public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		return super.onCreateAnimator(transit, enter, nextAnim);
	}

	@Override
	/**
	 * Called when a seek bar's value is changed
	 */
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, 0);

		if(seekBar.getId() == wordLengthBar.getId()){
			if(progress < 2){
				progress = 2;
				seekBar.setProgress(progress);
			}

			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("word_length", progress);
			editor.commit();

			wordLengthBarValue.setText(Integer.toString(progress));
		} else { //the only other seek bar is the guesses seek bar
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("num_of_guesses", progress);
			editor.commit();

			guessesBarValue.setText(Integer.toString(progress));
		}
	}

	@Override //Auto-generated method stub
	public void onStartTrackingTouch(SeekBar seekBar){}

	@Override //Auto-generated method stub
	public void onStopTrackingTouch(SeekBar seekBar){}
}