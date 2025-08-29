package com.android.messaging.ui.conversationlist;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.messaging.R;
import com.android.messaging.util.DebugUtils;

import androidx.appcompat.app.ActionBar;

public class DeletedConversationListActivity extends AbstractConversationListActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ConversationListFragment fragment =
                ConversationListFragment.createDeletedConversationListFragment();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        invalidateActionBar();
    }

    protected void updateActionBar(ActionBar actionBar) {
        actionBar.setTitle(getString(R.string.deleted_activity_title));
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(
                        R.color.archived_conversation_action_bar_background_color_dark)));
        actionBar.show();
        super.updateActionBar(actionBar);
    }

    @Override
    public void onBackPressed() {
        if (isInConversationListSelectMode()) {
            exitMultiSelectState();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (super.onCreateOptionsMenu(menu)) {
            return true;
        }
        getMenuInflater().inflate(R.menu.archived_conversation_list_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_debug_options);
        if (item != null) {
            final boolean enableDebugItems = DebugUtils.isDebugEnabled();
            item.setVisible(enableDebugItems).setEnabled(enableDebugItems);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_debug_options) {
            onActionBarDebug();
            return true;
        }
        if (itemId == android.R.id.home) {
            onActionBarHome();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActionBarHome() {
        onBackPressed();
    }

    @Override
    public boolean isSwipeAnimatable() {
        return false;
    }
}