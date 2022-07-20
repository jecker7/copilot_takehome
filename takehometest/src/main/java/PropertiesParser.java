//        Part 1
//        =======
//
//        The code below was written with some bugs in it and we must fix them. Please write unit tests to expose the bugs;
//        then fix the code;
//        then, ideally, add a couple more unit tests to make sure the code works now and after the next refactoring.
//        Please provide a runnable project (using either Gradle or Maven) with at least the fixed class, and the unit tests;
//        including comments/javadoc as appropriate.
//
//        Note - the javadoc may also be incomplete, so please don't assume that all possible test
//        cases are covered there.
//


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h3>PropertiesParser</h3>
 *
 * Resolves key substitution on a properties set; in other words, given:
 * <pre>FOO = foo
 * BAR = bar
 * baz = ${FOO} is ${BAR}
 * </pre>
 *
 * the following code will satisfy the assertion:
 * <pre> Properties props = Properties.load('my.properties');
 *  PropertiesParser parser = new PropertiesParser(props);
 *  parser.parse();
 *  assert("foo is bar".equals(props.getProperty("baz"));
 * </pre>
 */
public class PropertiesParser {


    /** A RegEx pattern for a key subst: ${key} */
    private final String PATTERN = "\\Q${\\E(\\w*)}";
    private final Properties props;



    public PropertiesParser(Properties props) {
        this.props = props;
    }


    public void parse() {
        for (Object key : props.keySet().toArray()) {
            String value = props.getProperty( (String) key);
            if (hasSubstKey(value)) {
                String newVal = applyPattern(value);
                props.put(key, newVal);
            }
        }
    }


    /**
     * Executes string substitution for the {@code String value}
     *
     * @param value - a string containing a regex ${} to replace
     * @return the fully substituted value, if possible
     */
    protected String applyPattern(String value) {
        Pattern keyPattern = Pattern.compile(PATTERN);
        Matcher m = keyPattern.matcher(value);
        String result = value;
        while(m.find()){
            result = result.replace(m.group(0), extractKeyValue(m.group(1)));
        }
        System.out.println("Result: " + result);
        return result;
    }


    /** Retrieves an exsiting key from properties if that key exists
     *
     * @param key - a string which is the key of a property to retrieve
     * */
    public String extractKeyValue(String key) {
        if(this.props.containsKey(key)){
            return (String) this.props.get(key);
        }
        return null;
    }


    /** Checks if the string value contains the right regex to replace
     *
     * @param value - a string to check for regex
     * */
    public boolean hasSubstKey(String value) {
        Pattern pat = Pattern.compile(PATTERN);
        Matcher m = pat.matcher(value);
        return m.find();
    }

    public Properties getProperties(){
        return this.props;
    }

    public String getPATTERN(){
        return PATTERN;
    }
}




