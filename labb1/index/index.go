package index

import (
	"bytes"
	"encoding/gob"
	"github.com/maxnordlund/adk/labb1/lexer"
	"io"
	"os"
)

const SIZE = 8

// Pointer within a file
type filePointer uint64

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

func Create() (err error) {
	return
}
