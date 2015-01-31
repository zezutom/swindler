package org.zezutom.swindler;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwindlerTest {

    public static final String SAFE_PLACE = "You cannot change me";

    public static String JUST_A_STATIC_FIELD = "Change at will";

    @Test
    public void constants_cannot_be_easily_changed() {
        Swindler.with(SwindlerTest.class).get("SAFE_PLACE").set("hacked");
        assertEquals(SAFE_PLACE, "You cannot change me");
    }

    @Test
    public void variables_are_easy_to_change() {
        Swindler.with(SwindlerTest.class).get("JUST_A_STATIC_FIELD").set("hacked");
        assertEquals(JUST_A_STATIC_FIELD, "hacked");
    }

    @Test
    public void everything_is_true() {
        Swindler.with(Boolean.class).get("FALSE").set(true);
        assertTrue(Boolean.FALSE);
    }

    @Test
    public void math_random_totally_predictable() {
        Swindler.with("java.lang.Math$RandomNumberGeneratorHolder")
                .get("randomNumberGenerator")
                .set(new Random() {
                    @Override
                    public double nextDouble() {
                        return 1;
                    }
                });

        assertEquals(Math.random(), 1.0, 0);
    }
}
