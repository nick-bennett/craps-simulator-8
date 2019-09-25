package edu.cnm.deepdive.crapssimulator.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Round;
import edu.cnm.deepdive.crapssimulator.R;
import edu.cnm.deepdive.crapssimulator.view.RoundAdapter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Game game;
  private Random rng;
  private RoundAdapter adapter;
  private TextView tally;
  private ListView rolls;
  private boolean running;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tally = findViewById(R.id.tally);
    rolls = findViewById(R.id.rolls);
    adapter = new RoundAdapter(this);
    rolls.setAdapter(adapter);
    rng = new Random();
    resetGame();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.play_one).setVisible(!running);
    menu.findItem(R.id.fast_forward).setVisible(!running);
    menu.findItem(R.id.pause).setVisible(running);
    menu.findItem(R.id.reset).setEnabled(!running);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.play_one:
        updateDisplay(game.play(), game.getWins(), game.getPlays(), game.getPercentage());
        break;
      case R.id.fast_forward:
        running = true;
        new Runner().start();
        invalidateOptionsMenu();
        break;
      case R.id.pause:
        running = false;
        break;
      case R.id.reset:
        resetGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void updateDisplay(Round round, int wins, int plays, double percentage) {
    adapter.add(round);
    String winsLabel = getResources().getQuantityString(R.plurals.wins, wins);
    String playsLabel = getResources().getQuantityString(R.plurals.plays, plays);
    tally.setText(getString(R.string.tally_format,
        wins, plays, 100 * percentage, winsLabel, playsLabel));
  }

  private void resetGame() {
    game = new Game(rng);
    updateDisplay(null, 0, 0, 0);
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
        Round round = game.play();
        if (game.getPlays() % 500 == 0) {
          int wins = game.getWins();
          int plays = game.getPlays();
          double percentage = game.getPercentage();
          runOnUiThread(new Updater(round, wins, plays, percentage));
        }
      }
      runOnUiThread(new Updater(game.play(), game.getWins(), game.getPlays(), game.getPercentage()));
      invalidateOptionsMenu();
    }

  }

  private class Updater implements Runnable {

    private final Round round;
    private final int wins;
    private final int plays;
    private final double percentage;

    private Updater(Round round, int wins, int plays, double percentage) {
      this.round = round;
      this.wins = wins;
      this.plays = plays;
      this.percentage = percentage;
    }

    @Override
    public void run() {
      updateDisplay(round, wins, plays, percentage);
    }

  }

}
