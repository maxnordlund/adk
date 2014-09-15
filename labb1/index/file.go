package index

import (
	"github.com/maxnordlund/adk/labb1/lexer"
	"io"
	"os"
)

// Index of word positions in the lexicon file
type fileIndex map[string][]filePointer

func NewFileIndex(name string) (fi fileIndex, err error) {
	korpus, err := os.Open(name)
	if err != nil {
		return
	}
	tokenizer := lexer.New(korpus)
	fi = make(fileIndex)
	var (
		advance  uint64
		word     string
		position filePointer
	)
	for err == nil {
		advance, word, err = tokenizer.ReadToken()
		position += filePointer(advance)
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
