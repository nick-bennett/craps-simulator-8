package edu.cnm.deepdive.crapssimulator.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import edu.cnm.deepdive.craps.model.Game.Roll;
import edu.cnm.deepdive.craps.model.Game.Round;
import edu.cnm.deepdive.craps.model.Game.State;
import edu.cnm.deepdive.crapssimulator.R;

public class RoundAdapter extends ArrayAdapter<Roll> {

  private final Drawable[] faces;
  private State state;
  @ColorInt private final int winningRound;
  @ColorInt private final int losingRound;

  public RoundAdapter(Context context) {
    super(context, R.layout.single_roll);
    Resources res = context.getResources();
    String pkg = context.getPackageName();
    faces = new Drawable[6];
    for (int i = 0; i < faces.length; i++) {
      faces[i] = ContextCompat.getDrawable(context,
          res.getIdentifier("face_" + (i + 1), "drawable", pkg));
    }
    winningRound = ContextCompat.getColor(context, R.color.winningRound);
    losingRound = ContextCompat.getColor(context, R.color.losingRound);
  }

  public void add(Round round) {
    clear();
    if (round != null) {
      addAll(round.getRolls());
      this.state = round.getState();
    } else {
      this.state = null;
    }
  }

  @NonNull
  @Override
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    if (view == null) {
      view = LayoutInflater.from(getContext()).inflate(R.layout.single_roll, parent, false);
    }
    Roll roll = getItem(position);
    int die1 = roll.getDie1();
    int die2 = roll.getDie2();
    int value = roll.getValue();
    ((ImageView) view.findViewById(R.id.die1)).setImageDrawable(faces[die1 - 1]);
    ((ImageView) view.findViewById(R.id.die2)).setImageDrawable(faces[die2 - 1]);
    ((TextView) view.findViewById(R.id.value))
        .setText(getContext().getString(R.string.value_format, value));
    view.setBackgroundColor((state == State.WIN) ? winningRound : losingRound);
    return view;
  }

}
