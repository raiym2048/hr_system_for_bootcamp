package com.example.hr_system.enums;


public enum StatusOfVacancy {
    open("Открыто", "Open"),
    archive("В архиве", "Archive"),
    closed("Закрыто", "Closed");

    private final String russianTranslation;
    private final String englishTranslation;

    StatusOfVacancy(String russianTranslation, String englishTranslation) {
        this.russianTranslation = russianTranslation;
        this.englishTranslation = englishTranslation;
    }

    public String getRussianTranslation() {
        return russianTranslation;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    // Reverse lookup by Russian translation
    public static StatusOfVacancy getByRussianTranslation(String russianTranslation) {
        for (StatusOfVacancy status : StatusOfVacancy.values()) {
            if (status.russianTranslation.equals(russianTranslation)) {
                return status;
            }
        }
        return null;
    }
}





