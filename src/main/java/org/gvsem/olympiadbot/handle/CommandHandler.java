package org.gvsem.olympiadbot.handle;

import org.gvsem.olympiadbot.controller.MessageEnum;
import org.gvsem.olympiadbot.controller.QueryEnum;
import org.gvsem.olympiadbot.keyboards.KeyboardCollection;
import org.gvsem.olympiadbot.model.Olympiad;
import org.gvsem.olympiadbot.model.Track;
import org.gvsem.olympiadbot.service.OlympiadService;
import org.gvsem.olympiadbot.utils.CommandUtils;
import org.gvsem.olympiadbot.utils.TrackLevelComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Component
public class CommandHandler {

    protected final KeyboardCollection keyboardCollection;

    protected final OlympiadService olympiadService;

    protected final TrackLevelComparator trackLevelComparator = new TrackLevelComparator();

    @Autowired
    public CommandHandler(KeyboardCollection keyboardCollection, OlympiadService olympiadService) {
        this.keyboardCollection = keyboardCollection;
        this.olympiadService = olympiadService;
    }

    protected BotApiMethod<?> getStartMessage(String chatId) {
        SendMessage msg = new SendMessage(chatId, MessageEnum.START.toString());
        msg.enableMarkdown(true);
        msg.setReplyMarkup(keyboardCollection.getDemoKeyboard());
        return msg;
    }

    public BotApiMethod<?> handle(@NotNull String chatId, @Nullable String command) {

        QueryEnum cmd = CommandUtils.getCommand(command);

        if (cmd == null) {
            return new SendMessage(chatId, MessageEnum.ERROR.toString());
        }

        List<String> args = CommandUtils.getArguments(command);

        switch (cmd) {
            case START -> {
                return getStartMessage(chatId);
            }
            case GET_OLYMPIAD -> {
                if (args.size() != 1) {
                    return new SendMessage(chatId, MessageEnum.ID_REQUIRED.toString());
                }
                Long id = Long.valueOf(args.get(0));
                Optional<Olympiad> olympiad = olympiadService.getOlympiad(id);
                if (olympiad.isEmpty()) {
                    return new SendMessage(chatId, String.format(MessageEnum.OLYMPIAD_NOT_FOUND.toString(), id));
                }

                List<Track> tracks = olympiadService.getTracks(olympiad.get());
                tracks.sort(trackLevelComparator);

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("**%s**\n", olympiad.get().getTitle()));
                sb.append(String.format("[Website](%s) | Tracks Total: %d\n", olympiad.get().getPageUrl(), tracks.size()));

                for (Track t : tracks) {
                    sb.append(switch (t.getLevel()) {
                        case 1 -> "1️⃣";
                        case 2 -> "2️⃣";
                        case 3 -> "3️⃣";
                        default -> "?";
                    });
                    sb.append(" ").append(t.getProfile());
                    if (t.getSubjects().size() > 1 || !t.getSubjects().get(0).equals(t.getProfile())) {
                        sb.append(" (").append(String.join(", ", t.getSubjects())).append(")");
                    }
                    sb.append("\n");
                }
                SendMessage msg = new SendMessage(chatId, sb.toString());
                msg.enableMarkdown(true);
                return msg;
            }
            case GET_ALL_OLYMPIADS, GET_OLYMPIADS_BY_PROFILE -> {

                int page = 0;
                boolean hasPage = false;
                if (cmd.equals(QueryEnum.GET_OLYMPIADS_BY_PROFILE) && args.size() == 0) {
                    return new SendMessage(chatId, MessageEnum.PROFILE_REQUIRED.toString());
                }

                try {
                    if (args.size() != 0) {
                        page = Integer.parseInt(args.get(args.size() - 1));
                        hasPage = true;
                    }
                } catch (NumberFormatException ignored) {}

                StringBuilder queryBuilder = new StringBuilder();
                for (int i = 0; i < args.size() + (hasPage ? -1 : 0); i++) {
                    queryBuilder.append(args.get(i)).append(" ");
                }
                String query = queryBuilder.toString().trim();

                List<Olympiad> olympiads;
                String header;
                String nextPageCmd;
                if (cmd.equals(QueryEnum.GET_ALL_OLYMPIADS)) {
                    olympiads = olympiadService.getOlympiads(page);
                    header = String.format("**All Olympiads** (page %d)\n\n", page);
                    nextPageCmd = "/" + QueryEnum.GET_ALL_OLYMPIADS + " " + (page + 1);
                } else {
                    olympiads = olympiadService.getOlympiads(query, page);
                    header = String.format("**Subject = %s** (page %d)\n\n", query, page);
                    nextPageCmd = "/" + QueryEnum.GET_OLYMPIADS_BY_PROFILE + " " + query + " " + (page + 1);
                }

                if (olympiads.isEmpty()) {
                    return new SendMessage(chatId, String.format(MessageEnum.OLYMPIADS_NOT_FOUND.toString(), page));
                }

                SendMessage msg = new SendMessage(chatId, header);
                msg.enableMarkdown(true);
                msg.setReplyMarkup(keyboardCollection.getOlympiadsKeyboard(
                        olympiads,
                        nextPageCmd
                ));
                return msg;

            }
        }

        return new SendMessage(chatId, MessageEnum.ERROR.toString());

    }

}
