package seedu.inventorybro.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@@author kenpegrasio
class AutocompleterTest {
    private Autocompleter autocompleter;

    @BeforeEach
    void setUp() {
        autocompleter = new Autocompleter();
    }

    @Test
    void getMatches_exactKeyword_returnsSingleMatch() {
        List<String> matches = autocompleter.getMatches("addItem");
        assertEquals(1, matches.size());
        assertEquals("addItem", matches.get(0));
    }

    @Test
    void getMatches_partialSingleMatch_returnsCorrectKeyword() {
        List<String> matches = autocompleter.getMatches("lis");
        assertEquals(1, matches.size());
        assertEquals("listItems", matches.get(0));
    }

    @Test
    void getMatches_partialMultipleMatches_returnsAllMatches() {
        List<String> matches = autocompleter.getMatches("e");
        assertEquals(4, matches.size()); // editDescription + editPrice + editQuantity + exit
        assertTrue(matches.contains("editDescription"));
        assertTrue(matches.contains("editPrice"));
        assertTrue(matches.contains("editQuantity"));
        assertTrue(matches.contains("exit"));
    }

    @Test
    void getMatches_editPrefix_returnsAllEditCommands() {
        List<String> matches = autocompleter.getMatches("edit");
        assertEquals(3, matches.size());
        assertTrue(matches.contains("editDescription"));
        assertTrue(matches.contains("editPrice"));
        assertTrue(matches.contains("editQuantity"));
    }

    @Test
    void getMatches_editDescriptionExact_returnsSingleMatch() {
        List<String> matches = autocompleter.getMatches("editDescription");
        assertEquals(1, matches.size());
        assertEquals("editDescription", matches.get(0));
    }

    @Test
    void getMatches_editPriceExact_returnsSingleMatch() {
        List<String> matches = autocompleter.getMatches("editPrice");
        assertEquals(1, matches.size());
        assertEquals("editPrice", matches.get(0));
    }

    @Test
    void getMatches_editQuantityExact_returnsSingleMatch() {
        List<String> matches = autocompleter.getMatches("editQuantity");
        assertEquals(1, matches.size());
        assertEquals("editQuantity", matches.get(0));
    }

    @Test
    void getMatches_noMatch_returnsEmpty() {
        List<String> matches = autocompleter.getMatches("xyz");
        assertTrue(matches.isEmpty());
    }

    @Test
    void getMatches_emptyString_returnsAllKeywords() {
        List<String> matches = autocompleter.getMatches("");
        assertEquals(12, matches.size());
    }

    @Test
    void getMatches_uppercaseInput_returnsMatch() {
        List<String> matches = autocompleter.getMatches("ADDI");
        assertEquals(1, matches.size());
        assertEquals("addItem", matches.get(0));
    }

    @Test
    void getMatches_preservesOriginalCase_returnsProperCase() {
        List<String> matches = autocompleter.getMatches("listi");
        assertEquals(1, matches.size());
        assertEquals("listItems", matches.get(0));
    }
}
