package org.MongeElkan;

public record EnglishSynset(int offset, PartOfSpeech partOfSpeech) implements GlobalSynset {
    @Override
    public Language language() {
        return Language.English;
    }
}
