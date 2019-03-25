package nl.hva.dka.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IBucketListItemDao {

    @Insert
    void insert(BucketListItem product);

    @Update
    void update(BucketListItem product);

    @Delete
    void delete(BucketListItem product);

    @Query("SELECT * from item_table")
    List<BucketListItem> getAllItems();

}
