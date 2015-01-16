package unizar.si.tp6.androidnotepad.email;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by dbarelop on 28/11/14.
 */
public class JavaMailImplementor implements MailImplementor {
	private Activity sourceActivity;

	public JavaMailImplementor(Activity sourceActivity) {
		setSourceActivity(sourceActivity);
	}

	@Override
	public void setSourceActivity(Activity sourceActivity) {
		this.sourceActivity = sourceActivity;
	}

	@Override
	public void send(String subject, String body) {
		Intent i = new Intent(sourceActivity, JavaMailActivity.class);
		i.putExtra(JavaMailActivity.KEY_SUBJECT, subject);
		i.putExtra(JavaMailActivity.KEY_BODY, body);
		sourceActivity.startActivity(i);
	}
}
