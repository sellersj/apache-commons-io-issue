package com.github.sellers.commonsioissue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class FindFilesNotPickedBySurefireTest {

    private File dir = new File("target/file-tests");

    private String commonsIoVersion = "undefined";

    @Before
    public void printCommonsIoVersion() {
        try {
            String path = "META-INF/maven/commons-io/commons-io/pom.properties";
            List<String> lines = IOUtils.readLines(ClassLoader.getSystemResourceAsStream(path),
                StandardCharsets.ISO_8859_1);
            for (String string : lines) {
                if (string.contains("version")) {
                    commonsIoVersion = string;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Before
    public void setupTestFiles() throws Exception {
        dir.mkdirs();

        List<String> filenames = Arrays.asList( //
            // surefire patterns
            "TestSomething.java", //
            "SomethingTest.java", //
            "SomethingTests.java", //
            "SomethingTestCase.java", //
            // failsafe patterns
            "ITSomething.java", //
            "SomethingIT.java", //
            "SomethingITTestCase.java", //
            // bad patterns
            "BobTestSomething.java", //
            // not test cases or abstract
            "AbstractTestSomething.java" //
        );

        // touch the file to make sure that it exists
        for (String filename : filenames) {
            File file = new File(dir, filename);
            file.deleteOnExit();
            FileUtils.touch(file);
            assertTrue("the file " + file.getAbsolutePath() + " should exit", file.exists());
        }
    }

    @Test
    public void checkForTheCorrectFiles() {
        FindFilesNotPickedBySurefire checker = new FindFilesNotPickedBySurefire();
        Collection<File> matchingFilesOnPath = checker.getMatchingFilesOnPath(dir);
        assertEquals(
            "not the number of files we expected " + matchingFilesOnPath + " for commons-io " + commonsIoVersion, 1,
            matchingFilesOnPath.size());
    }

}
