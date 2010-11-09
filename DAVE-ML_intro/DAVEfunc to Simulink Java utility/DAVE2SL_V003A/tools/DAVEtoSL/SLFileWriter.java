//
//  SLFileWriter.java
//
//    Provides special output functions for writing Simulink files.
//
//    020419 E. B. Jackson <e.b.jackson@larc.nasa.gov>
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.IOException;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SLFileWriter extends FileWriter
{
    /**
     *
     * <p> Constructor for SLFileWriter; derived from FileWriter
     *  but specialized to write Simulink .mdl files 
     *
     * @param fileName Name of file to open
     * @throws IOException
     *
     */

    public SLFileWriter(String fileName) throws IOException
    {
	super(fileName);
    }

    /**
     *
     * <p> Addeds newline to the end of each write </p>
     *
     * @param cbuf String containing text to write
     *
     */

    public void writeln( String cbuf ) throws IOException
    {
	super.write( cbuf + "\n" );
    }

    /** 
     *
     * <p> Writes the header for a Simulink representation </p>
     *
     * @param writer Instance of the SLFileWriter class
     * @param modelName Name of model being implemented
     * @throws IOException
     *
     */

    public void writeSLHeader(SLFileWriter writer, String modelName )
      throws IOException
    {
	SimpleDateFormat timeStamp = new SimpleDateFormat("EE MMM d HH:mm:ss yyyy");

	writer.writeln("Model {");
	writer.write("  Name			  \"");
	writer.write(modelName);
	writer.writeln("\"");
	writer.writeln("  Version		  4.00");
	writer.writeln("  SampleTimeColors	  off");
	writer.writeln("  LibraryLinkDisplay	  \"none\"");
	writer.writeln("  WideLines		  off");
	writer.writeln("  ShowLineDimensions	  off");
	writer.writeln("  ShowPortDataTypes	  off");
	writer.writeln("  ShowStorageClass	  off");
	writer.writeln("  ExecutionOrder	  off");
	writer.writeln("  RecordCoverage	  off");
	writer.writeln("  CovPath		  \"/\"");
	writer.writeln("  CovSaveName		  \"covdata\"");
	writer.writeln("  CovMetricSettings	  \"dw\"");
	writer.writeln("  CovNameIncrementing	  off");
	writer.writeln("  CovHtmlReporting	  on");
	writer.writeln("  BlockNameDataTip	  off");
	writer.writeln("  BlockParametersDataTip  off");
	writer.writeln("  BlockDescriptionStringDataTip	off");
	writer.writeln("  ToolBar		  on");
	writer.writeln("  StatusBar		  on");
	writer.writeln("  BrowserShowLibraryLinks off");
	writer.writeln("  BrowserLookUnderMasks	  off");
	writer.write("  Created		  \"");
	writer.write(timeStamp.format( new Date() ));
	writer.writeln("\"");
	writer.writeln("  UpdateHistory		  \"UpdateHistoryNever\"");
	writer.writeln("  ModifiedByFormat	  \"%<Auto>\"");
	writer.writeln("  ModifiedDateFormat	  \"%<Auto>\"");
	writer.write("  LastModifiedDate	  \"");
	writer.write(timeStamp.format( new Date() ));
	writer.writeln("\"");
	writer.writeln("  ModelVersionFormat	  \"1.%<AutoIncrement:2>\"");
	writer.writeln("  ConfigurationManager	  \"None\"");
	writer.writeln("  SimParamPage		  \"Solver\"");
	writer.writeln("  StartTime		  \"0.0\"");
	writer.writeln("  StopTime		  \"10.0\"");
	writer.writeln("  SolverMode		  \"Auto\"");
	writer.writeln("  Solver		  \"ode45\"");
	writer.writeln("  RelTol		  \"1e-3\"");
	writer.writeln("  AbsTol		  \"auto\"");
	writer.writeln("  Refine		  \"1\"");
	writer.writeln("  MaxStep		  \"auto\"");
	writer.writeln("  MinStep		  \"auto\"");
	writer.writeln("  MaxNumMinSteps	  \"-1\"");
	writer.writeln("  InitialStep		  \"auto\"");
	writer.writeln("  FixedStep		  \"auto\"");
	writer.writeln("  MaxOrder		  5");
	writer.writeln("  OutputOption		  \"RefineOutputTimes\"");
	writer.writeln("  OutputTimes		  \"[]\"");
	writer.writeln("  LoadExternalInput	  off");
	writer.writeln("  ExternalInput		  \"[t, u]\"");
	writer.writeln("  SaveTime		  on");
	writer.writeln("  TimeSaveName		  \"tout\"");
	writer.writeln("  SaveState		  off");
	writer.writeln("  StateSaveName		  \"xout\"");
	writer.writeln("  SaveOutput		  on");
	writer.writeln("  OutputSaveName	  \"yout\"");
	writer.writeln("  LoadInitialState	  off");
	writer.writeln("  InitialState		  \"xInitial\"");
	writer.writeln("  SaveFinalState	  off");
	writer.writeln("  FinalStateName	  \"xFinal\"");
	writer.writeln("  SaveFormat		  \"Array\"");
	writer.writeln("  LimitDataPoints	  on");
	writer.writeln("  MaxDataPoints		  \"1000\"");
	writer.writeln("  Decimation		  \"1\"");
	writer.writeln("  AlgebraicLoopMsg	  \"warning\"");
	writer.writeln("  MinStepSizeMsg	  \"warning\"");
	writer.writeln("  UnconnectedInputMsg	  \"warning\"");
	writer.writeln("  UnconnectedOutputMsg	  \"warning\"");
	writer.writeln("  UnconnectedLineMsg	  \"warning\"");
	writer.writeln("  InheritedTsInSrcMsg	  \"warning\"");
	writer.writeln("  SingleTaskRateTransMsg  \"none\"");
	writer.writeln("  MultiTaskRateTransMsg	  \"error\"");
	writer.writeln("  IntegerOverflowMsg	  \"warning\"");
	writer.writeln("  CheckForMatrixSingularity \"none\"");
	writer.writeln("  UnnecessaryDatatypeConvMsg \"none\"");
	writer.writeln("  Int32ToFloatConvMsg	  \"warning\"");
	writer.writeln("  InvalidFcnCallConnMsg	  \"error\"");
	writer.writeln("  SignalLabelMismatchMsg  \"none\"");
	writer.writeln("  LinearizationMsg	  \"none\"");
	writer.writeln("  VectorMatrixConversionMsg \"none\"");
	writer.writeln("  SfunCompatibilityCheckMsg \"none\"");
	writer.writeln("  BlockPriorityViolationMsg \"warning\"");
	writer.writeln("  ArrayBoundsChecking	  \"none\"");
	writer.writeln("  ConsistencyChecking	  \"none\"");
	writer.writeln("  ZeroCross		  on");
	writer.writeln("  Profile		  off");
	writer.writeln("  SimulationMode	  \"normal\"");
	writer.writeln("  RTWSystemTargetFile	  \"grt.tlc\"");
	writer.writeln("  RTWInlineParameters	  off");
	writer.writeln("  RTWRetainRTWFile	  off");
	writer.writeln("  RTWTemplateMakefile	  \"grt_default_tmf\"");
	writer.writeln("  RTWMakeCommand	  \"make_rtw\"");
	writer.writeln("  RTWGenerateCodeOnly	  off");
	writer.writeln("  TLCProfiler		  off");
	writer.writeln("  TLCDebug		  off");
	writer.writeln("  TLCCoverage		  off");
	writer.writeln("  AccelSystemTargetFile	  \"accel.tlc\"");
	writer.writeln("  AccelTemplateMakefile	  \"accel_default_tmf\"");
	writer.writeln("  AccelMakeCommand	  \"make_rtw\"");
	writer.writeln("  TryForcingSFcnDF	  off");
	writer.writeln("  ExtModeMexFile	  \"ext_comm\"");
	writer.writeln("  ExtModeBatchMode	  off");
	writer.writeln("  ExtModeTrigType	  \"manual\"");
	writer.writeln("  ExtModeTrigMode	  \"normal\"");
	writer.writeln("  ExtModeTrigPort	  \"1\"");
	writer.writeln("  ExtModeTrigElement	  \"any\"");
	writer.writeln("  ExtModeTrigDuration	  1000");
	writer.writeln("  ExtModeTrigHoldOff	  0");
	writer.writeln("  ExtModeTrigDelay	  0");
	writer.writeln("  ExtModeTrigDirection	  \"rising\"");
	writer.writeln("  ExtModeTrigLevel	  0");
	writer.writeln("  ExtModeArchiveMode	  \"off\"");
	writer.writeln("  ExtModeAutoIncOneShot	  off");
	writer.writeln("  ExtModeIncDirWhenArm	  off");
	writer.writeln("  ExtModeAddSuffixToVar	  off");
	writer.writeln("  ExtModeWriteAllDataToWs off");
	writer.writeln("  ExtModeArmWhenConnect	  on");
	writer.writeln("  ExtModeSkipDownloadWhenConnect off");
	writer.writeln("  ExtModeLogAll		  on");
	writer.writeln("  ExtModeAutoUpdateStatusClock on");
	writer.writeln("  OptimizeBlockIOStorage  on");
	writer.writeln("  BufferReuse		  on");
	writer.writeln("  ParameterPooling	  on");
	writer.writeln("  BlockReductionOpt	  on");
	writer.writeln("  RTWExpressionDepthLimit 5");
	writer.writeln("  BooleanDataType	  off");
	writer.writeln("  BlockDefaults {");
	writer.writeln("    Orientation		    \"right\"");
	writer.writeln("    ForegroundColor	    \"black\"");
	writer.writeln("    BackgroundColor	    \"white\"");
	writer.writeln("    DropShadow		    off");
	writer.writeln("    NamePlacement	    \"normal\"");
	writer.writeln("    FontName		    \"Helvetica\"");
	writer.writeln("    FontSize		    10");
	writer.writeln("    FontWeight		    \"normal\"");
	writer.writeln("    FontAngle		    \"normal\"");
	writer.writeln("    ShowName		    on");
	writer.writeln("  }");
	writer.writeln("  AnnotationDefaults {");
	writer.writeln("    HorizontalAlignment	    \"center\"");
	writer.writeln("    VerticalAlignment	    \"middle\"");
	writer.writeln("    ForegroundColor	    \"black\"");
	writer.writeln("    BackgroundColor	    \"white\"");
	writer.writeln("    DropShadow		    off");
	writer.writeln("    FontName		    \"Helvetica\"");
	writer.writeln("    FontSize		    10");
	writer.writeln("    FontWeight		    \"normal\"");
	writer.writeln("    FontAngle		    \"normal\"");
	writer.writeln("  }");
	writer.writeln("  LineDefaults {");
	writer.writeln("    FontName		    \"Helvetica\"");
	writer.writeln("    FontSize		    9");
	writer.writeln("    FontWeight		    \"normal\"");
	writer.writeln("    FontAngle		    \"normal\"");
	writer.writeln("  }");
	writer.writeln("  System {");
	writer.writeln("    Name		    \"example\"");
	writer.writeln("    Location		    [480, 85, 1164, 424]");
	writer.writeln("    Open		    on");
	writer.writeln("    ModelBrowserVisibility  off");
	writer.writeln("    ModelBrowserWidth	    200");
	writer.writeln("    ScreenColor		    \"automatic\"");
	writer.writeln("    PaperOrientation	    \"landscape\"");
	writer.writeln("    PaperPositionMode	    \"auto\"");
	writer.writeln("    PaperType		    \"usletter\"");
	writer.writeln("    PaperUnits		    \"inches\"");
	writer.writeln("    ZoomFactor		    \"100\"");
	writer.writeln("    ReportName		    \"simulink-default.rpt\"");

    }


    /** 
     *
     * <p> Writes the footer for a Simulink representation
     *
     * @param writer Instance of the SLFileWriter class
     * @throws IOException
     *
     */

    public void writeSLFooter(SLFileWriter writer)
      throws IOException
    {
	writer.writeln("  }");	// closes System
	writer.writeln("}");	// closes Model
    }

}
