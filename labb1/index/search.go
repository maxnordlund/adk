package index

import (
	"bytes"
	"encoding/gob"
	"os"
)

// Index of word positions in the index file
type searchIndex map[string]filePointer

func NewSearchIndex(name string, fi fileIndex) (si searchIndex, err error) {
	index, err := os.Create(name)
	if err != nil {
		return
	}
	si = make(searchIndex)
	position := filePointer(0)
	for word, positions := range fi {
		var n int
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
		n, err = index.Write(buf.Bytes())
		if err != nil {
			return
		}
		si.add(word, position)
		position += filePointer(n)
	}
	err = index.Close()
	return
}

// Adds a index position for a word
func (si searchIndex) add(word string, position filePointer) {
	si[word] = position
}
