package com.mysms.android.tablet.theme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysms.android.tablet.theme.dark.R;

public class ThemeActivity extends Activity {
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setVisibility(View.GONE);
		setContentView(layout);

		showDialog(0);
	}
	
	@Override
	protected Dialog onCreateDialog(final int id) {
        final String packageName = getString(R.string.app_package_name);
        final String marketUrl = getString(R.string.market_url, packageName);
		
		final View messageView = LayoutInflater.from(this).inflate(R.layout.theme_dialog, null);
		final TextView primaryMessageTextView = (TextView) messageView.findViewById(R.id.primary_message);
		final TextView secondaryMessageTextView = (TextView) messageView.findViewById(R.id.secondary_message);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(messageView);
		builder.setTitle(R.string.theme_dialog_title);

		/* check if mysms is installed */
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = getPackageManager().getApplicationInfo(packageName, 0);
		} catch (final Exception e) {
		}
		
		if (applicationInfo != null) {
			final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
					try {
						Intent i = getPackageManager().getLaunchIntentForPackage(packageName);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra(ThemeConstants.INTENT_EXTRA_THEME_ACTIVATE, getPackageName());
						startActivity(i);
					} catch (Exception e) {}
					finish();
				}
			};
			primaryMessageTextView.setText(R.string.theme_dialog_activate_primary_text);
			secondaryMessageTextView.setText(R.string.theme_dialog_activate_secondary_text);
			builder.setPositiveButton(R.string.theme_dialog_activate_text, listener);
		} else {

			primaryMessageTextView.setText(R.string.theme_dialog_install_primary_text);
			secondaryMessageTextView.setText(R.string.theme_dialog_install_secondary_text);

			final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
					try {
						final Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} catch (final Exception e) {
					}
					finish();
				}
			};
			builder.setPositiveButton(R.string.theme_dialog_install_text, listener);
		}

		builder.setNegativeButton(android.R.string.cancel, null);

		final Dialog dialog = builder.create();
		dialog.setOnDismissListener(new Dialog.OnDismissListener() {
			public void onDismiss(final DialogInterface dialog) {
				finish();
			}
		});
		return dialog;
	}
}
