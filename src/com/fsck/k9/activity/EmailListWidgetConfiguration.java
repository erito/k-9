package com.fsck.k9.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fsck.k9.BaseAccount;
import com.fsck.k9.provider.EmailListWidgetProvider;

/*
 * Activity that configures the which account we will be pulling from.  Much of the configuration is
 * pulled from UnreadWidgetConfiguration.  Maybe a TODO is to abstract this functionality into another
 * class to inherit from.
 */

public class EmailListWidgetConfiguration extends AccountList {
    
	
	/**
     * Name of the preference file to store the widget configuration.
     */
	
    private static final String PREFS_NAME = "list_widget_configuration.xml";

    /**
     * Prefix for the preference keys.
     */
    private static final String PREF_PREFIX_KEY = "list_widget.";
    
    /**
     * The ID of the widget we are configuring.
     */
    
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	@Override
	protected boolean displaySpecialAccounts() {
		return true;
	}

	@Override
	protected void onAccountSelected(BaseAccount account) {
		
		String accountUuid = account.getUuid();
		saveAccountUuid(this, mAppWidgetId, accountUuid);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		EmailListWidgetProvider.initializeListWidget(getApplicationContext(), appWidgetManager, mAppWidgetId, accountUuid);
		
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
		
	}
	   
	private static void saveAccountUuid(Context context, int appWidgetId, String accountUuid) {
	        SharedPreferences.Editor editor =
	                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
	        editor.putString(PREF_PREFIX_KEY + appWidgetId, accountUuid);
	        editor.commit();
	    }

	    public static String getAccountUuid(Context context, int appWidgetId) {
	        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	        String accountUuid = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
	        return accountUuid;
	    }

	    public static void deleteWidgetConfiguration(Context context, int appWidgetId) {
	        Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
	        editor.remove(PREF_PREFIX_KEY + appWidgetId);
	        editor.commit();
	    }
}
