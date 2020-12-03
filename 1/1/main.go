package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
)

func main() {
	fileName := "input.txt"

	file, err := os.Open(fileName)

	if err != nil {
		log.Fatalf("Could not read file: %s", fileName)
	}

	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	// Part 1 (and some data for part 2)
	seen := make(map[int]int)
	numbers := make([]int, 0)

	for scanner.Scan() {
		number64, _ := strconv.ParseInt(scanner.Text(), 10, 64)
		number := int(number64)
		numbers = append(numbers, number)

		other, found := seen[2020-number]
		if found {
			fmt.Printf("Answer: %v\n", number*other)
		} else {
			seen[number] = number
		}
	}

	// Part 2
	type tuple struct {
		one int
		two int
	}
	seenSums := make(map[int]tuple)

	for i, n1 := range numbers {
		for j, n2 := range numbers[i+1:] {
			seenSums[n1+n2] = tuple{i, i + j + 1}
		}
	}

	for _, n3 := range numbers {
		rest := 2020 - n3
		otherTwo, found := seenSums[rest]
		if found && otherTwo.one != n3 && otherTwo.two != n3 {
			fmt.Printf("Answer 2: %v\n", n3*numbers[otherTwo.one]*numbers[otherTwo.two])
			break
		}
	}

}
