package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.StateComparator;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.ConsoleObserver;
import simulator.view.MainWindow;

public class Main {
	// default values for some parameters
	//
	private final static int  _stepsDefaultValue = 150;
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static int _steps = 0;
	private static Double _dtime = null;
	private static Integer _axe =null;
	private static boolean _usedAxe = false;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _expOutFile = null;
	private static String _mode = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;



	private static void init() {
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLossingBodyBuilder());
		bodyBuilders.add(new TrappedBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		forceLawsBuilders.add(new MovingTowardsTwoFixedPointsBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);

		ArrayList<Builder<StateComparator>> stateCmpBuilders = new ArrayList<>();
		stateCmpBuilders.add(new EpsilonEqualStateBuilder());
		stateCmpBuilders.add(new MassEqualStateBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(stateCmpBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line); // -m
			parseInFileOption(line);
			parseOutFileOption(line); // -o
			parseExpectedOutputOption(line); // -eo
			parseStepsOption(line); // -s
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseAxeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// mode -m
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: "
				+ "'batch' (Batch mode), 'gui' (Graphical User Interface mode). Default value: "
				+ _modeDefaultValue + ".").build());
		
		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output file -o
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written.\n"
				+ "Default value: the standard output.").build());

		// expected output file -eo
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg().desc("The expected output file. If not provided\n"
				+ "no comparison is applied").build());

		// steps  -s
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg()
				.desc("An integer representing the number of\n"
						+ "simulation steps. Default value: "
						+ _stepsDefaultValue + ".").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".").build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.").build());

		// comparator
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.").build());
		
		cmdLineOptions.addOption(Option.builder("a").longOpt("axe").hasArg().desc("axe mode option").build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}
	
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_mode.equals("BATCH") && _inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseOutFileOption(CommandLine line)throws ParseException {
		_outFile = line.hasOption("o") ? line.getOptionValue("o") : null;
	}

	private static void parseExpectedOutputOption(CommandLine line)throws ParseException {
		_expOutFile = line.hasOption("eo") ? line.getOptionValue("eo") : null;
	}

	private static void parseStepsOption(CommandLine line)throws ParseException {
		String stepsValue = String.valueOf(_stepsDefaultValue);
		String s = line.getOptionValue("s", stepsValue);
		try {
			_steps = Integer.parseInt(s);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + s);
		}
	}
	 
	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.hasOption("m") ? line.getOptionValue("m") : _modeDefaultValue;
	}
	
	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}
	private static void parseAxeOption(CommandLine line)throws ParseException {
		String axe = line.getOptionValue("a");
		if(axe==null)
		{
			_usedAxe=false;
		}
		else
		{
			_usedAxe=true;
		
		try {
			_axe = Integer.parseInt(axe);
			assert (_axe == 0||_axe==1);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + axe);
		}
		}
		
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;
	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		try {
			InputStream is = new FileInputStream(new File(_inFile));
			OutputStream os = _outFile == null ? System.out : new FileOutputStream(new File(_outFile));

			ForceLaws forcelaws = null;
			forcelaws = _forceLawsFactory.createInstance(_forceLawsInfo);
			PhysicsSimulator simulator = new PhysicsSimulator(_dtime, forcelaws);

			Controller control = new Controller(simulator, _bodyFactory, _forceLawsFactory);

			InputStream expOut = null;
			StateComparator stateCmp = null;
			if (_expOutFile != null) {
				expOut = new FileInputStream(new File(_expOutFile));
				stateCmp = _stateComparatorFactory.createInstance(_stateComparatorInfo);
			}
			
			control.loadBodies(is);
			control.run(_steps, os, expOut, stateCmp);
			if(_usedAxe==true)
			{
				ConsoleObserver co = new ConsoleObserver(_axe);
				control.addObserver(co);
				int n=0;
				for(int i:co.getChanges())
				{
					n++;
					System.out.println("El body"+n+"="+i);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static void startGUIMode() throws Exception {
		try {		
			ForceLaws forcelaws = null;
			forcelaws = _forceLawsFactory.createInstance(_forceLawsInfo);
			PhysicsSimulator simulator = new PhysicsSimulator(_dtime, forcelaws);

			Controller control = new Controller(simulator, _bodyFactory, _forceLawsFactory);

			try { 
				InputStream is = _inFile != null ? new FileInputStream(new File(_inFile)) : null;
				if (is != null) control.loadBodies(is);
			}
			catch (FileNotFoundException fnfe) { System.err.println("File not found. Starting without loading a file"); }
			catch (JSONException jse) { System.err.println("Failed to load bodies. Starting without loading bodies"); }

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new MainWindow(control);
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if (_mode.equalsIgnoreCase("batch")) startBatchMode();
		else if (_mode.equalsIgnoreCase("gui")) startGUIMode();
		else throw new Exception("Invalid mode selected");

	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong...");
			System.err.println();
			e.printStackTrace();
		}
	}
}