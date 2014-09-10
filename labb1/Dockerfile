# Dockerfile to cross compile `konkordans`

FROM golang:1.3-cross

ADD . /go/src/github.com/maxnordlund/adk/labb1
WORKDIR /go/src/github.com/maxnordlund/adk/labb1

# Download (but not install) dependencies
RUN go get -d -v ./...

# Turn this container into `make`
ENTRYPOINT ["/usr/bin/make"]
