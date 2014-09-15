package lexer

import (
	"bufio"
	"code.google.com/p/go.text/transform"
	"io"
)

// Alphabet in Latin-1 order
const (
	ALPHABET  = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÅÖ"
	BASE      = uint64(len(ALPHABET) + 1)
	MAX_VALUE = BASE*BASE*BASE + 1
)

// Normalization translation table
var normalize table

type table map[byte]byte

func (t *table) Transform(dst, src []byte, atEOF bool) (int, int, error) {
	for i, b := range src {
		dst[i] = t[b]
	}
	return len(src), len(src), nil
}

func init() {
	var ch rune
	runes := make(map[rune]rune)
	table = make(map[byte]byte)

	// Början på översatt kod från tokenizer.c

	/*
	 * tokenizer.c delar upp en text i sina ord och matar ut orden
	 * omvandlade till små bokstäver, ett ord per rad följt av ordets
	 * teckenposition i texten. Ett ord definieras som en eller flera
	 * på varandra följande bokstäver i det svenska alfabetet.
	 * Copyright Viggo Kann, viggo@nada.kth.se, 1999
	 *
	 * Lätt omhackad av Jakob Nordström 2003 för att klara tecken med accenter
	 * (t.ex. á, è. ü osv.).
	 */

	for i := rune(0); i < 256; i++ {
		runes[i] = 0
	}

	for _, s := range ALPHABET {
		ch = s + 'a' - 'A'
		runes[s] = ch
		runes[ch] = ch
	}

	/*
	 * Nedan följer speciallösning för att klara accenterade tecken
	 *
	 */

	for ch = 224; ch <= 227; ch++ { /* a med accent (utom å och ä) */
		runes[ch] = 'a'
		runes[ch+-'a'+'A'] = 'a'
	}

	ch = 230 /* ae till ä */
	runes[ch] = 'ä'
	runes[ch+-'a'+'A'] = 'ä'

	ch = 231 /* c med cedilj till c*/
	runes[ch] = 'c'
	runes[ch+-'a'+'A'] = 'c'

	for ch = 232; ch <= 235; ch++ { /* e med accent (även é) */
		runes[ch] = 'e'
		runes[ch+-'a'+'A'] = 'e'
	}

	for ch = 236; ch <= 239; ch++ { /* i med accent */
		runes[ch] = 'i'
		runes[ch+-'a'+'A'] = 'i'
	}

	ch = 241 /* n med ~ rill n */
	runes[ch] = 'n'
	runes[ch+-'a'+'A'] = 'n'

	for ch = 242; ch <= 245; ch++ { /* o med accent (förutom ö) */
		runes[ch] = 'o'
		runes[ch+-'a'+'A'] = 'o'
	}

	ch = 248 /* o genomskuret till ö */
	runes[ch] = 'ö'
	runes[ch+-'a'+'A'] = 'ö'

	for ch = 249; ch <= 252; ch++ { /* u med accent */
		runes[ch] = 'u'
		runes[ch+-'a'+'A'] = 'u'
	}

	ch = 253 /* y med accent */
	runes[ch] = 'y'
	runes[ch+-'a'+'A'] = 'y'
	ch = 255
	runes[ch] = 'y'
	runes[ch+-'a'+'A'] = 'y'

	// Slut på översatt kod från tokenizer.c

	for from, to := range runes {
		table[byte(from)] = byte(to)
	}
}

func Hash(word string) uint64 {
	switch len(word) {
	case 0:
		return 0
	case 1:
		return hash(word[0])
	case 2:
		return hash(word[1]) + hash(word[0])*BASE
	default:
		return hash(word[2]) + hash(word[1])*BASE + hash(word[0])*BASE*BASE
	}
}

func hash(b byte) rune {
	if table[b] == 0 {
		return 0
	} else {
		return table[b] - 'a' + 1
	}
}

type tokenizer struct {
	input    *bufio.Reader
	position uint64
}

func New(reader io.Reader) *tokenizer {
	return &tokenizer{bufio.NewReader(transform.NewReader(reader, normalize)), 0}
}

func (t *tokenizer) ReadToken() (advance uint64, token string, err error) {
	// Consume non-alphabet characters
	for ch := byte(0); ch == 0; ch, err = t.input.ReadByte() {
		advance++
		if err != nil {
			return
		}
	}

	err = t.input.UnreadByte()
	if err != nil {
		return
	}
	advance--

	token, err = t.input.ReadString(0)
	if len(token) > 0 {
		// Remove the trailing zero byte
		advance += uint64(len(token))
		token = token[:len(token)-1]
	}
	return
}
