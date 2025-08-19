package com.android.messaging.datamodel.data;

import android.content.Context;

public class DeletedConversationListData extends ConversationListData {
    
    public DeletedConversationListData(final Context context, 
            final ConversationListDataListener listener) {
        super(context, listener, false);
    }
    
    @Override
    protected String getConversationListWhereClause() {
        return "(" + ConversationListViewColumns.DELETED_STATUS + " = 1)";
    }
}