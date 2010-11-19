# validation lab Makefile
#
# Copies latest versions of known models to this directory
# so Oxygen can validate them
#
#
# 061117 EBJ

DAVE_HOME_DIR = $(HOME)/Documents/Projects/DAVE
TECH_DIR      = $(DAVE_HOME_DIR)/Tech
MODELS_DIR    = $(TECH_DIR)/Models
F16_DIR       = $(MODELS_DIR)/F-16
HL20_DIR      = $(HOME)/Documents/Projects/HL-20/Sim_models/DAVE
EXAMPLES_DIR  = ../Examples

VPATH         = $(EXAMPLES_DIR):$(F16_DIR):$(HL20_DIR)

make: simple_aero_copy.dml                   \
      simplest_aero_copy.dml                 \
      twoD_table.dml twoD_ungridded_copy.dml \
      threeD_ungridded_copy.dml              \
      F16_aero_copy.dml                      \
      HL20_aero_copy.dml                     \
      aero_cm_copy.dml                       \
      uncertain_1D_table_copy.dml            \
      uncertain_correl_variables_copy.dml    \
      uncertain_variable_copy.dml            \
      uncertain_variable_asym_copy.dml       \
      uncertain_variable_table_copy.dml      \

%_copy.dml : %.dml
	@-chmod 666 $@
	cp $< $@
	@chmod 444 $@
