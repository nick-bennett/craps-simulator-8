package edu.cnm.deepdive.crapssimulator.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Round;
import edu.cnm.deepdive.crapssimulator.R;
import edu.cnm.deepdive.crapssimulator.view.RoundAdapter;
import edu.cnm.deepdive.crapssimulator.viewmodel.MainViewModel;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private RoundAdapter adapter;
  private TextView tally;
  private boolean running;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupUI();
    setupViewModel();
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
        viewModel.playOne();
        break;
      case R.id.fast_forward:
        viewModel.run();
        break;
      case R.id.pause:
        viewModel.stop();
        break;
      case R.id.reset:
        viewModel.reset();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void setupUI() {
    tally = findViewById(R.id.tally);
    ListView rolls = findViewById(R.id.rolls);
    adapter = new RoundAdapter(this);
    rolls.setAdapter(adapter);
  }

  private void setupViewModel() {
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    viewModel.getGame().observe(this, this::updateTally);
    viewModel.getRound().observe(this, this::updateRolls);
    viewModel.isRunning().observe(this, (running) -> {
      this.running = running;
      invalidateOptionsMenu();
    });
  }

  private void updateRolls(Round round) {
    adapter.add(round);
  }

  private void updateTally(Game game) {
    int wins = game.getWins();
    int plays = game.getPlays();
    double percentage = game.getPercentage();
    String winsLabel = getResources().getQuantityString(R.plurals.wins, wins);
    String playsLabel = getResources().getQuantityString(R.plurals.plays, plays);
    tally.setText(
        getString(R.string.tally_format, wins, plays, 100 * percentage, winsLabel, playsLabel));
  }

}
