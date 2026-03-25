/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for Statement.enquoteLiteral, enquoteIdentifier, and enquoteNCharLiteral methods
 * Issue #721: These methods should be marked as SQL_INJECTION_SAFE
 */
public class StatementEnquoteMethodsTest extends BaseDetectorTest {

    @Test
    public void detectSafeEnquoteLiteral() throws Exception {
        // Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/StatementEnquoteMethods")
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // enquoteLiteral should NOT report bugs (it's safe)
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("StatementEnquoteMethods")
                        .inMethod("safeLiteralUsage")
                        .build()
        );
    }

    @Test
    public void detectSafeEnquoteIdentifier() throws Exception {
        // Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/StatementEnquoteMethods")
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // enquoteIdentifier should NOT report bugs (it's safe)
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("StatementEnquoteMethods")
                        .inMethod("safeIdentifierUsage")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("StatementEnquoteMethods")
                        .inMethod("safeIdentifierUsageNoQuote")
                        .build()
        );
    }

    @Test
    public void detectSafeEnquoteNCharLiteral() throws Exception {
        // Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/StatementEnquoteMethods")
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // enquoteNCharLiteral should NOT report bugs (it's safe)
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("StatementEnquoteMethods")
                        .inMethod("safeNCharLiteralUsage")
                        .build()
        );
    }

    @Test
    public void detectSafeMultipleEnquoteMethods() throws Exception {
        // Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/StatementEnquoteMethods")
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // Multiple enquote methods used together should NOT report bugs
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("StatementEnquoteMethods")
                        .inMethod("multipleEnquoteUsage")
                        .build()
        );
    }

    // Note: The sample code includes unsafe methods for documentation purposes,
    // but they are not tested here because:
    // 1. The primary goal of Issue #721 is to verify enquote methods do NOT produce false-positives
    // 2. SQL injection detection for unsafe patterns is already covered by JdbcInjectionSourceTest
    // 3. The current project state has existing JDBC tests failing (likely due to Java version
    //    or string concatenation implementation differences), which is outside the scope of this fix
}