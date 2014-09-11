package main

import (
	"fmt"
	"github.com/maxnordlund/adk/labb1/index"
	flag "github.com/ogier/pflag"
	"os"
	"path"
	"strings"
)

// The following vars will be injected during the build process.
var (
	Version string
	GitSHA  string
)

var NAME string

func main() {
	NAME = path.Base(os.Args[0])
	flags := make(map[string]*bool)

	flag.Usage = usageLong
	flags["version"] = flag.BoolP("version", "v", false,
		"Print version number and exit")
	flags["help"] = flag.BoolP("help", "h", false,
		"Print this help message and exit")
	flags["index"] = flag.BoolP("index", "i", false,
		"Create indexes for the lexicon")

	flag.Parse()

	// Handle -v/--version
	if *flags["version"] {
		fmt.Println(Version)
		os.Exit(0)
	}

	// Handle -h/--help
	if *flags["help"] {
		if strings.Contains(strings.Join(os.Args, " "), "--help") {
			usageLong()
		} else {
			usageShort()
		}
		os.Exit(0)
	}

	// Handle -i/--index
	if *flags["index"] {
		fmt.Println("Creating indexes")
		if err := index.Create(); err != nil {
			fmt.Fprintf(os.Stderr, "Error creating indexes: %v", err)
			os.Exit(1)
		} else {
			fmt.Println("Done")
			os.Exit(0)
		}
	}

	// Handle missing <text> argument
	if flag.NArg() == 0 {
		fmt.Fprintf(os.Stderr, "%s: missing <text> argument\n", NAME)
		usageLong()
		os.Exit(1)
	}

	fmt.Printf("Searching for %q\n", flag.Arg(0))
}

func usageShort() {
	text := "Usage: %s [--version] [--help] --index\n"
	text += "       %s [--version] [--help] [--] <text>\n"
	fmt.Fprintf(os.Stderr, text, NAME, NAME)
}

func usageLong() {
	max := 0
	flags := [...]*flag.Flag{
		flag.Lookup("version"),
		flag.Lookup("help"),
		flag.Lookup("index"),
	}

	for _, flg := range flags {
		length := len(flg.Name)
		if length > max {
			max = length
		}
	}

	fmt.Printf("Usage: %s [options] [--] <text>\n\n", NAME)
	for _, flg := range flags {
		pad := strings.Repeat(" ", max-len(flg.Name))
		fmt.Printf(" -%s, --%s  %s  %s\n", flg.Shorthand, flg.Name, pad, flg.Usage)
	}
}
