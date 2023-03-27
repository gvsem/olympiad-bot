package org.gvsem.olympiadbot.utils;

import org.gvsem.olympiadbot.controller.QueryEnum;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CommandUtils {

    @Nullable
    public static QueryEnum getCommand(@Nullable String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        String[] parts = value.split(" ");
        if (parts.length > 0) {
            String cmd = parts[0];
            if (value.charAt(0) == '/') {
                cmd = cmd.substring(1);
            }
            try {
                return QueryEnum.valueOf(cmd.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public static List<String> getArguments(@NotNull String value) {
        String[] parts = value.split(" ");
        if (parts.length > 0) {
            List<String> args = new ArrayList<>(List.of(parts));
            args.remove(0);
            return args;
        }
        return new ArrayList<>();
    }

}
