/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.messaging.datamodel.action;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.messaging.datamodel.BugleDatabaseOperations;
import com.android.messaging.datamodel.DataModel;
import com.android.messaging.datamodel.DatabaseWrapper;
import com.android.messaging.datamodel.MessagingContentProvider;
import com.android.messaging.util.Assert;

public class UpdateConversationDeletedStatusAction extends Action {

    public static void deleteConversation(final String conversationId) {
        final UpdateConversationDeletedStatusAction action =
                new UpdateConversationDeletedStatusAction(conversationId, true /* isDeleted */);
        action.start();
    }

    public static void undeleteConversation(final String conversationId) {
        final UpdateConversationDeletedStatusAction action =
                new UpdateConversationDeletedStatusAction(conversationId, false /* isDeleted */);
        action.start();
    }

    private static final String KEY_CONVERSATION_ID = "conversation_id";
    private static final String KEY_IS_DELETED = "is_deleted";

    protected UpdateConversationDeletedStatusAction(
            final String conversationId, final boolean isDeleted) {
        Assert.isTrue(!TextUtils.isEmpty(conversationId));
        actionParameters.putString(KEY_CONVERSATION_ID, conversationId);
        actionParameters.putBoolean(KEY_IS_DELETED, isDeleted);
    }

    @Override
    protected Object executeAction() {
        final String conversationId = actionParameters.getString(KEY_CONVERSATION_ID);
        final boolean isDeleted = actionParameters.getBoolean(KEY_IS_DELETED);

        final DatabaseWrapper db = DataModel.get().getDatabase();
        db.beginTransaction();
        try {
            BugleDatabaseOperations.updateConversationDeletedStatusInTransaction(
                    db, conversationId, isDeleted);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        MessagingContentProvider.notifyConversationListChanged();
        MessagingContentProvider.notifyConversationMetadataChanged(conversationId);
        return null;
    }

    protected UpdateConversationDeletedStatusAction(final Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<UpdateConversationDeletedStatusAction> CREATOR
            = new Parcelable.Creator<UpdateConversationDeletedStatusAction>() {
        @Override
        public UpdateConversationDeletedStatusAction createFromParcel(final Parcel in) {
            return new UpdateConversationDeletedStatusAction(in);
        }

        @Override
        public UpdateConversationDeletedStatusAction[] newArray(final int size) {
            return new UpdateConversationDeletedStatusAction[size];
        }
    };

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        writeActionToParcel(parcel, flags);
    }
}