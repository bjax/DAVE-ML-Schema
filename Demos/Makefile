F16_DIR   = ~/Documents/Dev/DAVE/F-16

make: F16_aero.dml clean

F16_aero.dml: $(F16_DIR)/F16_aero.dml
	cp $< $@

.PHONY: clean

clean:
	-rm *.log
	-rm *.m
	-rm F16_aero.mdl
	-rm twoD_table.mdl
	-rm *.html
	-rm *~


