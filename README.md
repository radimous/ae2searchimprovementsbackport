# Backport of new AE2 search  
how it works now:  
everything gets split by spaces into intermediary terms that are treated as as if there was AND operator between them  
those then get split by "|" into terms and treated as if there was OR operator between them  
each term can have a prefix
| syntax   | example         |
|----------|-----------------|
| @mod     | @ae2            |
| #tooltip | #channeling     |
| $tag     | $ingot          |
| *id      | *minecraft:dirt |
| name     | iron ingot      |

searching is in disjunctive form  
for example "@vault jewel #rarity #hammer | @vault jewel #quantity #hammer" means item from vault mod called jewel with hammer in tooltip and either rarity or quantity in tooltip  



if you want to access the old behavior for some regex magic you must prefix your search with /o/  
ex. "/o/(?s)(?=.*jewel)(?=.*hammer)(?=.*rarity|.*quantity)" ( no way to search for mod here :( )
