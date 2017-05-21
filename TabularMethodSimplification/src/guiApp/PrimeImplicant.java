package guiApp;

public class PrimeImplicant {
	
	private int mintermDigit;
	private boolean visited;
	private boolean flagD;
	private char[] differenceBits;
	private int indexNumber = 0;
	
	public PrimeImplicant(int mintermDigit, int bitSize)
	{
		this.mintermDigit = mintermDigit;
		this.visited = false;
		this.flagD = false;
		this.differenceBits = new char[bitSize];
		for(int i = 0 ; i < bitSize ; i++)
		{
			this.differenceBits[i] = '0';
		}
	}
	
	public void setDigit(int value)
	{
		this.mintermDigit = value;
	}
	
	public void setVisited(boolean flag)
	{
		this.visited = flag;
	}
	
	public void setDifferenceBit(int value)
	{
		this.differenceBits[log2(value)] = 'x';
	}
	
	public void setValueBit(int value) // will be used in print by cloning the array //pure print
	{
		if(value == 0)
			return;
		if(Integer.bitCount(value) == 1)
			this.differenceBits[log2(value)] = '1';
		else // goes to the print function
		{
			String binaryRep = Integer.toBinaryString(value);
			for(int i = binaryRep.length() - 1 ; i >= 0 ; i--)
			{
				if(this.differenceBits[binaryRep.length() - i - 1] == '0' && binaryRep.charAt(i) == '1')
					this.differenceBits[binaryRep.length() - i - 1] = '1';
			}
		}
	}
	
	public void setArrayBits(char[] differenceBits)
	{
		this.differenceBits = differenceBits;
	}
	
	public void setflagD()
	{
		this.flagD = true;
	}
	
	public boolean flagD()
	{
		return this.flagD;
	}
	
	public boolean isVisited()
	{
		return this.visited;
	}
	
	public int getValue()
	{
		return this.mintermDigit;
	}
	
	public int getDifferenceBit(int index) // not used tii now
	{
		return this.differenceBits[index];
	}
	
	public char[] getArrayBits()
	{
		return this.differenceBits;
	}
	
	private static int log2(int n){
	    if(n <= 0) throw new IllegalArgumentException();
	    
	    if(Integer.bitCount(n) == 1)
	    	return 31 - Integer.numberOfLeadingZeros(n);
	    else
	    	return -1;
	}
	
	public void setIndex(int n)
	{
		this.indexNumber = n;
	}
	
	public int getIndex()
	{
		return this.indexNumber;
	}

}
