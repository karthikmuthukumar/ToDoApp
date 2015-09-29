package com.codepath.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private final int EDIT_REQUEST_CODE = 10;
    private final String FILE_NAME = "todo.txt";

    private List<String> todoItems;
    private EditText etItem;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTodoItems();

        etItem = (EditText) findViewById(R.id.etItem);
        ListView lvTodoItems = (ListView) findViewById(R.id.lvTodoItems);
        lvTodoItems.setAdapter(listAdapter);
        lvTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                listAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvTodoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = parent.getItemAtPosition(position).toString();
                openEditItem(data, position);
            }
        });

    }

    /**
     * Populates the todoItems list and set it in the listAdapter.
     */
    private void populateTodoItems() {
        readItems();
        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,todoItems);
    }

    /**
     * Read the todoItems from the file.
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, FILE_NAME);
        try {
            todoItems = new ArrayList<>(FileUtils.readLines(file));
        } catch (IOException e) {
            todoItems = new ArrayList<>();
        }
    }

    /**
     * Persist the todoItems in the file.
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, FILE_NAME);
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
     * Event listener while adding items using Submit button.
     * @param view - Provides the view.
     */
    public void onAddItem(View view) {
        String text = etItem.getText().toString();
        if (text == null || text.isEmpty()) {
            Toast.makeText(this, "Text should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        listAdapter.add(etItem.getText().toString());
        etItem.setText("");
        writeItems();
    }

    /**
     * Opens the next activity(Edit Item) and passes the required data to EditItemActivity.
     * @param data - data contains the selected item.
     * @param position - position contains the selected item's position.
     */
    private void openEditItem(String data, int position) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra("itemData", data);
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    /**
     * Handles the result
     * @param requestCode - requestCode contains the request code for the operation
     * @param resultCode - resultCode received from the another activity.
     * @param data - data received from the another activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // Extract itemData,position value from result extras
            String itemData = data.getExtras().getString("itemData");
            int position = data.getExtras().getInt("position");
            //Update itemData in todoItems and notify adapter
            todoItems.set(position, itemData);
            listAdapter.notifyDataSetChanged();
            //Persist the changes in the file.
            writeItems();
        }
    }
}
