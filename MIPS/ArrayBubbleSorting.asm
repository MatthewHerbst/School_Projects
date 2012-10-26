#Matthew Herbst - herbstmb@muohio.edu
#Last updated 10/18/2012
#CSE 278
#Currently hard coded for 10 integer inputs

.data
	myArray:	.word	0:10	#Initilize the array
	prompt1:	.asciiz "\nPlease enter the next value: "
	status1: 	.asciiz "\nAll values entered, sorting..."
	status2:	.asciiz "\nArray sorted."
	arrayPrint1:	.asciiz	"\n["
	arrayPrint2:	.asciiz "] = "
.text

main:
	la	$t0, myArray		#Load the address of myArray into $t0
	li	$t1, 0			#Put the value of 0 into $t1

# Get input ----------------------------------------------------------------------------------------------------
getData:		
	#Ask user for new input
	li	$v0, 4
	la	$a0, prompt1
	syscall

	#Get the new value
	li	$v0, 5
	syscall

	sw	$v0, ($t0)		#Store what is in $v0 (user input) into the address of $t0 (myArray)
	add	$t0, $t0, 4		#Offset the array
	add	$t1, $t1, 1		#Add 1 to $t1

	blt	$t1, 10, getData	#If the value of $t1 is < 10 get more input
# End getData --------------------------------------------------------------------------------------------------

	li	$v0, 4
	la	$a0, status1
	syscall
	
# Sorting Logic ------------------------------------------------------------------------------------------------
	la	$t0, myArray		#Put the starting address of myArray into $a0
	add	$t1, $t0, 4		#Put the next value (ints, so +4) of myArray into $a1
	add	$t9, $t0, 36		#Put the address of the last element of myArray [4*(size-1)] into $t9

startSorting:
	lw	$a2, ($t0)		#Load the value at memory address $a0 into $a2
	lw	$a3, ($t1)		#Load the value at memory address $a1 into $a3

	blt	$a2, $a3, movePointer	#If $a2 < $a3 then the two are in order
	jal	swap			#If the two above are not in order, swap them
	
movePointer:
	add	$t1, $t1, 4		#Move $a1 to the next value
	ble	$t1, $t9, startSorting	#Keep sorting if $a1 is not past the last element in the array
	add	$t0, $t0, 4		#Move $a0 to the next value
	add	$t1, $t0, 4		#Reset $a1 to be the value after $a0
	ble	$t0, $t9, startSorting	#Keep sorting if $a0 is not past the last element in the array
	
	la	$t0, myArray		#Load the address of myArray into $t0
	li	$t1, 0			#Put the value of 0 into $t1
	j	printArray		#Sorting is done! Print the sorted array
	
swap:
	sw	$a2, ($t1)		#Store what is at memory address $t1 into $a2
	sw	$a3, ($t0)		#Store what is at memory address $t0 into $a3
	jr	$ra			#Jump back
# End sorting-------------------------------------------------------------------------------------------------------------

printArray:
	li	$v0, 4			#Put the value of 4 (print string) into $v0
	la	$a0, arrayPrint1
	syscall				#Print the first part of the bracket
	li	$v0, 1			#Put the value of 1 (print integer) into $v0
	la	$a0, ($t1)
	syscall				#Print the array index value
	li	$v0, 4			#Put the value of 4 (print string) into $v0
	la	$a0, arrayPrint2
	syscall				#Print the second part of the bracket
	li	$v0, 1			#Put the value of 1 (print integer) into $v0
	lw	$a0, ($t0)
	syscall				#Print the value in the array
	
	add	$t0, $t0, 4		#Offset the array
	add	$t1, $t1, 1		#Increase the array index
	blt	$t1, 10, printArray	#If the value of $t1 is < 10 print the next value
	
done:
	li	$v0, 10
	syscall
