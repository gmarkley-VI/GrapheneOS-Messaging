package com.android.messaging.datamodel.action;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.messaging.Factory;
import com.android.messaging.R;
import com.android.messaging.datamodel.BugleDatabaseOperations;
import com.android.messaging.datamodel.DataModel;
import com.android.messaging.datamodel.DatabaseHelper;
import com.android.messaging.datamodel.DatabaseHelper.ConversationColumns;
import com.android.messaging.datamodel.DatabaseWrapper;
import com.android.messaging.datamodel.MessagingContentProvider;
import com.android.messaging.util.BuglePrefs;
import com.android.messaging.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Action to automatically delete old conversations that have been in deleted state
 * for longer than the configured retention period.
 */
public class AutoDeleteOldConversationsAction extends Action implements Parcelable {
    private static final String TAG = LogUtil.BUGLE_DATAMODEL_TAG;
    
    /**
     * Schedule auto-delete to run. This should be called daily.
     */
    public static void scheduleAutoDelete() {
        final AutoDeleteOldConversationsAction action = new AutoDeleteOldConversationsAction();
        action.start();
    }
    
    private AutoDeleteOldConversationsAction() {
        super();
    }
    
    @Override
    protected Object executeAction() {
        // Get the configured retention days from preferences
        final BuglePrefs prefs = BuglePrefs.getApplicationPrefs();
        final Context context = Factory.get().getApplicationContext();
        final String autoDeleteDaysKey = context.getString(R.string.auto_delete_days_pref_key);
        // Get default from resources (defined in constants.xml)
        final int defaultDays = context.getResources().getInteger(R.integer.auto_delete_days_default);
        // Now stored as integer in preferences
        final int retentionDays = prefs.getInt(autoDeleteDaysKey, defaultDays);
        
        // If retention is negative, disable auto-delete
        if (retentionDays < 0) {
            LogUtil.i(TAG, "Auto-delete disabled (retention days < 0)");
            return null;
        }
        
        // Calculate cutoff timestamp
        // If retention is 0, delete all deleted conversations immediately
        final long cutoffTimestamp;
        if (retentionDays == 0) {
            // Use MAX_VALUE to include all deleted conversations
            cutoffTimestamp = Long.MAX_VALUE;
        } else {
            // Calculate the cutoff based on retention days
            cutoffTimestamp = System.currentTimeMillis() - (retentionDays * 24L * 60L * 60L * 1000L);
        }
        
        final DatabaseWrapper db = DataModel.get().getDatabase();
        final List<String> conversationsToDelete = new ArrayList<>();
        
        // Find conversations marked as deleted that are older than retention period
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.CONVERSATIONS_TABLE,
                    new String[] { ConversationColumns._ID },
                    ConversationColumns.DELETED_STATUS + " = 1 AND " +
                    ConversationColumns.DELETED_TIMESTAMP + " > 0 AND " +
                    ConversationColumns.DELETED_TIMESTAMP + " <= ?",
                    new String[] { String.valueOf(cutoffTimestamp) },
                    null, null, null);
                    
            while (cursor.moveToNext()) {
                conversationsToDelete.add(cursor.getString(0));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        // Permanently delete the old conversations
        int deletedCount = 0;
        for (String conversationId : conversationsToDelete) {
            if (BugleDatabaseOperations.deleteConversation(db, conversationId, Long.MAX_VALUE)) {
                deletedCount++;
                LogUtil.i(TAG, "Auto-deleted old conversation: " + conversationId);
            }
        }
        
        if (deletedCount > 0) {
            LogUtil.i(TAG, "Auto-deleted " + deletedCount + " old conversations");
            MessagingContentProvider.notifyConversationListChanged();
        }
        
        return null;
    }
    
    protected AutoDeleteOldConversationsAction(final Parcel in) {
        super(in);
    }
    
    public static final Parcelable.Creator<AutoDeleteOldConversationsAction> CREATOR
            = new Parcelable.Creator<AutoDeleteOldConversationsAction>() {
        @Override
        public AutoDeleteOldConversationsAction createFromParcel(final Parcel in) {
            return new AutoDeleteOldConversationsAction(in);
        }
        
        @Override
        public AutoDeleteOldConversationsAction[] newArray(final int size) {
            return new AutoDeleteOldConversationsAction[size];
        }
    };
    
    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        writeActionToParcel(parcel, flags);
    }
}