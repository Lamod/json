OUT_DIR = out
SRC_DIR = src/main/java/lamo/json
TEST_DIR = src/test/java
JC = javac -d $(OUT_DIR) -cp $(OUT_DIR)

test : json
	$(JC) $(TEST_DIR)/*.java

json: parser
	$(JC) $(SRC_DIR)/*.java

parser : ast token
	$(JC) $(SRC_DIR)/parser/*.java

ast : token
	$(JC) $(SRC_DIR)/parser/ast/*.java

token : 
	$(JC) $(SRC_DIR)/parser/token/*.java

.PHONY: clean

clean :
	rm -rf $(OUT_DIR)/*
