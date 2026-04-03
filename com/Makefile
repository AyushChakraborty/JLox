JC = javac
BIN = bin
SRC_DIR = craftinginterpreters/lox
SRC = $(wildcard $(SRC_DIR)/*.java)

all: compile

compile:
	mkdir -p $(BIN)
	$(JC) -d $(BIN) $(SRC)

run:
	java -cp $(BIN) com.craftinginterpreters.lox.Lox

clean:
	rm -rf $(BIN)
