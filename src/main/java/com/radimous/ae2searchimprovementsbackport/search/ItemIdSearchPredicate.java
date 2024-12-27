package com.radimous.ae2searchimprovementsbackport.search;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.GridInventoryEntry;

public final class ItemIdSearchPredicate implements Predicate<GridInventoryEntry> {
    private final String term;

    public ItemIdSearchPredicate(String term) {
        this.term = term.toLowerCase();
    }

    @Override
    public boolean test(GridInventoryEntry gridInventoryEntry) {
        AEKey what = Objects.requireNonNull(gridInventoryEntry.getWhat());
        var id = what.getId().toString();
        return id.toLowerCase(Locale.ROOT).contains(term);
    }
}
