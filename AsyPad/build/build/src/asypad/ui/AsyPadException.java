package asypad.ui;

public class AsyPadException extends RuntimeException
{
	private static final long serialVersionUID = -6386696765076699700L;
	
	public AsyPadException()
	{
		super();
	}
	
	public AsyPadException(String err)
	{
		super(err);
	}
}
