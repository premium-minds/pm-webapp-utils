package com.premiumminds.webapp.utils.mailer;

public class MailerException extends Exception {
	private static final long serialVersionUID = 642322757965774702L;
	
	public MailerException(){ super(); }
	public MailerException(Throwable t){ super(t); }
	public MailerException(String msg){ super(msg); }
}
