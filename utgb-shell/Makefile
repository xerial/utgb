
INSTALL = install
MVN = mvn ${MVN_OPTS}
INSTALL_DIR=/usr/local
UTGB_DIR = $(INSTALL_DIR)/utgb

ARCHIVE = target/utgb-package.zip
SCRIPT_DIR = src/main/java/org/utgenome/shell/script

JAVA=$(shell find src/main/java -name '*.java')

.phony: all install clean package 


all: package

package: $(ARCHIVE)


$(ARCHIVE): $(JAVA)
	$(MVN) package

install: $(ARCHIVE) $(SCRIPT_DIR)/bin/utgb
	mkdir -p $(INSTALL_DIR)	
	unzip -o $(ARCHIVE) -d target
	cp -r target/utgb $(INSTALL_DIR)
	rm -rf target/utgb
	ln -sf $(UTGB_DIR)/bin/utgb $(INSTALL_DIR)/bin/utgb

uninstall: 
	rm -rf $(UTGB_DIR)
	rm -f $(INSTALL_DIR)/bin/utgb

dist:	

clean:
	mvn clean

