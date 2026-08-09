package com.github.gcastelo22.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a Pet entity in the API payload.
 *
 * This class maps JSON request and response payloads to Java objects using the Jackson library.
 * It encapsulates the pet's attributes, nested complex types ({@link Category} and {@link Tag}),
 * and lists of photo URLs and tags.
 *
 * The {@literal @JsonIgnoreProperties(ignoreUnknown = true)} annotation prevents deserialization
 * failures when the API returns unexpected or newly added JSON fields that are not defined in this class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    /** Unique identifier for the pet entity. */
    private Long id;

    /** Category group to which this pet belongs (e.g., Dogs, Cats). */
    private Category category;

    /** Name of the pet. */
    private String name;

    /** List of URLs hosting photos of the pet. */
    private List<String> photoUrls;

    /** List of descriptive tags associated with the pet. */
    private List<Tag> tags;

    /** Current status of the pet in the store lifecycle (e.g., "available", "pending", "sold"). */
    private String status;

    /**
     * Default no-argument constructor required by Jackson for JSON deserialization via reflection.
     */
    public Pet() {}

    /**
     * Convenience constructor for instantiating a Pet object with core mandatory properties.
     *
     * @param id     Unique identifier of the pet.
     * @param name   Name of the pet.
     * @param status Current availability status.
     */
    public Pet(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    // =========================================================================================
    // Getters and Setters (Encapsulation)
    // =========================================================================================

    /** @return Unique identifier of the pet. */
    public Long getId() {
        return id;
    }

    /** @param id Unique identifier to set. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return Category object assigned to the pet. */
    public Category getCategory() {
        return category;
    }

    /** @param category Category object to set. */
    public void setCategory(Category category) {
        this.category = category;
    }

    /** @return Name of the pet. */
    public String getName() {
        return name;
    }

    /** @param name Name to set. */
    public void setName(String name) {
        this.name = name;
    }

    /** @return List of photo URLs. */
    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    /** @param photoUrls List of photo URLs to set. */
    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    /** @return List of associated tags. */
    public List<Tag> getTags() {
        return tags;
    }

    /** @param tags List of tags to set. */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /** @return Status of the pet. */
    public String getStatus() {
        return status;
    }

    /** @param status Status string to set. */
    public void setStatus(String status) {
        this.status = status;
    }

    // =========================================================================================
    // Nested Inner Classes (Nested DTOs)
    // =========================================================================================

    /**
     * DTO representing a category classification for a pet.
     * Defined as a static nested class to maintain modularity and domain cohesion.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category {

        /** Unique identifier for the category. */
        private Long id;

        /** Display name of the category (e.g., "Dogs"). */
        private String name;

        /** Default no-arg constructor required for Jackson deserialization. */
        public Category() {}

        /**
         * Parametrized constructor to quickly build a Category instance.
         *
         * @param id   Category ID.
         * @param name Category name.
         */
        public Category(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * DTO representing a metadata tag assigned to a pet.
     * Defined as a static nested class for clean structural encapsulation.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {

        /** Unique identifier for the tag. */
        private Long id;

        /** Name or label of the tag (e.g., "friendly", "vaccinated"). */
        private String name;

        /** Default no-arg constructor required for Jackson deserialization. */
        public Tag() {}

        /**
         * Parametrized constructor to quickly build a Tag instance.
         *
         * @param id   Tag ID.
         * @param name Tag label.
         */
        public Tag(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}