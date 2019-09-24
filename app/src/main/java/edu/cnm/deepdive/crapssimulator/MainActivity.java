package edu.cnm.deepdive.crapssimulator;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Roll;
import edu.cnm.deepdive.craps.model.Game.Round;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Game game;
  private Random rng;
  private ArrayAdapter<Roll> adapter;
  private TextView tally;
  private ListView rolls;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tally = findViewById(R.id.tally);
    tally.setText("This will be my win/loss tally line");
    rolls = findViewById(R.id.rolls);
    adapter = new ArrayAdapter<>(this, R.layout.single_roll);
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
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.run:
        updateDisplay(game.play());
        break;
      case R.id.reset:
        resetGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void updateDisplay(Round round) {
    adapter.clear();
    if (round != null) {
      adapter.addAll(round.getRolls());
    }
    tally.setText(getString(R.string.tally_format,
        game.getWins(), game.getPlays(), 100 * game.getPercentage()));
  }

  private void resetGame() {
    game = new Game(rng);
    updateDisplay(null);
  }

}
