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

  private Random rng;
  private MutableLiveData<Game> game;
  private MutableLiveData<Round> round;

  public MainViewModel(@NonNull Application application) {
    super(application);
    rng = new SecureRandom();
    game = new MutableLiveData<>();
    round = new MutableLiveData<>();
    reset();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Round> getRound() {
    return round;
  }

  public void play() {
    round.setValue(game.getValue().play());
  }

  public void play(int rounds) {
    new Thread(() -> {
        Round round = null;
        for (int i = 0; i < rounds; i++) {
          round = game.getValue().play();
        }
        MainViewModel.this.round.postValue(round);
    }).start();
  }

  public void reset() {
    game.setValue(new Game(rng));
    round.setValue(null);
  }

}
