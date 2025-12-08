package br.com.webmobi.dadospessoais.webmvc.util;

public class AlertMessage {

	private final String text;

	private final AlertMessageType type;

	private final String bsAlertClassName;

	public AlertMessage(AlertMessageType type, String text) {
		this.text = text;
		this.type = type;
		this.bsAlertClassName = "alert-" + this.type.name().toLowerCase();
	}

	public String getText() {
		return text;
	}

	public AlertMessageType getType() {
		return type;
	}

	public String getBsAlertClassName() {
		return bsAlertClassName;
	}

	// Baseado nos tipos do Bootstrap 5
	// https://getbootstrap.com/docs/5.3/components/alerts/
	public static enum AlertMessageType {

		PRIMARY,

		SECONDARY,

		SUCCESS,

		DANGER,

		WARNING,

		INFO,

		LIGHT,

		DARK;

	}


}
