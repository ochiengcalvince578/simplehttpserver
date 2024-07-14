package http;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1",  1,  1);

    public final String LITERAL;

    public final int MAJOR;

    public final int MINOR;

    HttpVersion (String LITERAL, int MAJOR, int MINOR) {

        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
        this.LITERAL = LITERAL;

    }

    public static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException {

        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);

        if (!matcher.find() || matcher.groupCount() != 2) {

            //TODO create better ex
            throw  new BadHttpVersionException();
        }

        int major = Integer.parseInt(matcher.group("major"));

        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempBestCompatible = null;

        for (HttpVersion version : HttpVersion.values()) {

            if (version.LITERAL.equals(literalVersion)) {

                return version;
            }

            else {

                if (version.MAJOR == major) {

                    if (version.MINOR < minor) {

                        tempBestCompatible = version;
                    }
                }
            }
        }

   return tempBestCompatible;
    }
}
