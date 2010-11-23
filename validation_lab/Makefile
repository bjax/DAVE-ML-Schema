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
HERE          = $(shell pwd)
DTD           = ../Auto_DTD/DAVEfunc_auto.dtd

export XML_CATALOG_FILES:=./davefunc_catalog.xml

VPATH         = $(EXAMPLES_DIR):$(F16_DIR):$(HL20_DIR)

MODELS = simple_aero.xml                   \
         simplest_aero.xml                 \
         twoD_table.dml twoD_ungridded.xml \
         threeD_ungridded.xml              \
         F16_aero.xml                      \
         HL20_aero.xml                     \
         aero_cm.xml                       \
         uncertain_1D_table.xml            \
         uncertain_correl_variables.xml    \
         uncertain_variable.xml            \
         uncertain_variable_asym.xml       \
         uncertain_variable_table.xml      \

make: $(MODELS)

%.xml : %.dml $(DTD)
	xmllint --valid $< >$@

clean:
	-rm $(MODELS)
