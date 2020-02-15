package com.example.valentineday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    PulseView pulseView;

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private Animation pulse;
    Dialog MyCustomDialog;


    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Pause playback
                mMediaPlayer.pause();
                //Start at beginning audio file
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Stop playback
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyCustomDialog = new Dialog(this);

        ImageView heart = (ImageView) findViewById(R.id.song);
        pulseView = (PulseView) findViewById(R.id.pv);
        final TextView text = (TextView) findViewById(R.id.text);
        final ImageView chest = (ImageView) findViewById(R.id.chest);


        mMediaPlayer = MediaPlayer.create(this, R.raw.song);
        pulseView.setVisibility(View.INVISIBLE);



        pulse = AnimationUtils.loadAnimation(this, R.anim.heart_pulse);
        heart.startAnimation(pulse);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaPlayer.isPlaying() == true) {
                    mMediaPlayer.pause();
                    pulseView.finishPulse();

                    text.setVisibility(View.VISIBLE);
                    pulseView.setVisibility(View.INVISIBLE);
                    chest.setVisibility(View.INVISIBLE);
                    chest.clearAnimation();


                    Toast.makeText(MainActivity.this, ("\ud83d\ude18"), Toast.LENGTH_SHORT).show();

                } else {
                    pulseView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    chest.setVisibility(View.VISIBLE);

                    pulseView.startPulse();
                    mMediaPlayer.start();
                    chest.startAnimation(pulse);



                    Toast.makeText(MainActivity.this, "Enjoy", Toast.LENGTH_SHORT).show();
                }


                mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);

                    }
                });

            }
        });


    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null){
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public void showMyDialog(View view) {
        MyCustomDialog.setContentView(R.layout.my_custom_dialog);
        Button closeDialogBt = (Button) MyCustomDialog.findViewById(R.id.dialog_close);
        ImageView close = (ImageView) MyCustomDialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog.dismiss();
            }
        });
        MyCustomDialog.setCancelable(false);
        MyCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MyCustomDialog.show();
    }

    public void runAnimation()
    {
        // Animation for text
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_appear);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.text);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
