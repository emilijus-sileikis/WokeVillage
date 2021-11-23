package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.command.CommandContext;
import lt.vu.mif.it.paskui.village.command.CommandFlag;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

public class CommandContextTests {

    private final Command npcCmd = new TestCommand("npc");

    @ParameterizedTest
    @MethodSource("locationArgumentProvider")
    public void LocationParsingTest(String[] args, Location tLoc) {
        System.out.println(Arrays.toString(args));

        try {
            CommandContext context = new CommandContext(null, null, npcCmd, args);
            Assertions.assertEquals("npc.create", context.getCmd());

            if (context.hasArg(CommandFlag.NPC_LOCATION)) {
                Location loc = (Location) context.getArg(CommandFlag.NPC_LOCATION).value();
                Assertions.assertEquals(tLoc, loc);
            }
        } catch (CommandContext.MissingQuotesException | CommandContext.MissingArgumentDataException e) {
            FailedDueToException(e);
        }
    }

    private static @NotNull Stream<Arguments> locationArgumentProvider() {
        return Stream.of(
                Arguments.arguments(
                        "create -l 227 74 -195".split(" "),
                        new Location(null, 227, 74, -195)
                ),
                Arguments.arguments(
                        "create -l 10 0 6".split(" "),
                        new Location(null, 10, 0, 6)
                )
        );
    }

    @Test
    public void NoArgumentsRemoveCommandTest() {
        String[] args = "remove".split(" ");
        System.out.println(Arrays.toString(args));

        try {
            CommandContext context = new CommandContext(null, null, npcCmd, args);
            Assertions.assertEquals("npc.remove", context.getCmd());
            Assertions.assertEquals(context.getArgs().size(), 0);
        } catch (CommandContext.MissingQuotesException | CommandContext.MissingArgumentDataException e) {
            FailedDueToException(e);
        }
    }

    @Test
    public void IdArgumentRemoveCommandTest() {
        String[] args = "remove 0".split(" ");
        System.out.println(Arrays.toString(args));

        try {
            CommandContext context = new CommandContext(null, null, npcCmd, args);
            Assertions.assertEquals("npc.remove", context.getCmd());

            if (context.hasDefaultArg(0)) {
                int npcId = Integer.parseInt((String) context.getDefaultArg(0).value());
                Assertions.assertEquals(0, npcId);
            }
        } catch (CommandContext.MissingQuotesException | CommandContext.MissingArgumentDataException e) {
            FailedDueToException(e);
        }
    }

    private void FailedDueToException(@NotNull Exception e) {
        e.printStackTrace();
        Assertions.fail();
    }

    // helpers
    static class TestCommand extends Command {
        TestCommand(@NotNull String name) {
            super(name);
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            return false;
        }
    }
}
