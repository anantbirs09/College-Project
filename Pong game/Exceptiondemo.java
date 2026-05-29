class ExceptionDemo 
{
  public staic void main(string[] args)
  {
    int a,b,c;
	a=Integer.parseInt(args[0]);
	b=Integer.parseInt(args[1]);
	try
	{
	  c=a/b;
	   system.out.println("Division is "+c);
	 }
	  catch(ArithematicException e)
	  { 
	  system.out.println("Can not divide by zero...");
	     }
	    }
	}