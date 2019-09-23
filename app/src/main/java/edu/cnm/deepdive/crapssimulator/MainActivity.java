package edu.cnm.deepdive.crapssimulator;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
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
        Toast.makeText(this, R.string.run_toast, Toast.LENGTH_LONG).show();
        break;
      case R.id.reset:
        Toast.makeText(this, R.string.reset_toast, Toast.LENGTH_LONG).show();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

}









