package view.viewNotificacao.notification;

public enum Notifications {

	INFORMATION("view/viewNotificacao/info.png", "#2980b9", "#3498db"),
	NOTICE("view/viewNotificacao/notice.png", "#f39c12", "#f1c40f"),
	SUCCESS("view/viewNotificacao/success.png", "#27ae60", "#2ecc71"),
	WARNING("view/viewNotificacao/warning.png", "#c0392b", "#e74c3c"),
	ERROR("view/viewNotificacao/error.png", "#c0392b", "#e74c3c"),
	MENSAGEM("view/viewNotificacao/mensagem.png","#c7ecee","#dff9fb");

	private final String urlResource;
	private final String primaryColor;
	private final String secondColor;

	Notifications(String urlResource, String primaryColor, String secondColor) {
		this.urlResource = urlResource;
		this.primaryColor = primaryColor;
		this.secondColor = secondColor;
	}

	public String getURLResource() {
		return urlResource;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public String getSecondColor() {
		return secondColor;
	}

}
