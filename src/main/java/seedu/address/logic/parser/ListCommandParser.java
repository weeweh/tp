package seedu.address.logic.parser;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.card.Card;
import seedu.address.model.tag.Tag;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUESTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns an ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_QUESTION, PREFIX_TAG);

        List<Predicate<Card>> predicates = new ArrayList<>(Collections.singleton(PREDICATE_SHOW_ALL_CARDS));

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_QUESTION);
        if (argMultimap.getValue(PREFIX_QUESTION).isPresent()) {
            if (argMultimap.getValue(PREFIX_QUESTION).get().isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
            }

            predicates.add(parseQuestionPrefix(argMultimap.getValue(PREFIX_QUESTION).get()));
        }

        if (!argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            predicates.add(parseTagPrefix(argMultimap.getAllValues(PREFIX_TAG)));
        }

        return new ListCommand(predicates);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses {@code <String> prefix} into a {@code Predicate<Card>} if {@code prefix} is non-empty.
     */
    private static Predicate<Card> parseQuestionPrefix(String prefix) {
        assert(!prefix.isEmpty());

        return (card -> card.getQuestion().startsWith(prefix));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Predicate<Card>} if {@code tags} is non-empty.
     */
    private static Predicate<Card> parseTagPrefix(Collection<String> tags) throws ParseException {
        assert(!tags.isEmpty());

        List<Tag> tagSet = ParserUtil.parseTags(tags);

        return (card -> new HashSet<>(card.getTags()).containsAll(tagSet));
    }

}