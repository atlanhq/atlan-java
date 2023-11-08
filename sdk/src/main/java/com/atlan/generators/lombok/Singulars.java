/* SPDX-License-Identifier: Apache-2.0
   Copyright 2015 Atlan Pte. Ltd. */
package com.atlan.generators.lombok;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a copy / paste of the same class under lombok.core.handlers.Singulars
 * (with some minor simplification).
 * Unfortunately the class in Lombok is only distributed in a shaded, byte-compiled, not
 * publicly-accessible form -- meaning that the simplest way for us to reuse the logic it
 * uses internally to singularize names is just to copy / paste the logic directly into the
 * project here.
 */
public class Singulars {
    private static final List<String> SINGULAR_STORE; // intended to be immutable.

    private static final String SINGULARS_FILE = "singulars.txt";

    static {
        SINGULAR_STORE = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Singulars.class.getClassLoader().getResourceAsStream(SINGULARS_FILE), UTF_8))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;
                if (line.endsWith(" =")) {
                    SINGULAR_STORE.add(line.substring(0, line.length() - 2));
                    SINGULAR_STORE.add("");
                    continue;
                }

                int idx = line.indexOf(" = ");
                SINGULAR_STORE.add(line.substring(0, idx));
                SINGULAR_STORE.add(line.substring(idx + 3));
            }
        } catch (IOException e) {
            SINGULAR_STORE.clear();
        }
    }

    public static String autoSingularize(String in) {
        final int inLen = in.length();
        for (int i = 0; i < SINGULAR_STORE.size(); i += 2) {
            final String lastPart = SINGULAR_STORE.get(i);
            final boolean wholeWord = Character.isUpperCase(lastPart.charAt(0));
            final int endingOnly = lastPart.charAt(0) == '-' ? 1 : 0;
            final int len = lastPart.length();
            if (inLen < len) continue;
            if (!in.regionMatches(true, inLen - len + endingOnly, lastPart, endingOnly, len - endingOnly)) continue;
            if (wholeWord && inLen != len && !Character.isUpperCase(in.charAt(inLen - len))) continue;

            String replacement = SINGULAR_STORE.get(i + 1);
            if (replacement.equals("!")) return null;

            boolean capitalizeFirst =
                    !replacement.isEmpty() && Character.isUpperCase(in.charAt(inLen - len + endingOnly));
            String pre = in.substring(0, inLen - len + endingOnly);
            String post = capitalizeFirst
                    ? Character.toUpperCase(replacement.charAt(0)) + replacement.substring(1)
                    : replacement;
            return pre + post;
        }

        return null;
    }
}
