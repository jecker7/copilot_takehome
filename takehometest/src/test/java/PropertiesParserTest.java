import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesParserTest {

    private PropertiesParser parser;

    @Before
    public void setup(){
        this.parser = new PropertiesParser(getTestProps());
    }

    public Properties getTestProps(){
        Properties props = new Properties();
        props.put("test1", "hello");
        props.put("test2", "world");
        props.put("test3", "!");
        props.put("test4", "${test1} ${test2}${test3}");
        props.put("test5", "${test1} ${test1} ${test1}${test3}");
        return props;
    }

    @Test
    public void testParse(){
        this.parser.parse();
        Assert.assertEquals("hello world!", this.parser.getProperties().getProperty("test4"));
        Assert.assertEquals("hello hello hello!", this.parser.getProperties().getProperty("test5"));
    }

    @Test
    public void testApplyPattern(){
        String goodTest = "asdf ${test1} asdf";
        String goodTest2 = "asdf ${test1} asdf ${test2}";
        String badTest1 = "asdf  asdf";
        String badTest2 = "$asdf  {asdf}";

        Pattern p = Pattern.compile(parser.getPATTERN());
        Matcher m = p.matcher(goodTest);
        Matcher m1 = p.matcher(goodTest2);
        Matcher m2 = p.matcher(badTest1);
        Matcher m3 = p.matcher(badTest2);
        Assert.assertTrue(m.find());
        Assert.assertTrue(m1.find());
        Assert.assertFalse(m2.find());
        Assert.assertFalse(m3.find());
    }
}

