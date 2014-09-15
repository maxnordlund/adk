package index

import (
	"unsafe"
)

// Pointer within a file
type filePointer uint64

const SIZE = int(unsafe.Sizeof(filePointer(0)))
