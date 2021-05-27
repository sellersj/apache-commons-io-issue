package com.github.sellers.commonsioissue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 */
public class FindFilesNotPickedBySurefire {

    /**
     * @param directory to look at for test classes
     * @return any matching files
     */
    public Collection<File> getMatchingFilesOnPath(File directory) {
        // accepted patterns
        IOFileFilter includeFileFilter = getAllowedFileNamePatterns();

        // if it's NOT in the allowed list AND contains the work 'Test'
        IOFileFilter fileFilter = FileFilterUtils.and(//
            FileFilterUtils.notFileFilter(includeFileFilter), //
            new RegexFileFilter(".*Test.*\\.java"));

        // don't look at any of the CVS directories
        IOFileFilter dirFilter = FileFilterUtils.makeCVSAware(null);

        Collection<File> files = FileUtils.listFiles(directory, fileFilter, dirFilter);

        return files;
    }

    /**
     * @return all the accepted file names we're going to use.
     */
    private IOFileFilter getAllowedFileNamePatterns() {
        List<IOFileFilter> includeFilters = new ArrayList<>();

        // test case pattern from surefire
        // Test*.java
        // *Test.java
        // *TestCase.java
        includeFilters.add(new RegexFileFilter("Test.*\\.java"));
        includeFilters.add(new RegexFileFilter(".*Test\\.java"));
        includeFilters.add(new RegexFileFilter(".*Tests\\.java"));
        includeFilters.add(new RegexFileFilter(".*TestCase\\.java"));

        // test case pattern from failsafe
        includeFilters.add(new RegexFileFilter("IT.*\\.java"));
        includeFilters.add(new RegexFileFilter(".*IT\\.java"));
        includeFilters.add(new RegexFileFilter(".*ITTestCase\\.java"));

        // we're also going to ignore any classes that start with Abstract
        includeFilters.add(new RegexFileFilter("Abstract.*\\.java"));

        // logically OR all the filters
        IOFileFilter includeFileFilter = null;
        for (IOFileFilter ioFileFilter : includeFilters) {
            if (null == includeFileFilter) {
                includeFileFilter = ioFileFilter;
            } else {
                includeFileFilter = FileFilterUtils.or(includeFileFilter, ioFileFilter);
            }
        }

        return includeFileFilter;
    }

}
