#Matthew Herbst - herbstmb@muohio.edu
#Last updated 10/17/2012
#CSE 278

.data
	in:		.word 	-1	#User input storage
	arraySize:	.word	10	#The size of the array
	myArray:	.word	0:10	#Initilize the array
	counter:	.word	0	#Initialize a counter value at 0
	prompt1:	.asciiz "\nPlease enter the next value: "
	status1: 	.asciiz "\nAll values entered, sorting..."
	status2:	.asciiz "\nArray sorted."
	arrayPrint1:	.asciiz	"\n["
	arrayPrint2:	.asciiz "] = "
.text

main:
	la	$t0, myArray		#Load the address of myArray into $t0
	lw	$t1, counter		#Put the value of counter into $t1
	
	jal	getData			#Jump to getData
	
	#la	$t0, myArray		#Load the address of myArray into $t0
	#lw	$t1, counter		#Put the value of counter into $t1
	#la	$t0, myArray		#Load the address of myArray into $t0
	#jal	printArray		#Jump to printArray
	
	li	$v0, 4
	la	$a0, status1
	syscall
	
	la	$t0, myArray		#Load the address of myArray into $t0
	lw	$t1, counter		#Put the value of counter into $t1
	#lw	$t5, arraySize		#Put the value of arraySize into $t4
	#sub	$t6, $t5, 1		#Put $t5 (arraySize) minus 1 into $t6
	jal	loop1			#Start the sorting/looping!
	
	#la	$t0, myArray		#Load the address of myArray into $t0
	#lw	$t1, counter		#Put the value of counter into $t1
	#la	$t0, myArray		#Load the address of myArray into $t0
	#jal	printArray		#Jump to printArray
	
	j	done			#Jump to done

getData:		
	#Ask user for new input
	li	$v0, 4
	la	$a0, prompt1
	syscall
	
	#Get the new value
	li	$v0, 5
	syscall
	sw	$v0, in
	
	#Put the new data into the array
	sw	$v0, ($t0)		#Store what is in $v0 (user input) into the address of $t0 (myArray)
	add	$t0, $t0, 4		#Offset the array
	add	$t1, $t1, 1		#Add 1 to $t1 (counter)
	
	blt	$t1, 10, getData	#While the value of $t1 (counter) is < $t5 (arraySize) get more input
	
	jr	$ra			#Jump back

#--------------------------------Sorting Logic----------------------------------------------------------------
loop1: 	
	blt	$t1, 9, loop2		#Keep looping while $t1 (counter) is < $t6 (arraySize - 1)
	jr	$ra			#Loops have finished, jump back
	
loop2: 	
	add	$t1, $t1, 1		#Increment the counter for the first loop
	sw	$t1, ($t2)		#Put the value of $t1 (counter) into $t2
	blt	$t2, 10, sortLogic	#Keep looping while $t2 < $t5 (arraySize)
	j	loop1			#Jump back to the first loop
	
sortLogic:
	add	$t2, $t2, 1		#Increment the counter for the second loop
	
	la	$a1, ($t0)		#Load the array element at ($t0) into $a1
	add	$t0, $t0, 4		#Offset the array
	la	$a2, ($t0)		#Load the array element at ($t0) into $a2
	
	sub	$t4, $a2, $a1		#Subtract $a1 from $a2 
	bltz	$t4, swap		#If the comparison above is < 0 ($a1 > $a2), swap
	
	#NEED TO JUMP BACK TO LOOP2
	
swap:
	sw	$a2, ($t3)		#Store what is in $a2 into $t3
	sw	$a1, ($a2)		#Store what is in $a1 into $a2
	sw	$t3, ($a1)		#Store what is in $t3 into $a1
	
	#NEED TO JUMP BACK TO SORTLOGIC
#-------------------------------------------------------------------------------------------------------------
	
printArray:
	li	$v0, 4
	la	$a0, arrayPrint1
	syscall				#Print the first part of the bracket
	li	$v0, 4
	la	$a0, ($t1)
	syscall				#Print the counter value (array index)
	li	$v0, 4
	la	$a0, arrayPrint2
	syscall				#Print the second part of the bracket
	li	$v0, 4
	la	$a0, ($t0)
	syscall				#Print the value in the array
	add	$t0, $t0, 1		#Offset the array
	
	blt	$t1, 10, printArray	#While the value of $t1 (counter) is < $t5 (arraySize) keep printing
	
	jr	$ra			#Jump back

done:
	li	$v0, 10
	syscall
