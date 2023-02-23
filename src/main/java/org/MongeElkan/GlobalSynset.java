package org.MongeElkan;

public sealed interface GlobalSynset extends Comparable<GlobalSynset> permits EnglishSynset {
    Language language();

    @Override
    default int compareTo(GlobalSynset gss) {
        return this.toString().compareTo(gss.toString());
    }
}
