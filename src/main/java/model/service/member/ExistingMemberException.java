package model.service.member;

public class ExistingMemberException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistingMemberException() {
		super();
	}

	public ExistingMemberException(String arg0) {
		super(arg0);
	}
}
