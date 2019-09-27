package edu.cnm.deepdive.crapssimulator.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Round;
import java.security.SecureRandom;
import java.util.Random;

public class MainViewModel extends AndroidViewModel {

  private final Random rng;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Round> round;
  private final MutableLiveData<Boolean> running;
  private Runner runner;

  public MainViewModel(@NonNull Application application) {
    super(application);
    rng = new SecureRandom();
    game = new MutableLiveData<>();
    round = new MutableLiveData<>();
    running = new MutableLiveData<>();
    reset();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Round> getRound() {
    return round;
  }

  public LiveData<Boolean> isRunning() {
    return running;
  }

  public void playOne() {
    round.setValue(game.getValue().play());
    game.setValue(game.getValue());
  }

  public void fastForward() {
    if (runner != null) {
      runner.pause();
    }
    runner = new Runner();
    runner.start();
    running.setValue(true);
  }

  public void pause() {
    if (runner != null) {
      runner.pause();
    }
    runner = null;
  }

  public void reset() {
    game.setValue(new Game(rng));
    round.setValue(null);
    running.setValue(false);
  }

  private class Runner extends Thread {

    private static final int ROUND_BATCH_SIZE = 1000;

    private boolean running = true;

    @Override
    public void run() {
      Game game = MainViewModel.this.game.getValue();
      Round round = null;
      while (running) {
        int limit = ROUND_BATCH_SIZE - game.getPlays() % ROUND_BATCH_SIZE;
        for (int i = 0; i < limit; i++) {
          round = game.play();
        }
        MainViewModel.this.round.postValue(round);
        MainViewModel.this.game.postValue(game);
      }
      MainViewModel.this.running.postValue(false);
    }

    void pause() {
      running = false;
    }

  }

}
