package optix.commands.shows;

import optix.commands.shows.AddCommand;
import optix.commons.Model;
import optix.commons.Storage;
import optix.ui.Ui;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCommandTest {
    private Ui ui = new Ui();
    private static File currentDir = new File(System.getProperty("user.dir"));
    private static File filePath = new File(currentDir.toString() + "\\src\\test\\data\\testOptix");
    private Storage storage = new Storage(filePath);
    private Model model = new Model(storage);

    @Test
    void execute() {
        AddCommand testCommand = new AddCommand("dummy show name|20|5/5/2020 | 6/10/2020");

        testCommand.execute(model, ui, storage);
        String expected = "__________________________________________________________________________________\n"
                + "Noted. The following shows has been added:\n"
                + "1. dummy show name (on: 5/5/2020)\n"
                + "2. dummy show name (on: 6/10/2020)\n"
                + "__________________________________________________________________________________\n";
        assertEquals(expected, ui.showCommandLine());


        AddCommand testCommand2 = new AddCommand("dummy show name|20|7/10/2020|6/10/2020");

        testCommand2.execute(model, ui, storage);
        String expected2 = "__________________________________________________________________________________\n"
                + "Noted. The following shows has been added:\n"
                + "1. dummy show name (on: 7/10/2020)\n"
                + "\n"
                + "☹ OOPS!!! Unable to add the following shows:\n"
                + "1. dummy show name (on: 6/10/2020)\n"
                + "__________________________________________________________________________________\n";
        assertEquals(expected2, ui.showCommandLine());


        AddCommand testCommand3 = new AddCommand("dummy show name|20|5/5/2020|6/10/2020");

        testCommand3.execute(model, ui, storage);
        String expected3 = "__________________________________________________________________________________\n"
                + "☹ OOPS!!! Unable to add the following shows:\n"
                + "1. dummy show name (on: 5/5/2020)\n"
                + "2. dummy show name (on: 6/10/2020)\n"
                + "__________________________________________________________________________________\n";
        assertEquals(expected3, ui.showCommandLine());

        filePath.deleteOnExit();
    }

    @AfterAll
    static void cleanUp() {
        File deletedFile = new File(filePath, "optix.txt");
        deletedFile.delete();
    }
}