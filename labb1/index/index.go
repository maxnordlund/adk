package index

import (
	"unsafe"
)

// Pointer within a file
type filePointer uint64

const SIZE = int(unsafe.Sizeof(filePointer(0)))

func Create() (err error) {
	fi, err := NewFileIndex("data/korpus")
	if err != nil {
		return
	}
	si, err := NewSearchIndex("data/index", fi)
	if err != nil {
		return
	}
	li, err := NewLazyIndex("data/search", si)
	if err != nil {
		return
	}
	err = li.save("data/lazy")
	return
}
