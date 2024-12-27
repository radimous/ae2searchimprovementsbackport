package com.radimous.ae2searchimprovementsbackport.mixin;

import appeng.api.stacks.AEKey;
import appeng.client.gui.me.search.RepoSearch;
import appeng.menu.me.common.GridInventoryEntry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.radimous.ae2searchimprovementsbackport.search.AndSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.ItemIdSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.ModSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.NameSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.OrSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.TagSearchPredicate;
import com.radimous.ae2searchimprovementsbackport.search.TooltipsSearchPredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(value = RepoSearch.class, remap = false)
public class MixinRepoSearch {
    @Shadow @Final private Map<AEKey, String> tooltipCache;


    @Shadow private Predicate<GridInventoryEntry> search;

    @WrapOperation(method = "setSearchString", at = @At(value = "INVOKE", target = "Lappeng/client/gui/me/search/SearchPredicates;fromString(Ljava/lang/String;Lappeng/client/gui/me/search/RepoSearch;)Ljava/util/function/Predicate;"))
    private Predicate<GridInventoryEntry> improvedFromString(String searchString, RepoSearch repoSearch, Operation<Predicate<GridInventoryEntry>> original) {
        if (searchString.startsWith("/o/")){
            return original.call(searchString.substring(3), repoSearch);
        }
        return ae2searchimprovementsbackport$fromString(searchString);
    }

    @Unique private Predicate<GridInventoryEntry> ae2searchimprovementsbackport$fromString(String searchString) {
        var orParts = searchString.split("\\|");

        if (orParts.length == 1) {
            return AndSearchPredicate.of(ae2searchimprovementsbackport$getPredicates(orParts[0]));
        } else {
            var orPartFilters = new ArrayList<Predicate<GridInventoryEntry>>(orParts.length);

            for (String orPart : orParts) {
                orPartFilters.add(AndSearchPredicate.of(ae2searchimprovementsbackport$getPredicates(orPart)));
            }
            return OrSearchPredicate.of(orPartFilters);
        }
    }

    /*
     * Created as a helper function for {@code fromString()}. This is designed to handle between the | (or operations)
     * to and the searched together delimited by " " Each space in {@code query} treated as a separate 'and' operation.
     */
    @Unique private List<Predicate<GridInventoryEntry>> ae2searchimprovementsbackport$getPredicates(String query) {
        var terms = query.toLowerCase().trim().split("\\s+");
        var predicateFilters = new ArrayList<Predicate<GridInventoryEntry>>(terms.length);

        for (String part : terms) {
            if (part.startsWith("@")) {
                predicateFilters.add(new ModSearchPredicate(part.substring(1)));
            } else if (part.startsWith("#")) {
                predicateFilters.add(new TooltipsSearchPredicate(part.substring(1), tooltipCache));
            } else if (part.startsWith("$")) {
                predicateFilters.add(new TagSearchPredicate(part.substring(1)));
            } else if (part.startsWith("*")) {
                predicateFilters.add(new ItemIdSearchPredicate(part.substring(1)));
            } else {
                predicateFilters.add(new NameSearchPredicate(part));
            }
        }

        return predicateFilters;
    }
}
