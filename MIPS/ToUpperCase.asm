#Matthew Herbst - herbstmb@muohio.edu
#Last updated 10/15/2012
#CSE 278
#Convert a string to upper case

.data
	myString:	.asciiz	"Hello World"
	stringPrint1:	.asciiz "Original string: "
	stringPrint2:	.asciiz	"\nNew string: "
	
.text

main:
	la	$t0, myString		#Load the address of myString into $t0
	lb	$t1, ($t0)		#Load the first byte (first char) of the string into $t1
	
	li	$v0, 4			
	la	$a0, stringPrint1
	syscall				#Print stringPrint1
	
	li	$v0, 11			#Put the value of 11 (print char) into $v0
	jal	printString		#Print the original string

baseCase:	
	beq	$t1, 0, done		#If the string is of size 0, be done
	j	toLowerCase		#Else, jump to toLowerCase
	
movePointer:
	add	$t0, $t0, 1		#Offset the string
	lb	$t1, ($t0)		#Load the first byte (first char) of the string into $t1	
	beq	$t1, 0, done		#If the end of the string has been found, be done
	
toLowerCase:
	blt	$t1, 97, movePointer	#If the value is < A go to the next value
	bgt	$t1, 122, movePointer	#If the value is > Z go to the next value

	sub	$t1, $t1, 32		#Subtract 32 from $a0 to convert to upper case
	sb	$t1, ($t0)		#Store the now converted value back at the address of $t1
	j	movePointer		#Go to the next value
	
printString:
	lb	$a0, ($t0)
	syscall				#Print the loaded char
	add	$t0, $t0, 1		#Offset the string
	bgt	$a0, 0, printString	#If there are chars left to print then keep printing
	jr	$ra			#Jump back to where this instance of printString was called
	
done:
	li	$v0, 4			
	la	$a0, stringPrint2
	syscall				#Print stringPrint2
	
	la	$t0, myString		#Load the address of myString into $t0
	li	$v0, 11			#Put the value of 11 (print char) into $v0
	jal	printString		#Print the converted string
	
	li	$v0, 10
	syscall				#Clean system exit
