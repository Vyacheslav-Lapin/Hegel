package com.hegel.core;

import org.junit.jupiter.api.Test;

import static com.hegel.core.StringEncryptUtil.encrypt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class StringEncryptUtilTest {

    @Test
    void heffelfingerWasRight() {
        assertThat(encrypt("s3cret"), is("33e1b232a4e6fa0028a6670753749a17"));
    }
}
