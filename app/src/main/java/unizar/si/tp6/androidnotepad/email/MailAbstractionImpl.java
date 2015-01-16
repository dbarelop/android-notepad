package unizar.si.tp6.androidnotepad.email;

import android.app.Activity;
import android.util.Log;

/**
 * Created by dbarelop on 28/11/14.
 */
public class MailAbstractionImpl implements MailAbstraction {

	/**
	 * objeto delegado que facilita la implementación del método send
	 */
	private MailImplementor implementor;

	/**
	 * Constructor de la clase. Inicaliza el objeto delegado según el entorno de ejecución de la aplicación Android
	 *
	 * @param sourceActivity actividad desde la cual se abrirá la actividad encargada de gestionar el correo
	 */
	public MailAbstractionImpl(Activity sourceActivity) {
		String brand = android.os.Build.BRAND;
		// TODO: revisar
		if (brand.equals("generic")) {
            implementor = new BuiltInMailImplementor(sourceActivity);
            Log.d(MailAbstractionImpl.class.getName(), "Ejecutándose en un dispositivo real");
		} else {
            implementor = new JavaMailImplementor(sourceActivity);
            Log.d(MailAbstractionImpl.class.getName(), "Ejecutándose en el emulador");
		}
	}

	/**
	 * Envía el correo con el asunto (subject) y cuerpo (body) que se reciben como parámetros a través de un objeto delegado
	 *
	 * @param subject asunto
	 * @param body    cuerpo del mensaje
	 */
	public void send(String subject, String body) {
		implementor.send(subject, body);
	}
}
