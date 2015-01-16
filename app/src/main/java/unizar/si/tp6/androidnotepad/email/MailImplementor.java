package unizar.si.tp6.androidnotepad.email;

import android.app.Activity;

/**
 * Created by dbarelop on 28/11/14.
 */
public interface MailImplementor {

	/**
	 * Actualiza la actividad desde la cual se abrirá la actividad de gestión de correo
     *
     * @param source actividad desde la cual se abrirá la actividad encargada de gestionar el correo
	 */
	public void setSourceActivity(Activity source);

	/**
	 * Permite lanzar la actividad encargada de gestionar el correo enviado
     *
     * @param subject asunto
     * @param body    cuerpo del mensaje
	 */
	public void send(String subject, String body);

}
