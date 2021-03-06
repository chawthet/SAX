package net.seninp.jmotif.sax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import net.seninp.jmotif.sax.alphabet.Alphabet;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

/**
 * Test SAX factory methods.
 * 
 * @author Pavel Senin
 * 
 */
public class TestSAXProcessor {

  private static final String ts1File = "src/resources/test-data/timeseries01.csv";
  private static final String ts2File = "src/resources/test-data/timeseries02.csv";
  private static final String ts3File = "src/resources/test-data/timeseries03.csv";

  private static final String ts1StrRep10 = "bcjkiheebb";
  private static final String ts2StrRep10 = "bcefgijkdb";

  private static final String ts1StrRep14 = "bcdijjhgfeecbb";
  private static final String ts2StrRep14 = "bbdeeffhijjfbb";

  private static final String ts1StrRep7 = "bcggfddba";
  private static final String ts2StrRep7 = "accdefgda";

  private static final int length = 15;
  private static final int strLength = 10;

  private static final Alphabet normalA = new NormalAlphabet();

  private static final double delta = 0.001;

  /**
   * Testing the concatenated time series SAX conversion.
   * 
   * @throws NumberFormatException if error occurs.
   * @throws IOException if error occurs.
   * @throws SAXException if error occurs.
   */
  @Test
  public void testConnectedConversion() throws NumberFormatException, IOException, SAXException {

    SAXProcessor sp = new SAXProcessor();
    double[] ts = TSProcessor.readFileColumn(ts3File, 0, 0);

    ArrayList<Integer> skips = new ArrayList<Integer>();
    for (int i = 30 - 6; i < 30; i++) {
      skips.add(i);
    }

    SAXRecords regularSAX = sp.ts2saxViaWindow(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.NONE, 0.01);
    System.out.println("NONE: there are " + regularSAX.getAllIndices().size() + " words: \n"
        + regularSAX.getSAXString(" ") + "\n" + regularSAX.getAllIndices());
    SAXRecords saxData = sp.ts2saxViaWindowSkipping(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.NONE, 0.01, skips);
    System.out.println("NONE with skips: there are " + saxData.getAllIndices().size() + " words: \n"
        + saxData.getSAXString(" ") + "\n" + saxData.getAllIndices());

    regularSAX = sp.ts2saxViaWindow(ts, 6, 3, normalA.getCuts(3), NumerosityReductionStrategy.EXACT,
        0.01);
    assertNotNull("asserting the processing result", regularSAX);
    System.out.println("EXACT: there are " + regularSAX.getAllIndices().size() + " words: \n"
        + regularSAX.getSAXString(" ") + "\n" + regularSAX.getAllIndices());
    saxData = sp.ts2saxViaWindowSkipping(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.EXACT, 0.01, skips);
    System.out.println("EXACT with skips: there are " + saxData.getAllIndices().size()
        + " words: \n" + saxData.getSAXString(" ") + "\n" + saxData.getAllIndices());
  }

  /**
   * Test the SAX conversion.
   * 
   * @throws Exception if error occurs.
   */
  @Test
  public void testTs2string() throws Exception {

    SAXProcessor sp = new SAXProcessor();

    double[] ts1 = TSProcessor.readFileColumn(ts1File, 0, length);
    double[] ts2 = TSProcessor.readFileColumn(ts2File, 0, length);

    // series #1 based test
    double[] ser = { -1.0, -2.0, -1.0, 0.0, 2.0, 1.0, 1.0, 0.0 };
    TSProcessor tp = new TSProcessor();
    System.out.println(" ** " + Arrays.toString(tp.paa(ser, 3)));

    String ts1sax = sp.ts2saxByChunking(ts1, 10, normalA.getCuts(11), delta).getSAXString("");
    assertEquals("testing SAX", strLength, ts1sax.length());
    assertTrue("testing SAX", ts1StrRep10.equalsIgnoreCase(ts1sax));

    ts1sax = sp.ts2saxByChunking(ts1, 14, normalA.getCuts(10), delta).getSAXString("");
    assertEquals("testing SAX", 14, ts1sax.length());
    assertTrue("testing SAX", ts1StrRep14.equalsIgnoreCase(ts1sax));

    ts1sax = sp.ts2saxByChunking(ts1, 9, normalA.getCuts(7), delta).getSAXString("");
    assertEquals("testing SAX", 9, ts1sax.length());
    assertTrue("testing SAX", ts1StrRep7.equalsIgnoreCase(ts1sax));

    // series #2 goes here
    String ts2sax = sp.ts2saxByChunking(ts2, 10, normalA.getCuts(11), delta).getSAXString("");
    assertEquals("testing SAX", strLength, ts2sax.length());
    assertTrue("testing SAX", ts2StrRep10.equalsIgnoreCase(ts2sax));

    ts2sax = sp.ts2saxByChunking(ts2, 14, normalA.getCuts(10), delta).getSAXString("");
    assertEquals("testing SAX", 14, ts2sax.length());
    assertTrue("testing SAX", ts2StrRep14.equalsIgnoreCase(ts2sax));

    ts2sax = sp.ts2saxByChunking(ts2, 9, normalA.getCuts(7), delta).getSAXString("");
    assertEquals("testing SAX", 9, ts2sax.length());
    assertTrue("testing SAX", ts2StrRep7.equalsIgnoreCase(ts2sax));
  }

  /**
   * Test the distance function.
   *
   * @throws Exception if error occur.
   */
  @Test
  public void testTs2sax() throws Exception {

    TSProcessor tp = new TSProcessor();
    SAXProcessor sp = new SAXProcessor();

    double[] ts2 = TSProcessor.readFileColumn(ts2File, 0, length);

    String ts2str_0 = sp
        .ts2saxByChunking(tp.subseriesByCopy(ts2, 0, 5), 5, normalA.getCuts(10), delta)
        .getSAXString("");
    String ts2str_3 = sp
        .ts2saxByChunking(tp.subseriesByCopy(ts2, 3, 8), 5, normalA.getCuts(10), delta)
        .getSAXString("");
    String ts2str_7 = sp
        .ts2saxByChunking(tp.subseriesByCopy(ts2, 7, 12), 5, normalA.getCuts(10), delta)
        .getSAXString("");

    SAXRecords ts2SAX = sp.ts2saxViaWindow(ts2, 5, 5, normalA.getCuts(10),
        NumerosityReductionStrategy.NONE, delta);

    assertEquals("Testing conversion", ts2.length - 5, ts2SAX.size());

    assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_0));
    assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_3));
    assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_7));

    assertEquals("Testing ts2sax", ts2SAX.getByWord(ts2str_0).getIndexes().iterator().next(),
        new Integer(0));
    assertEquals("Testing ts2sax", ts2SAX.getByWord(ts2str_3).getIndexes().iterator().next(),
        new Integer(3));
    assertEquals("Testing ts2sax", ts2SAX.getByWord(ts2str_7).getIndexes().iterator().next(),
        new Integer(7));

  }

}
