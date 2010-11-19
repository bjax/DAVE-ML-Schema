#
# validation lab Makefile
#
# Copies latest versions of known models to this directory
# so Oxygen can validate them
#
#
# 061117 EBJ

DOCUMENTS_DIR = ../../Documents
EXAMPLES_DIR  = ../Examples
F16_DIR       = ../../F-16
HL20_DIR      = ~/Documents/Projects/HL-20/Sim_models/DAVE

make: simple_aero.dml twoD_table.dml twoD_ungridded.dml \
      threeD_ungridded.dml F16_aero.dml HL20_aero.dml \
      aero_cm.dml uncertain_1D_table.dml uncertain_correl_variables.dml \
      uncertain_variable.dml uncertain_variable_asym.dml \
      uncertain_variable_table.dml

aero_cm.dml: $(EXAMPLES_DIR)/aero_cm.dml

uncertain_1D_table.dml: $(EXAMPLES_DIR)/uncertain_1D_table.dml

uncertain_correl_variables.dml: $(EXAMPLES_DIR)/uncertain_correl_variables.dml

uncertain_variable.dml: $(EXAMPLES_DIR)/uncertain_variable.dml

uncertain_variable_asym.dml: $(EXAMPLES_DIR)/uncertain_variable_asym.dml

uncertain_variable_table.dml: $(EXAMPLES_DIR)/uncertain_variable_table.dml

simple_aero.dml: $(DOCUMENTS_DIR)/simple_aero.dml

twoD_table.dml: $(DOCUMENTS_DIR)/twoD_table.dml

twoD_ungridded.dml: $(DOCUMENTS_DIR)/twoD_ungridded.dml

threeD_ungridded.dml: $(DOCUMENTS_DIR)/threeD_ungridded.dml

F16_aero.dml: $(F16_DIR)/F16_aero.dml

HL20_aero.dml: $(HL20_DIR)/HL20_aero.dml

%.dml: */%.dml
	@-chmod 666 $@
	cp $< $@
	@chmod 444 $@
