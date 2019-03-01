
EXE_NAME=sem4cli

default:
	@less README.md

build: test
	(cd cli ; go build -o ../$(EXE_NAME))

test:
	go test ./...

clean:
	rm -f $(EXE_NAME)
