package cq_server.factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannedIpsLoader {
	private static final Pattern IP_PATTERN = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");

	public List<String> load(final String fileName) {
		try {
			final List<String> bannedIps = new ArrayList<>();
			Path path = Paths.get(fileName);
			if (!Files.exists(path))
				Files.createFile(path);
			final List<String> list = Files.readAllLines(path);
			for (final String item : list) {
				final Matcher matcher = IP_PATTERN.matcher(item);
				if (matcher.matches())
					bannedIps.add(matcher.group(1));
			}
			return bannedIps;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
