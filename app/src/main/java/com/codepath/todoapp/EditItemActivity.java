package com.codepath.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditItemActivity extends Activity {

    private EditText etEditItem;
    private String itemData;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        itemData = getIntent().getStringExtra("itemData");
        position = getIntent().getIntExtra("position",0);
        if (itemData != null && !itemData.isEmpty()) {
            etEditItem.setText(itemData);
            etEditItem.setSelection(itemData.length());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Event handler while Saving the item data after edit.
     * @param view - Provided the view.
     */
    public void onSubmit(View view) {
        Intent data = new Intent();
        String text = etEditItem.getText().toString();
        if (text == null || text.isEmpty()) {
            Toast.makeText(this,"Text should not be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        data.putExtra("itemData", etEditItem.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        finish();
    }
}