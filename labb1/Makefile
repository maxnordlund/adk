VERSION := $(shell git tag -l v*.*.* | sort -r -n -t . | head -1)
GITSHA1 := $(shell git rev-parse --short HEAD)
GOFILES := $(shell find . -type f -and -name "*.go")
GOFLAGS := -ldflags "-X main.Version $(VERSION) -X main.GitSHA $(GITSHA1)"
DOCKER_IMAGE := konkordans-golang
DOCKER_CONTAINER := konkordans-build
DOCKER_SRC_PATH := /go/src/github.com/maxnordlund/adk/labb1

default: $(shell echo bin/$(VERSION)/{darwin,linux,windows}-{amd64,386})
	@true

# Remove built binaries and Docker containers
clean:
	$(eval NAMES := $(shell docker ps -a | tail -n +2 | grep -Eo "view-build_.*"))
	$(if $(NAMES),docker rm $(NAMES))
	go clean -i
	rm -fr bin/v*.*.*

# Build Docker container
docker: clean
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

docker-run: bin/$(VERSION)/$(shell go env GOHOSTOS)-$(shell go env GOHOSTARCH)
	@$< --index
	@$< komplexiteten

run: $(GOFILES)
	@go install
	@konkordans --index
	@konkordans komplexiteten
