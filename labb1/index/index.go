package index

import (
	"bytes"
	"encoding/binary"
	"encoding/gob"
	"github.com/maxnordlund/adk/labb1/lexer"
	"io"
	"math"
	"os"
	"sort"
	"unsafe"
)

// Pointer within a file
type filePointer uint64

const SIZE = unsafe.Sizeof(filePointer(0))

func Create() (err error) {
	fi, err := NewFileIndex("data/korpus")
	if err != nil {
		return
	}
	si, err := NewSearchIndex("data/index", fi)
	if err != nil {
		return
	}
	li, err := NewLazyHashIndex("data/search", si)
	if err != nil {
		return
	}
	err = li.save("data/lazy")
	return
}

// Index of word positions in the lexicon file
type fileIndex map[string][]filePointer

func NewFileIndex(name string) (fi fileIndex, err error) {
	korpus, err := os.Open(name)
	if err != nil {
		return
	}
	tokenizer := lexer.New(korpus)
	fi = make(fileIndex)

	for position := 0; err == nil; advance, word, err := tokenizer.ReadToken() {
		position += advance
		fi.add(word, position)
	}
	if err != io.EOF {
		return
	}
	err = korpus.Close()
	return
}

// Adds a lexicon position for a word
func (fi fileIndex) add(word string, position filePointer) {
	fi[word] = append(fi[word], position)
}

// Index of word positions in the index file
type searchIndex map[string]filePointer

func NewSearchIndex(name, fi fileIndex) (si searchIndex, err error) {
	index, err := os.Open("data/index")
	if err != nil {
		return
	}
	si = make(searchIndex)
	position := 0
	for word, positions := range fi {
		buf := bytes.NewBuffer(make([]byte, 0, len(word)+len(positions)*SIZE))
		enc := gob.NewEncoder(buf)
		err = enc.Encode(word)
		if err != nil {
			return
		}
		err = enc.Encode(positions)
		if err != nil {
			return
		}
		n, err := index.Write(buf.Bytes())
		if err != nil {
			return
		}
		si.add(word, position)
		position += n
	}
	err = index.Close()
	return
}

// Adds a index position for a word
func (si searchIndex) add(word string, position filePointer) {
	si[word] = position
}

// Index of three letter prefix to first word in search index file
type lazyIndex map[uint64]filePointer

func NewLazyIndex(name string, si searchIndex) (li lazyIndex, err error) {
	search, err := os.Open(name)
	if err != nil {
		return
	}
	li = make(lazyIndex)

	words := make([]string, 0, len(si))
	for word, _ := range si {
		words = append(words, word)
	}
	words = sort.Strings(words)

	for i, word := range words {
		buf := bytes.NewBuffer(make([]byte, 0, SIZE))
		err = binary.Write(buf, binary.LittleEndian, si[word])
		if err != nil {
			return
		}
		_, err := search.Write(buf.Bytes())
		if err != nil {
			return
		}
		li.add(word, i*SIZE)
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
	lazy, err := os.Open(lazyPath)
	if err != nil {
		return
	}
	err = binary.Write(lazy, binary.LittleEndian, uint64(0))
	if err != nil {
		return
	}
	for i := 1; i <= uint64(math.Pow(lexer.BASE, 3))+1; {
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
