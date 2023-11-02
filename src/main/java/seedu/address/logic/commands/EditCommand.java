package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.card.*;
import seedu.address.model.tag.Tag;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;

/**
 * Edits the details of an existing Card in the Deck.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the Card identified "
            + "by the index number used in the displayed card list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_QUESTION + "QUESTION] "
            + "[" + PREFIX_ANSWER + "ANSWER] "
            + "[" + PREFIX_TAG + "TAG] "
            + "[" + PREFIX_HINT + "[HINT] ";

    public static final String MESSAGE_EDIT_CARD_SUCCESS = "Edited Card: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CARD = "This card already exists in the Deck.";

    /** Specific {@code Index} in Deck to edit */
    private final Index index;

    /** A representation of the edited details of the new {@code Card} */
    private final EditCardDescriptor editCardDescriptor;

    /**
     * @param index              of the Card in the filtered Deck to edit
     * @param editCardDescriptor details to edit the Card with
     */
    public EditCommand(Index index, EditCardDescriptor editCardDescriptor) {
        requireNonNull(index);
        requireNonNull(editCardDescriptor);

        this.index = index;
        this.editCardDescriptor = new EditCardDescriptor(editCardDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Card> lastShownList = model.getFilteredCardList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CARD_DISPLAYED_INDEX);
        }

        Card cardToEdit = lastShownList.get(index.getZeroBased());
        Card editedCard = createEditedCard(cardToEdit, editCardDescriptor);

        if (!cardToEdit.isSameCard(editedCard) && model.hasCard(editedCard)) {
            throw new CommandException(MESSAGE_DUPLICATE_CARD);
        }

        model.setCard(cardToEdit, editedCard);
//        model.updateFilteredCardList(PREDICATE_SHOW_ALL_CARDS);
        return new CommandResult(String.format(MESSAGE_EDIT_CARD_SUCCESS, Messages.format(editedCard)));
    }

    /**
     * Creates and returns a {@code Card} with the details of {@code cardToEdit}
     * edited with {@code editCardDescriptor}.
     */
    private static Card createEditedCard(Card cardToEdit, EditCardDescriptor editCardDescriptor) {
        assert cardToEdit != null;

        Question updatedQuestion = editCardDescriptor.getQuestion().orElse(cardToEdit.getQuestion());
        Answer updatedAnswer = editCardDescriptor.getAnswer().orElse(cardToEdit.getAnswer());
        List<Tag> updatedTags = editCardDescriptor.getTags().orElse((cardToEdit.getTags()));
        Difficulty difficulty = Difficulty.NEW;
        PracticeDate nextPracticeDate = cardToEdit.getNextPracticeDate();
        PracticeDate lastPracticeDate = cardToEdit.getLastPracticeDate();
        SolveCount solveCount = cardToEdit.getSolveCount();
        Hint hint = editCardDescriptor.getHint().orElse(cardToEdit.getHint());

        return new Card(updatedQuestion, updatedAnswer, difficulty, updatedTags,
                nextPracticeDate, lastPracticeDate, solveCount, hint);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // compare Index and EditCardDescriptor equality
        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editCardDescriptor.equals(otherEditCommand.editCardDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editCardDescriptor", editCardDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the Card with. Each non-empty field value will
     * replace the corresponding field value of the Card.
     */
    public static class EditCardDescriptor {
        private Question question;
        private Answer answer;
        private List<Tag> tags;
        private Hint hint;

        public EditCardDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditCardDescriptor(EditCardDescriptor toCopy) {
            setQuestion(toCopy.question);
            setAnswer(toCopy.answer);
            setTags(toCopy.tags);
            setHint(toCopy.hint);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(question, answer, tags, hint);
        }

        // Question
        public void setQuestion(Question question) {
            this.question = question;
        }

        public Optional<Question> getQuestion() {
            return Optional.ofNullable(question);
        }

        // Answer
        public void setAnswer(Answer answer) {
            this.answer = answer;
        }

        public Optional<Answer> getAnswer() {
            return Optional.ofNullable(answer);
        }

        // Tags
        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public Optional<List<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        // Hint
        public Optional<Hint> getHint() {
            return Optional.ofNullable(hint);
        }
        public void setHint(Hint hint) {
            this.hint = hint;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCardDescriptor)) {
                return false;
            }

            // compare Question, Answer and Tag equality
            EditCardDescriptor otherEditCardDescriptor = (EditCardDescriptor) other;
            return Objects.equals(question, otherEditCardDescriptor.question)
                    && Objects.equals(answer, otherEditCardDescriptor.answer)
                    && Objects.equals(tags, otherEditCardDescriptor.tags)
                    && Objects.equals(hint, otherEditCardDescriptor.hint);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("question", question)
                    .add("answer", answer)
                    .add("tags", tags)
                    .add("hint", hint)
                    .toString();
        }
    }
}
