package com.vg.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 9002398529119645273L;
    private final Long id;

    public EntityNotFoundException(final long id) {
        super("Entity could not be found with id: " + id);
        this.id = id;
    }

    public EntityNotFoundException(final long id, final String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
