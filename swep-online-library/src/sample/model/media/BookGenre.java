package sample.model.media;

/**
 * A list of all available book genres. If you wish to update this list, add both the same values in this enum
 * and the corresponding database table "book_genres_list". An admin will be able to pick a list of book genres when
 * adding new books to the inventory; so will users when filtering the book catalog for specific genres.
 *
 * IMPORTANT: Values of Constants need to be the same name as genre names in Database!
 *
 * Genres taken from this list: https://gladreaders.com/types-or-genres-of-books/
 * Disclaimer: For demonstration purposes, this list is not complete and misses other potential characteristics, ex.
 * "fiction vs. non-fiction".
 *
 * Acceptable value formats: "genre name", "genrename", "genre_name"
 * Not acceptable: "genre-name"
 *
 */
public enum BookGenre {

    // 23 book genres currently available:

    ACTION_AND_ADVENTURE("Action and Adventure"),
    ANTHOLOGY("Anthology"),
    CLASSIC("Classic"),
    COMIC("Comic"),
    CRIME_AND_DETECTIVE("Crime and Detective"),
    DRAMA("Drama"),
    FABLE("Fable"),
    FAIRY_TALE("Fairy Tale"),
    FAN_FICTION("Fan Fiction"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    HUMOR("Humor"),
    LEGEND("Legend"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SATIRE("Satire"),
    SCIENCE_FICTION("Science Fiction"),
    SHORT_STORY("Short Story"),
    THRILLER("Thriller"),

    BIOGRAPHY("Biography"),
    ESSAY("Essay"),
    MEMOIR("Memoir"),
    PERIODICALS("Periodicals"),
    SELF_HELP("Self Help"),
    TEXTBOOK("Textbook"),
    POETRY("Poetry"),

    // Empty

    UNDEFINED("Undefined");

    private String name;

    public String getName() {
        return name;
    }

    BookGenre(String name) {
        this.name = name;
    }

}