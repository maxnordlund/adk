package index

import ()

// Pointer within a file
type filePointer uint64

// Index of word positions in the lexicon file
type fileIndex map[string][]filePointer

// Adds a lexicon position for a word
func (fi fileIndex) add(word string, position filePointer) {
	fi[word] = append(fi[word], position)
}

// Index of word positions in the index file
type searchIndex map[string]filePointer

// Adds a index position for a word
func (si searchIndex) add(word string, position filePointer) {
	si[word] = position
}

func Create() (err error) {
	return
}
