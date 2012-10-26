#Matthew Herbst - herbstmb@muohio.edu
#Last updated 10/15/2012
#CSE 278
#Fill an predefined-sized array and fill it with a fibonacci sequence
#Currently requires an array of at least size 2

.data
	myArray:	.word	1:12	#Initilize the array
	arrayPrint1:	.asciiz	"\n["	#For printing the array
	arrayPrint2:	.asciiz "] = "	#For printing the array
.text

main:
	la	$t0, myArray		#Load the address of myArray into $t0
	add	$t1, $t0, 4		#Load the address of myArray + 4 into $t1
	add	$t9, $t0, 40		#Put the address of the last element of myArray [4*(size-1)] into $t9
	
	ble	$t1, $t9, genFib	#If there are numbers to generate, start generating!
	j	printArray		#Else, print out the current array **Only occurs with an array of size 2
	
genFib:
	add	$t2, $t1, 4		#Load the address of myArray + 8 (the element to be set) into $t2
	lw	$a0, ($t0)		#Load the value of myArray into $a0
	lw	$a1, ($t1)		#Load the value of myArray + 4 into $a1
	add	$a2, $a0, $a1		#Store the value of $a0 + $a1 into $a2
	sw	$a2, ($t2)		#Store $a2 into the address of $t2 (myArray + 8)
	add	$t0, $t0, 4		#Offset the first pointer
	add	$t1, $t1, 4		#Offset the second pointer
	ble	$t1, $t9, genFib	#If there are numbers to generate, keep generating!

	la	$t0, myArray		#Load the address of myArray into $t0
	li	$t8, 0			#Put the value of 0 into $t1
printArray:
	li	$v0, 4			#Put the value of 4 (print string) into $v0
	la	$a0, arrayPrint1
	syscall				#Print the first part of the bracket
	li	$v0, 1			#Put the value of 1 (print integer) into $v0
	la	$a0, ($t8)
	syscall				#Print the array index value
	li	$v0, 4			#Put the value of 4 (print string) into $v0
	la	$a0, arrayPrint2
	syscall				#Print the second part of the bracket
	li	$v0, 1			#Put the value of 1 (print integer) into $v0
	lw	$a0, ($t0)
	syscall				#Print the value in the array
	
	add	$t0, $t0, 4		#Offset the array
	add	$t8, $t8, 1		#Increase the array index
	blt	$t8, 12, printArray	#If the value of $t1 is < 12 print the next value
	
done:
	li	$v0, 10
	syscall

	