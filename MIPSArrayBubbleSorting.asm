#Matthew Herbst - herbstmb@muohio.edu
#Last updated 10/17/2012
#CSE 278

.data
	num1:		.word 	99
	myArray:	.word	0:10	#Initilize the array
	counter:	.word	0	#Initialize a counter value at 0
	prompt1:	.asciiz "\nPlease enter the next value: "
	status1: 	.asciiz "\nAll values entered, sorting..."
	status2:	.asciiz "\nArray sorted."
.text

main:
	la	$t0, myArray		#Load the address of myArray into $t0
	lw	$t1, counter		#Put the value of counter into $t1
	
	jal	getData			#Jump to getData
	
	jal	printArray		#Jump to printArray
	
	li	$v0, 4
	la	$a0, status1
	syscall
	
	la	$t0, myArray		#Load the address of myArray into $t0
	lw	$t1, counter		#Put the value of counter into $t1
	jal	loop1			#Start the sorting/looping!
	
	la	$t0, myArray		#Load the address of myArray into $t0
	jal	printArray		#Jump to printArray
	
	j	done			#Jump to done

getData:		
	#Ask user for new input
	li	$v0, 4
	la	$a0, prompt1
	syscall
	
	#Get the new value
	li	$v0, 5
	syscall
	sw	$v0, num1
	
	#Put the new data into the array
	sw	$v0, ($t0)		#Store what is in $v0 (user input) into the address of $t0 (myArray)
	add	$t0, $t0, 4		#Offset the array
	add	$t1, $t1, 1		#Add 1 to $t1 (counter)
	
	blt	$t1, 10, getData	#If the value of $t1 (counter) is < 10 get more input
	
	jr	$ra			#Jump back

#--------------------------------Sorting Logic----------------------------------------------------------------
loop1: 	
	blt	$t1, 9, loop2		#Keep looping while $t1 (counter) is < 9
	jr	$ra			#loops have finished, jump back
	
loop2: 	
	add	$t1, $t1, 1		#Increment the counter for the first loop
	lw	$t2, ($t1)		#Put the value of $t1 into $t2
	blt	$t2, 10, sortLogic		#Keep looping while $t2 < 10
	
sortLogic:
	la	$a1, ($t0)		#Load the array element at ($t0) into $a1
	add	$t0, $t0, 4		#Offset the array
	la	$a2, ($t0)		#Load the array element at ($t0) into $a2
	sub	$t4, $a1, $a2		#Subtract $a2 from $a1 
	bltz	$t4, swap		#If the comparison above is < 0 (then $a2 > $a1), swap
		
	j	loop1			#Jump back to the first loop
	
swap:
	sw	$a2, $t3		#Store what is in $a2 into $t3
	sw	$a1, $a2		#Store what is in $a1 into $a2
	sw	$t3, $a1		#Store what is in $t3 into $a1
	
	j	loop1			#Jump back to the first loop
#-------------------------------------------------------------------------------------------------------------
	
printArray:
	la	$a0, "\n["
	syscall
	la	$a0, $t2
	syscall
	la	$a0, "] = "
	syscall
	la	$a0, 0($t0) 		#Load the value of the array element into $t2
	syscall
	
	add	$t2, $t2, 1		#Increment $t2
	
	blt	$t2, 10, printArray	#If the value of $t1 (counter) is < 10 keep printing
	
	jr	$ra			#Jump back

done:
	li	$v0, 10
	syscall
