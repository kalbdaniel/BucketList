package nl.hva.dka.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, IUpdateCallback {

    List<BucketListItem> bucketList = new ArrayList<>();
    BucketListItemAdapter bucketListItemAdapter;
    RecyclerView recyclerView;
    private BucketListItemDatabase bucketItemRoomDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBucketListItem.class);
                startActivityForResult(intent, 1);
            }
        });

        bucketItemRoomDatabase = BucketListItemDatabase.getDatabase(this);

        recyclerView = findViewById(R.id.recyclerView);
        bucketListItemAdapter = new BucketListItemAdapter(bucketList, this);
        recyclerView.setAdapter(bucketListItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = recyclerView.getChildAdapterPosition(child);
                    deleteItem(bucketList.get(adapterPosition));
                }
            }
        });
        recyclerView.addOnItemTouchListener(this);
        getAllItems();

    }
    private void updateUI(List<BucketListItem> items) {
        bucketList.clear();
        bucketList.addAll(items);
        bucketListItemAdapter.notifyDataSetChanged();
    }

    private void getAllItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<BucketListItem> items = bucketItemRoomDatabase.itemDao().getAllItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(items);
                    }
                });
            }
        });
    }


    private void insertItem(final BucketListItem item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bucketItemRoomDatabase.itemDao().insert(item);
                getAllItems();
            }
        });
    }

    public void deleteItem(final BucketListItem item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bucketItemRoomDatabase.itemDao().delete(item);
                getAllItems();
            }
        });
    }

    public void updateItem(final BucketListItem item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bucketItemRoomDatabase.itemDao().update(item);
                getAllItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");


                BucketListItem item = new BucketListItem(title, description, false);
                insertItem(item);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
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


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
