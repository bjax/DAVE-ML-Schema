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

# Note file ends with .xml to generate local version that's been lint-picked

MODELS = \
         F16_aero.xml                      \
         HL20_aero.xml                     \
         aero_cm.xml                       \
	 atmos_76.xml                      \
         alpha_beta_to_alphaT_phi.xml      \
         simple_aero.xml                   \
         simplest_aero.xml                 \
         threeD_ungridded.xml              \
         twoD_table.dml twoD_ungridded.xml \
         uncertain_1D_table.xml            \
         uncertain_correl_variables.xml    \
         uncertain_variable.xml            \
         uncertain_variable_asym.xml       \
         uncertain_variable_table.xml      \
         limited_variableDef.xml           \
         unary_and_binary_minus.xml        \

make: $(MODELS)

%.xml : %.dml $(DTD)
	xmllint --valid $< >$@

clean:
	-rm $(MODELS)
