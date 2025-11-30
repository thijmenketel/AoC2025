.PHONY: *

build:
	./gradlew build

clean:
	./gradlew clean

run-all:
	./gradlew runAll

today:
	./gradlew today

run-day%:
	./gradlew runday$*

all: clean build

increment:
	./make-increment.sh
