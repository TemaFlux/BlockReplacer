package me.temaflex.blockreplacer.api;

import org.bukkit.Bukkit;
//import org.mineacademy.fo.exception.FoException;

//import lombok.Getter;

/**
 * Represents the current Minecraft version the plugin loaded on
 */
public final class MinecraftVersion {

    /**
     * The string representation of the version, for example V1_13
     */
    private static String serverVersion;

    /**
     * The wrapper representation of the version
     */
    //@Getter
    private static V current;

    /**
     * The version wrapper
     */
    public enum V {
        v1_16(16, false),
        v1_15(15),
        v1_14(14),
        v1_13(13),
        v1_12(12),
        v1_11(11),
        v1_10(10),
        v1_9(9),
        v1_8(8),
        v1_7(7),
        v1_6(6),
        v1_5(5),
        v1_4(4),
        v1_3_AND_BELOW(3);

        /**
         * The numeric version (the second part of the 1.x number)
         */
        private final int ver;

        /**
         * Is this library tested with this Minecraft version?
         */
        //@Getter
        @SuppressWarnings("unused")
        private final boolean tested;

        /**
         * Creates new enum for a MC version that is tested
         *
         * @param version
         */
        V(int version) {
            this(version, true);
        }

        /**
         * Creates new enum for a MC version
         *
         * @param version
         * @param tested
         */
        V(int version, boolean tested) {
            this.ver = version;
            this.tested = tested;
        }

        /**
         * Attempts to get the version from number
         *
         * @throws RuntimeException if number not found
         * @param number
         * @return
         * @throws Exception 
         */
        protected static V parse(int number) throws Exception {
            for (final V v : values())
                if (v.ver == number)
                    return v;

            throw new Exception("Invalid version number: " + number);
        }
    }

    /**
     * Does the current Minecraft version equal the given version?
     *
     * @param version
     * @return
     */
    public static boolean equals(V version) {
        return compareWith(version) == 0;
    }

    /**
     * Is the current Minecraft version older than the given version?
     *
     * @param version
     * @return
     */
    public static boolean olderThan(V version) {
        return compareWith(version) < 0;
    }

    /**
     * Is the current Minecraft version newer than the given version?
     *
     * @param version
     * @return
     */
    public static boolean newerThan(V version) {
        return compareWith(version) > 0;
    }

    /**
     * Is the current Minecraft version at equals or newer than the given version?
     *
     * @param version
     * @return
     */
    public static boolean atLeast(V version) {
        return equals(version) || newerThan(version);
    }

    // Compares two versions by the number
    private static int compareWith(V version) {
        try {
            return getCurrent().ver - version.ver;

        } catch (final Throwable t) {
            t.printStackTrace();

            return 0;
        }
    }

    private static V getCurrent() {
        return current;
    }

    /**
     * Return the class versioning such as v1_14_R1
     *
     * @return
     */
    public static String getServerVersion() {
        return serverVersion.equals("craftbukkit") ? "" : serverVersion;
    }

    // Initialize the version
    static {
        try {

            final String packageName = Bukkit.getServer() == null ? "" : Bukkit.getServer().getClass().getPackage().getName();
            final String curr = packageName.substring(packageName.lastIndexOf('.') + 1);
            final boolean hasGatekeeper = !"craftbukkit".equals(curr);

            serverVersion = curr;

            if (hasGatekeeper) {
                int pos = 0;

                for (final char ch : curr.toCharArray()) {
                    pos++;

                    if (pos > 2 && ch == 'R')
                        break;
                }

                final String numericVersion = curr.substring(1, pos - 2).replace("_", ".");

                int found = 0;

                for (final char ch : numericVersion.toCharArray())
                    if (ch == '.')
                        found++;

                checkBoolean(found == 1);

                current = V.parse(Integer.parseInt(numericVersion.split("\\.")[1]));

            } else
                current = V.v1_3_AND_BELOW;

        } catch (final Throwable t) {
            error(t, "Error detecting your Minecraft version. Check your server compatibility.");
        }
    }
    
    public static void checkBoolean(boolean expression) throws Exception {
        if (!expression)
            throw new Exception();
    }
    
    public static void error(final Throwable t, final String... messages) {
        error(t, messages);
    }
}