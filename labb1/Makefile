VERSION    := $(shell git tag -l v*.*.* | sort -r -n -t . | head -1)
GITSHA1    := $(shell git rev-parse --short HEAD)
GOFILES    := $(shell find . -type f -and -name "*.go")
GOHOSTOS   := $(shell go env GOHOSTOS)
GOHOSTARCH := $(shell go env GOHOSTARCH)
GOFLAGS    := -ldflags "-X main.Version $(VERSION) -X main.GitSHA $(GITSHA1)"
DOCKER_IMAGE     := konkordans-golang
DOCKER_CONTAINER := konkordans-build
DOCKER_SRC_PATH  := /go/src/github.com/maxnordlund/adk/labb1

default: $(shell echo bin/$(VERSION)/{darwin,linux,windows}-{amd64,386})
	@true

# Remove built binaries and Docker containers
clean:
	$(eval DOCKER_HOST := $(shell boot2docker shellinit | \
		sed "s/ * export DOCKER_HOST=//"))
	$(eval NAMES := $(shell DOCKER_HOST=$(DOCKER_HOST) docker ps -a | \
		grep -Eo "konkordans-build_.*"))
	$(if $(NAMES),docker rm $(NAMES))
	go clean -i
	rm -fr bin/v*.*.* data/{index,lazy,search}

# Build Docker container
docker:
	docker build -t "$(DOCKER_IMAGE)" .

# Compile the program using the above container
bin/%: docker
	docker run --name "$(DOCKER_CONTAINER)_$(*F)" "$(DOCKER_IMAGE)" build/$*
	docker cp "$(DOCKER_CONTAINER)_$(*F)":"$(DOCKER_SRC_PATH)/build/$*$(if \
		$(filter windows-%, $(*F)),.exe)" $(@D) || true
	docker rm "$(DOCKER_CONTAINER)_$(*F)"

# Native Go build per OS-ARCH combo, inside container.
build/%: $(GOFILES)
	GOOS=$(subst -, GOARCH=,$(*F)) go build $(GOFLAGS) -o \
		$@$(if $(filter windows-%, $(*F)),.exe)

# Automatically download the needed korpus
data/korpus:
	scp share-02.csc.kth.se:/info/adk14/labb1/korpus $@

docker-run: bin/$(VERSION)/$(GOHOSTOS)-$(GOHOSTARCH) data/korpus
	@$< --index
	@$< komplexiteten

install: $(GOFILES) data/korpus
	@go install

run: install
	@labb1 --index
	@labb1 komplexiteten

.PHONY: clean default docker docker-run install run
