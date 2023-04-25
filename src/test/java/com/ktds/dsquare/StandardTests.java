package com.ktds.dsquare;

import org.junit.jupiter.api.*;

public class StandardTests {

    @BeforeAll
    static void initAll() {}

    @BeforeEach
    void init() {}

    @Test
    void someTest_AndSucceeding() {}

    @Test
    void someTest_AndFailing() {
//        fail("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
        // not executed
    }

    @Test
    void abortedTest() {
//        assumeTrue("abc".contains("z"));
//        fail("test should have been aborted");
    }

    @AfterEach
    void tearDown() {}

    @AfterAll
    static void tearDownAll() {}

}
