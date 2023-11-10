package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CARDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.model.card.Card;




public class ListCommandParserTest {

    private static final List<Predicate<Card>> TEST_PREDICATE =
            new ArrayList<>(Collections.singleton(PREDICATE_SHOW_ALL_CARDS));

    private ListCommandParser parser = new ListCommandParser();






    @Test
    public void parse_listAll_success() {
        // no index specified
        assertParseSuccess(parser, "", new ListCommand(TEST_PREDICATE));
    }





}
