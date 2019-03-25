package nl.hva.dka.bucketlist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {BucketListItem.class}, version = 1, exportSchema = false)
public abstract class BucketListItemDatabase extends RoomDatabase {

    private final static String NAME_DATABASE = "bucketlist_database";
    public abstract IBucketListItemDao itemDao();

    private static volatile BucketListItemDatabase INSTANCE;

    protected static BucketListItemDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BucketListItemDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BucketListItemDatabase.class, NAME_DATABASE).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

