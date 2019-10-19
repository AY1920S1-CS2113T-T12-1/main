package optix.commands.shows;

import optix.commands.Command;
import optix.commons.Model;
import optix.commons.Storage;
import optix.ui.Ui;
import optix.util.OptixDateFormatter;

import java.time.LocalDate;
import java.util.ArrayList;

public class DeleteCommand extends Command {
    private String[] showDates;
    private String showName;

    private OptixDateFormatter formatter = new OptixDateFormatter();

    private static final String MESSAGE_SUCCESSFUL = "Noted. The following shows has been deleted:\n";

    private static final String MESSAGE_ENTRY = "%1$d. %2$s (on: %3$s)\n";

    private static final String MESSAGE_SHOW_NOT_FOUND = "☹ OOPS!!! Unable to find the following shows:\n";

    public DeleteCommand(String showName, String showDate) {
        this.showDates = showDate.split("\\|");
        this.showName = showName;
    }

    @Override
    public void execute(Model model, Ui ui, Storage storage) {
        StringBuilder message = new StringBuilder(MESSAGE_SUCCESSFUL);
        ArrayList<String> missingShows = new ArrayList<>();

        int counter = 1;

        for (int i = 0; i < showDates.length; i++) {
            String date = showDates[i].trim();

            if (!hasValidDate(date)) {
                missingShows.add(date);
                continue;
            }

            LocalDate showLocalDate = formatter.toLocalDate(date);

            if (model.containsKey(showLocalDate) && model.hasSameName(showLocalDate, showName)) {
                model.deleteShow(showLocalDate);
                message.append(String.format(MESSAGE_ENTRY, counter, showName, date));
                counter++;
            } else {
                missingShows.add(date);
            }
        }

        if (missingShows.size() != 0) {
            message.append("\n" + MESSAGE_SHOW_NOT_FOUND);
            for (int i = 0; i < missingShows.size(); i++) {
                message.append(String.format(MESSAGE_ENTRY, i+1, showName, missingShows.get(i)));
            }
        }

        ui.setMessage(message.toString());
    }

    private boolean hasValidDate(String date) {
        return formatter.isValidDate(date);
    }
}