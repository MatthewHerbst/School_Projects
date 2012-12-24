import java.util.Scanner;

/**
 * A class replicating the addition of two two-bit numbers with adders
 * No error checking is implemented in this code (bad input, etc.)
 * Support for any size number of bits
 * CSE 278 - 9/13/2012
 * @author Matthew Herbst
 */
public class AdderAddition {
	
	//Values entered by user
	static int firstValue;
	static int secondValue;
	
	//Values created by the half-adder
	static int halfAdderValue;
	static int halfAdderCarry;
	
	//Values created by the full adder
	static int fullAdderValue;
	static int fullAdderCarry;
	
	public static void main(String[] args)
	{
		//Initialize variables
		firstValue = 0;
		secondValue = 0;
		halfAdderValue = -1;
		halfAdderCarry = -1;
		fullAdderValue = -1;
		fullAdderCarry = -1;
		
		Scanner in = new Scanner(System.in);
		
		//Keep the program running until a negative input is received
		while(true)
		{
			//Get the first value
			System.out.print("Enter first number: ");
			while(!in.hasNext()){}
			firstValue = Integer.parseInt(in.next());
			
			if(firstValue < 0)
				System.exit(0);
			
			//Get the second value
			System.out.print("Enter second number: ");
			while(!in.hasNext()){}
			secondValue = Integer.parseInt(in.next());
			
			if(secondValue < 0)
				System.exit(0);
			
			//Add the values and print the results
			add(firstValue, secondValue);
		}
	}
	
	public static void add(int a, int b)
	{
		System.out.println("Result: ");
		
		//Print the versions
		System.out.println("Decimal version: " + a + " + " + b);
		System.out.println("Binary version is: " + getBitPattern(a) + " + " + getBitPattern(b));
		
		//Print the calculations
		System.out.println("Decimal result: " + (a+b));
		System.out.println("Binary result: " + bitwiseAdd(a, b) + "\n");
	}
	
	public static String bitwiseAdd(int a, int b)
	{
		int valOne = a;
		int valTwo = b;
		String bitPattern = "";
		
		//Do the half-adder
		halfAdder(valOne & 1, valTwo & 1);
		bitPattern = bitPattern + halfAdderValue;
		
		valOne = valOne >> 1;
		valTwo = valTwo >> 1;
		
		//Do as many full-adders as needed
		int mask = 1;
		int carryOver = halfAdderCarry;
		for(int i = 0; i < Integer.SIZE; i++)
		{
			fullAdder(valOne & 1, valTwo & 1, carryOver);
			carryOver = fullAdderCarry;
			bitPattern = fullAdderValue + bitPattern;
			
			valOne = valOne >> 1;
			valTwo = valTwo >> 1;
		}
		
		return bitPattern;
	}
	
	public static String getBitPattern(int in)
	{
		int input = in;
		int count = 0;
		int mask = 1;
		int bit = 0;
		
		String pattern = "";
		
		//Go through all bits
		for(int i = 0; i < Integer.SIZE; i++)
		{
			//Single & for bit wise AND
			bit = mask & input;
			
			if(bit == mask)
			{
				count++;
			}
			
			pattern = bit + pattern;
			
			//Bit-shift
			input = input >> 1;
		}
		
		return pattern;
	}
	
	public static void halfAdder(int firstOne, int secondOne)
	{
		//XOR
		halfAdderValue = firstOne ^ secondOne;
		
		//AND
		halfAdderCarry = firstOne & secondOne;
	}
	
	public static void fullAdder(int firstTen, int secondTen, int carryOver)
	{
		//XOR
		int tempXOR = firstTen ^ secondTen;

		//XOR
		fullAdderValue = tempXOR ^ carryOver;
		
		//OR
		fullAdderCarry = (firstTen & secondTen) | (tempXOR & carryOver);
	}
}