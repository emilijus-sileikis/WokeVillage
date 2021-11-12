package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.command.CommandFlag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CommandFlagDetectionTests {

    @Test
    void defaultFlagReturn() {
        CommandFlag flag = CommandFlag.fromString("John");
        Assertions.assertSame(CommandFlag.CMD_ARGUMENT, flag);
    }

    @ParameterizedTest
    @MethodSource("listOfFlagsAndStrings")
    void flagFromString(String str, CommandFlag expected) {
        Assertions.assertSame(expected, CommandFlag.fromString(str));
    }

    static Stream<Arguments> listOfFlagsAndStrings() {
        return Stream.of(
                Arguments.arguments("-n", CommandFlag.NPC_NAME),
                Arguments.arguments("--name", CommandFlag.NPC_NAME),
                Arguments.arguments("-l", CommandFlag.NPC_LOCATION)
        );
    }
}
