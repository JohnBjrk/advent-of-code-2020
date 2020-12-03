package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

func main() {
	fileName := "input.txt"

	file, err := os.Open(fileName)

	if err != nil {
		log.Fatalf("Could not read file: %s", fileName)
	}

	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	numValid := 0
	numValid2 := 0

	for scanner.Scan() {
		line := scanner.Text()
		segments := strings.Split(line, " ")
		limits := strings.Split(segments[0], "-")
		lower64, _ := strconv.ParseInt(limits[0], 10, 64)
		lower := int(lower64)
		upper64, _ := strconv.ParseInt(limits[1], 10, 64)
		upper := int(upper64)
		char := segments[1][0]
		passwd := segments[2]
		num := strings.Count(passwd, string(char))

		// Part 1
		if num >= lower && num <= upper {
			numValid++
		}

		// Part 2
		numMatches := 0
		if passwd[lower-1] == char {
			numMatches++
		}
		if passwd[upper-1] == char {
			numMatches++
		}
		if numMatches == 1 {
			numValid2++
		}
	}
	fmt.Printf("Answer: %v\n", numValid)
	fmt.Printf("Answer2: %v\n", numValid2)
}
