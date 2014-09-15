package index

import (
	"bytes"
	"encoding/binary"
	"github.com/maxnordlund/adk/labb1/lexer"
	"os"
	"sort"
)

// Index of three letter prefix to first word in search index file
type lazyIndex map[uint64]filePointer

func NewLazyIndex(name string, si searchIndex) (li lazyIndex, err error) {
	search, err := os.Create(name)
	if err != nil {
		return
	}
	li = make(lazyIndex)

	words := make([]string, 0, len(si))
	for word, _ := range si {
		words = append(words, word)
	}
	sort.Strings(words)

	for i, word := range words {
		buf := bytes.NewBuffer(make([]byte, 0, SIZE))
		err = binary.Write(buf, binary.LittleEndian, si[word])
		if err != nil {
			return
		}
		_, err = search.Write(buf.Bytes())
		if err != nil {
			return
		}
		li.add(word, filePointer(i*SIZE))
	}
	err = search.Close()
	return
}

// Adds a index position for a word, only the minimum position is stored
func (li lazyIndex) add(word string, position filePointer) {
	i := lexer.Hash(word)
	if position < li[i] {
		li[i] = position
	}
}

func (li lazyIndex) save(name string) (err error) {
	lazy, err := os.Create(name)
	if err != nil {
		return
	}
	err = binary.Write(lazy, binary.LittleEndian, uint64(0))
	if err != nil {
		return
	}
	for i := uint64(1); i <= lexer.MAX_VALUE; {
		j := i
		for ; li[j] == 0; j++ {
			// Find first non zero position
		}

		// Update i with the amount of gaps skipped
		i += j - i + 1
		position := li[j]

		// Fill all gaps with the position found above
		// It's OK to do it in reverse order since all gaps are filled with the same
		// value, aka the first non zero position found above
		for ; i <= j; j-- {
			err = binary.Write(lazy, binary.LittleEndian, position)
			if err != nil {
				return
			}
		}
	}
	err = lazy.Close()
	return
}
